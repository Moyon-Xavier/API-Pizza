package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;
import utils.DS;

public class DAOIngredient {
    /*public static void main(String [] args ){
        DAOIngredient dao = new DAOIngredient();
        System.out.println("SELECT * : ");
        for (Ingredient ing : dao.findAll()){
            System.out.println(ing.toString());
        }
        System.out.println("SELECT * WWHERE  ");
        System.out.println(dao.findById(2));
        System.out.println("SELECT * WWHERE Name  ");
        System.out.println(dao.findNameById(2));
        System.out.println("Ajout d'un ingredients : ");
        System.out.println("Lunnetes : " + dao.save("lunettes", 18.0));
        System.out.println("Casquette : " + dao.save(18,"Casquette", 18.0));
        System.out.println("SELECT * : ");
        for (Ingredient ing : dao.findAll()){
            System.out.println(ing.toString());
        }
        System.out.println("Suppression Casquette : " + dao.removeById(18));
        System.out.println("SELECT * : ");
        for (Ingredient ing : dao.findAll()){
            System.out.println(ing.toString());
        }
    }*/
    

    public DAOIngredient(){
        // Ajout elem base PSQL
    }

    
    public List<Ingredient> findAll(){
        List<Ingredient> ings = new ArrayList<>();
        Connection con = DS.getConnection();
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Ingredient");
            while(rs.next()){
                ings.add(new Ingredient(rs.getInt(1),rs.getString(2),rs.getDouble(3)));
            } 
        }catch(Exception e){
            e.getMessage();
        }
        try{
            con.close();
        }catch(Exception e){
            e.getMessage();
        }
        return ings;
    }

    public Ingredient findById(int id){
        Ingredient ing =  null;
        Connection con = DS.getConnection();
        try{ 
            PreparedStatement pst = con.prepareStatement("SELECT * FROM Ingredient WHERE ino = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                ing =new Ingredient(rs.getInt(1),rs.getString(2),rs.getDouble(3));
            }
        }catch(Exception e){
            e.getMessage();
        }
        try{
            con.close();
        }catch(Exception e){
            e.getMessage();
        }
        return ing;
    }

    public String findNameById(int id){
        Ingredient ingre = findById(id);
        if (ingre!=null){
            return ingre.getName();
        }
        return null;
    }

    public Ingredient save(Ingredient ingr){
        Connection con = DS.getConnection();
        Ingredient result = null;
        try{
            PreparedStatement pst;
            if (ingr.getId()==-1){
                pst = con.prepareStatement("INSERT INTO Ingredient(nom,prix) VALUES(?,?) returning *");
                pst.setString(1, ingr.getName());
                pst.setDouble(2, ingr.getPrice());
            }else{
                pst = con.prepareStatement("INSERT INTO Ingredient VALUES(?,?,?) returning *");
                pst.setInt(1, ingr.getId());
                pst.setString(2, ingr.getName());
                pst.setDouble(3, ingr.getPrice());
            }
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                result = new Ingredient(rs.getInt(1),rs.getString(2),rs.getDouble(3));
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

    public Ingredient save(int id, String name, double price){
        return save(new Ingredient(id,name,price));
    }

    public Ingredient save( String name, double price){
        System.out.println("In save without id ");
        Ingredient ing = new Ingredient(name,price);
        System.out.println("Ing = " + ing );
        return save(ing);
    }

    public Ingredient removeById(int id){
        Connection con = DS.getConnection();
        Ingredient result = null;
        try{
            PreparedStatement pst;  
            pst = con.prepareStatement("DELETE FROM ingredient WHERE ino = ? returning * ");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                result = new Ingredient(rs.getInt(1),rs.getString(2),rs.getDouble(3));
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

    public Ingredient put(int id, Ingredient ing){
        Connection con = DS.getConnection();
        Ingredient result = null;
        System.out.println(ing);
        try{
            PreparedStatement pst;  
            pst = con.prepareStatement("UPDATE ingredient SET nom=?, prix=? WHERE ino = ? returning * ");
            pst.setString(1, ing.getName());
            pst.setDouble(2, ing.getPrice());
            pst.setInt(3, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                result = new Ingredient(rs.getInt(1),rs.getString(2),rs.getDouble(3));
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

    public Ingredient patch(int id, Ingredient ing){
        Ingredient result = null;
        Ingredient ingTmp = this.findById(id);
        if(ing.getName()==null){
            ing.setName(ingTmp.getName());
        }
        if(ing.getPrice()==0){
            ing.setPrice(ingTmp.getPrice());
        }
        result = put(id, ing);
        return result;        
    }
}