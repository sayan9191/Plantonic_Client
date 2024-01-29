package co.in.plantonic.firebaseClasses;

public class AddressItem {

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    public AddressItem(String userId, String fullName, String phoneNo, String pinCode, String state, String city, String area, String addressType, String email, String landmark, String specialInstruction) {
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.pinCode = pinCode;
        this.state = state;
        this.city = city;
        this.area = area;
        this.addressType = addressType;
        this.email = email;
        this.landmark = landmark;
        this.specialInstruction = specialInstruction;
    }

    public AddressItem() {
        this.userId = "";
        this.fullName = "";
        this.phoneNo = "";
        this.pinCode = "";
        this.state = "";
        this.city = "";
        this.area = "";
        this.addressType = "";
        this.email = "";
        this.landmark = "";
        this.specialInstruction = "";
    }

    private String userId;
    private String fullName;
    private String phoneNo;
    private String pinCode;
    private String state;
    private String city;
    private String area;
    private String addressType;
    private String email;
    private String landmark;
    private String specialInstruction;
}
