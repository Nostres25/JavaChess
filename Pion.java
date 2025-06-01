public class Pion extends Piece {

        private boolean premierDeplacement;

        public Pion (Couleur couleur){
        super(couleur,"Pion",'P');
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
        int ligneDiffAbs = Math.abs(ligneDiff);                                                          
        
        return 
            (this.getCouleur() == Couleur.Blanc && ligneDiff > 0 || 
                this.getCouleur() == Couleur.Noir && ligneDiff < 0) &&

            ((!this.premierDeplacement && ligneDiffAbs == 2) || ligneDiffAbs == 1) && 

            (destination.getPiece() == null ||
                (ligneDiffAbs == Math.abs(colonneDiff) && 
                    destination.getPiece().getCouleur() == Partie.getCouleurOpposee(this.couleur) &&
                        ligneDiffAbs == 1)); 
    }
}



    

