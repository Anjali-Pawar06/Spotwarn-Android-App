package com.example.spotwarn;

public class PoliceModel {
    private String designation, email, name, phone;

    public PoliceModel() { } // Firebase needs empty constructor

    public PoliceModel(String designation, String email, String name, String phone) {
        this.designation = designation;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public String getDesignation() { return designation; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
}