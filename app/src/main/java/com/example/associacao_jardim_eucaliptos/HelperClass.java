package com.example.associacao_jardim_eucaliptos;

public class HelperClass {
    private String name;
    private String email;
    private String password;
     boolean admin;

    public HelperClass() {
    }

    public HelperClass(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.admin = false;
    }

    public HelperClass(String name, String email, String password, boolean admin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.admin = false;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
