/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermarketmanagementsystem;

import java.sql.Date;

/**
 *
 * @author Wahyu Isnan
 */
public class employeeData {
    
    public String employeeId;
    public String password;
    public String firstName;
    public String lastName;
    public String gender;
    public Date date;
    
    public employeeData(String employeeId, String password, String firstName, String lastName, String gender, Date date) {
        this.employeeId = employeeId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.date = date;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getGender() {
        return gender;
    }
    
    public Date getDate() {
        return date;
    }
    
}
