package waterplace.finalproj.model;

public class SupplierDistance {
    private Supplier supplier;
    private double distance;
    private String uid;

    public SupplierDistance(Supplier supplier, double distance, String uid) {
        this.supplier = supplier;
        this.distance = distance;
        this.uid = uid;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public double getDistance() {
        return distance;
    }

    public String getUid() {
        return uid;
    }
}
