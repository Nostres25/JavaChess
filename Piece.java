public abstract class Piece {
    public Couleur couleur;
    public String nom;
    public char icone;
    public Case emplacement;

    public Piece(Couleur couleur, Case emplacement) { 
        this.couleur = couleur;
        this.emplacement = emplacement;

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

    public String getNomComplet(){
        return this.icone + " " + this.nom; 
    }

    public abstract boolean deplacement ( Case destination);



    }



    

