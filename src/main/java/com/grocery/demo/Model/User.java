package com.grocery.demo.Model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.Set;


@Entity
public class User{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yy-MM-dd")
    private Date dob;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @NotEmpty(message = "*Please fill in the username")
    private String fname;
    @NotEmpty(message = "*Please fill in the lastname")
    private String lname;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @OneToMany
    private Set<Role> roles;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

// , columnDefinition="ENUM('CLIENT','ADMIN','SUPPLIER')"

    @NotEmpty(message = "*Provide a valid email")
    @Email
    private String email;
    @NotEmpty(message = "*Username can not be left empty")
    @Column(nullable = false, unique = true)
    @Size(min = 8, max = 20)
    private String uname;
    @NotEmpty(message = "*Password should not be less than 8 or more than 26")
    @Column (length = 100)
    //@Size(min = 8, max = 26)
    private String pword;
//
//    @Pattern(regexp="^+(?:254)?(7(?:(?:[12][0-9])|(?:0[0-8])|(9[0-2]))[0-9]{6})$", message = "" +
//            "the phone number must start with country code!")
    @NotEmpty(message = "Phone Number Can not be empty.")
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    //    @Enumerated(EnumType.STRING)
//    @Column (name = "user_type", nullable = false)
//    private UserType utype;
//
//    @Enumerated(EnumType.STRING)
//   //@Column (name = "user_type", nullable = false)
//    public UserType getUtype() {
//
//        return this.utype;
//    }
//
//    @Enumerated(EnumType.STRING)
//    public void setUtype(UserType utype) {
//
//        this.utype = utype;
//    }
}
