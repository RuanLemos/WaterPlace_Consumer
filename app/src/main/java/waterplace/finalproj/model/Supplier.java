package waterplace.finalproj.model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private String name;
    private Long cnpj;
    private String phone;
    private Address address;
    private List<Product> products;
    private String uid;

    // Cria um construtor com o padrão singleton
    private static Supplier instance;

    // Construtor privado sem parâmetros para o padrão singleton
    public Supplier(){}

    // Construtor privado para o padrão singleton

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    public String getUid(){
        return uid;
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
}
