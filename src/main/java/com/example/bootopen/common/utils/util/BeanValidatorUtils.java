package com.example.bootopen.common.utils.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author chaijd
 **/

@Component
@Slf4j
public class BeanValidatorUtils<T> {
    @Autowired
    private Validator validator;

    public void validate(T t) throws ValidationException {
        log.info("bean validate start");
        Set<ConstraintViolation<T>> set = validator.validate(t);
        if (set.isEmpty()) {
            log.info("bean validate success");
            return;
        }
        log.error("bean validate error");

        throw new ValidationException(set.iterator().next().getMessage());

//        StringBuilder validateError = new StringBuilder();
//        for (ConstraintViolation<T> val : set) {
//            validateError.append(val.getMessage());
//            break;
//        }
//        throw new ValidationException(validateError.toString());
    }

    public void validate(T t,Class... group) throws ValidationException {
        log.info("bean validate start");
        Set<ConstraintViolation<T>> set = validator.validate(t,group);
        if (set.isEmpty()) {
            log.info("bean validate success");
            return;
        }
        log.error("bean validate error");

        throw new ValidationException(set.iterator().next().getMessage());

//        StringBuilder validateError = new StringBuilder();
//        for (ConstraintViolation<T> val : set) {
//            validateError.append(val.getMessage());
//            break;
//        }
//        throw new ValidationException(validateError.toString());
    }
}