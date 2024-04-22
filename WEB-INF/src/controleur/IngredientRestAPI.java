package controleur;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOIngredient;
import dto.Ingredient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.UserConvertor;

@WebServlet("/ingredient/*")
public class IngredientRestAPI extends HttpServlet{
    
    DAOIngredient dao = new DAOIngredient();

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, res);
        }else if(req.getMethod().equalsIgnoreCase("PUT")){
            doPut(req, res);
        }else{
            super.service(req, res);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        //out.println("retour d'un GET");
        
        if (info == null || info.equals("/")) {
            Collection<Ingredient> l = dao.findAll();
            String jsonstring = objectMapper.writeValueAsString(l);
            out.println(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        //out.println("Requete : " + info);
       
        /*for (int i = 0 ; i<splits.length;i++){
            out.println("Parameter " + i + " : " + splits[i] + "  ");
        }/* */
        if (splits.length > 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        Ingredient in =null;
        String id = splits[1];
        if (splits.length>2 ){
            if (splits[2].equals("name")){
                String name = dao.findNameById(Integer.parseInt(id));
                if (name==null){
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return ; 
                }
                
                out.println(objectMapper.writeValueAsString(name));
                return ;
            }else{
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            
        }
        in = dao.findById(Integer.parseInt(id));
        if (in==null){
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.println(objectMapper.writeValueAsString(in));
        return;
    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        res.setContentType("application/json;charset=UTF-8");
        if (!UserConvertor.canAccess(req)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            
            return;
        }
        PrintWriter out = res.getWriter();
        String data = req.getReader().readLine();
        ObjectMapper objectMapper = new ObjectMapper();
        Ingredient ing = objectMapper.readValue(data, Ingredient.class);
        Ingredient resultat = dao.save(ing);
        if (resultat==null){
            res.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }else{
            out.println(objectMapper.writeValueAsString(resultat)); 
            res.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        out.close();
    }
    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        if (!UserConvertor.canAccess(req)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            
            return;
        }
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();
        //out.println("retour d'un GET");
        
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = info.split("/");
        //out.println("Requete : " + info);
       
        /*for (int i = 0 ; i<splits.length;i++){
            out.println("Parameter " + i + " : " + splits[i] + "  ");
        }/* */
        if (splits.length > 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Ingredient in =null;
        String id = splits[1];
        in = dao.removeById(Integer.parseInt(id));
        if (in==null){
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_ACCEPTED);
        out.println(objectMapper.writeValueAsString(in));
        return;
    }
    
    public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        if (!UserConvertor.canAccess(req)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            
            return;
        }
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String data = req.getReader().readLine();
        String info = req.getPathInfo();
        Ingredient ing = objectMapper.readValue(data, Ingredient.class);
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length > 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Ingredient result = dao.patch(Integer.parseInt(splits[1]),ing);
        if (result==null){
            res.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }else{
            out.println(objectMapper.writeValueAsString(result)); 
            res.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        out.close();
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        if (!UserConvertor.canAccess(req)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            
            return;
        }
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String data = req.getReader().readLine();
        String info = req.getPathInfo();
        Ingredient ing = objectMapper.readValue(data, Ingredient.class);
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length > 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Ingredient result = dao.put(Integer.parseInt(splits[1]),ing);
        if (result==null){
            res.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }else{
            out.println(objectMapper.writeValueAsString(result)); 
            res.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        out.close();
    }   
}