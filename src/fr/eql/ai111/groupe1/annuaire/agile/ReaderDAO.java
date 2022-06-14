package fr.eql.ai111.groupe1.annuaire.agile;

import java.io.*;

public class ReaderDAO {
    public boolean createAccount(
            String login,
            String password,
            String role) {
        File folder = new File("Connexion");
        // Si le dossier n'existe pas, je le crée.
        if (!folder.exists()) {
            folder.mkdir();
        }
        boolean isCreated = false;
        // Je déclare le fichier utilisateur
        File userFile = new File(folder + "/" + login + ".lib");
        try {
            // Je tente de créer le fichier sur le disque, s'il n'existe pas déjà.
            isCreated = userFile.createNewFile();
            if (isCreated) {
                FileWriter fw = new FileWriter(userFile, false);
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(login);
                bw.newLine();
                bw.write(password);
                bw.newLine();
                bw.write(role);
                bw.close();
                fw.close();
            }
        } catch (IOException e) {
            System.out.println("Le fichier utilisateur n'a pas été créé.");
        }
        return isCreated;
    }

    public Administrateur connect(String login, String password) {
        File userFile = new File("Connexion/" + login + ".lib");
        Administrateur reader = null;
        if (!userFile.exists()) {
            return null;
        }
        try {
            FileReader fr = new FileReader(userFile);
            BufferedReader br = new BufferedReader(fr);
            String loginInFile = br.readLine();
            String passwordInFile = br.readLine();
            String roleInFile = br.readLine();
            reader = new Administrateur(
                    loginInFile, passwordInFile,roleInFile);
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Un problème s'est produit lors de la lecture du fichier utilisateur.");
        }
        // Si le password est correct, on retourne l'instance du reader.
        if (reader.getPassword().equals(password)) {
            return reader;
        } else {
            return null;
        }
    }
}
