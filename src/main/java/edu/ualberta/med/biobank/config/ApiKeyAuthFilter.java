package edu.ualberta.med.biobank.config;

import edu.ualberta.med.biobank.services.UserService;
import edu.ualberta.med.biobank.util.LoggingUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
class ApiKeyAuthFilter extends OncePerRequestFilter {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    private static final String HEADER_NAME = "x-biobank-api-key";

    private UserService userService;

    public ApiKeyAuthFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        var apiKey = request.getHeader(HEADER_NAME);

        if (apiKey != null) {
            var userMaybe = userService.findByApiKey(apiKey);
            if (userMaybe.isLeft()) {
                logger.debug("invalid api key: %s".formatted(LoggingUtils.prettyPrintJson(userMaybe.getLeft().get())));
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }

            var user = userMaybe.getRight().get();
            ApiKeyAuthentication authentication = ApiKeyAuthentication.authenticated(user.username(), user.password());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
