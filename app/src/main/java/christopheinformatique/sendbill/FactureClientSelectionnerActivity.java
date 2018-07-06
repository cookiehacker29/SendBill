package christopheinformatique.sendbill;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FactureClientSelectionnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context c = this;

        final String nom = getIntent().getStringExtra("NOM");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facture_client_selectionner);

        TextView nomL = findViewById(R.id.nomCS);
        nomL.setText(nom);

        Button retour = findViewById(R.id.retourFactureCSToMain);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(FactureClientSelectionnerActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

        final EditText real = findViewById(R.id.dateRealETCS);
        final EditText ref = findViewById(R.id.referenceETCS);
        final EditText desc = findViewById(R.id.descTacheETCS);
        final EditText duree = findViewById(R.id.dureeIntETCS);
        final EditText remarque = findViewById(R.id.remarqueETCS);
        final Switch autSig = findViewById(R.id.autoriserSigCS);

        Button facture = findViewById(R.id.validerFactureCS);
        facture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Client v = GestionClient.recupClientParNom(c, nom);
                    String sig = null;
                    Intent in = null;
                    String mail = null;
                    if (autSig.isChecked()) {
                        File file = new File(c.getFilesDir() + "/lastSig.png");
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        Bitmap bmp = Objects.requireNonNull(v).getSignature();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        sig = file.getAbsolutePath();
                        mail = RemplissageMail.avec("email.txt", c, v.getNom(), real.getText().toString(),
                                ref.getText().toString(), v.getDateIntervention(), String.valueOf(v.getSoldeRestant()),
                                desc.getText().toString(), duree.getText().toString(),
                                String.valueOf(v.getSoldeRestant() - Double.parseDouble(duree.getText().toString())),
                                remarque.getText().toString(), sig);
                        in = new Intent(FactureClientSelectionnerActivity.this, ConfirmationFactureActivity.class);

                    } else {
                        mail = RemplissageMail.avec("emailSansSignature.txt", c, v.getNom(), real.getText().toString(),
                                ref.getText().toString(), v.getDateIntervention(), String.valueOf(v.getSoldeRestant()),
                                desc.getText().toString(), duree.getText().toString(),
                                String.valueOf(v.getSoldeRestant() - Double.parseDouble(duree.getText().toString())),
                                remarque.getText().toString(), null);
                        in = new Intent(FactureClientSelectionnerActivity.this, ConfirmationFactureSansSignatureActivity.class);
                    }


                    in.putExtra("MESSAGE", mail);
                    in.putExtra("EMAIL", v.getMail());
                    in.putExtra("ID", String.valueOf(v.getIdent()));
                    in.putExtra("NOM", v.getNom());
                    in.putExtra("SOLDERESTANT", String.valueOf(v.getSoldeRestant() - Double.parseDouble(duree.getText().toString())));
                    in.putExtra("DATE", real.getText().toString());
                    in.putExtra("SIGNATURE", GestionClient.BitMapToString(v.getSignature()));
                    in.putExtra("PATH", sig);
                    in.putExtra("TYPE", "2");

                    c.startActivity(in);
                } catch (IOException | JSONException | NumberFormatException e) {
                    new AlertDialog.Builder(c)
                            .setTitle("SendBill")
                            .setMessage("Erreur ! Saisie invalide !")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).show();
                }
            }
        });
    }
}
