package jvpg.cgodin.qc.ca.projetpldl.Private;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import jvpg.cgodin.qc.ca.projetpldl.entities.Musique;

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

public class ModifierMusiqueActivity extends AppCompatActivity {

    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/consulterMusique/";
    String url2 = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/modifierMusique/";
    String urlTicket = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String fluxJSON = "";
    String fluxJSONTicket="";
    int noTicket;
    int cle;
    String chaineConfirmation;
    String params = "";
    Musique musique;


    TextView addMusiqueTitre;
    TextView addMusiqueArtiste;
    Button btnSave;
    ToggleButton togglePublique;
    ToggleButton toggleActif;
    int idMusique;

    String strTempo="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_musique);
        Bundle extras = getIntent().getExtras();



        idMusique  = getIntent().getIntExtra("idMusique",0);


        addMusiqueTitre = (TextView) findViewById(R.id.addMusiqueTitre);
        addMusiqueArtiste = (TextView) findViewById(R.id.addMusiqueArtiste);
        btnSave = (Button) findViewById(R.id.btnSave);
        togglePublique = (ToggleButton) findViewById(R.id.togglePublique);
        toggleActif = (ToggleButton) findViewById(R.id.toggleActif);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new LoginActivity.GGDownloadTask().execute("test");

                ///Log.i("LoginActivity!",fluxJSON);
                //Musique m = new Musique();

                new GGDownloadTaskTicket().execute("test1");
            }
        });

        new GGDownloadTaskTicket().execute("test");
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
                if(strTempo=="")
                {
                    new GGDownloadTask().execute("test");
                    strTempo="dsadasd";
                }
                else
                {
                    new GGDownloadTask2().execute("test2");
                }

            }
            else{
                Toast.makeText(this, "Impossible de générer un ticket", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params = noTicket + "/" + chaineConfirmation + "/" + utilConnecte.getId()+"/"+idMusique;
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
            Log.i("JSON",fluxJSON);
            doAction();
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

    public void doAction(){

        try {
            JSONObject jObj = new JSONObject(fluxJSON);
            musique = new Musique();
            musique.setId(jObj.getInt("id"));
            musique.setProprietaire(jObj.getInt("proprietaire"));
            musique.setTitre(jObj.getString("titre"));
            Toast.makeText(ModifierMusiqueActivity.this, musique.getTitre()+"t1", Toast.LENGTH_SHORT).show();
            musique.setArtiste(jObj.getString("artiste"));
            musique.setMusique(jObj.getString("musique"));
            musique.setVignette(jObj.getString("vignette"));
            musique.setPublique(jObj.getBoolean("publique"));
            musique.setActive(jObj.getBoolean("active"));

            addMusiqueTitre.setText(jObj.getString("titre"));
            addMusiqueArtiste.setText(jObj.getString("artiste"));
            togglePublique.setChecked(jObj.getBoolean("publique"));
            toggleActif.setChecked(jObj.getBoolean("active"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void doAction2(){
       Toast.makeText(ModifierMusiqueActivity.this,"Musique Modifiée" , Toast.LENGTH_SHORT).show();
       finish();
    }


    public class GGDownloadTask2 extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TextView addMusiqueArtiste;
            //Toast.makeText(ModifierMusiqueActivity.this, musique.getTitre()+"t2", Toast.LENGTH_SHORT).show();

            String titreTempo=addMusiqueTitre.getText().toString();
            String artisteTempo=addMusiqueArtiste.getText().toString();





            try {
                titreTempo = URLEncoder.encode(titreTempo, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                artisteTempo = URLEncoder.encode(artisteTempo, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }




            //modifierMusique/{noTicket}/{chaineConfirmation}/{Id}/{IdUtil}/{Titre}/{Artiste}/{Publique}/{Active}
            params = noTicket + "/" + chaineConfirmation + "/" + idMusique +"/"+utilConnecte.getId() +"/"+titreTempo
                    +"/"+artisteTempo+"/"+((Boolean)togglePublique.isChecked()).toString()+"/"+((Boolean)toggleActif.isChecked()).toString();

            //((Boolean) m.isActive()).toString()+((Boolean) m.isActive()).toString()
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
            Log.i("JSON",fluxJSON);
            doAction2();
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(url2+params);
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
                Toast.makeText(ModifierMusiqueActivity.this,"Erreur" , Toast.LENGTH_SHORT).show();
                finish();
            }

            return resultat;
        }
    }


}
