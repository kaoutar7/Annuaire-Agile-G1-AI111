package fr.eql.ai111.groupe1.annuaire.agile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class LesStagiaires {
    private BufferedReader bfr;

    public LesStagiaires(String listeStagiaire) {
        try {
            //Création d'un flux pour le fichier RAF
            //le nom du fichier est passé en argument
            FileReader in = new FileReader("C:\\Projet1\\listeStagiaire.bin");
            bfr = new BufferedReader(in);
        } catch (IOException e) {
            System.out.println("Pb entrée sortie :" + e.getMessage());
        }
    }

    // Transforme une chaine en un objet de type Stagiaire
    //format de la chaine : 1*BARBE*Rue des Vignes Paris*0123546789*10000


    //Transformer le fichier en une collection de stagiaires
    public List<Stagiaires> fabriqueVecteur() {
        String chaine;
        Stagiaires stagiaire;
        List<Stagiaires> stagiaiire = new Vector<>();
        try {
            do {
                chaine = bfr.readLine();
                if (chaine != null) {
                    stagiaire = fabriqueStagiaires(chaine);
                    stagiaiire.add(stagiaire);
                }
            } while (chaine != null);
        } catch (IOException e) {
            System.out.println("Problème de lecture : " + e.getMessage());
        }
        return stagiaiire;

    }


    private Stagiaires fabriqueStagiaires(String chaine) {
        Stagiaires stagiaire = null;
        StringTokenizer st = new StringTokenizer(chaine, "*");
        if (st.countTokens() == 5) {
            String nom = st.nextToken();
            String prenom = st.nextToken();
            String departement = st.nextToken();
            String promo = st.nextToken();
            String année = String.valueOf(Double.valueOf(st.nextToken()));

            stagiaire = new Stagiaires(nom, prenom, departement, promo, année);
        }
        return stagiaire;

    }

    //Transforme le fichier en une chaine de caractères formée de plusieurs lignes
    public String fabriqueChaine(){
        StringBuffer chainebf=new StringBuffer();
        String chaine;
        try{
            do{
                chaine= bfr.readLine() ;
                if(chaine!=null){
                    chainebf.append(chaine +"\n");
                }
            }while(chaine!=null);
        }catch(IOException e){
            System.out.println("Problème de lecture : " +e.getMessage());

        }
        return chainebf.toString();
    }
}
