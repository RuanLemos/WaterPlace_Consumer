package waterplace.finalproj.model;

public class SupplierDistance {
    private Supplier supplier;
    private double distance;

    public SupplierDistance(Supplier supplier, double distance) {
        this.supplier = supplier;
        this.distance = distance;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public double getDistance() {
        return distance;
    }
}
