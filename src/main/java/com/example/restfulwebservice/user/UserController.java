package com.example.restfulwebservice.user;

import lombok.RequiredArgsConstructor;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserDaoService service;
    static final String TEST = "tests";
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public Resource<User> retrieveUser(@PathVariable Integer id) throws UserNotFoundException {
        User user = service.findOne(id);

        checkUserExists(id, user);

        // TODO HATEOAS
        Resource<User> resource = new Resource<>(user);
        ControllerLinkBuilder linkTo = linkTo(
                methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }

    @PostMapping("/v1/users")
    public User createUserV1(@RequestBody User user) {
        return service.save(user);
    }


    @PostMapping("/v2/users")
    public ResponseEntity<User> createUserV2(@RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @PostMapping("/v3/users")
    public ResponseEntity<User> createUserV3(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable("userId") Integer id) {
        User user = service.deleteById(id);

        checkUserExists(id, user);
    }

    private void checkUserExists(Integer id, User user) throws UserNotFoundException {
        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }
}
