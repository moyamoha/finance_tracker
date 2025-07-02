package com.finance_tracker._shared;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Identifier<T> {

    @Getter @Setter
    private T id;

    public Identifier(T id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }
        this.id = id;
    }

    public boolean isInt() {
        return id instanceof Integer;
    }

    public boolean isString() {
        return id instanceof String;
    }

    public boolean isUUID() {
        return id instanceof UUID;
    }

    @Override
    public String toString() {
        return  id.toString();
    }
}
