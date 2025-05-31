public class Pion extends Piece {

        private boolean premierDeplacement;

        public Pion (Couleur couleur, Case emplacement){
        super(emplacement,couleur,"Pion",'P');
        this.premierDeplacement = false; 
    }


        public boolean aBouge(){
            return this.premierDeplacement; 


        }

        public void seDeplace(){
            this.premierDeplacement = true; 
        }


    @Override

    public boolean deplacement (Case destination){

        int colonne = this.getCase().getColonne();                                                                                                  
        int ligne = this.getCase().getLigne();
        int colonneDiff = destination.getColonne() - colonne;      
        int ligneDiff = destination.getLigne() - ligne;                                                           
        
        return (this.getCouleur() == Couleur.Blanc && ligneDiff < 0 || this.getCouleur() == Couleur.Noir && ligneDiff > 0) &&

        Math.abs(ligneDiff) == Math.abs(colonneDiff)

        return destination.getLigne() - ligne  == -1 && this.getCouleur() == Couleur.Blanc || destination.getLigne() - ligne  == 1 && this.getCouleur() == Couleur.Noir ||


        (destination.getLigne() - ligne  == 2 && this.getCouleur() == Couleur.Blanc || destination.getLigne() - ligne  == 2 && this.getCouleur() == Couleur.Noir) && !premierDeplacement || 





        



    }



    
}
