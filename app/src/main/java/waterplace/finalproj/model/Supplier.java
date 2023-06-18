package waterplace.finalproj.model;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Supplier implements Serializable{
    private String name;
    private Long cnpj;
    private String phone;
    private Address address;
    private List<Product> products;
    private double rating;

    // Cria um construtor com o padrão singleton
    private static Supplier instance;

    public Supplier(){
        this.rating = 0.0;
    }

    // Construtor privado para o padrão singleton

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCnpj() {
        return cnpj;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Product> getProducts() {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public static Supplier getInstance() {
        return instance;
    }

    public static void setInstance(Supplier instance) {
        Supplier.instance = instance;
    }
}
