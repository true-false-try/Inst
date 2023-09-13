package com.for_cv.project.security;

import com.for_cv.project.payload.response.InvalidLoginResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.for_cv.project.security.SecurityConstants.CONTENT_TYPE;

/**
 * This class has worked to catch 401 error, if our user will want to get security resources without authorization.
 */

@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        InvalidLoginResponse invalidLoginResponse = new InvalidLoginResponse();
        String jsonLoginResponse = new Gson().toJson(invalidLoginResponse);
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(jsonLoginResponse);
    }
}
