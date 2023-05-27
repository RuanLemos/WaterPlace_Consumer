package waterplace.finalproj.model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String name;
    private String phone;
    private Date birthdate;
    private List<Address> addresses;

    private List<String> requests;
    private static User instance;
    public List<String> getRequests() {
        return requests;
    }

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }

    public User() {}

    private User(DataSnapshot dataSnapshot) {
        this.name = dataSnapshot.child("name").getValue().toString();
        this.phone = dataSnapshot.child("phone").getValue().toString();
        GenericTypeIndicator<ArrayList<Address>> addressesType = new GenericTypeIndicator<ArrayList<Address>>() {};
        this.addresses = dataSnapshot.child("Addresses").getValue(addressesType);
    }

    public static User getInstance() {
        return instance;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
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
