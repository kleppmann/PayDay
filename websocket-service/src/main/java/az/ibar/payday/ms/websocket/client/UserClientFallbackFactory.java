package az.ibar.payday.ms.websocket.client;

import az.ibar.payday.ms.websocket.logger.SafeLogger;
import az.ibar.payday.ms.websocket.model.UserDto;
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
