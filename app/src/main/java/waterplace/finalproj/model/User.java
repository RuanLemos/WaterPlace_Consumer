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
    private List<Address> addresses;

    private List<String> requests;
    private static User instance;
    private static UserListener listener;
    public List<String> getRequests() {
        return requests;
    }

    public static void setListener(UserListener listener){
        User.listener = listener;
    }


    public void setRequests(List<String> requests) {
        this.requests = requests;
    }

    public User() {}

    private User(DataSnapshot dataSnapshot, String uid) {
        this.addresses = new ArrayList<>();
        this.name = dataSnapshot.child("name").getValue().toString();
        this.phone = dataSnapshot.child("phone").getValue().toString();
        DatabaseReference addressesRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Addresses");
        this.birthdate = dataSnapshot.child("birthdate").getValue(Date.class);
        addressesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                    Address address = addressSnapshot.getValue(Address.class);
                    System.out.println(address.getLatitude());
                    addresses.add(address);
                }
                if (listener != null) {
                    System.out.println("FILHA DA PUTAAAA");
                    listener.onDataLoaded(instance);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("puts", "deu merda");
            }
        });

        System.out.println("testehfxkjcnvj");

        //GenericTypeIndicator<ArrayList<Address>> addressesType = new GenericTypeIndicator<ArrayList<Address>>() {};
        //this.addresses = dataSnapshot.child("Addresses").getValue(addressesType);
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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}