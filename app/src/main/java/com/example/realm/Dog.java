package com.example.realm;

import io.realm.RealmObject;

// Define your model class by extending RealmObject
public class Dog extends RealmObject {
    private String name;
    private int age;

    public void setName(String name) {
        this.name = name;
    }


    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    // ... Generated getters and setters ...
}
