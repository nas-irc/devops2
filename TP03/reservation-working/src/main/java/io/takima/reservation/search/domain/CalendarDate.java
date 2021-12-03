package io.takima.reservation.search.domain;

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
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Immutable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"serviceId", "date"})
@Entity
@IdClass(CalendarDate.Key.class)
@Table(name = "CALENDAR_DATES")
public class CalendarDate implements Serializable {
    @Id
    @Column(name = "SERVICE_ID")
    Long serviceId;

    @Id
    @Column(name = "DATE")
    LocalDate date;

    @Column(name = "EXCEPTION_TYPE")
    Integer exceptionType;

    @Data
    @NoArgsConstructor
    public static class Key implements Serializable {
        private Long serviceId;
        private LocalDate date;
    }
}
