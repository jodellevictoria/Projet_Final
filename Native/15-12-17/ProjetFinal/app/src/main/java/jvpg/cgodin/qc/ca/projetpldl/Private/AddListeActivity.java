package jvpg.cgodin.qc.ca.projetpldl.Private;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jvpg.cgodin.qc.ca.projetpldl.R;

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

public class AddListeActivity extends AppCompatActivity {

    EditText nomListe;
    Button btnSave;
    ToggleButton togglePublique;
    ToggleButton toggleActif;

    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecture/creerListeDeLecture/";
    String urlTicket = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String params = "";
    String fluxJSONTicket = "";
    String fluxJSON = "";
    int cle;
    int noTicket;
    String chaineConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_liste);

        nomListe = (EditText) findViewById(R.id.addListeNom);
        btnSave = (Button) findViewById(R.id.btnAjouter);
        togglePublique = (ToggleButton) findViewById(R.id.toggleListPublic);
        toggleActif = (ToggleButton) findViewById(R.id.toggleListeActif);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GGDownloadTaskTicket().execute("test");
            }
        });
    }

    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String nomListeTempo = nomListe.getText().toString();
            try {
                nomListeTempo = URLEncoder.encode(nomListeTempo, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            params = noTicket + "/" + chaineConfirmation + "/" + utilConnecte.getId() + "/"
                    + nomListeTempo + "/" + togglePublique.isChecked() + "/" + toggleActif.isChecked();
        }

        //cette méthode prend en argument un tableau illimité de chaines de caractères
        @Override
        protected String doInBackground(String... params) {
            String resultString = null;
            resultString = getJSON();
            fluxJSON = resultString;
            return resultString;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(AddListeActivity.this, fluxJSON, Toast.LENGTH_SHORT).show();
            finish();
            Log.i("JSON",fluxJSON);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(url+params);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                StringBuffer sb = new StringBuffer();
                InputStream is = null;

                is = new BufferedInputStream(c.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = "";
                while ((line = br.readLine()) != null){
                    sb.append(line);
                }
                resultat = sb.toString();
            }
            catch(Exception e){
                Log.e("Read JSON Fail", Log.getStackTraceString(e));
            }

            return resultat;
        }

    }

    public void useTicket(){
        try {
            JSONObject jObj = new JSONObject(fluxJSONTicket);

            cle = jObj.getInt("cle");
            noTicket = jObj.getInt("noTicket");

            if(noTicket != -1){
                String cleMDP = cle + utilConnecte.getMotDePasse();
                try {
                    MessageDigest m= null;
                    m = MessageDigest.getInstance("MD5");
                    m.update(cleMDP.getBytes(),0,cleMDP.length());
                    chaineConfirmation = new BigInteger(1,m.digest()).toString(16);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                new GGDownloadTask().execute("test");

            }
            else{
                Toast.makeText(this, "Impossible de générer un ticket", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GGDownloadTaskTicket extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params = utilConnecte.getCourriel();
        }

        //cette méthode prend en argument un tableau illimité de chaines de caractères
        @Override
        protected String doInBackground(String... params) {
            String resultString = null;
            resultString = getJSON();
            fluxJSONTicket = resultString;
            return resultString;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(LoginActivity.this, "Fin de l'exécution du traitement en arrière-plan", Toast.LENGTH_SHORT).show();
            //doAction();
            useTicket();
            Log.i("JSON",fluxJSONTicket);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(urlTicket+params);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                StringBuffer sb = new StringBuffer();
                InputStream is = null;

                is = new BufferedInputStream(c.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = "";
                while ((line = br.readLine()) != null){
                    sb.append(line);
                }
                resultat = sb.toString();
            }
            catch(Exception e){
                Log.e("Read JSON Fail", Log.getStackTraceString(e));
            }

            return resultat;
        }

    }
}
