
public class Fou extends Piece {

    public Fou(Couleur couleur) {
        super(couleur, "Fou", 'F');
    }

    public boolean deplacement(Case destination) {
        int ligneDiff = destination.getLigne() - this.getCase().getLigne();
        int colonneDiff = destination.getColonne() - this.getCase().getColonne();

        return Math.abs(ligneDiff) == Math.abs(colonneDiff);
    }
}
