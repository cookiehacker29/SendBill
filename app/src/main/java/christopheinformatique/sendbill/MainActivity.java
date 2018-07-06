package christopheinformatique.sendbill;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Context c = this;

        File bd = new File(this.getFilesDir() + "/bd_Client.json");

        if (!bd.exists()) {
            new AlertDialog.Builder(this)
                    .setTitle("SendBill")
                    .setMessage("Bonjour nouveau utilisateur !\nVous venez de lancé votre application" +
                            " pour la premiere fois.\n" +
                            "Votre base de donnée est en cours de création.")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File filebd = new File(c.getFilesDir(), "bd_Client.json");
                            try {
                                filebd.createNewFile();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }


                        }
                    }).show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.client);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClientActivity.class));
            }
        });

        Button f = findViewById(R.id.facture);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FactureActivity.class));
            }
        });


    }

}
