package edu.ualberta.med.biobank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyAuthentication.class);

    private final String username;

    private final String password;

    private ApiKeyAuthentication() {
        super(AuthorityUtils.NO_AUTHORITIES);
        super.setAuthenticated(false);
        this.username = null;
        this.password = null;
    }

    private ApiKeyAuthentication(String username, String password) {
	super(AuthorityUtils.createAuthorityList("ROLE_user"));
	super.setAuthenticated(true);
        this.username = username;
        this.password = password;
    }

    public static ApiKeyAuthentication authenticated(String username, String password) {
	return new ApiKeyAuthentication(username, password);
    }

    public static ApiKeyAuthentication unauthenticated() {
	return new ApiKeyAuthentication();
    }

    @Override
    public Object getCredentials() {
        return this.getPassword();
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new RuntimeException("DON'T CHANGE THE AUTH STATUS 😱");
    }
}
