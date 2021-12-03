package io.takima.reservation.booking.service.impl;

import io.takima.reservation.booking.domain.Travel;
import io.takima.reservation.booking.dto.requests.TravelUpdateRequest;
import io.takima.reservation.booking.dto.responses.TravelDetail;
import io.takima.reservation.booking.repository.TravelDao;
import io.takima.reservation.booking.service.TravelService;
import io.takima.reservation.exception.InvalidInputException;
import io.takima.reservation.search.domain.StopTime;
import io.takima.reservation.trains.domain.Car;
import io.takima.reservation.trains.domain.Train;
import io.takima.reservation.trains.dto.responses.CarWithAvailability;
import io.takima.reservation.trains.dto.responses.SeatWithAvailability;
import io.takima.reservation.trains.dto.responses.TrainWithAvailability;
import io.takima.reservation.trains.repository.TrainDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class TravelServiceImpl implements TravelService {

    @Autowired
    private TravelDao travelDao;

    @Autowired
    private TrainDao trainDao;

    @Override
    public Travel getRandom() {
        var count = (int) travelDao.count();
        var randIdx = new SecureRandom().nextInt(count);

        var p = PageRequest.of(randIdx, 1, Sort.by("travelId").ascending());

        return travelDao.findAll(p).getContent().get(0);
    }

    @Override
    public List<Travel> getAllTravels() {
        return travelDao.findAll();
    }


    @Override
    public Travel getTravel(Long id) {
        return travelDao.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("No travel with id %d", id)));
    }

    @Override
    public Travel updateTravel(Long travelId, TravelUpdateRequest request) {
        if (travelId == null) {
            throw new InvalidInputException("Travel update request is missing Travel id");
        } else if (request.isEmpty()) {
            throw new InvalidInputException("Travel update request is missing update values");
        }

        var travel = getTravel(travelId);

        travel.mergePriceFirstClass(request.getPriceFirstClass());
        travel.mergePriceSecondClass(request.getPriceSecondClass());

        var trainId = request.getTrainId();
        if (trainId != null) {
            var train = trainDao.findById(trainId).orElseThrow(() -> new NoSuchElementException(String.format("No Train with id %d", trainId)));
            travel = travel.mergeTrain(train);
        }

        return travelDao.save(travel);
    }

    @Override
    public Travel assignRandomTravel(Train train) {
        var travel = getRandom();

        travel.setTrain(train);

        return travelDao.save(travel);
    }


    @Override
    public TravelDetail getTravelDetail(Long id, String stationFrom, String stationTo) {
        var travel = getTravel(id);

        var stations = travel.getTrip().getStopTimes().stream().filter(st -> st.getStop()
                        .getParentStation().equals(stationFrom) || st.getStop().getParentStation().equals(stationTo))
                .sorted(Comparator.comparingInt(StopTime::getSequence)).map(StopTime::getStop).collect(Collectors.toList());

        if (stations.size() != 2) {
            throw new InvalidInputException("Unkown station error");
        }
        var distance = travel.getTrip().getTotalDistance(stations.get(0), stations.get(1));

        if (travel.getTrain() == null) {
            return TravelDetail.from(travel, null, 0.0f, 0.0f);
        }

        Function<Car, CarWithAvailability> carToCarWithAvailability = car -> {
            var seatsWithAvailability = car.getSeats()
                    .stream()
                    .map(seat -> SeatWithAvailability.from(seat, !travel.isSeatBooked(seat)))
                    .collect(Collectors.toList());
            return CarWithAvailability.from(car, seatsWithAvailability);
        };

        var carsWithAvailability = travel.getTrain()
                .getCars()
                .stream()
                .map(carToCarWithAvailability)
                .collect(Collectors.toList());

        var trainWithAvailability = TrainWithAvailability.from(travel.getTrain(), carsWithAvailability);

        return TravelDetail.from(travel, trainWithAvailability, travel.getPriceFirstClass() * distance, travel.getPriceSecondClass() * distance);
    }

}
