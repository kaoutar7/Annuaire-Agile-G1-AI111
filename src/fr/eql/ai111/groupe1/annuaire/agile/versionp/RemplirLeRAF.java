package fr.eql.ai111.groupe1.annuaire.agile.versionp;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RemplirLeRAF {

    public static final int NOMSTAGIARE = 50;
    public static final int PRENOMSTAGIARE = 20;
    public static final int DEPARTEMENT = 20;
    public static final int PROMO = 10;
    public static final int ANNEE = 4; // il faut je pense l'enlever et j'ajoute case 4
    public static final int LONGUEUSTAGIARE = ((NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO + ANNEE) * 2);

    public static void main(String[] args) {
        int compteurTab = 0;
        String ligne = "";
        String mot = "";
        int compteurStagiaire = 0;


        RandomAccessFile raf;

        try {
            //Création du RAF
            raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
            //Lecture de fichier.txt
            FileReader fichierOriginal = new FileReader("c:/Projet1/stagiaires.txt");
            BufferedReader bf = new BufferedReader(fichierOriginal);

            //tant qu'il y a des lignes dans le fichier.txt
            while ((ligne = bf.readLine()) != null) {

                if (!ligne.equals("*")) { // le problème est là: c'est bon problème résolue
                    //MAinteneant le problème pourquoi ils ont coller l'un à l'autre "prénom, nom"
                    compteurTab = 0;
                    for (int i = 0; i < ligne.length(); i++) {
                        if (ligne.charAt(i) != ' ') {
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
                                //case 4:
                                //int nb = Integer.parseInt(mot);
                                // raf.writeInt(nb);
                                //  mot = completer(mot, ANNEE);
                                //raf.writeChars(mot);
                                // break;

                                default:
                                    break;
                            }
                            compteurTab += 1;
                            mot = "";

                        }
                    }
                    mot = completer(mot, ANNEE);
                    raf.writeChars(mot);
                    mot = "";

                    //int nb = Integer.parseInt(mot);
                    //raf.writeInt(nb);
                    compteurStagiaire += 1;
                }

            }


            // triParExtraction(LONGUEURVIN, compteurVins, raf);
            listeVins(compteurStagiaire, raf);
            String rechercheVin = JOptionPane.showInputDialog("Veuillez entrer le nom d'un vin");
            rechercheVin = completer(rechercheVin, NOMSTAGIARE);
            rechercheDichotomique(0, compteurStagiaire, raf, rechercheVin);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    public static String lectureStag(int longueurChaine, RandomAccessFile raf) {

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

    public static void listeVins(int compteurVins, RandomAccessFile raf) {

        try {
            raf.seek(0);
            int taille1 = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO + ANNEE;
            //int taille2 = ANNEE;
            for (int j = 0; j < compteurVins; j++) {
                String chaine = "";
                String chaine1 = lectureStag(taille1, raf);
                //chaine = chaine + chaine1 + raf.readInt() + "	";
                //String chaine2 = lectureStag(taille2, raf);
                chaine += chaine1;
                System.out.println(chaine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void triParExtraction(int longueurVin, int compteurVin, RandomAccessFile raf) {
        try {
            for (int i = 0; i < compteurVin - 1; i += 1) {
                int imini = i;
                raf.seek(i * LONGUEUSTAGIARE);
                String nomVinMini = "";
                nomVinMini = lectureStag(NOMSTAGIARE, raf);

                for (int j = i + 1; j < compteurVin; j += 1) {
                    raf.seek(j * LONGUEUSTAGIARE);
                    String nomVinCourant = "";
                    nomVinCourant = lectureStag(NOMSTAGIARE, raf);

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
            int vinChaineTaille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO;

            String vinChaine = "";
            int vinSurface = 0;
            String vinStand = "";

            String vin2Chaine = "";
            int vin2Surface = 0;
            String vin2Stand = "";

            raf.seek(frontiere);

            vinChaine = lectureStag(vinChaineTaille, raf);
            vinSurface = raf.readInt();
            vinStand = lectureStag(ANNEE, raf);

            raf.seek(imini);
            vin2Chaine = lectureStag(vinChaineTaille, raf);
            vin2Surface = raf.readInt();
            vin2Stand = lectureStag(ANNEE, raf);

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
                nomVinCourant = lectureStag(NOMSTAGIARE, raf);
                if (nomVinRecherche.compareToIgnoreCase(nomVinCourant) == 0) {
                    raf.seek(pivot * LONGUEUSTAGIARE);
                    System.out.println("----------------------------------------------------\r\n"
                            + "Voici les informations concernant le vin recherché :\r\n" + "Nom du vin : "
                            + lectureStag(NOMSTAGIARE, raf) + "\r\n" + "Appellation : " + lectureStag(PRENOMSTAGIARE, raf)
                            + "\r\n" + "Région : " + lectureStag(DEPARTEMENT, raf) + "\r\n" + "Nom du propriétaire : "
                            + lectureStag(PROMO, raf) + "\r\n" + "Numéro de stand : " + lectureStag(ANNEE, raf));
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
