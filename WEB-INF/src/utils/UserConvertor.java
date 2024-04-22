package utils;

import java.util.Base64;

import dao.UserDao;
import dto.User;
import jakarta.servlet.http.HttpServletRequest;

public class UserConvertor {
    public static boolean canAccess(HttpServletRequest req){
        
        String authorization = req.getHeader("Authorization");
        System.out.println(authorization);
        if (authorization == null || !authorization.startsWith("Basic"))
        return false;
        // on décode le token
        System.out.println(authorization);
        
        String token = authorization.substring("Basic".length()).trim();
        byte[] base64 = Base64.getDecoder().decode(token);
        String[] lm = (new String(base64)).split(":");
        String login = lm[0];
        String pwd = lm[1];
        User usr = new User(login,pwd);
        System.out.println(login + ":"+pwd);
        return new UserDao().isAdmin(usr);
    }
}
