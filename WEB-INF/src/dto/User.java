package dto;

import java.util.Base64;

public class User {
    String login;
    String password;
    
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public User(String log, String pass){
        login=log;
        password=pass;
    }
    public User(){

    }
    public String getToken(){
        String beforeConvert = this.login+":"+this.password;
        return  Base64.getEncoder().encodeToString(beforeConvert.getBytes());
    }
}
