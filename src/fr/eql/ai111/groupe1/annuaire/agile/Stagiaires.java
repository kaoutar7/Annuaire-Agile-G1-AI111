package fr.eql.ai111.groupe1.annuaire.agile;

public class Stagiaires {

    private  String nom;
    private String prenom;
    private  String departement;
    private String promo;
    private Integer année;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public Integer getAnnée() {
        return année;
    }

    public void setAnnée(Integer année) {
        this.année = année;
    }

    public Stagiaires(String nom, String prenom, String departement, String promo, String année) {
        this.nom = nom;
        this.prenom = prenom;
        this.departement = departement;
        this.promo = promo;
        this.année = Integer.valueOf(année);


    }
}
