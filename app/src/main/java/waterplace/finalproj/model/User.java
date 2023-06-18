package waterplace.finalproj.model;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.util.JsonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import waterplace.finalproj.listener.UserListener;

public class User {
    private String name;
    private String phone;
    private Date birthdate;
    private Address address;
    private static User instance;

    public User() {}

    private User(DataSnapshot dataSnapshot, String uid) {
        this.name = dataSnapshot.child("name").getValue().toString();
        this.phone = dataSnapshot.child("phone").getValue().toString();
        DatabaseReference addressesRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Addresses");
        this.birthdate = dataSnapshot.child("birthdate").getValue(Date.class);
        this.address = dataSnapshot.child("address").getValue(Address.class);
    }

    public static User getInstance() {
        return instance;
    }

    public static void setInstance(DataSnapshot snapshot, String uid) {
        instance = new User(snapshot, uid);
        System.out.println("INFERNOOOO");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}