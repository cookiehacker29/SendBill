package christopheinformatique.sendbill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ClientSelectionnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Context c = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_selectionner);

        int idClient = Integer.parseInt(getIntent().getStringExtra("IDCLIENT"));

        try {
            final Client s = GestionClient.recupClientParID(this, idClient);

            LinearLayout sig = findViewById(R.id.LayoutSignature);

            ImageView im = new ImageView(this);
            im.setImageBitmap(Objects.requireNonNull(s).getSignature());

            TextView n = findViewById(R.id.nomClientL);
            n.setText(s.getNom());

            TextView e = findViewById(R.id.emailClientL);
            e.setText(s.getMail());

            TextView i = findViewById(R.id.interventionClientL);
            i.setText(s.getDateIntervention());

            TextView solde = findViewById(R.id.soldeRestantL);
            solde.setText(String.valueOf(s.getSoldeRestant()));

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    WRAP_CONTENT
            );
            sig.addView(im, p);

            Button r = findViewById(R.id.retoursUserToClient);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ClientSelectionnerActivity.this, ClientActivity.class));
                }
            });

            Button supp = findViewById(R.id.supprimerClient);
            supp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GestionClient.supprimerClient(c, s.getIdent());
                    startActivity(new Intent(ClientSelectionnerActivity.this, ClientActivity.class));
                }
            });

            Button modif = findViewById(R.id.modifierClient);
            modif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(ClientSelectionnerActivity.this, ModifierClientActivity.class);
                    in.putExtra("IDCLIENT", String.valueOf(s.getIdent()));
                    startActivity(in);
                }
            });

            Button facture = findViewById(R.id.factureClientSelect);
            facture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(ClientSelectionnerActivity.this, FactureClientSelectionnerActivity.class);
                    in.putExtra("NOM", s.getNom());
                    startActivity(in);
                }
            });

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }
}
