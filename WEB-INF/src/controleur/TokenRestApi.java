package controleur;

import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.UserDao;
import dto.Token;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/users/token")
public class TokenRestApi extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String data = req.getReader().readLine();
        User usr = objectMapper.readValue(data, User.class);
        UserDao base = new UserDao();
        String token = base.generateToken(usr);
        
        if (token!=null){
            
            out.println(objectMapper.writeValueAsString(new Token(token)));
            
            out.close();
        }else{
            res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
        return ;
    }
}
