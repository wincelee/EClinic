package manu.apps.eclinic.classes;

public class User {

    private String email;
    private String userName;
    private String phoneNumber;
    private String county;
    private String latitude;
    private String longitude;

    public User() {

    }

    public User(String email, String userName, String phoneNumber, String county) {
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.county = county;
    }

    // Getters

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCounty() {
        return county;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    // Setters

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
