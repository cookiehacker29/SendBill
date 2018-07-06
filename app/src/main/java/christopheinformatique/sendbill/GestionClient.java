package christopheinformatique.sendbill;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class GestionClient {

    public static void creerClient(Context context, int ident, String nom, String mail, double soldeRestant, String date, Bitmap sig) throws JSONException, IOException {

        String s = BitMapToString(sig);

        JSONObject obj = new JSONObject();
        obj.put("id", String.valueOf(ident));
        obj.put("nom", nom);
        obj.put("mail", mail);
        obj.put("soldeRestant", soldeRestant);
        obj.put("dateIntervention", date);
        obj.put("signature", s);
        try {
            Writer output = null;
            File file = new File(context.getFilesDir(), "bd_Client.json");
            output = new BufferedWriter(new FileWriter(file, true));
            output.write(obj.toString() + "\n");
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Client> recupClients(Context context) throws IOException, JSONException {
        List<Client> ls = new ArrayList<>();

        FileReader read = new FileReader(context.getFilesDir() + "/bd_Client.json");
        BufferedReader br = new BufferedReader(read);
        String s;
        int x = 0;
        while ((s = br.readLine()) != null) {

            JSONObject res = new JSONObject(s);
            x += 1;
            ls.add(new Client(Integer.parseInt(res.getString("id")), res.getString("nom"),
                    res.getString("mail"), Double.parseDouble(res.getString("soldeRestant")),
                    res.getString("dateIntervention"), StringToBitMap(res.getString("signature"))));
        }
        return ls;
    }

    public static void supprimerClient(Context c, int idClient) {
        try {
            List<Client> lsClient = recupClients(c);

            int indice = 0;

            for (Client x : lsClient) {
                if (x.getIdent() == idClient) {
                    lsClient.remove(indice);
                    break;
                }
                indice += 1;
            }

            File file = new File(c.getFilesDir(), "bd_Client.json");
            file.delete();
            file.createNewFile();

            for (Client x : lsClient) {
                creerClient(c, x.getIdent(), x.getNom(), x.getMail(), x.getSoldeRestant(), x.getDateIntervention(), x.getSignature());
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static Client recupClientParID(Context c, int idClient) throws IOException, JSONException {
        List<Client> ls = recupClients(c);

        for (Client x : ls) {
            if (x.getIdent() == idClient) {
                return x;
            }
        }
        return null;
    }

    public static Client recupClientParNom(Context c, String nom) throws IOException, JSONException {
        List<Client> ls = recupClients(c);

        for (Client x : ls) {
            if (x.getNom().equals(nom)) {
                return x;
            }
        }
        return null;
    }

    public static void modifierClient(Context c, int ident, String nom, String mail, double soldeRestant, String date, Bitmap sig) throws IOException, JSONException {
        List<Client> lsClient = recupClients(c);

        int indice = 0;

        for (Client x : lsClient) {
            if (x.getIdent() == ident) {
                lsClient.set(indice, new Client(ident, nom, mail, soldeRestant, date, sig));
                break;
            }
            indice += 1;
        }

        File file = new File(c.getFilesDir(), "bd_Client.json");
        file.delete();
        file.createNewFile();

        for (Client x : lsClient) {
            creerClient(c, x.getIdent(), x.getNom(), x.getMail(), x.getSoldeRestant(), x.getDateIntervention(), x.getSignature());
        }

    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
