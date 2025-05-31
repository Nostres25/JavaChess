public abstract class Piece {

    public Couleur couleur;
    public String nom;
    public char icone;
    public Case emplacement;

    public Piece(Case emplacement,Couleur couleur,String nom,char icone ) { 
        this.setCase(emplacement);
        this.couleur = couleur;
        this.nom = nom;
        this.icone = icone;
    }

    public Couleur getCouleur() {
        return this.couleur;
    }

    public String getNom(){
        return this.nom;
    }

    public char getIcone(){
        return this.icone;
    }

    public Case getCase(){
        return this.emplacement;
    }

    public void setCase(Case nouvelleCase){
        this.emplacement = nouvelleCase;
        nouvelleCase.setPiece(this);
    }

    public abstract boolean deplacement ( Case destination);

     @Override
    public String toString() {
        return this.getNom() + " " + this.getCouleur() + " ("+this.getCase().getNumero()+")";
    }


    }



    

