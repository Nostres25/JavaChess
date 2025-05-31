public class Tour extends Piece {

    public Tour (Couleur couleur, Case emplacement){
        super(emplacement,couleur,"Tour",'T');
    }

     public boolean deplacement ( Case destination){

         return this.getCase().getColonne() == destination.getColonne() || this.getCase().getLigne() == destination.getLigne() ;
    
       
     }



    
}
