package christopheinformatique.sendbill;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class AjoutClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Context c = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_client);
        LinearLayout mContent = findViewById(R.id.content);
        final CaptureSignatureView mSig = new CaptureSignatureView(this, null);
        mContent.addView(mSig, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        Button b = findViewById(R.id.retoursversclient);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjoutClientActivity.this, ClientActivity.class));
            }
        });

        Button valider = findViewById(R.id.validerClient);

        final EditText nom = findViewById(R.id.ptNom);
        final EditText email = findViewById(R.id.ptEmail);
        final EditText solde = findViewById(R.id.ptSolde);
        final Context context = this;

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Client> ls = GestionClient.recupClients(context);
                    int id = 0;
                    if (!ls.isEmpty()) {
                        id = ls.get(ls.size() - 1).getIdent() + 1;
                    }

                    if (GestionClient.recupClientParNom(context, nom.getText().toString()) == null) {
                        GestionClient.creerClient(context, id, nom.getText().toString(),
                                email.getText().toString(), Double.parseDouble(solde.getText().toString()),
                                "Aucune intervention", mSig.getBitmap());

                        new AlertDialog.Builder(c)
                                .setTitle("SendBill")
                                .setMessage("Client ajouté !")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).show();

                        nom.setText("");
                        email.setText("");
                        solde.setText("");
                        mSig.ClearCanvas();
                    } else {
                        new AlertDialog.Builder(c)
                                .setTitle("SendBill")
                                .setMessage("Client déjà présent !")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                }).show();
                    }

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
