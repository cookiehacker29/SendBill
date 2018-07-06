package christopheinformatique.sendbill;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RemplissageMail {

    public static String avec(String fileMail, Context c, String nom, String dateReal, String reference, String date, String solde, String description, String duree, String newSolde, String remarque, String sigClient) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(c.getAssets().open(fileMail)));
        String s;
        String res = "";
        boolean trouve = false;
        int x = 0;
        while ((s = br.readLine()) != null) {
            for (String v : getLsVariable(c)) {
                if (s.contains(v)) {
                    int indice = getLsVariable(c).indexOf(v);
                    String nv = "N/A";
                    switch (indice + 1) {
                        case 1:
                            nv = nom;
                            break;
                        case 2:
                            nv = dateReal;
                            break;
                        case 3:
                            nv = reference;
                            break;
                        case 4:
                            nv = date;
                            break;
                        case 5:
                            nv = solde;
                            break;
                        case 6:
                            nv = description;
                            break;
                        case 7:
                            nv = duree;
                            break;
                        case 8:
                            nv = newSolde;
                            break;
                        case 9:
                            nv = remarque;
                            break;
                        case 10:
                            nv = sigClient;
                            break;
                    }
                    String test = s.replace(v, nv);
                    res += test + "\n";
                    trouve = true;
                }

            }

            if (!trouve) {
                res += s + "\n";
            } else {
                trouve = false;
            }
        }
        return res;
    }

    private static List<String> getLsVariable(Context c) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(c.getAssets().open("email.txt")));
        int s;
        char x;
        String v = "";
        boolean recup = false;
        List<String> res = new ArrayList<>();
        while ((s = br.read()) != -1) {
            x = (char) s;
            if (recup) {
                v += x;
            }
            if (x == ']') {
                res.add(v);
                v = "";
                recup = false;
            }
            if (x == '[') {
                recup = true;
                v += "[";
            }
        }
        return res;
    }
}
