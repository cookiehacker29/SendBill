package christopheinformatique.sendbill;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FactureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Context c = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facture);

        final Spinner spin = findViewById(R.id.spinner);

        List<Client> list = new ArrayList<>();

        try {
            list = GestionClient.recupClients(this);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<Client> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);

        Button retour = findViewById(R.id.retourFactureToMain);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(FactureActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

        final EditText real = findViewById(R.id.dateRealET);
        final EditText ref = findViewById(R.id.referenceET);
        final EditText desc = findViewById(R.id.descTacheET);
        final EditText duree = findViewById(R.id.dureeIntET);
        final EditText remarque = findViewById(R.id.remarqueET);
        final Switch autSig = findViewById(R.id.autoriserSig);

        Button facture = findViewById(R.id.validerFacture);
        facture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client v = (Client) spin.getSelectedItem();
                try {
                    String sig = null;
                    Intent in = null;
                    String mail = null;
                    if (autSig.isChecked()) {
                        File file = new File(c.getFilesDir() + "/lastSig.png");
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        Bitmap bmp = v.getSignature();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        sig = file.getAbsolutePath();
                        mail = RemplissageMail.avec("email.txt", c, v.getNom(), real.getText().toString(),
                                ref.getText().toString(), v.getDateIntervention(), String.valueOf(v.getSoldeRestant()),
                                desc.getText().toString(), duree.getText().toString(),
                                String.valueOf(v.getSoldeRestant() - Double.parseDouble(duree.getText().toString())),
                                remarque.getText().toString(), sig);
                        in = new Intent(FactureActivity.this, ConfirmationFactureActivity.class);

                    } else {
                        mail = RemplissageMail.avec("emailSansSignature.txt", c, v.getNom(), real.getText().toString(),
                                ref.getText().toString(), v.getDateIntervention(), String.valueOf(v.getSoldeRestant()),
                                desc.getText().toString(), duree.getText().toString(),
                                String.valueOf(v.getSoldeRestant() - Double.parseDouble(duree.getText().toString())),
                                remarque.getText().toString(), null);
                        in = new Intent(FactureActivity.this, ConfirmationFactureSansSignatureActivity.class);
                    }


                    in.putExtra("MESSAGE", mail);
                    in.putExtra("EMAIL", v.getMail());
                    in.putExtra("ID", String.valueOf(v.getIdent()));
                    in.putExtra("NOM", v.getNom());
                    in.putExtra("SOLDERESTANT", String.valueOf(v.getSoldeRestant() - Double.parseDouble(duree.getText().toString())));
                    in.putExtra("DATE", real.getText().toString());
                    in.putExtra("SIGNATURE", GestionClient.BitMapToString(v.getSignature()));
                    in.putExtra("PATH", sig);
                    in.putExtra("TYPE", "1");

                    c.startActivity(in);
                } catch (IOException | NumberFormatException | NullPointerException e) {
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
