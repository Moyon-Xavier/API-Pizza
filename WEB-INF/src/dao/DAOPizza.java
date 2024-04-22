package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;
import dto.Pizza;
import dto.PizzaWithIntIng;
import utils.DS;

public class DAOPizza {
    

    public DAOPizza(){}

    public List<Pizza> findAll(){
        List<Pizza> pizzas = new ArrayList<>();
        Connection con =null;
        try{
            con = DS.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select Distinct pno from pizza");
            while(rs.next()){
                pizzas.add(this.findById(rs.getInt(1)));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        try{
            con.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("Pizzas : "+pizzas);
        return pizzas;
    }
    public Pizza findById(int id){
        Pizza p = null;
        Connection con =null;
        try{
            con=DS.getConnection();
            PreparedStatement pst = con.prepareStatement("select * from pizza as p LEFT JOIN contient as c using(pno) LEFT join ingredient as i using(ino) WHERE pno = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                p = new Pizza(rs.getInt(2), rs.getString(3), rs.getDouble(4),new ArrayList<Ingredient>());
                if(rs.getString(5)!=null){
                    p.getCompo().add(new Ingredient(rs.getInt(1),rs.getString(5),rs.getDouble(6)));
                }
            }
            while(rs.next()){
                p.getCompo().add(new Ingredient(rs.getInt(1),rs.getString(5),rs.getDouble(6)));
            }
            p.setFinalPrice();

        }catch(Exception e){
            e.getMessage();
        }
        try{
            con.close();
        }catch(Exception e){
            e.getMessage();
        }
        System.out.println(p);
        return p;
    }

    public String findNameById(int id){
        return findById(id).getNom();
    }

    
    public Pizza save(PizzaWithIntIng ingr){

        Connection con = DS.getConnection();
        Pizza result = null;
        try{
            PreparedStatement pst;
            System.out.print("Pizza en prepa");
            if (ingr.getPno()==-1){
                pst = con.prepareStatement("INSERT INTO Pizza(nom,prix) VALUES(?,?) returning *");
                pst.setString(1, ingr.getNom());
                pst.setDouble(2, ingr.getPrix());
            }else{
                pst = con.prepareStatement("INSERT INTO Pizza VALUES(?,?,?) returning *");
                pst.setInt(1, ingr.getPno());
                pst.setString(2, ingr.getNom());
                pst.setDouble(3, ingr.getPrix());
            }
            
            ResultSet rs = pst.executeQuery();
            int id =-12;
            

            if (rs.next()){
                
                System.out.print("Is Next");
                id =rs.getInt(1);
                pst = con.prepareStatement("INSERT INTO contient(pno,ino) VALUES(?,?)");
                for(Integer i : ingr.getCompo()){
                    pst.setInt(1,rs.getInt(1) );
                    pst.setInt(2,i );
                    try{
                        pst.executeUpdate();
                       
                    }catch(Exception e){
                        System.out.print(e.getMessage());
                         
                    }
                }
                
                System.out.print(id + " ");
                System.out.println(this.findNameById(id));
                return this.findById(id);
            }else{
                return null;
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

    public Pizza save(int pno,String nom, double prix){
        return save(new PizzaWithIntIng(pno,nom,prix));
    }
    public Pizza save(String nom, double prix){
        return save(new PizzaWithIntIng(nom,prix));
    }

    public boolean deleteById(int id){
        boolean result = false;
        Connection con =null;
        try{
            con=DS.getConnection();
            PreparedStatement pst;  
            pst = con.prepareStatement("DELETE FROM Pizza WHERE pno = ?");
            pst.setInt(1, id);
            int rs = pst.executeUpdate();
            result = rs>=1;
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

    public boolean deletePizzaIngredientById(int idPizza,int idIng){
        boolean result = false;
        Connection con =null;
        try{
            con=DS.getConnection();
            PreparedStatement pst;  
            pst = con.prepareStatement("DELETE FROM contient WHERE pno = ? AND ino = ?");
            pst.setInt(1, idPizza);
            pst.setInt(2, idIng);
            int rs = pst.executeUpdate();
            result = rs>=1;
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
    public Pizza addIngredient(int idPizza, int idIngr){
        boolean result = false;
        Pizza resultObj = null;
        Connection con =null;
        try{
            con=DS.getConnection();
            PreparedStatement pst;  
            pst = con.prepareStatement("INSERT INTO contient(pno,ino) VALUES(?,?)");
            pst.setInt(1, idPizza);
            pst.setInt(2, idIngr);
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
            resultObj=findById(idPizza);
        }
        return resultObj;
    }

    public Pizza put(int idPizza, PizzaWithIntIng pizza){
        Pizza resultObj = null;
        Connection con =null;
        try{
            con=DS.getConnection();
            con.setAutoCommit(false);
            PreparedStatement pst;  
            pst = con.prepareStatement("DELETE FROM contient WHERE pno = ?");
            pst.setInt(1, idPizza);
            pst.executeUpdate();
            Savepoint save=null ;
            save = con.setSavepoint();
            for (Integer ino : pizza.getCompo()){
                pst = con.prepareStatement("Insert into contient VALUES (?,?)");
                pst.setInt(1, idPizza);
                pst.setInt(2, ino);
                try{
                    pst.executeUpdate();
                    save = con.setSavepoint();
                }catch(Exception e){
                    System.out.println(e.getMessage() + " idPizza : " + idPizza + " ino : " + ino);
                    con.rollback(save);
                }
            }
            pst = con.prepareStatement("update pizza SET nom=? WHERE pno = ?");
            pst.setString(1, pizza.getNom());
            pst.setInt(2, idPizza);
            int rs = pst.executeUpdate();
            con.commit();
            resultObj=findById(idPizza);       
        }catch(Exception e){
            System.out.println(e.getMessage());
            try{
                con.rollback();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        try{ 
            con.close();
        }catch(Exception e){
            e.getMessage();
        }
        return resultObj;
    }
    

    public Pizza doPatch(int idPizza, PizzaWithIntIng pizza){
        Pizza tmpPizza = this.findById(idPizza);
        if(tmpPizza!=null){
            PizzaWithIntIng obj = new PizzaWithIntIng();
        obj.setNom(tmpPizza.getNom());
        obj.setPno(idPizza);
        for(Ingredient i : tmpPizza.getCompo()){
            obj.getCompo().add(i.getId());
        }
        if (pizza.getNom()!=null){
            obj.setNom(pizza.getNom());
        }
        if(pizza.getCompo().size()!=0){
            obj.setCompo(pizza.getCompo());
        }
        return put(idPizza, obj);
        }
        return tmpPizza;
    }
}
