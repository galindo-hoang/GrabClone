package com.example.authentication.exception;

import com.example.authentication.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UsernameAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final User user;

    public UsernameAlreadyExistException(String message) {
        super(message);
        user = null;
    }

    public UsernameAlreadyExistException(String message, User user) {
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
