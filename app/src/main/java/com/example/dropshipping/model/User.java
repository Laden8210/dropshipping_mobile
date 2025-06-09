package com.example.dropshipping.model;

import org.json.JSONObject;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String birthdate;
    private String password;
    private String confirmPassword;
    private String gender;

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", firstName);
            jsonObject.put("last_name", lastName);
            jsonObject.put("email", email);
            jsonObject.put("phone_number", phone);
            jsonObject.put("birthdate", birthdate);
            jsonObject.put("password", password);
            jsonObject.put("confirm_password", confirmPassword);
            jsonObject.put("gender", gender);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
