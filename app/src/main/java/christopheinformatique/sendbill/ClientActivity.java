package christopheinformatique.sendbill;

import android.annotation.SuppressLint;
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

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ClientActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Button b = findViewById(R.id.retour);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientActivity.this, MainActivity.class));
            }
        });

        Button plus = findViewById(R.id.p);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientActivity.this, AjoutClientActivity.class));
            }
        });

        try {
            List<Client> vals = GestionClient.recupClients(this);
            for (int i = 0; i < vals.size(); i++) {
                LinearLayout layout = findViewById(R.id.layout);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        WRAP_CONTENT
                );
                Button buttonView = new Button(this);
                buttonView.setText(vals.get(i).getNom());
                buttonView.setId(vals.get(i).getIdent());
                buttonView.setOnClickListener(new ActionButtonClient(this));
                layout.addView(buttonView, p);
            }
        } catch (IOException ignored) {

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final EditText nomR = findViewById(R.id.nomR);
        Button recherche = findViewById(R.id.rechercheClient);
        recherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("ResourceType") Client select = null;
                try {
                    select = GestionClient.recupClientParNom(context, nomR.getText().toString());
                    if (select != null) {
                        Intent in = new Intent(context, ClientSelectionnerActivity.class);
                        in.putExtra("IDCLIENT", String.valueOf(select.getIdent()));
                        context.startActivity(in);
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("SendBill")
                                .setMessage("Client introuvable !")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public Context getContext() {
        return context;
    }
}
