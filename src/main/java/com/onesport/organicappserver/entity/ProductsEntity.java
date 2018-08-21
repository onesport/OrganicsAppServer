package com.onesport.organicappserver.entity;

import javax.persistence.*;

@Entity
@Table(name = "products")
@NamedQuery(name = "ProductsEntity.findAll", query = "SELECT d FROM ProductsEntity d")
public class ProductsEntity {

    @Id
    @Column(name = "productid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer productId;

    @Column(name = "productname")
    private String productname;

    @Column(name = "productstatus")
    private String productstatus;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(String productstatus) {
        this.productstatus = productstatus;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    @Column(name = "productdescription")
    private String productdescription;
}
