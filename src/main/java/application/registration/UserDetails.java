package application.registration;

import com.google.gson.annotations.SerializedName;

public class UserDetails {
    public static final String NAME="name";
    public static final String EMAIL_ID="email_id";
    public static final String PHONE_NO="phone_no";
    public static final String USER_ID="userid";

    @SerializedName(NAME)
    private String name;

    @SerializedName(EMAIL_ID)
    private String email;

    @SerializedName(PHONE_NO)
    private String phoneNo;


    
    public void validate() throws Exception{
        if(name==null){
            throw new RuntimeException("name cannot be null");
        }
        if(email==null){
            throw new RuntimeException("email cannot be null");
        }
        if(phoneNo==null){
            throw new RuntimeException("phone number cannot be null");
        }
    }

    @Override
    public String toString() {
        return "{"+NAME+":"+name+","+EMAIL_ID+":"+email+","+PHONE_NO+":"+phoneNo+"}";
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmailId(){
        return email;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public String getPhoneNo(){
        return phoneNo;
    }
}
