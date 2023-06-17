package waterplace.finalproj.model;

import java.io.Serializable;

public class Results implements Serializable {

    private Product product;
    private String uid;
    private String prodOwner;

    public Results(Product product, String uid){
        this.product = product;
        this.uid = uid;
    }

    public Product getProduct() { return product; }

    public String getUid() { return uid; }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getProdOwner() { return prodOwner; }

    public void setProdOwner(String prodOwner) { this.prodOwner = prodOwner; }
}


