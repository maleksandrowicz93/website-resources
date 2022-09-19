package com.github.maleksandrowicz93.websiteresources.converters;

public interface Converter<T, R> {

    R convert(T t);
}
