package models;

public class LoginEntry {
    public String username;
    public String password;

    public LoginEntry(){}

    public  LoginEntry(String u, String p){
        username = u;
        password = p;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
