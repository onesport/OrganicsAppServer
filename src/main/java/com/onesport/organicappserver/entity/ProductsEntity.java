package com.onesport.organicappserver.entity;

import javax.persistence.*;

@Entity
@Table(name = "products")
@org.hibernate.annotations.NamedQueries({
@org.hibernate.annotations.NamedQuery(name = "ProductsEntity.findAll", query = "SELECT d FROM ProductsEntity d"),
@org.hibernate.annotations.NamedQuery(name = "ProductsEntity.getAllProducts", query = "SELECT u FROM ProductsEntity u WHERE u.productstatus = :status")
})
public class ProductsEntity {
    public enum ProductAvailability{
        AVAILABLE,OUTOFSTOCK,WITHDRAWN
    }
    @Id
    @Column(name = "productid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer productId;

    @Column(name = "productname")
    private String productname;

    @Column(name = "productstatus")
    @Enumerated(EnumType.STRING)
    private ProductAvailability productstatus;

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

    public ProductAvailability getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(ProductAvailability productstatus) {
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

    //    INSERT INTO products (productname, productstatus,productdescription) VALUES ('oil','OUTOFSTOCK','hai');
//    public static String createProductsTableQuery(){
//        return "create table products(" +
//                "   productid INT NOT NULL AUTO_INCREMENT," +
//                "   productname VARCHAR(50) NOT NULL," +
//                "   productstatus enum('AVAILABLE','OUTOFSTOCK','WITHDRAWN') default 'AVAILABLE'," +
//                "   productdescription VARCHAR(1000) NOT NULL," +
//                "   PRIMARY KEY (productid)" +
//                ");";
//    }
