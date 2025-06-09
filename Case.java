
public class Case {
    private final int colonne;
    private final int ligne;
    private Piece contenu;


    public Case(int colonne, int ligne){
        this.colonne = colonne;
        this.ligne = ligne; 

    }
      public Case(int colonne, int ligne, Piece contenu){
        this.colonne = colonne;
        this.ligne = ligne;
        this.contenu = contenu; 

}
    public int getColonne(){
        return this.colonne;
    }

    public int getLigne(){
        return this.ligne;
    }

    public Piece getPiece(){
        return this.contenu;
    }

    public String getNumero() {
        char lettreColonne = Echiquier.getLettreColonne(this.colonne + 1); 
        int numeroLigne = this.ligne + 1; 

        return "" + lettreColonne + numeroLigne;
    }

    public void setPiece(Piece piece){
        this.contenu = piece; 
        if (piece != null) piece.setCase(this);
    }



}