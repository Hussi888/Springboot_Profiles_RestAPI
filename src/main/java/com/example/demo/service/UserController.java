/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserRepository repository;
    private User currentUser;
    
    @PostMapping("/signup")
    public String saveUser(@RequestBody User user) {
        if (user.comparePasswords()) {
            repository.save(user);
            currentUser = user;
            return "Signup Successful.\n" + user.getDetails();
        }
        return "Signup Failed. Passwords do not match.\n";
    }
    @PostMapping("/login")
    public String checkUser(@RequestBody User user) {
        Optional<User> user_db = repository.findById(user.getId());
        if (user_db != null) {
            if (user_db.get().getEmail().equals(user.getEmail())) {
                if (user_db.get().getPassword().equals(user.getPassword())) {
                    return "Login Successful.\n" + user_db.get().getDetails();
                }
                else {
                    return "Login failed. Incorrect Password.";
                }
            }
            else {
                return "Login failed. Incorrect Email."+
                        user_db.get().getEmail() + "\n" +
                        user.getEmail();
            }
        }
        else
            return "Login failed. No such user found.";
    }
    @PutMapping("/editprofile")
    public String editProfile(@RequestBody User user) {
        if (user.getPassword() != currentUser.getPassword() ||
            !user.comparePasswords())
            return "Details update failed. Change in password detected.\n";
        repository.save(user);
        return "Details update successful.\n" + user.getDetails();
    }
    @PutMapping("editpassword")
    public String editPassword(@RequestBody User user) {
        if (user.comparePasswords()) {
            repository.save(user);
            return "Password update successful.\n" + user.getDetails();
        }
        return "Password update failed. Passwords do not match.\n";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        repository.deleteById(id);
        return "User deleted with id: " + id;
    }

    @GetMapping("/findAllUsers")
    public List<User> getUsers() {
        return repository.findAll();
    }
    @GetMapping("/findAllUsers/{id}")
    public Optional<User> getUser(@PathVariable int id) {
        return repository.findById(id);
    }

}
