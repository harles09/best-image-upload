package org.example.bestimageupload.Service;

import lombok.AllArgsConstructor;
import org.example.bestimageupload.Model.User;
import org.example.bestimageupload.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;

    public ResponseEntity<String> saveUser(User user){
        user.setLastActive(new Timestamp(System.currentTimeMillis()));
        Optional<User> existingUser = userRepository.findUserByUsername(user.getUsername());
        if(existingUser.isPresent()){
            return new ResponseEntity<>("Sorry, Username already taken",HttpStatus.CREATED);
        }else{
            userRepository.save(user);
            return new ResponseEntity<>("Username Created",  HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> getUserById(UUID id){
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isPresent()){
            return new ResponseEntity<>(existingUser.get(),HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("User not found",  HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> deleteUser(UUID id){
        userRepository.deleteById(id);
        return new ResponseEntity<>("User has been Deleted",HttpStatus.OK);
    }

    public ResponseEntity<List<User>> getAllUser(){
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }
}