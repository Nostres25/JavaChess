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
                if (piece == null || this.joueurActuel.getCouleur() != piece.getCouleur()) {
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
                    caseArrivee = this.getCase(scanner.nextLine().replace(" ", "")); // TODO revoir si faut re verif si c'est valide
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

                // Vérification des obstacles et récupération du premier obstacle 
                Piece obstacle = this.findObstacle(caseDepart, caseArrivee);

                // Une autre pièce ne doit pas empêcher le déplacement selon les règles du jeu
                if (obstacle != null) {
                    System.out.println("La piece " + obstacle.getNomComplet() + " fait obstacle en " + piece.getCase().getNumero() + ".");
                    caseArrivee = null;
                    continue;
                }
                
                // TODO doit sauver son roi si le joueur est en echec =>(impique) le roi ne doit pas être en echec après le coup

                // Validation du coup et changement de tour
                this.validerCoup(caseDepart, caseArrivee);

                System.out.print("Coup joué par " + this.joueurActuel.getNom() + " : " + piece.getNomComplet() + " " + caseDepart.getNumero() + " --> " + caseArrivee.getNumero());

                this.joueurActuel = this.getJoueurAdverse();

                System.out.println("Au tour de " + this.joueurActuel.getNom() +" !");

                if (this.estEnEchec(this.joueurActuel)) { // TODO Is fin vérifie les echecs aussi -> doublon inutile
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

    public Joueur getJoueurAdverse() {
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

    //TODO Finir
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

    public boolean estEnEchec(Joueur joueur) {
        Roi roi = joueur.getRoi();

        // Recherche une pièce du joueur adverse qui peut accéder sans obstacle au roi du joueur courant
        for (Piece piece : this.getJoueurAdverse().getPieces()) {
            if (
                piece.deplacement(roi.getCase()) && 
                this.findObstacle(piece.getCase(), roi.getCase()) == null
            ) return true;
        }
        
        return false;
    }

    public boolean isFin() {

        /* TODO retirer ce test
        Roi roi = this.joueurActuel.getRoi();
        ArrayList<Piece> estEnEchecPar = new ArrayList<>();

        ArrayList<Piece> piecesJouables = this.joueurActuel.getPieces();

        for (Piece piece : this.getProchainJoueur().getPieces()) {
            if (piece.deplacement(roi.getCase())) {
                ArrayList<Piece> obstacles = this.findObstacles(piece.getCase(), roi.getCase()); 
                if (obstacles.isEmpty()) {
                    estEnEchecPar.add(piece);
                } else if (obstacles.size() == 1 && piecesJouables.contains(obstacles.get(0))) {
                    //TODO retirer le message d'erreur si tout fonctionne bien
                    if (!piecesJouables.remove(obstacles.get(0))) System.err.println("Debug: dans isFin(), une pièce (" + obstacles.get(0) + ") qui fait obstacle à l'enemie pour mettre en echec le roi ("+roi+") n'a pas pu être retiré de la liste des pièces jouables ("+piecesJouables+") alors qu'elle est censée y être");
                }
            }
        }*/

        // Recherche de coup jouable après lequel le roi n'est pas en echec
        for (Case[] ligneCases : this.echiquier.getCases()) {
            for (Case caseTestee: ligneCases) {

                // Vérifier pour toutes les pièces jouables si la case est accessible et sans obstacle
                for (Piece pieceTestee : this.joueurActuel.getPieces()) {

                    if (pieceTestee.deplacement(caseTestee) && !this.findObstacle(pieceTestee.getCase(), caseTestee)) {

                        // Variables tampon pour revenir en arrière une fois le sénario testé
                        Piece pieceEcrasee = caseTestee.getPiece();
                        Case caseActuelle = pieceTestee.getCase();
                        
                        // Verifier si le roi est en echec si la pièce testée est déplacée à la case testée
                        pieceTestee.setCase(caseTestee);
                        if (!this.estEnEchec(this.joueurActuel)) return false;

                        // Remise en état de l'échiquier
                        pieceTestee.setCase(caseActuelle);
                        if (pieceEcrasee != null) pieceEcrasee.setCase(caseTestee);

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
            if (!this.getJoueurAdverse().getPieces().remove(pieceMangee)) System.err.println("La pièce mangée n'a pas été retirée de la liste des pièces du joueur adverse");
            // TODO remplir la liste des pieces mangées et retirer le condition
        }
        caseDepart.getPiece().setCase(caseArrivee);
    }

    //TODO Finir
    public void fin() {

        if (this.estEnEchec(this.joueurActuel)) {
            System.out.println("Echec et mat !");
        } else {
            System.out.println("Match nul !");
        }

    }  

}
