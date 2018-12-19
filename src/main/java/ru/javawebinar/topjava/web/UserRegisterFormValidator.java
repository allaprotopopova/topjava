package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

public class UserRegisterFormValidator implements Validator {

    @Autowired
    UserService service;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserTo.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserTo user = (UserTo) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.userTo.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.userTo.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.userTo.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "caloriesPerDay", "NotEmpty.userTo.caloriesPerDay");

        if (user.getId()==null) {
            try {
                User userDb =  service.getByEmail(user.getEmail());
                if (userDb!=null) {
                    errors.rejectValue("email", "Validation.userTo.email");
                }
            }catch (NotFoundException e){
            }

        }

        if (user.getCaloriesPerDay()!=null && (user.getCaloriesPerDay()<10||user.getCaloriesPerDay()>10000)) {
            errors.rejectValue("caloriesPerDay", "NotEmpty.userTo.caloriesPerDay");
        }


    }
}
