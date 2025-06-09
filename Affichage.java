public abstract class Affichage {
    public final static String RESET = "\033[0m";
        
    
    
    public static void actualiserAffichage(Partie partie, boolean enEchec, Piece pieceDeplacee, Case ancienneCase) { 

        Case[][] cases = partie.getEchiquier().getCases();

        String affichage = "    A  B  C  D  E  F  G  H\n";
        for (int ligneI = cases.length - 1; ligneI >= 0; ligneI--) {

            // Écriture d'une nouvelle ligne
            int ligneAffichee = ligneI+1;
            affichage += (ligneAffichee) + " |";

            for (int colI = 0; colI < cases[ligneI].length; colI++) {

                //Écriture d'une nouvelle case
                Piece piece = cases[ligneI][colI].getPiece();
                String contenu ;

                if (piece == null) contenu = " ";
                else if (piece.getCouleur() == Couleur.Noir) {
                    contenu = "\033[1;30m"+piece.getIcone();
                } else {
                    contenu = "\033[1;37m"+piece.getIcone()+"";
                }

                // Souligner la pièce précédemment déplacée
                if (pieceDeplacee != null && pieceDeplacee.equals(piece)) {
                    contenu = "\033[4;34m" + contenu;
                }

                // Couleurs de fond : fond bleu pour la position de la pièce précédemment déplacée ou damier
                if (ancienneCase != null && ligneI == ancienneCase.getLigne() && colI == ancienneCase.getColonne()) {
                    affichage += "\033[44m " + contenu + " " + RESET;
                } else if ((ligneI%2 == 0 && colI%2 == 0) || (ligneI%2 != 0 && colI%2 != 0)) {
                    affichage += "\033[45m "+contenu+" "+RESET;
                } else {
                    affichage += " " + contenu + " "+RESET;
                }
            }

            affichage += "| "+ligneAffichee;

            // Affichage d'informations supplémentaires
            switch (ligneI) {
                case 6: {
                    // Coup joué précédemment
                    if (pieceDeplacee != null && ancienneCase != null)
                        affichage += "   " + bleu("Coup joué par "+partie.getJoueur(pieceDeplacee.getCouleur()).getNom()+" : " + ancienneCase.getNumero() + " --> " + pieceDeplacee);
                    break;
                }
                case 4: {
                    // Horloge du joueur noir
                    if (!partie.getHorlogesActivees()) break;
                    Joueur joueurNoir = partie.getJoueur(Couleur.Noir);
                    double secondes = joueurNoir.getHorloge() / 1000;
                    double minutes = Math.floor(secondes / 60);
                    secondes = secondes - minutes * 60;
                    affichage += "   - " + joueurNoir.getNom() + " (" + joueurNoir.getCouleur() + ") --> " + bleu("Temps restant: "+(int)minutes+"min "+secondes+"s");
                    break;
                }
                case 3: {
                    // Horloge du joueur blanc
                    if (!partie.getHorlogesActivees()) break;
                    Joueur joueurBlanc = partie.getJoueur(Couleur.Blanc);
                    double secondes = joueurBlanc.getHorloge() / 1000;
                    double minutes = Math.floor(secondes / 60);
                    secondes = secondes - minutes * 60;
                    affichage += "   - " + joueurBlanc.getNom() + " (" + joueurBlanc.getCouleur() + ") --> " + bleu("Temps restant: "+(int)minutes+"min "+secondes+"s");
                    break;
                }
                default: break;
            }

            affichage += "\n";
        }

        affichage += "    A  B  C  D  E  F  G  H";
        System.out.println(affichage);
        if (enEchec) critique(partie.getJoueurActuel(), "Echec !!");

    }

    public static char getIcone(Piece piece) {
        if (piece.getCouleur() == Couleur.Blanc) {
            switch (piece.getNom()) {
                case "Pion": return '♙';
                case "Cavalier": return '♘';
                case "Roi": return '♔';
                case "Dame": return '♕';
                case "Fou": return  '♗';
                case "Tour": return '♖';
                default: return '?';
            }
        } else {
            switch (piece.getNom()) {
                case "Pion": return '♟';
                case "Cavalier": return '♞';
                case "Roi": return '♚';
                case "Dame": return '♛';
                case "Fou": return '♝';
                case "Tour": return '♜';
                default: return '?';
            }
        }
    }

    public static void repondre(Joueur joueur, String message) {
        System.out.println("-> " + bleu(joueur.getNom()) + " (" + joueur.getCouleur() + ") : " + message);
    }

    public static void info(Joueur joueur, String message) {
        repondre(joueur, bleu(message));
    }

    public static void question(Joueur joueur, String question, String details) {
        repondre(joueur, jaune(question) + "\n" + details);
    }

    public static void erreur(Joueur joueur, String message) {
        repondre(joueur, rouge(message, false));
    }

    public static void critique(Joueur joueur, String message) {
        repondre(joueur, rouge(message, true));
    }

    public static String bleu(String message) {
        return "\033[0;34m" + message + RESET;
    }

    public static String jaune(String message) {
        return "\033[0;33m" + message + RESET;
    }

    public static String vert(String message) {
        return "\033[0;32m" + message + RESET;
    }

    public static String rouge(String message, boolean gras) {
        int style = 0;
        if (gras) style = 1;
        return "\033["+ style +";31m" + message + RESET;
    }

    public static String noirf(String message) {
        return "\033[1;90m" + message + RESET;
    }
    

    public static String font_noir(String message) {
        return "\033[40m" + message + RESET;
    }

    public static String font_rouge(String message) {
        return "\033[41m" + message + RESET;
    }
}
