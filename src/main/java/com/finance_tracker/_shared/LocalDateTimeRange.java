package com.finance_tracker._shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LocalDateTimeRange {
    private final LocalDateTime start;
    private final LocalDateTime end;
}
