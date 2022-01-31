package az.ibar.payday.ms.auth.service.impl;

import az.ibar.payday.ms.auth.exception.AuthException;
import az.ibar.payday.ms.auth.logger.SafeLogger;
import az.ibar.payday.ms.auth.model.LoginRequest;
import az.ibar.payday.ms.auth.service.AuthService;
import az.ibar.payday.ms.auth.util.JwtHelper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final SafeLogger logger = SafeLogger.getLogger(AuthServiceImpl.class);
    private static final String LDAP_ACCOUNT_NAME_ATTR = "TEST";

    private final LdapTemplate ldapTemplate;
    private final JwtHelper jwtHelper;

    public AuthServiceImpl(LdapTemplate ldapTemplate, JwtHelper jwtHelper) {
        this.ldapTemplate = ldapTemplate;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public String login(LoginRequest request) {

        logger.info("ActionLog.login.start Ldap authentication for user: {}", request.getUsername());

        final String username = request.getUsername();
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(LDAP_ACCOUNT_NAME_ATTR, username));
        logger.debug("About to authenticate user: {}", username);
        boolean authenticated = false;
        try {
            authenticated = ldapTemplate.authenticate("", filter.encode(), request.getPassword());
        } catch (Exception e) {
            logger.error("ldap login error", e);
        }
        logger.debug("Authenticate user: {} returned: {}", username, authenticated);

        if (!authenticated) {
            logger.error("ActionLog.login.error for user: {} is not authenticated", request.getUsername());
            throw new AuthException("incorrect username or password");
        }

        logger.info("ActionLog.login.success Ldap authentication for user: {}", request.getUsername());
        return jwtHelper.generateToken(request);
    }
}
