package co.in.plantonic.firebaseClasses;

public class UserItem {

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }


    public UserItem(String phoneNo, String firstName, String lastName, String email, String userId, String authenticationType) {
        this.phoneNo = phoneNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
        this.authenticationType = authenticationType;
    }

    public UserItem() {
        this.phoneNo = "";
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.userId = "";
        this.authenticationType = "";
    }

    String phoneNo;
    String firstName;
    String lastName;
    String email;
    String userId;
    String authenticationType;
}
