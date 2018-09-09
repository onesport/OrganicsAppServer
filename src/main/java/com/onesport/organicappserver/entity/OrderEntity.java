package com.onesport.organicappserver.entity;


import javax.persistence.*;

@Entity
@Table(name = "orders")
public class OrderEntity {

    public enum OrderStatus{
        ORDERPLACED,ORDERDELIVERED,ORDERCANCELED
    }
    @Id
    @Column(name = "orderid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer orederId;

    @Column(name = "orderitems")
    private String orderitems;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserDetailsEntity userId;

    @Column(name = "orderstatus")
    @Enumerated(EnumType.STRING)
    private OrderStatus OrderStatustatus;

    @Column(name = "orderdetails")
    private String orderDetails;

    public Integer getOrederId() {
        return orederId;
    }

    public void setOrederId(Integer orederId) {
        this.orederId = orederId;
    }

    public String getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(String orderitems) {
        this.orderitems = orderitems;
    }

    public OrderStatus getOrderStatustatus() {
        return OrderStatustatus;
    }

    public void setOrderStatustatus(OrderStatus orderStatustatus) {
        OrderStatustatus = orderStatustatus;
    }

    public UserDetailsEntity getUserId() {
        return userId;
    }

    public void setUserId(UserDetailsEntity userId) {
        this.userId = userId;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }
}
