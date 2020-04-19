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
                    currentUser = user_db.get();
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
        if (user.getPassword() != null)
            if (!user.getPassword().equals(currentUser.getPassword()) ||
                !user.comparePasswords())
                return "Details update failed. Change in password detected.\n";
            user.updatePassword(currentUser.getPassword());
            repository.save(user);
        return "Details update successful.\n" + user.getDetails();
    }
    @PutMapping("editpassword")
    public String editPassword(@RequestBody User user) {
        if (user.comparePasswords()) {
            currentUser.updatePassword(user.getPassword());
            repository.save(currentUser);
            return "Password update successful.\n" + currentUser.getDetails();
        }
        return "Password update failed. Passwords do not match.\n";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        if (repository.findById(id) != null && currentUser != null) {
            repository.deleteById(id);
            currentUser = null;
            return "User with id " + id + " is deleted.";
        }
        return "No user, found with " + id + " in the session. Delete failed.";
    }
    @GetMapping("/logout/{id}")
    public String logout(@PathVariable int id) {
        if (repository.findById(id) != null && currentUser != null) {
            currentUser = null;
            return "User with id " + id + " logged out.";
        }
        return "No user, found with " + id + " in the session. Log out failed.";
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
