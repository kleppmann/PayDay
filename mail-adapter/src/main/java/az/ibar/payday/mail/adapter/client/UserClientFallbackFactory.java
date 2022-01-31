package az.ibar.payday.mail.adapter.client;

import az.ibar.payday.mail.adapter.logger.SafeLogger;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    private static final SafeLogger logger = SafeLogger.getLogger(UserClientFallbackFactory.class);

    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {

            @Override
            public UserDto getUserDto(Long id) {
                return null;
            }
        };
    }
}
