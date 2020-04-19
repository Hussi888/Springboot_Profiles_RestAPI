/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 *
 * @author hussi
 */
@Getter
@Setter
@ToString

@Document(collection="User")
public class User {
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDetails() {
        return "\n" + id + "\n" + name + "\n" + email + "\n" + password + "\n" + gender + "\n" + dob;
    }

    public boolean comparePasswords() {
        if (password.equals(confirmPassword))
            return true;
        else
            return false;
    }
    public void updatePassword(String _password) {
        password = _password;
        confirmPassword = _password;
    }

    @Id
    private int id;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String gender;
    private String dob;

    public int getId() {
        return id;
    }
}
