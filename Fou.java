
public class Fou extends Piece {

    public Fou(Couleur couleur, Case emplacement) {
        super(emplacement, couleur, this.getClass().getSimpleName(), 'F');
    }

    public boolean deplacement(Case destination) {
        int ligneDiff = destination.getLigne() - this.getCase().getLigne();
        int colonneDiff = destination.getColonne() - this.getCase().Colonne();

        return Math.abs(ligneDiff) == Math.abs(colonneDiff);
    }

    @Override
    public String toString() {
        return this.getNom() + " " + this.getCouleur() + " ("+this.getCase().getNumero()+")";
    }
}
