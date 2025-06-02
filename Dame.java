
public class Dame extends Piece {

    public Dame(Couleur couleur) {
        super(couleur, "Dame", 'D');
    }

    public boolean deplacement(Case destination) {
        int ligneDiff = destination.getLigne() - this.getCase().getLigne();
        int colonneDiff = destination.getColonne() - this.getCase().getColonne();

        return ligneDiff == 0 || colonneDiff == 0 || Math.abs(ligneDiff) == Math.abs(colonneDiff);
    }
}
