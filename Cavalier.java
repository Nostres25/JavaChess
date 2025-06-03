public class Cavalier extends Piece {

    public Cavalier (Couleur couleur){
         super(couleur, "Cavalier", 'C');
 
    }

    @Override
    public boolean deplacement ( Case destination){

        int colonne = this.getCase().getColonne();
        int ligne = this.getCase().getLigne();


        return
            Math.abs(colonne - destination.getColonne()) == 2 && Math.abs(ligne - destination.getLigne()) == 1 ||
            Math.abs(colonne - destination.getColonne()) == 1 && Math.abs(ligne - destination.getLigne()) == 2;
    

    }

}

    
