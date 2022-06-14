package fr.eql.ai111.groupe1.annuaire.agile.versionp;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RAFTraitement {

    /*
    Variable :

NomStagiaire <-- 20 : chaîne de caratères
PrenomStagiaire<-- 20 : chaîne de caratères
Departement <-- 20 : chaîne de caratères
Promo <-- 10 : chaîne de caratères
Année : entier

CompteurTab : entier
Ligne : chaîne de caratères
Mot : chaîne de caratères
FichierBinaire : fichier binaire
CompteurStagiaire : entier

LongueurStagiaire <-- (NomStagiaire+ PrenomStagiair + Departement  + Promo ) * 2 + 4 {4 pour l'année}


     */
    public static final int  NOMSTAGIARE = 20;
    public static final int PRENOMSTAGIARE= 20;
    public static final int DEPARTEMENT = 20;
    public static final int PROMO = 10;
    public static final int ANNEE = 4;
    public static final int LONGUEUSTAGIARE = ((NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO ) * 2)
            + 4; // 4 représentant un int pour l'année

    public static void main(String[] args) {

        int compteurTab = 0;
        String ligne = "";
        String mot = "";
        int compteurStagiaire = 0;



        RandomAccessFile raf;


        //Création du RAF
        try {
            raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
            //Lecture de fichier.txt
            FileReader fichierOriginal = new FileReader("c:/Projet1/stagiaires.txt");
            BufferedReader bf = new BufferedReader(fichierOriginal);

            //******************1er essai*************************

            while ((ligne = bf.readLine()) != null) { //Tant qu'il y a des lignes dans le fichier

                //******************05/06/2022*************************
               // while (!ligne.equals("*")){
                    compteurTab = 0;
                    System.out.println("Je suis ligne !*");
                    for (int i = 0; i< ligne.length(); i++){
                        System.out.println("Je suis  dans for");
                        if (ligne.charAt(i) != '*'){
                        if (ligne.charAt(i) != ' '){
                            System.out.println("Je suis dans le if ");
                            mot+= ligne.charAt(i);
                        }else {
                            System.out.println("Je suis dans le else");

                            switch (compteurTab) {
                                case 0:
                                    mot = completer(mot, NOMSTAGIARE);
                                    raf.writeChars(mot);
                                    break;
                                case 1:
                                    mot = completer(mot, PRENOMSTAGIARE);
                                    raf.writeChars(mot);
                                    break;
                                case 2:
                                    mot = completer(mot, DEPARTEMENT);
                                    raf.writeChars(mot);
                                    break;
                                case 3:
                                    mot = completer(mot, PROMO);
                                    raf.writeChars(mot);
                                    break;
                                /*case 4:
                                    int nb = Integer.parseInt(mot);
                                    raf.writeInt(nb);
                                    break;*/
                                default:
                                    break;
                            }
                            compteurTab += 1;
                            mot = "";
                        }

                    }

                }



                //*************************Fin 05/06/2022********************************
               /* compteurTab = 0;
                for (int i = 0; i < ligne.length(); i++) {
                    if (ligne.charAt(i) == '*'){
                        compteurStagiaire += 1;


                    } else if  (ligne.charAt(i) != ' ') {
                        mot += ligne.charAt(i);


                    } else {

                        switch (compteurTab) {
                            case 0:
                                mot = completer(mot, NOMSTAGIARE);
                                raf.writeChars(mot);
                                break;
                            case 1:
                                mot = completer(mot, PRENOMSTAGIARE);

                                raf.writeChars(mot);
                                break;
                            case 2:
                                mot = completer(mot, DEPARTEMENT);

                                raf.writeChars(mot);
                                break;
                            case 3:
                                mot = completer(mot, PROMO);

                                raf.writeChars(mot);
                                break;
                            default:
                                break;
                        }
                        compteurTab += 1;
                        mot = "";
                    }

                }*/
                mot = completer(mot, ANNEE);
                raf.writeChars(mot);
                mot = "";
                compteurStagiaire +=1;

            }


            triParExtraction(LONGUEUSTAGIARE, compteurStagiaire, raf);
            listeStagiaires(compteurStagiaire, raf);

            String rechercheStag = JOptionPane.showInputDialog("Veuillez entrer le nom du Stagiaire");
            rechercheStag = completer(rechercheStag, NOMSTAGIARE);
            rechercheDichotomique(0, compteurStagiaire, raf, rechercheStag);







            //*************************Fin *****************************


        } catch (IOException e) {
            throw new RuntimeException(e);//A changer
        }
    }

// METHODES

    public static String completer(String mot, int taille) {

        int nbEspace = taille - mot.length();
        for (int i = 0; i < nbEspace; i++) {
            mot += " ";
        }
        return mot;
    }
    public static String lectureStagiaire(int longueurChaine, RandomAccessFile raf) {

        String chaine = "";
        try {
            for (int i = 0; i < longueurChaine; i++) {
                chaine += raf.readChar();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chaine;
    }



    public static void listeStagiaires(int compteurStagiaire, RandomAccessFile raf) {

        try {
            raf.seek(0);
            int taille1 = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT+ PROMO;
            int taille2 = ANNEE;
            for (int j = 0; j < compteurStagiaire; j++) {
                String chaine = "";
                String chaine1 = lectureStagiaire(taille1, raf);
                chaine = chaine + chaine1 + raf.readInt() + "	";
                String chaine2 = lectureStagiaire(taille2, raf);
                chaine += chaine2;
                System.out.println(chaine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void triParExtraction(int longueurStagiaire, int compteurStagiaire, RandomAccessFile raf) {
        try {
            for (int i = 0; i < compteurStagiaire - 1; i += 1) {
                int imini = i;
                raf.seek(i * LONGUEUSTAGIARE);
                String nomVinMini = "";
                nomVinMini = lectureStagiaire(NOMSTAGIARE, raf);

                for (int j = i + 1; j < compteurStagiaire; j += 1) {
                    raf.seek(j * LONGUEUSTAGIARE);
                    String nomVinCourant = "";
                    nomVinCourant = lectureStagiaire(NOMSTAGIARE, raf);

                    if (nomVinCourant.compareTo(nomVinMini) < 0) {
                        imini = j;
                        nomVinMini = nomVinCourant;
                    }
                }
                permuter(imini * LONGUEUSTAGIARE, i * LONGUEUSTAGIARE, raf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

private static void permuter(int imini, int frontiere, RandomAccessFile raf) {
        try {
            int vinChaineTaille = NOMSTAGIARE + PRENOMSTAGIARE+ DEPARTEMENT + PROMO;

            String vinChaine = "";
            int vinSurface = 0;
            String vinStand = "";

            String vin2Chaine = "";
            int vin2Surface = 0;
            String vin2Stand = "";

            raf.seek(frontiere);

            vinChaine = lectureStagiaire(vinChaineTaille, raf);
            vinSurface = raf.readInt();
            vinStand = lectureStagiaire(ANNEE, raf);

            raf.seek(imini);
            vin2Chaine = lectureStagiaire(vinChaineTaille, raf);
            vin2Surface = raf.readInt();
            vin2Stand = lectureStagiaire(ANNEE, raf);

            raf.seek(frontiere);
            raf.writeChars(vin2Chaine);
            raf.writeInt(vin2Surface);
            raf.writeChars(vin2Stand);

            raf.seek(imini);
            raf.writeChars(vinChaine);
            raf.writeInt(vinSurface);
            raf.writeChars(vinStand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void rechercheDichotomique(int borneInf, int borneSup, RandomAccessFile raf, String nomVinRecherche) {
        int pivot = 0;
        String nomVinCourant = "";
        try {

            if (borneInf <= borneSup) {
                pivot = (borneInf + borneSup) / 2;
                raf.seek(pivot * LONGUEUSTAGIARE);
                nomVinCourant = lectureStagiaire(NOMSTAGIARE, raf);
                if (nomVinRecherche.compareToIgnoreCase(nomVinCourant) == 0) {
                    raf.seek(pivot * LONGUEUSTAGIARE);
                    System.out.println("----------------------------------------------------\r\n"
                            + "Voici les informations concernant le vin recherché :\r\n" + "Nom  : "
                            + lectureStagiaire(NOMSTAGIARE, raf) + "\r\n" + "Prénom : " + lectureStagiaire(PRENOMSTAGIARE, raf)
                            + "\r\n" + "Région : " + lectureStagiaire(DEPARTEMENT, raf) + "\r\n" + "PROMO : "
                            + lectureStagiaire(ANNEE, raf) );
                } else {
                    if (nomVinRecherche.compareToIgnoreCase(nomVinCourant) < 0) {
                        rechercheDichotomique(borneInf, pivot - 1, raf, nomVinRecherche);
                    } else {
                        rechercheDichotomique(pivot + 1, borneSup, raf, nomVinRecherche);
                    }
                }
            } else {
                System.out.println("------------------------------\r\nVin introuvable.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


