package dto;

import java.util.ArrayList;

public class PizzaWithIntIng {
    private int pno = -1;
    private String nom;
    private double prix;

    private ArrayList<Integer> compo = new ArrayList<>();
    
    public PizzaWithIntIng(){}

    public PizzaWithIntIng(int pno,String nom, double prix, ArrayList<Integer> compo){
        this.pno=pno;
        this.nom = nom;
        this.prix = prix;
        this.compo = compo;
    }

    public PizzaWithIntIng(int pno,String nom, double prix){
        this(pno, nom, prix, null);
    }

    public PizzaWithIntIng(String nom, double prix){
        this(-1,nom, prix, null);
    }

    public int getPno(){
        return this.pno;
    }

    public void setPno(int pno){
        this.pno = pno;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public ArrayList<Integer> getCompo() {
        return compo;
    }

    public void setCompo(ArrayList<Integer> compo) {
        this.compo = compo;
    }

    @Override
    public String toString() {
        return "Pizza [nom=" + nom + ", prixFinal=" + prix + ", compo=" + compo + "]";
    }
}
