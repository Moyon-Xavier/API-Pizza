package controleur;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOPizza;
import dto.Pizza;
import dto.PizzaWithIntIng;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.UserConvertor;

@WebServlet("/pizza/*")
public class PizzaRestAPI extends HttpServletParent{
    
    DAOPizza dao = new DAOPizza();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            Collection<Pizza> l = dao.findAll();
            String jsonstring = objectMapper.writeValueAsString(l);
            out.println(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length > 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Pizza piz = null;
        String id = splits[1];
        if (splits.length>2 ){
            if (splits[2].equals("nom")){
                String name = dao.findNameById(Integer.parseInt(id));
                if (name==null){
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return ; 
                }else{
                    out.println(objectMapper.writeValueAsString(name));
                    return ;
                }
            }else if(splits[2].equals("prixfinal")){
                piz=dao.findById(Integer.parseInt(id));
                out.println(piz.getFinalPrice());
                return;  
            }else{
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
            }
        }
        piz = dao.findById(Integer.parseInt(id));
        if (piz==null){
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.println(objectMapper.writeValueAsString(piz));
        out.close();
        return ;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        if (!UserConvertor.canAccess(req)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Pizza result =null;
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            String data = req.getReader().readLine();
            PizzaWithIntIng piz = objectMapper.readValue(data, PizzaWithIntIng.class);
            result = dao.save(piz);
        }else{
            System.out.println(info);
            String[] splits = info.split("/");
            int i = 0;
            for(String s : splits){
                System.out.println("index : " + i + " valeur : " + s);
                i++;
            }
            if (splits.length==3){
                String idPizza = splits[1];
                String idIngre = splits[2];
                result = dao.addIngredient(Integer.parseInt(idPizza),Integer.parseInt(idIngre));
            }else{
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        if(result==null){
            res.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }else{
            out.println(objectMapper.writeValueAsString(result)); 
            res.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        out.close();
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        if (!UserConvertor.canAccess(req)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            
            return;
        }
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = info.split("/");
        int i = 0;
        for(String s : splits){
            System.out.println("index : " + i + " valeur : " + s);
            i++;
        }
        boolean result=false;
        if (splits.length==2){
            String idPizza = splits[1];
             result = dao.deleteById(Integer.parseInt(idPizza));
        }else if(splits.length==3){
            String idPizza = splits[1];
            String idIng = splits[2];
            result = dao.deletePizzaIngredientById(Integer.parseInt(idPizza),Integer.parseInt(idIng));
        }else if (splits.length > 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Pizza piz = null;
        if (!result){
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_ACCEPTED);
        out.println(objectMapper.writeValueAsString(piz));
        out.close();
        return;
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!UserConvertor.canAccess(req)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = info.split("/");
        Pizza piz =null;
        if (splits.length==2){
            String data = req.getReader().readLine();
            PizzaWithIntIng putPizza = objectMapper.readValue(data, PizzaWithIntIng.class);
            String idPizza = splits[1];
            piz = dao.doPatch(Integer.parseInt(idPizza),putPizza);
        }else{
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (piz==null){
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_ACCEPTED);
        out.println(objectMapper.writeValueAsString(piz));
        out.close();
        return;
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!UserConvertor.canAccess(req)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = info.split("/");
        Pizza piz =null;
        if (splits.length==2){
            String data = req.getReader().readLine();
            PizzaWithIntIng putPizza = objectMapper.readValue(data, PizzaWithIntIng.class);
            String idPizza = splits[1];
            piz = dao.put(Integer.parseInt(idPizza),putPizza);
        }else{
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (piz==null){
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_ACCEPTED);
        out.println(objectMapper.writeValueAsString(piz));
        out.close();
        return;
    }


}
