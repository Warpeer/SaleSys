package com.example.salesys.models;

public class Employee {

    private int id;
    private String email;
    private String password;
    private String role;
    private String hire_date;

    public Employee(int id, String email, String password, String role, String hire_date) {
        this.id=id;
        this.email = email;
        this.password = password;
        this.role=role;
        this.hire_date=hire_date;
    }
    public Employee(){

    }
    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHire_date() {
        return hire_date;
    }

    public void setHire_date(String hire_date) {
        this.hire_date = hire_date;
    }
}
