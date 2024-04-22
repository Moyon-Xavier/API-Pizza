package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dto.User;
import utils.DS;

public class UserDao {
    public User findByLogin(String log , String password){
        User usr = null;
        Connection con =null;
        try{
            con = DS.getConnection();
            PreparedStatement st = con.prepareStatement("select * from usersRest WHERE login = ? and password = ?");
            st.setString(1, log);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
               usr =  new User(log, password);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        try{
            con.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return usr;
    }
    public User findByLogin(User usr){
        return this.findByLogin(usr.getLogin(), usr.getPassword());
    }
    public String generateToken(User usr){
        if (findByLogin(usr)!=null){
            return usr.getToken();
        }
        return null;
    }
    public boolean isAdmin(User usr){
        return findByLogin(usr)!=null;
    }
}
