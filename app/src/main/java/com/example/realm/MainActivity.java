package com.example.realm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.UUID;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



// Use them like regular java objects
        Dog dog = new Dog();
        dog.setName("Rex");
        dog.setAge(1);

// Initialize Realm (justhv once per application)
        Realm.init(getApplicationContext());



// Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

// Query Realm for all dogs younger than 2 years old
        final RealmResults<Dog> puppies = realm.where(Dog.class).lessThan("age", 2).findAll();
        puppies.size(); // => 0 because no dogs have been added to the Realm yet

        Log.w("DEBUG_DATA1","puppies.size() " + puppies.size());

// Persist your data in a transaction
        realm.beginTransaction();
        final Dog managedDog = realm.copyToRealm(dog); // Persist unmanaged objects
        Person person = realm.createObject(Person.class, 125); // Create managed objects directly
        person.getDogs().add(managedDog);
        realm.commitTransaction();

        Log.w("DEBUG_DATA2","puppies.size() " + puppies.size());

// Listeners will be notified when data changes
        puppies.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Dog>>() {
            @Override
            public void onChange(RealmResults<Dog> results, OrderedCollectionChangeSet changeSet) {
                // Query results are updated in real time with fine grained notifications.
                changeSet.getInsertions(); // => [0] is added.


                Log.w("DEBUG_DATA","change");
            }
        });


// Asynchronously update objects on a background thread
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Dog dog = bgRealm.where(Dog.class).equalTo("age", 1).findFirst();
                dog.setAge(3);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Original queries and Realm objects are automatically updated.
                Log.w("DEBUG_DATA","puppies.size() " + puppies.size()); // => 0 because there are no more puppies younger than 2 years old
                Log.w("DEBUG_DATA","managedDog.getAge()　" + managedDog.getAge());   // => 3 the dogs age is updated


            }
        });
    }
}
