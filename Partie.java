import java.util.*;

public class Partie {
    private final Joueur joueurBlanc;
    private final Joueur joueurNoir;
    private final Echiquier echiquier; 
    private Joueur joueurActuel;
    private boolean horlogesActives;

    public Partie(String nomJoueur1, String nomJoueur2, boolean avecHorloge) {
        this.horlogesActives = avecHorloge;
        this.echiquier = new Echiquier();

        // Initialisation des joueurs
        this.joueurBlanc = new Joueur(this, Couleur.Blanc, nomJoueur1);
        this.joueurNoir = new Joueur(this, Couleur.Noir, nomJoueur2);
    }
    public Partie(Partie partie) {
        this(partie.getJoueur(Couleur.Blanc).getNom(), partie.getJoueur(Couleur.Noir).getNom(), partie.getHorlogeActivee());
    }

    public static Couleur getCouleurOpposee(Couleur couleur) {
        if (couleur == Couleur.Blanc) return Couleur.Noir;
        else return Couleur.Blanc;
    }

    public static void nouvellePartie() {
        try {
            System.out.println("Création d'une nouvelle partie d'echec...");

            // Demander le pseudo des joueurs
            System.out.println(Affichage.bleu("Entrez le nom du joueur qui jouera les ")+"blancs:");
            Scanner scanner = new Scanner(System.in);
            
            String nomJoueur1 = scanner.nextLine();

            System.out.println(Affichage.bleu("Entrez le nom du joueur qui jouera les ")+Affichage.noirf("noirs:"));
            String nomJoueur2 = scanner.nextLine();

            String reponse ;

            // Demander s'il est souhaité de jouer avec l'horloge
            boolean avecHorloge = false;
            System.out.println(Affichage.jaune("Souhaitez-vous jouer avec un temps limité défini à 10 minutes de temps de réflexion ? (envoyer \"oui\" pour un temps limité, \"non\" sinon)"));
            reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("oui")) {
                avecHorloge = true;
            }

            // Création de la partie
            Partie partie = new Partie(nomJoueur1, nomJoueur2, avecHorloge);

            // Demander s'il est souhaité de jouer avec l'affichage des pièces d'échecs avec les caractères unicode
            System.out.println(Affichage.jaune("Souhaitez-vous essayer le mode d'affichage experimental ? Le jeu sera bien plus facile à lire mais il se pourrait qu'il ne fonctionne pas dans votre terminal, notamment si vous êtes sur windows ! (envoyer \"oui\" pour essayer, \"non\" sinon)"));
            reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("oui")) {
                // Si oui, modification de l'icone des pièces
                for (Piece piecesBlanches : partie.getJoueur(Couleur.Blanc).getPieces()) {
                    piecesBlanches.setIcone(Affichage.getIcone(piecesBlanches));
                }

                for (Piece piecesNoires : partie.getJoueur(Couleur.Noir).getPieces()) {
                    piecesNoires.setIcone(Affichage.getIcone(piecesNoires));
                }

            } else {
                // Affichage de la légende pour chaque type de pièce
                System.out.println("- R -> Roi\n- D -> Dame\n- C -> Cavalier\n- F -> Fou\n- T -> Tour\n- P -> Pion ");
            }
            
            // Lancement du premier tour
            partie.changerDeTour(null, null);;
            
        } catch (Exception e) {
            System.err.println("Une erreur est survenue au lancement de la partie !");
            e.printStackTrace();
        }

    }

    public boolean estDeplacementValide(Piece piece, Case caseArrivee) {

                // Verification du déplacement de la pièce.
                // La pièce doit pouvoir atteindre la case selon les règles du jeu.
                if (!piece.deplacement(caseArrivee)) {
                    Affichage.erreur(this.joueurActuel, "La pièce " + piece.getNom() + " ne peut pas effectuer un tel déplacement !");
                    // TODO peut-être ajouter un piece.getRegle() 
                    // pour un message d'erreur personnalisé en fonction du type de pièce
                    // exemple: Une tour ne peut que se déplacer en ligne droite !
                    return false;
                }

                // Vérification des obstacles et récupération du premier obstacle 
                Piece obstacle = this.findObstacle(piece.getCase(), caseArrivee);

                // Une autre pièce ne doit pas empêcher le déplacement selon les règles du jeu
                if (obstacle != null) {
                    Affichage.erreur(this.joueurActuel, "La piece " + obstacle + " fait obstacle en " + obstacle.getCase().getNumero() + ".");
                    return false;
                }

                if (estEnEchec(piece.getCase(), caseArrivee)) {
                    Affichage.erreur(this.joueurActuel, "Vous ne pouvez pas effectuer ce déplacement, votre roi serait en echec");
                    return false;
                }

                return true;
    }

    public void demanderAction(boolean enEchec, long debutTour) {


        // Possibilité de jouer d'un coup ou en deux questions
        try {
                Scanner scanner = new Scanner(System.in);
        
                Affichage.question(this.joueurActuel, "Que souhaitez-vous jouer ?"," Par exemple, envoyez a1 pour déplacer la pièce présente sur la case a1. Vous pouvez aussi répondre \"ff\" pour déclarer forfait");

                // Utilisation de "replace" à la place de "trim" pour enlever l'espace entre deux cases
                // Exemple: " a2 a4 " -> "a2a4"
                // Il est peu probable qu'un autre caractère invisible soit inséré par l'utilisateur
                
                String ligne = scanner.nextLine().replace(" ", "").toUpperCase();
                // Déclarer forfait
                if (ligne.equals("FF")) {
                    Joueur joueurAdverse = this.getJoueurAdverse(this.joueurActuel);
                    Affichage.question(joueurAdverse, this.joueurActuel.getNom() + " veut déclarer forfait, acceptez vous ? (\"oui\" ou \"non\")", "");
                    ligne = scanner.nextLine();
                    if (ligne.equals("oui")) {
                        this.joueurActuel = this.getJoueurAdverse(this.joueurActuel);
                        fin("forfait");
                        return;
                    } else {
                        Affichage.erreur(this.joueurActuel, this.getJoueurAdverse(this.joueurActuel).getNom() + " a refusé la victoire par forfait.");
                        demanderAction(enEchec, debutTour);
                        return;
                    }
                }

                // Récupération de la case de départ et de la pièce à jouer
                Case caseDepart = this.getCase(ligne);
                Piece piece = caseDepart.getPiece();

                // Le joueur doit avoir une pièce à la case sélectionnée
                if (piece == null || this.joueurActuel.getCouleur() != piece.getCouleur()) {
                    Affichage.erreur(this.joueurActuel, "Vous n'avez pas de pièce à la case " + caseDepart.getNumero());
                    demanderAction(enEchec, debutTour);
                    return;
                }

                Case caseArrivee = null ;

                // Le joueur peut directement préciser la deuxième case pour jouer directement
                if (ligne.chars().count() > 2) {
                    caseArrivee = this.getCase(ligne.charAt(2) +""+ ligne.charAt(3));
                }


                // Le joueur peut aussi jouer en deux questions
                if (caseArrivee == null) {
                    Affichage.question(this.joueurActuel, "Vers quelle case voulez-vous déplacer votre " + caseDepart.getPiece() + " ?"," (exemple: a4)" );
                    caseArrivee = this.getCase(scanner.nextLine().replace(" ", "").toUpperCase());
                }

                // TODO Fin du scanner, la case de départ et la case d'arrivée ont été sélectionnés avec succès.
                float tempsPasse = System.currentTimeMillis() - debutTour;
                if (this.joueurActuel.getHorloge() - tempsPasse <= 0) {
                    this.joueurActuel.retirerTemps(tempsPasse);
                    fin("temps écoulé");
                    return;
                }


                // Sécurité concernant la possibilité de ne pas se déplacer
                if (caseArrivee.equals(caseDepart)) {
                    Affichage.erreur(this.joueurActuel, "Vous ne pouvez pas rester sur la même case !");
                    demanderAction(enEchec, debutTour);
                    return;
                }

                if (!this.estDeplacementValide(piece, caseArrivee)) {
                    demanderAction(enEchec, debutTour);
                    return;
                }

                // Validation du coup et changement de tour
                this.validerCoup(caseDepart, caseArrivee, tempsPasse);
                this.changerDeTour(piece, caseDepart);

             } catch (NoSuchElementException e) {
                Affichage.erreur(this.joueurActuel, "Veuillez entrer un numéro de case ! (format: <lettreColonne><numéroLigne>, ex: A2)");
                demanderAction(enEchec, debutTour);
            } catch (IndexOutOfBoundsException e) {
                //TODO cette erreur est survenue anormalement à un moment - corrigee ???
                Affichage.erreur(this.joueurActuel, "Veuillez entrer un numéro de case valide ! Les lettres des colonnes sont de A à H et les numéros de lignes de 1 à 8. (format: <lettreColonne><numéroLigne>, ex: A2)");
                //e.printStackTrace();
                demanderAction(enEchec, debutTour);
            } catch (NumberFormatException e) {
                Affichage.erreur(this.joueurActuel, "Veuillez préciser une lettre pour la colonne suivie d'un chiffre pour la ligne (format: <lettreColonne><numéroLigne>, ex: A2)");
                demanderAction(enEchec, debutTour);
            } catch (Exception e) {
                System.err.println(Affichage.font_rouge("Une erreur inattendue est survenue !"));
                e.printStackTrace(System.out);
                demanderAction(enEchec, debutTour);
            }

    }

    public void changerDeTour(Piece pieceDeplacee, Case ancienneCase) {
        boolean enEchec = false;

        if (this.joueurActuel == null)  this.joueurActuel = joueurBlanc;
        else {
            this.joueurActuel = this.getJoueurAdverse(this.joueurActuel);
            enEchec = this.estEnEchec(); 
        }
                
        Affichage.actualiserAffichage(this, enEchec, pieceDeplacee, ancienneCase);

        if (isFin()) {

            String raison = "Match nul";
            if (enEchec) raison = "echec";
 
            fin(raison);
            return;
        }

        Affichage.info(this.getJoueurActuel(), "Au tour de " + this.getJoueurActuel().getNom() +" !");
        demanderAction(enEchec, System.currentTimeMillis());
    }

    public Joueur getJoueur(Couleur couleur) {
        if (couleur == Couleur.Blanc) {
            return joueurBlanc;
        } else {
            return joueurNoir;
        }
    }

    public Joueur getJoueurActuel() {
        return this.joueurActuel;
    }

    public Joueur getJoueurAdverse(Joueur joueur) {
        return this.getJoueur(getCouleurOpposee(joueur.getCouleur()));
    }

    public Echiquier getEchiquier() {
        return this.echiquier;
    }

    public Case getCase(String numeroCase) throws NumberFormatException, IndexOutOfBoundsException {
        // numeroCase doit être dans le format <LETTRE_COLONNE><numéroLigne>
        // Exemple: "A2" pour la case à la colonne a et sur la ligne 2
        int colonne = Echiquier.getNombreColonne(numeroCase.charAt(0)) - 1;
        int ligne = Integer.parseInt(numeroCase.charAt(1)+"") - 1;

        return this.echiquier.getCase(colonne, ligne);
    }

    public boolean getHorlogeActivee() {
        return this.horlogesActives;
    }


    public Piece findObstacle(Case caseDepart, Case caseArrivee) {
        Piece piece = caseDepart.getPiece();

        // Une pièce du même camp ne doit pas être dans la case d'arrivée
        Piece obstacle = caseArrivee.getPiece();
        if (obstacle != null && obstacle.getCouleur() == piece.getCouleur()) {
            return obstacle;
        }

        //TODO pourquoi ne pas utiliser instanceof ? 
        if (piece instanceof Cavalier) return null;

        // Partir de la case d'arrivée pour aller jusqu'à la case de départ
        int ligneI = caseArrivee.getLigne();
        int colonneI = caseArrivee.getColonne();

        // Calcul de la direction du déplacement
        int ligneDiff = caseArrivee.getLigne() - caseDepart.getLigne();
        int colonneDiff = caseArrivee.getColonne() - caseDepart.getColonne();

        // TODO trouver une meilleure solution
        int ligneDirection ;
        if (ligneDiff > 0) {
            ligneDirection = -1;
        } else if (ligneDiff < 0) {
            ligneDirection = 1;
        } else {
            ligneDirection = 0;
        }
        int colonneDirection ;
        if (colonneDiff > 0) {
            colonneDirection = -1;
        } else if (colonneDiff < 0) {
            colonneDirection = 1;
        } else {
            colonneDirection = 0;
        }

        // Tant que la dernière case (avant case de départ) n'est pas atteinte et tant qu'il n'y a pas d'obstacle
        int ligneDerniereCase = caseDepart.getLigne() - ligneDirection;
        int colonneDerniereCase = caseDepart.getColonne() - colonneDirection;

        while (ligneI != ligneDerniereCase || colonneI != colonneDerniereCase) {

            // Deplacement vers la prochaine case à vérifier
            ligneI += ligneDirection;
            colonneI += colonneDirection;

            // Si un obstacle est trouvé, le retourner (n'importe quelle pièce sur le chemin)

            //TODO vérifier ici parfois colonneI ou ligneI = -1 (ex: echec et matt). Voir peut-être pour quelles cases

            //System.out.println("findObstacle -> CaseArrivee: " + caseArrivee.getNumero() +" CaseDepart: " + caseDepart.getNumero() + " ligneDiff: " + ligneDiff + " colonneDiff: " + colonneDiff + " ligneDirection: " + ligneDirection + " colonneDirection: " + colonneDirection + " ligneDerniereCase: " + ligneDerniereCase + " colonneDireniereCase: " + colonneDerniereCase + " ligneI: " + ligneI + " colonneI: " + colonneI);
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
        caseDepart.getPiece().deplacer(caseArrivee);
        

        boolean enEchec = estEnEchec();

        caseArrivee.getPiece().deplacer(caseDepart);
        if (pieceEcrasee != null) caseArrivee.setPiece(pieceEcrasee);
        
        return enEchec;
    }

    public boolean isFin() {

        // Recherche de coup jouable après lequel le roi n'est pas en echec
        for (Case[] ligneCases : this.echiquier.getCases()) {
            for (Case caseTestee : ligneCases) {

                // Vérifier pour toutes les pièces jouables si la case est accessible et sans obstacle
                for (Piece pieceTestee : this.joueurActuel.getPieces()) {

                    if (pieceTestee.deplacement(caseTestee) && this.findObstacle(pieceTestee.getCase(), caseTestee) == null) {
                        // Verifier si le roi est en echec si la pièce testée est déplacée à la case testée
                        if (!this.estEnEchec(pieceTestee.getCase(), caseTestee)) {
                            //TODO retirer message debug System.out.println("isFin() ? -> Un coup est possible avec " + pieceTestee + "en " + caseTestee.getNumero());
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }


    public void validerCoup(Case caseDepart, Case caseArrivee, float tempsPasse) {
        // Retirer le temps de reflexion à l'horloge du joueur
        if (this.horlogesActives)
            this.joueurActuel.retirerTemps(tempsPasse);

        // Deplacer la pièce de la case de départ à la case d'arrivée
        // TODO prendre en paramètre Piece ?
        Piece pieceDeplacee = caseDepart.getPiece();
        Piece pieceMangee = caseArrivee.getPiece();

        pieceDeplacee.deplacer(caseArrivee);

        // Retirer la piece mangée de la liste des pièces du joueur adverse
        if (pieceMangee != null) {
            if (!this.getJoueurAdverse(this.joueurActuel).getPieces().remove(pieceMangee)) System.err.println("La pièce mangée n'a pas été retirée de la liste des pièces du joueur adverse");
            // TODO remplir la liste des pieces mangées et retirer le condition
        }

        // Si un pion a été déplacé, déclarer son déplacement
        if (pieceDeplacee instanceof Pion pion) {
            if (!pion.aBouge()) pion.seDeplace();
        }
    }

    //TODO Finir
    public void fin(String raison) {

        Joueur gagnant ;
        switch (raison) {
            case "forfait" ->  {
                gagnant = this.joueurActuel;
                Affichage.info(gagnant, this.getJoueurAdverse(gagnant).getNom() + " a déclaré forfait.");
            }
            case "echec" ->  {
                gagnant = this.getJoueurAdverse(this.joueurActuel);
                Affichage.critique(this.joueurActuel, "Echec et mat ! Vous ne disposez d'aucun coup valider pour sauver votre " + this.joueurActuel.getRoi());
            }
            case "temps écoulé" ->  {
                gagnant = this.getJoueurAdverse(this.joueurActuel);
                double secondes = this.joueurActuel.getHorloge() / 1000;
                double minutes = Math.floor(secondes / 60);
                secondes = secondes - minutes * 60;
                Affichage.critique(this.joueurActuel, "Temps écoulé : "+(int)minutes+"min "+secondes+"s");
            }
            default -> {
                System.out.println(Affichage.jaune("Partie nulle ! ") + this.joueurActuel.getNom() + ", qui jouait les " + this.joueurActuel.getCouleur() + "s, n'est pas en echec mais ne dispose d'aucun coup valide.");
                return;
            }
        }

        System.out.println("La partie est remportée par " + gagnant.getNom() + " !");

    }  
}
