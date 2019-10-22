package com.grocery.demo.ExceptionHandling;

import com.nimbusds.oauth2.sdk.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@EnableWebMvc
public class CentralExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class})
    public void handleUsernameNotFound(UserNameNotFoundException exception, HttpServletResponse response)
        throws IOException{
        response.sendError( HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

}
