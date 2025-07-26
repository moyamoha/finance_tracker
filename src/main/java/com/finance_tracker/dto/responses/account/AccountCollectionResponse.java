package com.finance_tracker.dto.responses.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AccountCollectionResponse {
    private List<SingleAccountResponse> content;
    private Long totalElements;
}
