package dto;

import java.sql.Date;
import java.util.ArrayList;

public class Commande {
    
    private int cno=-1;
    private String userName;
    private Date date;
    private double prix;

    private ArrayList<Pizza> compo = new ArrayList<>();

    public Commande(){}

    public Commande(int cno, String userName, Date date, double prix,ArrayList<Pizza> list){
        this.cno = cno;
        this.userName = userName;
        this.date = date;
        this.prix = prix;
        if(list != null){
            this.compo = list;
        }
    }

    public Commande(int cno, String userName, Date date, double prix){
        this(cno,userName,date,prix,null);
    }

    public Commande(String userName, Date date, double prix){
        this(-1,userName,date,prix,null);
    }

    public int getCno() {
        return cno;
    }

    public void setCno(int cno) {
        this.cno = cno;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Commande [cno=" + cno + ", userName=" + userName + ", date=" + date + ", prix=" + prix + "]";
    }

    public ArrayList<Pizza> getCompo() {
        return compo;
    }

    public void setCompo(ArrayList<Pizza> compo) {
        this.compo = compo;
    }

}
