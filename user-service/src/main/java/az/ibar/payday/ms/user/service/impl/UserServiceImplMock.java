package az.ibar.payday.ms.user.service.impl;

import az.ibar.payday.ms.user.model.UserDto;
import az.ibar.payday.ms.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplMock implements UserService {

    @Override
    public UserDto getById(Long userId) {
        return UserDto.builder()
                .username("kamal")
                .email("kamallhome64@gmail.com")
                .name("Kamal")
                .surname("Novruz")
                .build();
    }
}
