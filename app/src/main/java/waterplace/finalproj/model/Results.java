package waterplace.finalproj.model;

import java.io.Serializable;

public class Results extends Product implements Serializable  {

    private SupplierDistance supplierDistance;
    private String supId;

    public Results(SupplierDistance supplier, String supId) {
        this.supplierDistance = supplier;
        this.supId = supId;
    }

    public SupplierDistance getSupplier() {
        return supplierDistance;
    }

    public void setSupplier(SupplierDistance supplier) {
        this.supplierDistance = supplier;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }
}


