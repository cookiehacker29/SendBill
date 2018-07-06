package christopheinformatique.sendbill;

import android.graphics.Bitmap;

public class Client {
    private int Ident;
    private String nom, mail;
    private double soldeRestant;
    private String dateIntervention;
    private Bitmap signature;

    Client(int Ident, String nom, String mail, double soldeRestant, String date, Bitmap signature) {
        this.Ident = Ident;
        this.mail = mail;
        this.nom = nom;
        this.soldeRestant = soldeRestant;
        this.dateIntervention = date;
        this.signature = signature;
    }

    public Bitmap getSignature() {
        return this.signature;
    }

    public String getDateIntervention() {
        return dateIntervention;
    }

    public int getIdent() {
        return Ident;
    }

    public String getMail() {
        return mail;
    }

    public double getSoldeRestant() {
        return soldeRestant;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return this.getNom();
    }
}
