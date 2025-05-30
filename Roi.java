
public class Roi extends Piece {

    public Roi(Couleur couleur, Case emplacement) {
        super(emplacement, couleur, this.getClass().getSimpleName(), 'R');
    }

    public boolean deplacement(Case destination) {
        return 
            Math.abs(destination.getLigne() - this.getCase().getLigne()) <= 1 ||
            Math.abs(destination.getColonne() - this.getCase().getColonne()) <= 1;
    }

    @Override
    public String toString() {
        return this.getNom() + " " + this.getCouleur() + " ("+this.getCase().getNumero()+")";
    }
}
