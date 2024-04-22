package dto;

import java.util.ArrayList;

public class Pizza {
    
    private int pno = -1;
    private String nom;
    private double prix;
    private double finalPrice;
    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
    private ArrayList<Ingredient> compo = new ArrayList<>();
    
    public Pizza(){}

    public Pizza(int pno,String nom, double prix, ArrayList<Ingredient> compo){
        this.pno=pno;
        this.nom = nom;
        this.prix = prix;
        this.compo = compo;
        finalPrice=getPrixFinal();
    }

    public Pizza(int pno,String nom, double prix){
        this(pno, nom, prix, null);
    }

    public Pizza(String nom, double prix){
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

    public ArrayList<Ingredient> getCompo() {
        return compo;
    }

    public void setCompo(ArrayList<Ingredient> compo) {
        this.compo = compo;
    }

    @Override
    public String toString() {
        return "Pizza [nom=" + nom + ", prixFinal=" + prix + ", compo=" + compo + "]";
    }

    public double getPrixFinal(){
        int prixIng = 0;
        for (Ingredient ingredient : compo){
            prixIng += ingredient.getPrice();
        }
        return this.prix + prixIng;
    }
    public void setFinalPrice(){
        this.finalPrice=this.getPrixFinal();
    }
}
