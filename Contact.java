package com.example.user.listviewcontacts;

public class Contact {
    String name, phone, sex;

    @Override
    public String toString() {
        return (name + " " + phone + " " + sex);
    }
}
