public abstract class Piece {

    private final Couleur couleur;
    private final String nom;
    private char icone;
    private Case emplacement;

    public Piece(Couleur couleur,String nom,char icone ) { 
        this.couleur = couleur;
        this.nom = nom;
        this.icone = icone;
    }

    public Couleur getCouleur() {
        return this.couleur;
    }

    public String getNom(){
        return this.nom;
    }

    public char getIcone(){
        return this.icone;
    }

    public Case getCase(){
        return this.emplacement;
    }

    public void setCase(Case nouvelleCase){
        this.emplacement = nouvelleCase;
    }

    public void deplacer(Case nouvellCase){
        this.emplacement.setPiece(null);
        nouvellCase.setPiece(this);
        
    }

    public void setIcone(char nouvelleIcone) {
        this.icone = nouvelleIcone;
    }

    public abstract boolean deplacement(Case destination);

    @Override
    public String toString() {
        return this.getNom() + " " + this.getCouleur() + " ("+this.getCase().getNumero()+")";
    }


}



    

