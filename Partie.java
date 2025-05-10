import java.util.*;

public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;
    private Echiquier echiquier; 
    private Couleur tour;

    public Partie(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.echiquier = new Echiquier();
        this.tour = Couleur.Blanc;
    }
    public Partie(Partie partie) {
        this.joueur1 = partie.getJoueur1();
        this.joueur2 = partie.getJoueur2();
        this.echiquier = new Echiquier();
        this.tour = Couleur.Blanc;
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public static void nouvellePartie() {
        System.out.println("Création d'une nouvelle partie d'echec...");
        System.out.println("Entrez le nom du joueur 1 qui jouera les blancs:");
        Scanner scanner = new Scanner(System.in);
        
        Joueur joueur1 = new Joueur(scanner.nextLine());

        System.out.println("Entrez le nom du joueur 2 qui jouera les noirs:");
        Joueur joueur2 = new Joueur(scanner.nextLine());

        scanner.close();

        new Partie(joueur1, joueur2).commencer();
    }

    public void commencer() {
        Case caseDepart ;
        Case caseArrivee = null ;

        while (!this.isFin()) {
            this.actualiserAffichage();

            Scanner scanner = new Scanner(System.in);
            // Possibilité de jouer d'un coup ou en deux questions
            System.out.println("Que souhaitez-vous jouer ? (Par exemple, envoyez a1 pour déplacer la pièce présente sur la case a1)");
            
            try {
                // Utilisation de "replace" à la place de "trim" pour enlever l'espace entre deux cases
                // Exemple: " a2 a4 " -> "a2a4"
                // Il est peu probable qu'un autre caractère invisible soit inséré par l'utilisateur
                String ligne = scanner.nextLine().replace(" ", "").toUpperCase();

                // Récupération de la case de départ et de la pièce à jouer
                System.out.println("Quelle pièce voulez-vous déplacer ? (Par exemple, envoyez a1 pour déplacer le pion en a1)");
                caseDepart = this.getCase(ligne);
                Piece piece = caseDepart.getPiece();

                // Le joueur doit avoir une pièce à la case sélectionnée
                if (piece == null || this.tour != piece.getCouleur()) {
                    System.out.println("Vous n'avez pas de pièce à la case " + caseDepart.getNumero());
                    scanner.close();
                    continue;
                }

                // Le joueur peut directement préciser la deuxième case pour jouer directement
                if (ligne.chars().count() > 2) {
                    caseArrivee = this.getCase(ligne.charAt(2) + ligne.charAt(3) + "");
                }

                // Le joueur peut aussi jouer en deux questions
                if (caseArrivee == null) {
                    System.out.println("Vers quelle case voulez-vous déplacer votre " + caseDepart.getPiece().getNom() + " ? (exemple: a4)" );
                    caseArrivee = this.getCase(scanner.nextLine().replace(" ", ""));
                }

                // Fin du scanner, la case de départ et la case d'arrivée ont été sélectionnés avec succès.
                scanner.close();

                // Verification du déplacement de la pièce.
                // La pièce doit pouvoir atteindre la case selon les règles du jeu.
                if (!piece.deplacement(caseArrivee)) {
                    System.out.println("La pièce " + piece.getNom() + " ne peut pas effectuer un tel déplacement !");
                    caseArrivee = null;
                    // TODO peut-être ajouter un piece.getRegle() 
                    // pour un message d'erreur personnalisé en fonction du type de pièce
                    // exemple: Une tour ne peut que se déplacer en ligne droite !
                    continue;
                }

                // Vérification des obstacles
                Piece obstacle = this.findObstacle(caseDepart, caseArrivee);

                // Une autre pièce ne doit pas empêcher le déplacement selon les règles du jeu
                if (obstacle != null) {
                    System.out.println("La piece " + piece.getNomComplet() + " fait obstacle en " + piece.getCase().getNumero() + ".");
                    caseArrivee = null;
                    continue;
                }
                
                // TODO doit sauver son roi si le joueur est en echec =>(impique) le roi ne doit pas être en echec après le coup

                // Validation du coup et changement de tour
                this.validerCoup(caseDepart, caseArrivee);
                if (tour == Couleur.Blanc) tour = Couleur.Noir;
                else tour = Couleur.Blanc;

                if (estEnEchec(tour)) {
                    System.out.println("Echec !!");
                }

            } catch (NoSuchElementException e) {
                scanner.close();
                System.out.println("Veuillez entrer un numéro de case !");
                caseArrivee = null;
            } catch (IndexOutOfBoundsException e) {
                scanner.close();
                System.out.println("Veuillez entrer un numéro de case valide !");
                caseArrivee = null;
            } catch (NumberFormatException e) {
                scanner.close();
                System.out.println("Veuillez préciser un chiffre pour localiser la ligne. (et une lettre pour la colonne)");
                caseArrivee = null;
            } catch (Exception e) {
                scanner.close();
                System.err.println("Une erreur inattendue est survenue !");
                e.printStackTrace(System.out);
                caseArrivee = null;
            }

        }

        this.fin();

    }

    public Joueur getJoueur1() {
        return this.joueur1;
    }

    public Joueur getJoueur2() {
        return this.joueur2;
    }

    public Echiquier getEchiquier() {
        return this.echiquier;
    }

    public Couleur getTour() {
        return this.tour;
    }

    public Case getCase(String numeroCase) throws NumberFormatException, IndexOutOfBoundsException {
        // numeroCase doit être dans le format <LETTRE_COLONNE><numéroLigne>
        // Exemple: "A2" pour la case à la colonne a et sur la ligne 2
        int colonne = getColonne(numeroCase.charAt(0));
        int ligne = Integer.parseInt(numeroCase.charAt(1)+"");

        return this.echiquier.getCase(colonne, ligne);
    }

    //TODO Finir
    public Piece findObstacle(Case caseDepart, Case caseArrivee) {
        Piece piece = caseDepart.getPiece();

        // Une pièce du même camp ne doit pas être dans la case d'arrivée
        Piece obstacle = caseArrivee.getPiece();
        if (obstacle != null && obstacle.getColor() == piece.getColor()) {
            return obstacle;
        }

        if (piece.getNom() == "Cavalier") return null;

        // Vérification des autres cases entre la case d'arrivée et la case de départ
        obstacle = null;

        //Partir de la case d'arrivée pour aller jusqu'à la case de départ
        int ligneI = caseArrivee.getLigne();
        int colonneI = caseArrivee.getColonne();

        // Tant que la case de départ n'est pas atteinte et qu'un obstacle n'a pas été trouvé
        while (obstacle == null) {
            // Deplacement vers la prochaine case à vérifier
            if (ligneI > caseDepart.getLigne()) ligneI--;
            else if (ligneI < caseDepart.getLigne()) ligneI++;

            if (colonneI > caseDepart.getColonne()) colonneI--;
            else if (colonneI < caseDepart.getColonne()) colonneI++;

            if (ligneI == caseDepart.getLigne() && colonneI == caseDepart.getColonne()) return null;

            obstacle = Echiquier.getCase(colonneI, ligneI).getPiece();
        }

        return obstacle;
    }

    public boolean estEnEchec(Couleur tour) {
        // TODO si un pion adverse peu manger le roi
        return false;
    }

    //TODO Finir
    public boolean isFin() {
        boolean echec = estEnEchec(tour);
        //TODO détecter: echec + ne peut pas bouger OU match nul
        return false;
    }

    //TODO Finir
    public void actualiserAffichage() { 

        String affichage = "";
        Case[][] cases = this.echiquier.getCases();
        for (int ligneI = 0; ligneI < cases.length; ligneI++) {

            // Écriture d'une nouvelle ligne
            affichage += "\n" + ligneI+1 + " |";

            for (int caseI = 0; caseI < cases[ligneI].length; caseI++) {

                //Écriture d'une nouvelle case
                Piece piece = cases[ligneI][caseI].getPiece();
                char contenu ;
                if (piece == null) contenu = ' ';
                else if (piece.getCouleur() == Couleur.Noir) {
                    contenu = "\u001B[30m"+piece.getIcone();
                } else {
                    contenu = piece.getIcon();
                }
                

                if ((ligneI%2 == 0 && caseI%2 == 0) || (ligneI%2 != 0 && caseI%2 != 0)) {
                    affichage += "\u001B[47m " + contenu + " \u001B[0m";
                } else {
                    affichage += " " + contenu + " ";
                }
            }
        }

        affichage += "    A   B   C   D   E   F   G   H";
        System.out.println(affichage);
    }

    //TODO Finir
    public void validerCoup(Case caseArrivee, Case caseDepart) {
        Piece piece = caseDepart.getPiece();

        caseDepart.setPiece(null);
        caseArrivee.setPiece(piece);
        
    }

    //TODO Finir
    public void fin() {

    }  

}
