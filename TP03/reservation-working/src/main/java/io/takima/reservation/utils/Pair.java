package io.takima.reservation.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Pair<T, U> {
    T first;
    U second;
}
