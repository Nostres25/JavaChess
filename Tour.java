public class Tour extends Piece {

    public Tour (Couleur couleur){
        super(couleur,"Tour",'T');
    }

    @Override
    
    public boolean deplacement ( Case destination){

        return this.getCase().getColonne() == destination.getColonne() || this.getCase().getLigne() == destination.getLigne() ;
    
       
    }



    
}
