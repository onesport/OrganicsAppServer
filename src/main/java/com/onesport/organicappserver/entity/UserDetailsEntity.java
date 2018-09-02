package com.onesport.organicappserver.entity;

import javax.persistence.*;

@Entity
@Table(name = "userdetails")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "UserDetailsEntity.findAll", query = "SELECT d FROM UserDetailsEntity d"),
        @org.hibernate.annotations.NamedQuery(name = "UserDetailsEntity.findByEmail", query = "SELECT u FROM UserDetailsEntity u WHERE u.email = :email")
})
public class UserDetailsEntity {

    @Id
    @Column(name = "userid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "name")
    private String name;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneno;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneno = phoneNo;
    }

    @Column(name = "email")
    private String email;

    @Column(name = "phone_no")
    private String phoneno;
}
