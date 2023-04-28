package anu.capstone.showyourthings.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "barcode")
public class BarcodeInfo {

    @Id
    private String barcode;
    private String product;

    public BarcodeInfo(String barcode, String product) {

        this.barcode = barcode;
        this.product = product;
    }

    public BarcodeInfo() {

    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barocde) {
        this.barcode = barocde;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String proudct) {
        this.product = proudct;
    }

}
