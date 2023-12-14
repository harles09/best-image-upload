package org.example.bestimageupload.Controller;

import lombok.AllArgsConstructor;
import org.example.bestimageupload.Model.User;
import org.example.bestimageupload.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") UUID id){
        return userService.getUserById(id);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        return userService.getAllUser();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") UUID id){
        return userService.deleteUser(id);
    }
}
