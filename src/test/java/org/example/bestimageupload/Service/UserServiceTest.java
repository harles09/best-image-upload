package org.example.bestimageupload.Service;

import org.example.bestimageupload.Model.Image;
import org.example.bestimageupload.Model.User;
import org.example.bestimageupload.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void saveUser() {
        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        ResponseEntity<String> response = userService.saveUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Username Created", response.getBody());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userService.getUserById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void deleteUser() {
        UUID id = UUID.randomUUID();

        doNothing().when(userRepository).deleteById(id);

        ResponseEntity<String> response = userService.deleteUser(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User has been Deleted", response.getBody());

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void getAllUser() {
        User user = new User();
        List<User> users = Collections.singletonList(user);

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userService.getAllUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());

        verify(userRepository, times(1)).findAll();
    }
}