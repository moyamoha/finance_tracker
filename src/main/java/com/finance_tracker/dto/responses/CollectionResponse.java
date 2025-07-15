package com.finance_tracker.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionResponse<T> {
    private List<T> content;
    private long totalElements;
    private int page;
    private int size;
    private boolean isLast;
}
