
public class Roi extends Piece {

    public Roi(Couleur couleur) {
        super(couleur, "Roi", 'R');
    }

    @Override
    public boolean deplacement(Case destination) {
        return 
            Math.abs(destination.getLigne() - this.getCase().getLigne()) <= 1 &&
            Math.abs(destination.getColonne() - this.getCase().getColonne()) <= 1;
    }
}
