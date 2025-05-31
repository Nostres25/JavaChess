public abstract class Piece {
    public Couleur couleur;
    public String nom;
    public char icone;
    public Case emplacement;

    public Piece(Case emplacement,Couleur couleur,String nom,char icone ) { 
        this.emplacement = emplacement;
        this.couleur = couleur;
        this.nom = nom;
        this.icone = icone;
    }

    public Couleur getcouleur() {
        return this.couleur;
    }

    public String getnom(){
        return this.nom;
    }

    public char geticone(){
        return this.icone;
    }

    public Case getemplacement(){
        return this.emplacement;
    }

    public abstract boolean deplacement ( Case destination);

    @Override
    public abstract String toString();



    }



    

