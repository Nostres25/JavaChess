
public class Reine extends Piece {

    public Reine(Couleur couleur, Case emplacement) {
        super(emplacement, couleur, "Reine", 'E');
    }

    public boolean deplacement(Case destination) {
        int ligneDiff = destination.getLigne() - this.getCase().getLigne();
        int colonneDiff = destination.getColonne() - this.getCase().Colonne();

        return ligneDiff == 0 || colonneDiff == 0 || Math.abs(ligneDiff) == Math.abs(colonneDiff);
    }
}
