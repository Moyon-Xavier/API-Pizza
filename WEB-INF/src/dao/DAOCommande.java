package dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.Commande;
import dto.CommandeWithPiz;
import dto.Pizza;
import utils.DS;

public class DAOCommande {
    
    DAOPizza daoPizza = new DAOPizza();

    public DAOCommande(){
    }

    public List<Commande> findAll(){
        List<Commande> comm = new ArrayList<>();
        Connection con = DS.getConnection();
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT Distinct cno FROM Commande");
            while(rs.next()){
                comm.add(this.findById(rs.getInt(1)));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        try{
            con.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return comm;
    }

    public Commande findById(int id){
        Commande comm = null;
        Connection con = DS.getConnection();
        try{
            PreparedStatement pst = con.prepareStatement("select * from commande as c LEFT JOIN compose as co using(cno) LEFT join pizza as p using(pno) WHERE cno = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                comm = new Commande(rs.getInt(2), rs.getString(3), rs.getDate(4), rs.getDouble(5), new ArrayList<Pizza>());
                if(rs.getString(5)!=null){
                    comm.getCompo().add(new Pizza(rs.getInt(1),rs.getString(6), rs.getDouble(7)));
                    //comm.getCompo().get(0).setFinalPrice();
                }
            }
            while(rs.next()){
                comm.getCompo().add(new Pizza(rs.getInt(1),rs.getString(6), rs.getDouble(7)));
                //comm.getCompo().get(0).setFinalPrice();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        try{
            con.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return comm;
    }

    public String findNameById(int id){
        Commande comm = findById(id);
        if(comm != null){
            return comm.getUserName();
        }
        return null;
    }

    public Commande save(CommandeWithPiz comm){
        Connection con = DS.getConnection();
        Commande result = null;
        try{
            PreparedStatement pst;
            if(comm.getCno() == -1){
                pst = con.prepareStatement("INSERT INTO Commande(userName,date,prix) VALUES (?,?,?) returning *");
                pst.setString(1, comm.getUserName());
                pst.setDate(2, comm.getDate());
                pst.setDouble(3, comm.getPrix());
            }else{
                pst = con.prepareStatement("INSERT INTO Commande(cno,userName,date,prix,pizza) VALUES (?,?,?,?,?) returning *");
                pst.setInt(1,comm.getCno());
                pst.setString(2, comm.getUserName());
                pst.setDate(3, comm.getDate());
                pst.setDouble(4, comm.getPrix());
                pst.setInt(5,comm.getContient().get(0));
            }
            ResultSet rs = pst.executeQuery();
            int id = 0;
            if(rs.next()){
                id = rs.getInt(1);
                pst = con.prepareStatement("INSERT INTO compose(cno,pno) VALUES(?,?)");
                for(Integer i : comm.getContient()){
                    pst.setInt(1, rs.getInt(1));
                    pst.setInt(2, i);
                    try{
                        pst.executeUpdate();
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                return this.findById(id);
            }
        }catch(Exception e){
            e.getMessage();
        }
        try{
            con.close();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    public Commande addPizz(int idCommande, int idPizza){
        boolean result = false;
        Commande resultObj = null;
        Connection con =null;
        try{
            con=DS.getConnection();
            PreparedStatement pst;  
            pst = con.prepareStatement("INSERT INTO compose(cno,pno) VALUES(?,?)");
            pst.setInt(1, idCommande);
            pst.setInt(2, idPizza);
            int rs = pst.executeUpdate();
            result=rs>0;  
        }catch(Exception e){
            e.getMessage();
        }
        try{
            con.close();
        }catch(Exception e){
            e.getMessage();
        }
        if(result){
            System.out.println("RESULT TRUE ");          
        }
        resultObj=findById(idCommande);
        return resultObj;
    }

    public Commande save(Commande com){
        Connection con = DS.getConnection();
        Commande comm = null;
        try{
            PreparedStatement pst;
            if(com.getCno()==-1){
                pst = con.prepareStatement("INSERT INTO Commande(userName,date,prix) VALUES (?,?,?) returning *");
                pst.setString(1, com.getUserName());
                pst.setDate(2, com.getDate());
                pst.setDouble(3, com.getPrix());
            }else{
                pst = con.prepareStatement("INSERT INTO Commande(cno,userName,date,prix) VALUES (?,?,?,?) returning *");
                pst.setInt(1,com.getCno());
                pst.setString(2, com.getUserName());
                pst.setDate(3, com.getDate());
                pst.setDouble(4, com.getPrix());
            }
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                comm = new Commande(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getDouble(4), new ArrayList<Pizza>());
            }
        }catch(Exception e){
            e.getMessage();
        }
        try{
            con.close();
        }catch(Exception e){
            e.getMessage();
        }
        return comm;
    }

    public Commande save(int cno, String userName, Date date, double prix){
        return save(new Commande(cno,userName,date,prix));
    }

    public Commande removeById(int id){
        Connection con = DS.getConnection();
        Commande comm = null;
        try{
            PreparedStatement pst;
            pst = con.prepareStatement("DELETE FROM Commande WHERE cno = ? returning *");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                comm = new Commande(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getDouble(4), new ArrayList<Pizza>());
            }
        }catch(Exception e){
            e.getMessage();
        }
        try{
            con.close();
        }catch(Exception e){
            e.getMessage();
        }
        return comm;
    }
}
