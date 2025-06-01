import java.util.ArrayList;

public class Joueur {
    private final Partie partie;
    private final Couleur couleur;
    private final String nom;
    private final ArrayList<Piece> pieces;
    private Roi roi;
    private float horloge;

    public Joueur(Partie partie, Couleur couleur, String nom) {
        this.partie = partie;
        this.couleur = couleur;
        this.nom = nom;

        // Pour des raisons d'optimisation du nombre d'opérations:
        // Récupération des références des pièces du joueur dans la liste "piece"
        // pour ne pas avoir à vérifier toutes les cases de l'échiquier pour récupérer les pièces du joueur. 
        // Ainsi que récupération du roi dans un attribut "roi" pour le récupérer rapidement également.
        this.pieces = new ArrayList<>();

        // Récupération des pièces de toutes les cases des deux dernières lignes de l'échiquier si le joueur joue les noirs
        // ou des deux premières lignes de l'échiquier si le joueur joue les blancs
        int ligneDebut = 0;
        if (couleur == Couleur.Noir) ligneDebut = 6;
        
        Echiquier echiquier = this.partie.getEchiquier(); 
        for (int ligneI = ligneDebut; ligneI <= ligneDebut + 1; ligneI++) {
            for (int caseI = 0; caseI < 8; caseI++) {
                Piece piece = echiquier.getCase(caseI, ligneI).getPiece();
                pieces.add(piece);
                if (piece instanceof Roi) this.roi = (Roi) piece;
            }
        }
    }

    public Partie getPartie() {
        return this.partie;
    }

    public Couleur getCouleur() {
        return this.couleur;
    }

    public String getNom() {
        return this.nom;
    }

    public float getHorloge() {
        return this.horloge;
    }

    public ArrayList<Piece> getPieces() {
        return this.pieces;
    }

    public Roi getRoi() {
        return this.roi;
    }

    public void setHorloge(float nouveauTemps) {
        this.horloge = nouveauTemps;
    }
    
}
