import java.util.*;

public class Partie {
    private final Joueur joueurBlanc;
    private final Joueur joueurNoir;
    private final Echiquier echiquier; 
    private Joueur joueurActuel;

    public Partie(String nomJoueur1, String nomJoueur2) {
        this.echiquier = new Echiquier();

        // Initialisation des joueurs
        this.joueurBlanc = new Joueur(this, Couleur.Blanc, nomJoueur1);
        this.joueurNoir = new Joueur(this, Couleur.Noir, nomJoueur2);
    }
    public Partie(Partie partie) {
        this(partie.getJoueur(Couleur.Blanc).getNom(), partie.getJoueur(Couleur.Noir).getNom());
    }

    public static void nouvellePartie() {
        try {
            System.out.println("Création d'une nouvelle partie d'echec...");
            System.out.println("Entrez le nom du joueur 1 qui jouera les blancs:");
            Scanner scanner = new Scanner(System.in);
            
            String nomJoueur1 = scanner.nextLine();

            System.out.println("Entrez le nom du joueur 2 qui jouera les noirs:");
            String nomJoueur2 = scanner.nextLine();

            scanner.close();

            new Partie(nomJoueur1, nomJoueur2).commencer();
            
        } catch (Exception e) {
            System.out.println("Une erreur est survenue au lancement de la partie !");
        }

    }

    public static void repondre(Joueur joueur, String message) {
        System.out.println("-> " + joueur.getNom() + " (" + joueur.getCouleur() + ") : " + message);
    }

    public void commencer() {
        Case caseDepart ;
        Case caseArrivee = null ;
        boolean enEchec = false;

        while (!this.isFin()) {
            this.actualiserAffichage();

            System.out.println("Au tour de " + this.joueurActuel.getNom() +" !");

            Scanner scanner = new Scanner(System.in);
            // Possibilité de jouer d'un coup ou en deux questions
            repondre(this.joueurActuel, "Que souhaitez-vous jouer ? Par exemple, envoyez a1 pour déplacer la pièce présente sur la case a1. Vous pouvez aussi répondre \"ff\" pour déclarer forfait");
            
            try {
                // Utilisation de "replace" à la place de "trim" pour enlever l'espace entre deux cases
                // Exemple: " a2 a4 " -> "a2a4"
                // Il est peu probable qu'un autre caractère invisible soit inséré par l'utilisateur
                String ligne = scanner.nextLine().replace(" ", "").toUpperCase();

                // Déclarer forfait
                if (ligne.equals("ff")) {
                    Joueur joueurAdverse = this.getJoueurAdverse(this.joueurActuel);
                    repondre(joueurAdverse, this.joueurActuel.getNom() + " veut déclarer forfait, acceptez vous ? (\"oui\" ou \"non\")");
                    ligne = scanner.nextLine();
                    if (ligne.equals("oui")) {
                        this.joueurActuel = this.getJoueurAdverse(this.joueurActuel);
                        break;
                    } 
                }

                // Récupération de la case de départ et de la pièce à jouer
                repondre(this.joueurActuel, "Quelle pièce voulez-vous déplacer ? (Par exemple, envoyez a1 pour déplacer le pion en a1)");
                caseDepart = this.getCase(ligne);
                Piece piece = caseDepart.getPiece();

                // Le joueur doit avoir une pièce à la case sélectionnée
                if (piece == null || this.joueurActuel.getCouleur() != piece.getCouleur()) {
                    repondre(this.joueurActuel, "Vous n'avez pas de pièce à la case " + caseDepart.getNumero());
                    scanner.close();
                    continue;
                }

                // Le joueur peut directement préciser la deuxième case pour jouer directement
                if (ligne.chars().count() > 2) {
                    caseArrivee = this.getCase(ligne.charAt(2) + ligne.charAt(3) + "");
                }

                // Le joueur peut aussi jouer en deux questions
                if (caseArrivee == null) {
                    repondre(this.joueurActuel, "Vers quelle case voulez-vous déplacer votre " + caseDepart.getPiece().getNom() + " ? (exemple: a4)" );
                    caseArrivee = this.getCase(scanner.nextLine().replace(" ", ""));
                }

                // Fin du scanner, la case de départ et la case d'arrivée ont été sélectionnés avec succès.
                scanner.close();

                if (caseArrivee.equals(caseDepart)) {
                    repondre(this.joueurActuel, "Vous ne pouvez pas rester sur la même case !");
                    caseArrivee = null;
                    continue;
                }

                // Verification du déplacement de la pièce.
                // La pièce doit pouvoir atteindre la case selon les règles du jeu.
                if (!piece.deplacement(caseArrivee)) {
                    repondre(this.joueurActuel, "La pièce " + piece.getNom() + " ne peut pas effectuer un tel déplacement !");
                    caseArrivee = null;
                    // TODO peut-être ajouter un piece.getRegle() 
                    // pour un message d'erreur personnalisé en fonction du type de pièce
                    // exemple: Une tour ne peut que se déplacer en ligne droite !
                    continue;
                }

                // Vérification des obstacles et récupération du premier obstacle 
                Piece obstacle = this.findObstacle(caseDepart, caseArrivee);

                // Une autre pièce ne doit pas empêcher le déplacement selon les règles du jeu
                if (obstacle != null) {
                    repondre(this.joueurActuel, "La piece " + obstacle.getNomComplet() + " fait obstacle en " + piece.getCase().getNumero() + ".");
                    caseArrivee = null;
                    continue;
                }

                if (estEnEchec(caseDepart, caseArrivee)) {
                    repondre(this.joueurActuel, "Vous ne pouvez pas effectuer ce déplacement, votre roi serait en echec");
                    caseArrivee = null;
                    continue;
                }

                // Validation du coup et changement de tour
                this.validerCoup(caseDepart, caseArrivee);

                repondre(this.joueurActuel, "Coup joué : " + piece.getNomComplet() + " " + caseDepart.getNumero() + " --> " + caseArrivee.getNumero());

                this.joueurActuel = this.getJoueurAdverse(this.joueurActuel);

                enEchec = this.estEnEchec();
                if (enEchec) {
                    repondre(this.joueurActuel, "Echec !!");
                }

            } catch (NoSuchElementException e) {
                scanner.close();
                repondre(this.joueurActuel, "Veuillez entrer un numéro de case !");
                caseArrivee = null;
            } catch (IndexOutOfBoundsException e) {
                scanner.close();
                repondre(this.joueurActuel, "Veuillez entrer un numéro de case valide !");
                caseArrivee = null;
            } catch (NumberFormatException e) {
                scanner.close();
                repondre(this.joueurActuel, "Veuillez préciser un chiffre pour localiser la ligne. (et une lettre pour la colonne)");
                caseArrivee = null;
            } catch (Exception e) {
                scanner.close();
                System.err.println("Une erreur inattendue est survenue !");
                e.printStackTrace(System.out);
                caseArrivee = null;
            }

        }

        String raison = "echec";
        if (enEchec) raison = "Match nul"; 
        this.fin(raison);

    }

    public Joueur getJoueur(Couleur couleur) {
        if (couleur == Couleur.Blanc) {
            return joueurNoir;
        } else {
            return joueurBlanc;
        }
    }

    public Joueur getJoueurActuel() {
        return this.joueurActuel;
    }

    public Joueur getJoueurAdverse(Joueur joueur) {
        if (this.joueurActuel.getCouleur() == Couleur.Blanc) {
            return joueurNoir;
        } else {
            return joueurBlanc;
        }
    }

    public Echiquier getEchiquier() {
        return this.echiquier;
    }

    public Case getCase(String numeroCase) throws NumberFormatException, IndexOutOfBoundsException {
        // numeroCase doit être dans le format <LETTRE_COLONNE><numéroLigne>
        // Exemple: "A2" pour la case à la colonne a et sur la ligne 2
        int colonne = getColonne(numeroCase.charAt(0));
        int ligne = Integer.parseInt(numeroCase.charAt(1)+"");

        return this.echiquier.getCase(colonne, ligne);
    }


    public Piece findObstacle(Case caseDepart, Case caseArrivee) {
        Piece piece = caseDepart.getPiece();

        // Une pièce du même camp ne doit pas être dans la case d'arrivée
        Piece obstacle = caseArrivee.getPiece();
        if (obstacle != null && obstacle.getColor() == piece.getColor()) {
            return obstacle;
        }

        //TODO pourquoi ne pas utiliser instanceof ? 
        if (piece.getNom() == "Cavalier") return null;

        // Partir de la case d'arrivée pour aller jusqu'à la case de départ
        int ligneI = caseArrivee.getLigne();
        int colonneI = caseArrivee.getColonne();

        // Calcul de la direction du déplacement
        int ligneDiff = caseArrivee.getLigne() - caseDepart.getLigne();
        int colonneDiff = caseArrivee.getColonne() - caseDepart.getColonne();

        int ligneDirection = ligneDiff / Math.abs(ligneDiff);
        int colonneDirection = colonneDiff / Math.abs(colonneDiff);

        // Tant que la dernière case (avant case de départ) n'est pas atteinte et tant qu'il n'y a pas d'obstacle
        int ligneDerniereCase = caseDepart.getLigne() - ligneDirection;
        int colonneDerniereCase = caseDepart.getColonne() - colonneDirection;

        while (ligneI != ligneDerniereCase || colonneI != colonneDerniereCase) {

            // Deplacement vers la prochaine case à vérifier
            ligneI += ligneDirection;
            colonneI += colonneDirection;

            // Si un obstacle est trouvé, le retourner (n'importe quelle pièce sur le chemin)
            obstacle = this.echiquier.getCase(colonneI, ligneI).getPiece();

            if (obstacle != null) return obstacle;
        }

        return null;
    }

    public boolean estEnEchec() {
        Roi roi = this.joueurActuel.getRoi();

        // Recherche une pièce du joueur adverse qui peut accéder sans obstacle au roi du joueur courant
        for (Piece piece : this.getJoueurAdverse(this.joueurActuel).getPieces()) {
            if (
                piece.deplacement(roi.getCase()) && 
                this.findObstacle(piece.getCase(), roi.getCase()) == null
            ) return true;
        }
        
        return false;
    }

    public boolean estEnEchec(Case caseDepart, Case caseArrivee) {
        // Permet de tester si le roi est en echec à la suite d'un déplacement

        Piece pieceEcrasee = caseArrivee.getPiece();
        caseDepart.getPiece().setCase(caseArrivee);

        boolean enEchec = estEnEchec();

        caseArrivee.getPiece().setCase(caseDepart);
        if (pieceEcrasee != null) pieceEcrasee.setCase(caseArrivee);
        
        return enEchec;
    }

    public boolean isFin() {

        // Recherche de coup jouable après lequel le roi n'est pas en echec
        for (Case[] ligneCases : this.echiquier.getCases()) {
            for (Case caseTestee: ligneCases) {

                // Vérifier pour toutes les pièces jouables si la case est accessible et sans obstacle
                for (Piece pieceTestee : this.joueurActuel.getPieces()) {

                    if (pieceTestee.deplacement(caseTestee) && !this.findObstacle(pieceTestee.getCase(), caseTestee)) {
                        // Verifier si le roi est en echec si la pièce testée est déplacée à la case testée
                        if (!this.estEnEchec(pieceTestee.getCase(), caseTestee)) return false;
                    }
                }
            }
        }
        return true;
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

    public void validerCoup(Case caseArrivee, Case caseDepart) {
        Piece pieceMangee = caseArrivee.getPiece();

        if (pieceMangee != null) {
            if (!this.getJoueurAdverse(this.joueurActuel).getPieces().remove(pieceMangee)) System.err.println("La pièce mangée n'a pas été retirée de la liste des pièces du joueur adverse");
            // TODO remplir la liste des pieces mangées et retirer le condition
        }
        caseDepart.getPiece().setCase(caseArrivee);
    }

    //TODO Finir
    public void fin(String raison) {

        if (raison.equals("forfait")) {
            repondre(this.getJoueurAdverse(this.joueurActuel), this.joueurActuel.getNom() + " a déclaré forfait, la partie est remportée par " + joueurAdverse.getNom() + " !");
        }
        if (raison.equals("echec")) {
            repondre(this.joueurActuel, "Echec et mat !");
        } else {
            repondre(this.joueurActuel, "Match nul !");
        }

    }  

}
