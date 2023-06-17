package waterplace.finalproj.model;

import java.io.Serializable;

public class Results extends Product implements Serializable  {

    private Product product;
    private String suid;
    private String prodOwner;

    public Results(Product product, String uid){
        this.product = product;
        this.suid = suid;
    }

    public Product getProduct() { return product; }

    public String getUid() { return suid; }

    public void setUid(String uid) {
        this.suid = uid;
    }


    public String getProdOwner() { return prodOwner; }

    public void setProdOwner(String prodOwner) { this.prodOwner = prodOwner; }
}


