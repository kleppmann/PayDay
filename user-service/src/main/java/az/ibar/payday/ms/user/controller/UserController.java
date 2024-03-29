package az.ibar.payday.ms.user.controller;

import az.ibar.payday.ms.user.model.UserDto;
import az.ibar.payday.ms.user.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }
}
