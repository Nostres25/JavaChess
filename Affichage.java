public abstract class Affichage {
    public final static String RESET = "\033[0m";
        
    
    
    public static void actualiserAffichage(Partie partie, boolean enEchec) { 

        String affichage = "    A  B  C  D  E  F  G  H";
        Case[][] cases = partie.getEchiquier().getCases();
        for (int ligneI = cases.length - 1; ligneI >= 0; ligneI--) {

            // Écriture d'une nouvelle ligne
            int ligneAffichee = ligneI+1;
            affichage += "\n" + (ligneAffichee) + " |";

            for (int caseI = 0; caseI < cases[ligneI].length; caseI++) {

                //Écriture d'une nouvelle case
                Piece piece = cases[ligneI][caseI].getPiece();
                String contenu ;
                if (piece == null) contenu = " ";
                else if (piece.getCouleur() == Couleur.Noir) {
                    contenu = "\033[1;30m"+piece.getIcone();
                } else {
                    contenu = "\033[1;37m"+piece.getIcone()+"";
                }
                

                if ((ligneI%2 == 0 && caseI%2 == 0) || (ligneI%2 != 0 && caseI%2 != 0)) {
                    affichage += "\033[45m "+contenu+" "+RESET;
                } else {
                    affichage += " " + contenu + " "+RESET;
                }
            }

            affichage += "| "+ligneAffichee;
        }

        affichage += "\n    A  B  C  D  E  F  G  H";
        System.out.println(affichage);
        if (enEchec) critique(partie.getJoueurActuel(), "Echec !!");

    }

    public static void repondre(Joueur joueur, String message) {
        System.out.println("-> " + joueur.getNom() + " (" + joueur.getCouleur() + ") : " + message);
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
