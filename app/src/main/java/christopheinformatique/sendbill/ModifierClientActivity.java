package christopheinformatique.sendbill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

public class ModifierClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Context c = this;

        int idClient = Integer.parseInt(getIntent().getStringExtra("IDCLIENT"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_client);

        try {
            final Client client = GestionClient.recupClientParID(this, idClient);

            final TextView nom = findViewById(R.id.modifNomClient);
            nom.setText(Objects.requireNonNull(client).getNom());

            final EditText email = findViewById(R.id.modifEmail);
            email.setText(client.getMail());

            final EditText solde = findViewById(R.id.modifSoldeClient);
            solde.setText(String.valueOf(client.getSoldeRestant()));

            final EditText date = findViewById(R.id.modifDateClient);
            date.setText(client.getDateIntervention());

            LinearLayout modifSig = findViewById(R.id.modifSignatureClient);
            final CaptureSignatureView mSig = new CaptureSignatureView(this, null, client.getSignature());
            modifSig.addView(mSig, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

            Button retour = findViewById(R.id.retoursModifierToClient);
            retour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(ModifierClientActivity.this, ClientActivity.class);
                    startActivity(in);
                }
            });

            Button clear = findViewById(R.id.nettoyerSigModifier);
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSig.ClearCanvas();
                }
            });

            Button modif = findViewById(R.id.validerModif);
            modif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        GestionClient.modifierClient(c, client.getIdent(), nom.getText().toString(),
                                email.getText().toString(), Double.parseDouble(solde.getText().toString()),
                                date.getText().toString(), mSig.getBitmap());
                        Intent in = new Intent(ModifierClientActivity.this, ClientActivity.class);
                        startActivity(in);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }
}
