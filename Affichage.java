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

                if (pieceDeplacee != null && pieceDeplacee.equals(piece)) {
                    contenu = "\033[4;34m" + contenu;
                }

                if (ancienneCase != null && ligneI == ancienneCase.getLigne() && colI == ancienneCase.getColonne()) {
                    affichage += "\033[44m " + contenu + " " + RESET;
                } else if ((ligneI%2 == 0 && colI%2 == 0) || (ligneI%2 != 0 && colI%2 != 0)) {
                    affichage += "\033[45m "+contenu+" "+RESET;
                } else {
                    affichage += " " + contenu + " "+RESET;
                }
            }

            affichage += "| "+ligneAffichee;
            switch (ligneI) {
                case 6 -> {
                    if (pieceDeplacee != null && ancienneCase != null)
                        affichage += "   " + bleu("Coup joué par "+partie.getJoueur(pieceDeplacee.getCouleur()).getNom()+" : " + ancienneCase.getNumero() + " --> " + pieceDeplacee);
                }
                case 4 -> {
                    if (!partie.getHorlogeActivee()) break;
                    Joueur joueurNoir = partie.getJoueur(Couleur.Noir);
                    double secondes = joueurNoir.getHorloge() / 1000;
                    double minutes = Math.floor(secondes / 60);
                    secondes = secondes - minutes * 60;
                    affichage += "   - " + joueurNoir.getNom() + " (" + joueurNoir.getCouleur() + ") --> " + bleu("Temps restant: "+(int)minutes+"min "+secondes+"s");
                }
                case 3 -> {
                    if (!partie.getHorlogeActivee()) break;
                    Joueur joueurBlanc = partie.getJoueur(Couleur.Blanc);
                    double secondes = joueurBlanc.getHorloge() / 1000;
                    double minutes = Math.floor(secondes / 60);
                    secondes = secondes - minutes * 60;
                    affichage += "   - " + joueurBlanc.getNom() + " (" + joueurBlanc.getCouleur() + ") --> " + bleu("Temps restant: "+(int)minutes+"min "+secondes+"s");
                }
                default -> {}
            }

            affichage += "\n";
        }

        affichage += "    A  B  C  D  E  F  G  H";
        System.out.println(affichage);
        if (enEchec) critique(partie.getJoueurActuel(), "Echec !!");

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

    public static void succes(Joueur joueur, String message) {
        repondre(joueur, vert(message));
    }

    public static void erreur(Joueur joueur, String message) {
        repondre(joueur, rouge(message));
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

    public static String rouge(String message) {
        return "\033[0;31m" + message + RESET;
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

    public static String font_blanc(String message) {
        return "\033[47m" + message + RESET;
    }

    public static String font_bleu(String message) {
        return "\033[45m" + message + RESET;
    }

    public static char getIcone(Piece piece) {
        if (piece.getCouleur() == Couleur.Blanc) {
            return switch (piece.getNom()) {
                case "Pion" -> '♙';
                case "Cavalier" -> '♘';
                case "Roi" -> '♔';
                case "Dame" -> '♕';
                case "Fou" -> '♗';
                case "Tour" -> '♖';
                default -> '?';
            };
        } else {
            return switch (piece.getNom()) {
                case "Pion" -> '♟';
                case "Cavalier" -> '♞';
                case "Roi" -> '♚';
                case "Dame" -> '♛';
                case "Fou" -> '♝';
                case "Tour" -> '♜';
                default -> '?';
            };
        }
    }
}
