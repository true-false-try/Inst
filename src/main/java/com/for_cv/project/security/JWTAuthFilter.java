package com.for_cv.project.security;

import com.for_cv.project.entity.User;
import com.for_cv.project.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static com.for_cv.project.security.SecurityConstants.HEADER_STRING;
import static com.for_cv.project.security.SecurityConstants.TOKEN_PREFIX;

@Log4j2
@Component
@AllArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private JWTTokenProvider jwtTokenProvider;
    private CustomUserDetailService customUserDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validationToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt); // get user id from JWT
                User userDetails = customUserDetailService.findUserId(userId); // find this user in db

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // save user permits in security context in authentication
            }
        } catch (Exception ex) {
            log.error("Couldn't set user authentication");
        }

            filterChain.doFilter(request, response); // insert in chain request - response
    }

    /**
     * Method gives to us JWT token from request user.
      * @param request
     * @return
     */
   private String getJwtFromRequest(HttpServletRequest request) {
        String bearToken = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(bearToken) && bearToken.startsWith(TOKEN_PREFIX)) {
            return bearToken.split(" ")[1]; // return string after "Barer "
        }
        return null;

   }
}
