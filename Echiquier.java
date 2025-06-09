
public class Echiquier {

    private final Case[][] cases; 

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

    public static int getNombreColonne(char lettre) {

        switch (lettre) {
            case 'A': return 1;
            case 'B': return 2;
            case 'C': return 3;
            case 'D': return 4;
            case 'E': return 5;
            case 'F': return 6;
            case 'G': return 7;
            case 'H': return 8;
            default: return -1;
        }
    }

    public static char getLettreColonne(int index) {
        switch (index) {
            case 1: return 'A';
            case 2: return 'B';
            case 3: return 'C';
            case 4: return 'D';
            case 5: return 'E';
            case 6: return 'F';
            case 7: return 'G';
            case 8: return 'H';
            default: return '?';
        }
    }

    public Case[][] getCases(){
        return this.cases;
    }

    public Case getCase(int colonne, int ligne){
        return this.cases[ligne][colonne];
    }   
}
