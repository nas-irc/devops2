package io.takima.reservation.search.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Immutable
@NoArgsConstructor
@EqualsAndHashCode(of = {"serviceId", "startDate"})
@Entity
@IdClass(Calendar.Key.class)
@Table(name = "CALENDAR")
public class Calendar implements Serializable {
    @Id
    @Column(name = "SERVICE_ID")
    long serviceId;

    @Id
    @Column(name = "START_DATE")
    LocalDate startDate;

    @Column(name = "END_DATE")
    LocalDate endDate;

    @Column(name = "MONDAY")
    boolean monday;

    @Column(name = "TUESDAY")
    boolean tuesday;

    @Column(name = "WEDNESDAY")
    boolean wednesday;

    @Column(name = "THURSDAY")
    boolean thursday;

    @Column(name = "FRIDAY")
    boolean friday;

    @Column(name = "SATURDAY")
    boolean saturday;

    @Column(name = "SUNDAY")
    boolean sunday;

    @Data
    @NoArgsConstructor
    public static class Key implements Serializable {
        private long serviceId;
        private LocalDate startDate;
    }
}
