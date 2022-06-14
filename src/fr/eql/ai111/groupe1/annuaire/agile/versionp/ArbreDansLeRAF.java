package fr.eql.ai111.groupe1.annuaire.agile.versionp;



import javax.swing.JOptionPane;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

// C'est pour création de pdf

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;


/**
 *
 * @author Rihab
 */

public class ArbreDansLeRAF {
    /**
     *
     * Cette classe permet de faire les traitements au niveau du RAF:
     *  1. Charger le RAF à partir s'un fichier.txt d'une manière structurée
     *  2. Trie des stagiaires par ordre alphabétique
     *
     *
     */

    public static final int NOMSTAGIARE = 20;
    public static final int PRENOMSTAGIARE = 20;
    public static final int DEPARTEMENT = 15;
    public static final int PROMO = 10;

    public static final int LONGUEUSTAGIARE = ((NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO ) * 2) + 4;
    //le 4 c'est pour l'année puisque c'est un int donc on ne le mélange pas avec les string



    public static void main(String[] args) {

        RandomAccessFile raf;


        String ligne = "";
        String infoStag = "";
        int compteurChoix = 0;
        int compteurStag = 0; //donne le nombre des stagiaires



        //Création du RAF
        try {

            raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");

            //Lecture de fichier.txt
            FileReader fichierOriginal = new FileReader("c:/Projet1/stagiaires.txt");
            BufferedReader bf = new BufferedReader(fichierOriginal);

            while ((ligne = bf.readLine()) != null) {// Tant qu'il y a des lignes dans mon fichier.txt
                if (!ligne.equals("*")) {// si ma ligne != "*" je vais ajouter le stagiaire dans le RAF
                                            // sinon j'augmente le nombre des stagiaires

                    for (int i = 0; i < ligne.length(); i++) {

                        if (ligne.charAt(i) != ' ') {
                            infoStag += ligne.charAt(i);

                        }
                        }
                        switch (compteurChoix) {
                            case 0:
                                infoStag = completer(infoStag, NOMSTAGIARE);
                                raf.writeChars(infoStag);

                                break;
                            case 1:
                                infoStag = completer(infoStag, PRENOMSTAGIARE);
                                raf.writeChars(infoStag);

                                break;
                            case 2:
                                infoStag = completer(infoStag, DEPARTEMENT);
                                raf.writeChars(infoStag);

                                break;
                            case 3:
                                infoStag = completer(infoStag, PROMO);
                                raf.writeChars(infoStag);
                                break;
                            case 4:
                                int nbannee = Integer.parseInt(infoStag);
                                raf.writeInt(nbannee);
                                break;
                            default:
                                break;

                        }

                        compteurChoix +=1;


                        infoStag = "";


                } else {

                    compteurStag += 1;
                    compteurChoix = 0;
                }

                infoStag ="";

            }
            triParOrdreAlpha(LONGUEUSTAGIARE,compteurStag,raf);
            listeStag(compteurStag, raf);


            String rechercheStagiaire = JOptionPane.showInputDialog("Veuillez entrer le nom d'un stagiaire à afficher");

            rechercheStagiaire = completer(rechercheStagiaire, NOMSTAGIARE);
            rechercheStag(0, compteurStag, raf, rechercheStagiaire);


            System.out.println("*********** Ajout Stagiaire ************");
            addStag(compteurStag,raf);
            listeStag(compteurStag+2,raf);

           /* String stagASupprimer = JOptionPane.showInputDialog("Veuillez entrer le nom d'un stagiaire à supprimer");
            // new 08/06
            stagASupprimer = completer(stagASupprimer, NOMSTAGIARE);
            supprimerStag( stagASupprimer, compteurStag,0, raf);

            //new 09/06
            exporterEnFormatPDF( compteurStag,  raf);*/






            bf.close();// ajouté le 07/06 : 17:14
            raf.close();//ajouté le 07/06

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //Méthodes

    /**
     * La méthode completer permet de structurer les données qui vont être saisi dans le RAF
     * @param mot : c'est une chaine de caractère : prend nom, prénom, department, promo
     * @param taille : taille de chaque paramètres (nom, prénom, department, promo)
     * @return paramètres (nom, prénom, department, promo) structuré
     */
    public static String completer(String mot, int taille) {

        int nbEspace = taille - mot.length();
        for (int i = 0; i < nbEspace; i++) {
            mot += " ";
        }
        return mot;


    }


    /**
     *
     * Cette méthode permet de lire un stagiaire (que les paramètres String) à partir de RAF
     */
    public static String lectureStag (int taillechaine, RandomAccessFile raf){

        String chaine = "";

        for (int i = 0; i < taillechaine; i++){

            try {
                chaine += raf.readChar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return chaine;
    }

    /**
     * Cette méthode permet de lister les stagiaires dans la console
     * Elle va être modifiée pour lister dans une interface
     * @param compteurStag : il s'agit de nombre des stagiares dans le RAF
     * @param raf
     */


    public static void listeStag (int compteurStag, RandomAccessFile raf){

        try {
            raf.seek(0);
            int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT+ PROMO;
            for (int k = 0; k < compteurStag; k++){
                String chaine = "";
                String chaine1= lectureStag(taille,raf);
                chaine = chaine + chaine1 + raf.readInt() ;// on ajoute l'année aux données de type String
                System.out.println(chaine);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     *
     * Cette fonction permet d'afficher en console les informations d'un stagiaire dont le nom a été saisi par
     * l'utilisateur
     * C'est le principe des arbres binaires
     * On compare le nom saisi avec le premier puis on compare : si c'est le même bingo
     * sinon s'il est plus petit on cherche dans les noms <
     * sinon on cherche dans les noms >
     * c'est pour ça on doit trier le RAF avant de faire la recherche
     * @param borneInf
     * @param borneSup
     * @param raf
     * @param nomStagRecherche
     */
    public static void rechercheStag(int borneInf, int borneSup, RandomAccessFile raf, String nomStagRecherche){

        int pivot =0;
        String nomStagActuel ="";


            try {

                if (borneInf <= borneSup){
                    pivot = (borneInf + borneSup) / 2;
                    raf.seek(pivot* LONGUEUSTAGIARE);
                    nomStagActuel =  lectureStag(NOMSTAGIARE,raf);
                    if (nomStagRecherche.compareToIgnoreCase(nomStagActuel) == 0 ){
                        raf.seek(pivot*LONGUEUSTAGIARE);
                        System.out.println("**************\r\n" + " Les infos de stagiaire demandé :\r\n" +
                                "Nom  : "  + lectureStag(NOMSTAGIARE, raf) + "\r\n" +
                                "Prénom  : " + lectureStag(PRENOMSTAGIARE, raf) + "\r\n" +
                                "Département : " + lectureStag(DEPARTEMENT, raf) + "\r\n" +
                                 "Promo : " + lectureStag(PROMO, raf) + "\r\n" +
                                "Année : " + raf.readInt() );

                    }else {

                        if (nomStagRecherche.compareToIgnoreCase(nomStagActuel) < 0) {
                            rechercheStag(borneInf, pivot - 1, raf, nomStagRecherche);
                        } else {
                            rechercheStag(pivot + 1, borneSup, raf, nomStagRecherche);
                        }
                    }

                } else {

                    System.out.println("\r\n***** Stagiaire introuvable *****");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }





    }

    /**
     * Cette fonction permet de trier les stagiaires par ordre alphabétique qui sont dans le RAF
     * Elle prend comme pivot le premier nom de le RAF qu'elle va comparer avec le suivant
     * Si le deuxième plus petit : on permute (on fait appel à la fonction permuter)
     * sinon on compare avec le troisième ainsi de suite
     *
     * @param longueurStag
     * @param compteurStag
     * @param raf
     */
    public static void triParOrdreAlpha(int longueurStag, int compteurStag, RandomAccessFile raf){


            try {

                for (int i = 0; i < compteurStag - 1; i++) {
                    int indexmin = i;
                    raf.seek(i * longueurStag);
                    String nomStagMini = "";
                    nomStagMini = lectureStag(NOMSTAGIARE, raf);

                    for (int j = i + 1; j < compteurStag; j ++) {
                        raf.seek(j * longueurStag);
                        String nomStagCourant = "";
                        nomStagCourant = lectureStag(NOMSTAGIARE, raf);


                        if (nomStagCourant.compareTo(nomStagMini) < 0) {
                            indexmin = j;
                            nomStagMini = nomStagCourant;
                        }
                    }
                    permuter(indexmin * longueurStag, i * longueurStag, raf);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



    }


    /**
     * Cette méthode permet de permuter deux stagiaires
     * @param indexmin
     * @param frontiere
     * @param raf
     */
    private static void permuter(int indexmin, int frontiere, RandomAccessFile raf){


        try {

            int stagChaineTaille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO;

            String stagChaine = "";
            int stagAnnee = 0;

            String stag2Chaine = "";
            int stag2Annee = 0;

            raf.seek(frontiere);
            stagChaine= lectureStag(stagChaineTaille,raf);
            stagAnnee = raf.readInt();

            raf.seek(indexmin);
            stag2Chaine = lectureStag(stagChaineTaille,raf);
            stag2Annee= raf.readInt();

            raf.seek(frontiere);
            raf.writeChars(stag2Chaine);
            raf.writeInt(stag2Annee);

            raf.seek(indexmin);
            raf.writeChars(stagChaine);
            raf.writeInt(stagAnnee);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     *
     * Cette méthode permet de supprimer un stagiaire en donnant son nom en paramètre d'entrée
     * @param nomStag : nom de stagiaire à supprimer
     * @param compteurStag : nombre des stagiaires dans le RAF
     * @param borninf
     * @param raf
     */
    private static void supprimerStag(String nomStag, int compteurStag, int borninf, RandomAccessFile raf){

        RandomAccessFile newRaf;
        String name = "";
        String infoStagAsupp ="";

        try {
            int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO;
            newRaf = new RandomAccessFile("c:/Projet1/listeStagiaire2.bin", "rw");

            System.out.println("Le nom de stagiaire à supprimer " + nomStag);

            rechercheStag(borninf, compteurStag, raf, nomStag);

            System.out.println(raf.getFilePointer());
            raf.seek(raf.getFilePointer()-LONGUEUSTAGIARE);
            name = lectureStag(taille,raf) + raf.readInt();
            System.out.println("the name "+name);

            raf.seek(0);
            for (int i = 0 ;i < compteurStag; i++){

                String chaine = "";
                String chaine1 = lectureStag(taille, raf);
                int nbannee= raf.readInt();
                chaine = chaine + chaine1 + nbannee;
                if ( !chaine.equals(name)){

                    newRaf.writeChars(chaine1);
                    newRaf.writeInt(nbannee);
                }
            }
            System.out.println("la liste dans le nouveau raf\r\n");
            listeStag(compteurStag-1, newRaf);
            //**********Avant cette partie tout marche *********
            // il faut penser comment faire pour que le compteur soit mis à jour ( à faire le jeudi 09/06)
            raf.setLength(0);
            System.out.println("le raf vide");

            newRaf.seek(0);
            for (int j = 0; j < compteurStag -1; j++){

                String infostag = lectureStag(taille,newRaf);
                int nb = newRaf.readInt();

                raf.writeChars(infostag);
                raf.writeInt(nb);

            }

            System.out.println("liste des stagiaires dans le raf");
            listeStag(compteurStag-1, raf);



            //*********************Fin nouvelle partie ************

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * La méthode exporterEnFormatPDF permet d'exporter l'annuaire (liste de stagiaire) sous format pdf
     * @param compteurStag
     * @param raf
     */

    private static void exporterEnFormatPDF(int compteurStag, RandomAccessFile raf){
  // Remarque j'ai réussi à créer le pdf: reste à ajouter  la liste des stagiaires dedans

        try {
            String para2 ="";
            // path to create the file
            String file_path = "/c:/Projet1/Annuaire.pdf";

            // creating PdfWriter object
            PdfWriter pdf_writer = new PdfWriter(file_path);

            // Representing PdfDocument object
            PdfDocument pdf_doc = new PdfDocument(pdf_writer);
            // Creating a Document
            // Instantiating a document object from pdf document
            // object
            Document document = new Document(pdf_doc);

            //AJouter un Titre



            // paragraph to be added
            String para  =


                    " \r\n                       L'annuaire des stagiaires \r\n" +
                            "\r\n" +
                            "\r\n";

            // Creating Paragraph object
            Paragraph paragraph_obj
                    = new Paragraph(para);
            paragraph_obj.setBold();
            paragraph_obj.setUnderline();
            //paragraph_obj.setFontColor(layer2FontColor);
            document.add(paragraph_obj);


            //  Lire les elements dans le raf et les mettre dans le pdf

            raf.seek(0);
            int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT+ PROMO;

            for (int k = 0; k < compteurStag -1; k++){
                String chaine = "";
                String chaine1= lectureStag(taille,raf);
                chaine = chaine + chaine1 + raf.readInt() ;// on ajoute l'année aux données de type String
                para2 =para2 + (k+1) +" "+ chaine +".\r\n";

            }





            // Creating Paragraph object
            Paragraph paragraph_obj2 = new Paragraph(para2);

            // Adding paragraphs to document
           document.add(paragraph_obj2);




            // Closing the document
            document.close();

            // final message
            System.out.println("Finished writing contents to the file!");




            document.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    private static  void addStag (int compteurStag, RandomAccessFile raf) {
        //n'oublie pas il faut qu'on ajoute 1 à compteurStag qu'on ajoute un stagiaire dans le RAF

        try {

            String nom = JOptionPane.showInputDialog("Veuillez entrer le nom d'un stagiaire");
            String prenom = JOptionPane.showInputDialog("Veuillez entrer le prenom d'un stagiaire");
            String departement = JOptionPane.showInputDialog("Veuillez entrer le code postal du stagiaire");
            String promo = JOptionPane.showInputDialog("Veuillez entrer la promotion du stagiaire");
            int annee = Integer.parseInt(JOptionPane.showInputDialog("Veuillez entrer l'année du stagiaire"));
            int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO;

            raf.seek(raf.length());

            // System.out.println("l'endroit où pointe "+ raf.getFilePointer());

            //raf.seek(raf.getFilePointer()-LONGUEUSTAGIARE);

            // System.out.println("l'ndroit ou il pointe quand j'enlève longeurStagiare (ou il y a diana)" + raf.getFilePointer());

            //Je modifie la taille de RAF

            // long newRafLength = raf.length()+LONGUEUSTAGIARE;
            //raf.setLength(newRafLength);

            //raf.seek(raf.length());
            //System.out.println("la nouvelle taille de raf"+raf.getFilePointer());

            //Méthode 1 : Une fois que j'ai augmenté la taille je pointe à l'ancienne taille et j'écrit le nouveau
            //sinon j'écrit comme j'ai fait tout à l'heure mais il faut que je fais liste stagiaire dans le main
            //il faut tester sans le trie et avec le trie
            //Une fois que ça marché je trouve solution pour compteur stagiaire


            // String name = lectureStag(taille,raf) + raf.readInt();
            //System.out.println("the name "+name);

            // System.out.println(raf.readChar());

            String nomAdded = completer(nom, NOMSTAGIARE);
            raf.writeChars(nomAdded);

            String prenomAdded = completer(prenom, PRENOMSTAGIARE);
            raf.writeChars(prenomAdded);

            String departAdded = completer(departement, DEPARTEMENT);
            raf.writeChars(departAdded);

            String promoAdded = completer(promo, PROMO);
            raf.writeChars(promoAdded);

            raf.writeInt(annee);


            triParOrdreAlpha(LONGUEUSTAGIARE, compteurStag + 2, raf);
            //listeStag(compteurStag+1,raf);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
    /*
    raf.seek(raf.length());
//         compteurStag =compteurStag + 1;


        raf.writeChars(ajoutNom);
        for (int i = 0; i < NOMSTAGIARE - ajoutNom.length(); i++) {
            raf.writeChars(" ");
        }
        raf.writeChars(ajoutPrenom);
        for (int i = 0; i < PRENOMSTAGIARE - ajoutPrenom.length(); i++) {
            raf.writeChars(" ");
        }
        raf.writeChars(ajoutCodepostal);
        for (int i = 0; i < DEPARTEMENT - ajoutCodepostal.length(); i++) {
            raf.writeChars(" ");
        }
        raf.writeChars(ajoutPromo);
        for (int i = 0; i < PROMO - ajoutPromo.length(); i++) {
            raf.writeChars(" ");
        }
        raf.writeInt(ajoutAnnee);
        for (int i = 0; i < 4 ; i++) {
            raf.writeChars(" ");
        }

        try {
            System.out.println("Je suis dans le trie");
            for (int i = 0; i < compteurStag - 1; i++) {
                int indexmin = i;
                raf.seek(i * longueurStag);
                String nomStagMini = "";
                nomStagMini = lectureStag(NOMSTAGIARE, raf);
                System.out.println("Je suis dans le premier for");

                for (int j = i + 1; j < compteurStag; j ++) {
                    raf.seek(j * longueurStag);
                    String nomStagCourant = "";
                    nomStagCourant = lectureStag(NOMSTAGIARE, raf);
                    System.out.println("Je suis dans le deuxieme for");


                    if (nomStagCourant.compareTo(nomStagMini) < 0) {
                        indexmin = j;
                        nomStagMini = nomStagCourant;
                        System.out.println("Je suis dans le if");

                    }
                }
                permuter(indexmin * longueurStag, i * longueurStag, raf);
                System.out.println("J'ai fini le trie");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("**************\r\n" + " Les infos du nouveau stagiaire  :\r\n" +
                "Nom  : "  + ajoutNom + "\r\n" +
                "Prénom  : " + ajoutPrenom + "\r\n" +
                "Département : " + ajoutCodepostal + "\r\n" +
                "Promo : " + ajoutPromo + "\r\n" +
                "Année : " + ajoutAnnee );

        triParOrdreAlpha(LONGUEUSTAGIARE,compteurStag + 3,raf);
        listeStag(compteurStag + 3, raf);
    }


     */

}
