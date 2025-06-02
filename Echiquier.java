
public class Echiquier {

    private Case[][] cases; 

    public Echiquier() {
        this.cases = new Case[8][8];

        for ( int ligneI = 0; ligneI < this.cases.length; ligneI++){

            for ( int caseI = 0; caseI < this.cases[ligneI].length; caseI++){

                this.cases[ligneI][caseI] = new Case(caseI, ligneI); 


           }

             
        }

        // Pièces de couleur blanche 

        this.cases[0][4].setPiece(new Roi(Couleur.Blanc));
        this.cases[0][3].setPiece(new Dame(Couleur.Blanc));
        this.cases[0][2].setPiece(new Fou(Couleur.Blanc));
        this.cases[0][5].setPiece(new Fou(Couleur.Blanc));
        this.cases[0][1].setPiece(new Cavalier(Couleur.Blanc));
        this.cases[0][6].setPiece(new Cavalier(Couleur.Blanc));
        this.cases[0][0].setPiece(new Tour(Couleur.Blanc)); 
        this.cases[0][7].setPiece(new Tour(Couleur.Blanc));

        for (Case aCase : this.cases[1]) {
            aCase.setPiece(new Pion(Couleur.Blanc));
        }
        

        // Pièces de couleur noire 

        this.cases[7][4].setPiece(new Roi(Couleur.Noir));
        this.cases[7][3].setPiece(new Dame(Couleur.Noir));
        this.cases[7][2].setPiece(new Fou(Couleur.Noir));
        this.cases[7][5].setPiece(new Fou(Couleur.Noir));
        this.cases[7][1].setPiece(new Cavalier(Couleur.Noir));
        this.cases[7][6].setPiece(new Cavalier(Couleur.Noir));
        this.cases[7][0].setPiece(new Tour(Couleur.Noir));
        this.cases[7][7].setPiece(new Tour(Couleur.Noir));


        for (Case aCase : this.cases[6]) {
            aCase.setPiece(new Pion(Couleur.Noir));
        }
    }


    public Echiquier(Case[][] cases){
        this.cases = cases; 

    }


    public Case[][] getCases(){
        return this.cases;
    }

    public Case getCase(int colonne, int ligne){
        return this.cases[ligne][colonne];
    }

    public static int getNombreColonne(char lettre) {

        return switch (lettre) {
            case 'A' -> 1;
            case 'B' -> 2;
            case 'C' -> 3;
            case 'D' -> 4;
            case 'E' -> 5;
            case 'F' -> 6;
            case 'G' -> 7;
            case 'H' -> 8;
            default -> -1;
        };
    }

    public static char getLettreColonne(int index) {
        return switch (index) {
            case 1 -> 'A';
            case 2 -> 'B';
            case 3 -> 'C';
            case 4 -> 'D';
            case 5 -> 'E';
            case 6 -> 'F';
            case 7 -> 'G';
            case 8 -> 'H';
            default -> '?';
        };
    }
    
}
