package com.example.be.exception;

import com.example.be.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UsernameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final User user;

    public UsernameNotFoundException(String message) {
        super(message);
        user = null;
    }

    public UsernameNotFoundException(String message, User user) {
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
