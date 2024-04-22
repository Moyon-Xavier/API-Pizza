package controleur;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOCommande;
import dao.DAOPizza;
import dto.Commande;
import dto.CommandeWithPiz;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.UserConvertor;

@WebServlet("/commande/*")
public class CommandeRestAPI extends HttpServlet{

    DAOCommande daoCom = new DAOCommande();
    DAOPizza daoPizz = new DAOPizza();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();

        if (info == null || info.equals("/")) {
            Collection<Commande> l = daoCom.findAll();
            String jsonstring = objectMapper.writeValueAsString(l);
            out.println(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length > 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Commande com = null;
        String id = splits[1];
        if(splits.length>2){
            if(splits[2].equals("name")){
                String name = daoCom.findNameById(Integer.parseInt(id));
                if(name==null){
                    res.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return ;
                }else{
                    out.println(objectMapper.writeValueAsString(name));
                    return ;
                }
            }else{
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        com = daoCom.findById(Integer.parseInt(id));
        if(com == null){
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.println(objectMapper.writeValueAsString(com));
        out.close();
        return;        
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!UserConvertor.canAccess(req)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Commande com = null;
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            String data = req.getReader().readLine();
            CommandeWithPiz comm = objectMapper.readValue(data, CommandeWithPiz.class);
            com = daoCom.save(comm);
        }else{
            String[] splits = info.split("/");
            if(splits.length == 3){
                System.out.println("PASSE DANS LA BOUCLE LENGTH 3");
                String idCommande = splits[1];
                String idPizza = splits[2];
                System.out.println("ID COMMANDE : " + idCommande);
                System.out.println("ID PIZZA : " + idPizza);
                com = daoCom.addPizz(Integer.parseInt(idCommande), Integer.parseInt(idPizza));
                System.out.println("SORT DE LA BOUCLE");
            }else{
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        if(com==null){
            res.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }else{
            out.println(objectMapper.writeValueAsString(com)); 
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
        if (info == null || info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length > 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Commande com = null;
        String id = splits[1];
        com = daoCom.removeById(Integer.parseInt(id));
        if(com == null){
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_ACCEPTED);
        out.println(objectMapper.writeValueAsString(com));
        return;
    }
    
}
