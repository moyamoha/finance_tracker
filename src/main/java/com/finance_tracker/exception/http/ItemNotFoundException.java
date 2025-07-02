package com.finance_tracker.exception.http;

import com.finance_tracker._shared.Identifier;
import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends HttpException {
    public ItemNotFoundException(Object detail) {
        super(HttpStatus.NOT_FOUND, "Item not found", detail);
    }

    public static <T> ItemNotFoundException withIdentifierAndEntity (Class<?> clazz, Identifier<T> id) {
        String detail = clazz.getSimpleName() + " with id " + id.toString() + " not found";
        return new ItemNotFoundException(detail);
    }
}
