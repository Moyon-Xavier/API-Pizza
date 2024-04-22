package dto;

import java.sql.Date;
import java.util.ArrayList;

public class CommandeWithPiz {
    
    private int cno = -1;
    private String userName;
    private Date date;
    private double prix;

    ArrayList<Integer> contient = new ArrayList<>();

    public CommandeWithPiz(){}

    public CommandeWithPiz(int cno, String userName, Date date, double prix, ArrayList<Integer> list){
        this.cno = cno;
        this.userName = userName;
        this.date = date;
        this.prix = prix;
        this.contient = list;
    }

    public CommandeWithPiz(int cno, String userName, Date date, double prix){
        this(cno,userName,date,prix,null);
    }

    public CommandeWithPiz(String userName, Date date, double prix){
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

    public ArrayList<Integer> getContient() {
        return contient;
    }

    public void setContient(ArrayList<Integer> contient) {
        this.contient = contient;
    }
}
