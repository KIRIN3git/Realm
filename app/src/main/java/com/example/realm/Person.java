package com.example.realm;

import java.util.StringJoiner;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Person extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;
    private RealmList<Dog> dogs; // Declare one-to-many relationships

    public RealmList<Dog> getDogs() {
        return dogs;

    }

    // ... Generated getters and setters ...
}