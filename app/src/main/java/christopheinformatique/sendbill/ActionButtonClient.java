package christopheinformatique.sendbill;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;


public class ActionButtonClient implements View.OnClickListener {

    private ClientActivity app;

    ActionButtonClient(ClientActivity app) {
        this.app = app;
    }

    @Override
    public void onClick(View view) {
        Button b = (Button) view;
        try {
            @SuppressLint("ResourceType") Client select = GestionClient.recupClientParID(app.getContext(), b.getId());
            Intent in = new Intent(app.getContext(), ClientSelectionnerActivity.class);
            in.putExtra("IDCLIENT", String.valueOf(Objects.requireNonNull(select).getIdent()));
            app.startActivity(in);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
