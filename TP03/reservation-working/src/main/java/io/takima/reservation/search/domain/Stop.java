package io.takima.reservation.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Immutable
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "STOPS")
public class Stop implements Serializable {
    @Id
    @Column(name = "STOP_ID")
    String id;

    @Column(name = "STOP_NAME")
    String name;

    @Column(name = "STOP_DESC")
    Boolean desc;

    @Column(name = "STOP_LAT")
    Double latitude;

    @Column(name = "STOP_LON")
    Double longitude;

    @Column(name = "ZONE_ID")
    @JsonIgnore
    String zoneId;

    @Column(name = "STOP_URL")
    @JsonIgnore
    String url;

    @Column(name = "LOCATION_TYPE")
    @JsonIgnore
    Integer locationType;

    @Column(name = "PARENT_STATION")
    @JsonIgnore
    String parentStation;
}
