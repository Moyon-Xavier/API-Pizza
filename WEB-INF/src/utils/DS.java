package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DS {
    /**Cette methode permet de récuperer une connection a une base postgresql
     * Il suffit pour cela d'utiliser la commande suivante : DS.getConnection()
     * @return
     */
    public static Connection getConnection(){
        
        try{
            Class.forName("org.postgresql.Driver");
            String url="jdbc:postgresql://psqlserv/but2";
            String nom="manelboumansouretu";
            String mdp="moi";        
            Connection con = DriverManager.getConnection(url,nom,mdp);
            return con;
        }catch (ClassNotFoundException e){
            System.out.println("CNFE"+e.getMessage());
            return null;
        }catch (SQLException e){
            System.out.println("SQLE"+e.getMessage());
            return null;
        }
    }
}

