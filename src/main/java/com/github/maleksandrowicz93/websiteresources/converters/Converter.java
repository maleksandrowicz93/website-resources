package com.github.maleksandrowicz93.websiteresources.converters;

/**
 * This interface represents abstraction for classes intended for converting objects.
 * @param <T> - type to be converted
 * @param <R> - type after conversion
 */
public interface Converter<T, R> {

    R convert(T t);
}
