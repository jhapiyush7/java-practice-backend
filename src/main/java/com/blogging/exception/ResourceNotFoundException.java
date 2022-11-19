package com.blogging.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String resourceField;
    long resourceValue;

    public ResourceNotFoundException(String resourceName, String resourceField, long resourceValue) {
        super(String.format("%s not found having %s:%s", resourceName, resourceField, resourceValue));
    }
}
