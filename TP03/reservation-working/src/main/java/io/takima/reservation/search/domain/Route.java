package io.takima.reservation.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
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

@FieldDefaults(level = AccessLevel.PRIVATE)
@Immutable
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "routeId")
@Entity
@Table(name = "ROUTES")
public class Route implements Serializable {
    @Id
    @Column(name = "ROUTE_ID")
    String routeId;

    @Column(name = "AGENCY_ID")
    @JsonIgnore
    String agencyId;

    @Column(name = "ROUTE_SHORT_NAME")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String routeShortName;

    @Column(name = "ROUTE_LONG_NAME")
    String routeLongName;

    @Column(name = "ROUTE_DESC")
    @JsonIgnore
    String routeDesc;

    @Column(name = "ROUTE_TYPE")
    @JsonIgnore
    Integer routeType;

    @Column(name = "ROUTE_URL")
    @JsonIgnore
    String routeUrl;

    @Column(name = "ROUTE_COLOR")
    @JsonIgnore
    String routeColor;

    @Column(name = "ROUTE_TEXT_COLOR")
    @JsonIgnore
    String routeTextColor;
}
