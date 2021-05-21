package com.example.transaction.validation.convertor;

import java.util.Optional;

public interface Convertor<T, P> {
    Optional<P> convert(T t);
}
