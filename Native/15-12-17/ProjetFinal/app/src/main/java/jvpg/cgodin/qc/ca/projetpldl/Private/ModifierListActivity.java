package jvpg.cgodin.qc.ca.projetpldl.Private;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
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
import java.util.ArrayList;

import jvpg.cgodin.qc.ca.projetpldl.R;
import jvpg.cgodin.qc.ca.projetpldl.entities.ListeDeLecture;
import jvpg.cgodin.qc.ca.projetpldl.entities.Musique;

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

public class ModifierListActivity extends AppCompatActivity {

    EditText nomListe;
    Button btnSave;
    ToggleButton togglePublique;
    ToggleButton toggleActif;

    ListeDeLecture l = new ListeDeLecture();
    int idListe;

    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecture/";
    String params = "";
    String fluxJSON = "";

    int cle;
    int noTicket;
    String chaineConfirmation;

    String urlMODIF = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecture/modifierListeDeLecture/";
    String urlTicketMODIF = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String paramsMODIF = "";
    String fluxJSONTicketMODIF = "";
    String fluxJSONMODIF = "";
    int cleMODIF;
    int noTicketMODIF;
    String chaineConfirmationMODIF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_list);

        idListe = ListeUtilContentListActivity.idListe;

        nomListe = (EditText) findViewById(R.id.modifListeNom);
        btnSave = (Button) findViewById(R.id.btnSauvegarder);
        togglePublique = (ToggleButton) findViewById(R.id.toggleListPublic);
        toggleActif = (ToggleButton) findViewById(R.id.toggleListeActif);

        new GGDownloadTask().execute("test");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GGDownloadTaskTicketCOPY
                AlertDialog.Builder builder = new AlertDialog.Builder(ModifierListActivity.this);

                builder.setTitle("Faire des changements à la liste");
                builder.setMessage("Voulez-vous vraiment sauvegarder ces modifications?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        new GGDownloadTaskTicketCOPY().execute("test");
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void fill(){
        try {
            JSONObject listesJSON = new JSONObject(fluxJSON);

            l.setId(listesJSON.getInt("id"));
            l.setProprietaire(listesJSON.getInt("proprietaire"));
            l.setNom(listesJSON.getString("nom"));
            l.setPublique(listesJSON.getBoolean("publique"));
            l.setActive(listesJSON.getBoolean("active"));
            Log.i("Liste",l.toString());

            nomListe.setText(l.getNom());
            toggleActif.setChecked(l.isActive());
            togglePublique.setChecked(l.isPublique());

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params = idListe+"";
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
            //Toast.makeText(LoginActivity.this, "Fin de l'exécution du traitement en arrière-plan", Toast.LENGTH_SHORT).show();
            //doAction();
            fill();
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

    public class GGDownloadTaskCOPY extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

            String nomListeTempo = nomListe.getText().toString();
            try {
                nomListeTempo = URLEncoder.encode(nomListeTempo, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            super.onPreExecute();
            paramsMODIF =  noTicketMODIF + "/" + chaineConfirmationMODIF + "/" + idListe + "/" + utilConnecte.getId() + "/"
            + nomListeTempo + "/" + togglePublique.isChecked() + "/" + toggleActif.isChecked();
        }

        //cette méthode prend en argument un tableau illimité de chaines de caractères
        @Override
        protected String doInBackground(String... params) {
            String resultString = null;
            resultString = getJSON();
            fluxJSONMODIF = resultString;
            return resultString;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //fillList();
            Toast.makeText(ModifierListActivity.this, fluxJSONMODIF, Toast.LENGTH_SHORT).show();
            finish();
            Log.i("JSON",fluxJSONMODIF);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(urlMODIF+paramsMODIF);
                Log.i("getJSON",urlMODIF+paramsMODIF);
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

    public void useTicketMODIF(){
        try {
            JSONObject jObj = new JSONObject(fluxJSONTicketMODIF);

            cleMODIF = jObj.getInt("cle");
            noTicketMODIF = jObj.getInt("noTicket");

            if(noTicketMODIF != -1){
                String cleMDP = cleMODIF + utilConnecte.getMotDePasse();
                try {
                    MessageDigest m= null;
                    m = MessageDigest.getInstance("MD5");
                    m.update(cleMDP.getBytes(),0,cleMDP.length());
                    chaineConfirmationMODIF = new BigInteger(1,m.digest()).toString(16);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                new GGDownloadTaskCOPY().execute("test");

            }
            else{
                Toast.makeText(this, "Impossible de générer un ticket", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GGDownloadTaskTicketCOPY extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            paramsMODIF = utilConnecte.getCourriel();
        }

        //cette méthode prend en argument un tableau illimité de chaines de caractères
        @Override
        protected String doInBackground(String... params) {
            String resultString = null;
            resultString = getJSON();
            fluxJSONTicketMODIF = resultString;
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
            useTicketMODIF();
            Log.i("JSON",fluxJSONTicketMODIF);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(urlTicketMODIF+paramsMODIF);
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
