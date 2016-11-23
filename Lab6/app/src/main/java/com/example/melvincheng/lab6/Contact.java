package com.example.melvincheng.lab6;

import android.content.Intent;

/**
 * Created by melvincheng on 01/11/16.
 */
public class Contact {
    int id;
    String firstName;
    String lastName;
    String phone;

    public Contact(int id, String firstName, String lastName, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String toString() {
        return String.valueOf(this.id) + ", " + this.firstName + ", " + this.lastName + ", " + this.phone;
    }
}
