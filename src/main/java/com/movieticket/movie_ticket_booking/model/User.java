package com.movieticket.movie_ticket_booking.model;

public class User {
    private String firstName, lastName, email, gender, nic, address, mobile, password;

    public User() {}

    public User(String firstName, String lastName, String email, String gender,
                String nic, String address, String mobile, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.nic = nic;
        this.address = address;
        this.mobile = mobile;
        this.password = password;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
