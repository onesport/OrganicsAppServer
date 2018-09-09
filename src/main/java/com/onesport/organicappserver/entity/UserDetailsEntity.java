package com.onesport.organicappserver.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;

@Entity
@Table(name = "userdetails")
@org.hibernate.annotations.NamedQueries({
        @org.hibernate.annotations.NamedQuery(name = "UserDetailsEntity.findAll", query = "SELECT d FROM UserDetailsEntity d"),
        @org.hibernate.annotations.NamedQuery(name = "UserDetailsEntity.findByEmail", query = "SELECT u FROM UserDetailsEntity u WHERE u.email = :email")
})
public class UserDetailsEntity {
    private static final String NAME="name";
    private static final String EMAIL_ID="email_id";
    private static final String PHONE_NO="phone_no";
    private static final String USER_ID="userid";


    @Id
    @Column(name = "userid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "name")
    @SerializedName(NAME)
    private String name;

    @Column(name = "email")
    @SerializedName(EMAIL_ID)
    private String email;

    @Column(name = "phone_no")
    @SerializedName(PHONE_NO)
    private String phoneno;

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

    public void validate() throws Exception{
        if(name==null){
            throw new RuntimeException("name cannot be null");
        }
        if(email==null){
            throw new RuntimeException("email cannot be null");
        }
        if(phoneno==null){
            throw new RuntimeException("phone number cannot be null");
        }
        if(userId==null){
            throw new RuntimeException("user id cannot be null");
        }
    }

    @Override
    public String toString() {
        return "{"+NAME+":"+name+","+EMAIL_ID+":"+email+","+PHONE_NO+":"+phoneno+","+USER_ID+":"+userId;
    }
}

//    public static String createUserTableQuery(){
//        return "create table userdetails(" +
//                "   userid VARCHAR(20) NOT NULL," +
//                "   name VARCHAR(50) NOT NULL," +
//                "   email VARCHAR(50) NOT NULL," +
//                "   phone_no VARCHAR(10) NOT NULL," +
//                "   PRIMARY KEY (userid)" +
//                ");";
//    }

