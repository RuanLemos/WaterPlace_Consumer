package waterplace.finalproj.model;

public class Results {

    private Product product;

    private String uid;

    public Results(Product product, String uid){
        this.product = product;
        this.uid = uid;
    }

    public Product getProduct() { return product; }

    public String getUid() { return uid; }
}
