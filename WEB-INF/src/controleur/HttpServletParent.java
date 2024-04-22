package controleur;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class HttpServletParent extends HttpServlet{

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, res);
        }else {
            super.service(req, res);
        }
    }
    public abstract void doPatch(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException;
    
    
}
