package com.stadio.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.*;
import java.util.Map;
import java.util.Set;

public class JsonMapperValidate {

    private ObjectMapper objectMapper;
    private Validator validator;

    public JsonMapperValidate() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();

    }

    public <T> T read(Map<String, String> parameters,  Class<T> toValueType) throws Exception {
        return validate(objectMapper.convertValue(parameters, toValueType));
    }

    private <T> T  validate( T input) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(input);
        if (constraintViolations.isEmpty()) {
            return input;
        } else {
            throw new ConstraintViolationException(constraintViolations);
        }

    }

}
