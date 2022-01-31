package az.ibar.payday.ms.websocket.client;

import az.ibar.payday.ms.websocket.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Primary
@FeignClient(value = "user-service", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @GetMapping(value = "/users/{id}")
    UserDto getUserDto(@PathVariable Long id);
}
