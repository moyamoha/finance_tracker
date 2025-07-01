package com.finance_tracker.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
@AllArgsConstructor
public class TransactionCollectionResponse {
    private List<SingleTransactionResponse> content;
    private int totalElements;
    private int page;
    private int size;
    private boolean isLast;
}
