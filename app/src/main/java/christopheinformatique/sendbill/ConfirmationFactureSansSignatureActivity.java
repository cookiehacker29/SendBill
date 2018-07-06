package christopheinformatique.sendbill;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.io.IOException;

public class ConfirmationFactureSansSignatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context c = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_facture_sans_signature);

        final String message = getIntent().getStringExtra("MESSAGE");
        final String email = getIntent().getStringExtra("EMAIL");
        final String nom = getIntent().getStringExtra("NOM");
        final int id = Integer.parseInt(getIntent().getStringExtra("ID"));
        final double solde = Double.parseDouble(getIntent().getStringExtra("SOLDERESTANT"));
        final String date = getIntent().getStringExtra("DATE");
        final Bitmap sig = GestionClient.StringToBitMap(getIntent().getStringExtra("SIGNATURE"));
        final int type = Integer.parseInt(getIntent().getStringExtra("TYPE"));

        Spanned messageFormater = Html.fromHtml(message);

        EditText aff = findViewById(R.id.confirmMessageSansSigET);
        aff.setKeyListener(null);
        aff.setText(messageFormater);


        Button retour = findViewById(R.id.retourConfirmationSansSigToFacture);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    startActivity(new Intent(ConfirmationFactureSansSignatureActivity.this, FactureActivity.class));
                } else {
                    Intent in = new Intent(ConfirmationFactureSansSignatureActivity.this, FactureClientSelectionnerActivity.class);
                    in.putExtra("NOM", nom);
                    startActivity(in);
                }
            }
        });

        Button valider = findViewById(R.id.validerMailSansSig);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GestionClient.modifierClient(c, id, nom, email, solde, date, sig);
                    new MailAPI(c, email, "Votre facture", message).execute();
                    if (type == 1) {
                        startActivity(new Intent(ConfirmationFactureSansSignatureActivity.this, FactureActivity.class));
                    } else {
                        Intent in = new Intent(ConfirmationFactureSansSignatureActivity.this, FactureClientSelectionnerActivity.class);
                        in.putExtra("NOM", nom);
                        startActivity(in);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
