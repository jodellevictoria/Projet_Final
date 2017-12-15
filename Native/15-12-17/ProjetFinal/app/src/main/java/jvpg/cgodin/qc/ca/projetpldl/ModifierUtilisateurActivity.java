package jvpg.cgodin.qc.ca.projetpldl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.List;

import jvpg.cgodin.qc.ca.projetpldl.Private.ModifierMusiqueActivity;
import jvpg.cgodin.qc.ca.projetpldl.Private.SelectAvatarActivity;
import jvpg.cgodin.qc.ca.projetpldl.entities.Musique;
import jvpg.cgodin.qc.ca.projetpldl.entities.Utilisateur;

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

public class ModifierUtilisateurActivity extends AppCompatActivity {
    List<Utilisateur> listeUtil = new ArrayList<Utilisateur>();
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/modifierProfil/";
    String urlTicket = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String fluxJSON = "";
    String fluxJSONTicket="";
    int noTicket;
    int cle;
    String chaineConfirmation;
    String params = "";

    EditText etNom;
    EditText etPassword;
    ImageButton etAvatar;
    Button btnModifierCompte;

    public static int avatarChosen = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_profil);
        avatarChosen = Integer.parseInt(utilConnecte.getAvatar());
        new GGDownloadAvatar().execute("test");

        etNom = (EditText) findViewById(R.id.etNom);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etAvatar = (ImageButton) findViewById(R.id.avatarButton);
        btnModifierCompte = (Button) findViewById(R.id.btnModifierCompte2);

        etNom.setText(utilConnecte.getAlias());
        etPassword.setText("");



        etAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SelectAvatarActivity.class);
                startActivityForResult(i,1);
            }
        });

        btnModifierCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new LoginActivity.GGDownloadTask().execute("test");
                new GGDownloadTaskTicket().execute("test");
                Log.i("LoginActivity!",fluxJSON);
            }
        });
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

                new GGDownloadTask().execute("test");
            }
            else{
                Toast.makeText(this, "Impossible de générer un ticket", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doAction(){
        Toast.makeText(ModifierUtilisateurActivity.this,fluxJSON , Toast.LENGTH_SHORT).show();
        finish();
    }

    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //@Path("modifierProfil/{noTicket}/{chaineConfirmation}/{idUtil}/{courriel}/{motDePasse}/{alias}/{avatar}")

            String aliasTempo=etNom.getText().toString();
            try {
                aliasTempo = URLEncoder.encode(aliasTempo, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String passwordTempo=etPassword.getText().toString();

            try {
                MessageDigest m= null;
                m = MessageDigest.getInstance("MD5");
                m.update(etPassword.getText().toString().getBytes(),0,etPassword.getText().toString().length());
                //System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
                passwordTempo = new BigInteger(1,m.digest()).toString(16);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            //@Path("modifierProfil/{noTicket}/{chaineConfirmation}/{idUtil}/{courriel}/{motDePasse}/{alias}/{avatar}")
            params = noTicket + "/" + chaineConfirmation + "/" + utilConnecte.getId()+"/"+utilConnecte.getCourriel()+"/"+passwordTempo+"/"+aliasTempo+"/"+avatarChosen;

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
                resultat="failed";
            }

            return resultat;
        }
    }

    public void changePicture(){
        new GGDownloadAvatar().execute("test");
    }

    public class GGDownloadAvatar extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            try {
                JSONObject avatarJSON = new JSONObject(fluxJSON);

                byte[] decodedString = Base64.decode(avatarJSON.getString("avatar"), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                etAvatar.setImageBitmap(decodedByte);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL("http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/avatar/"+avatarChosen);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int result=data.getIntExtra("result",1);
                avatarChosen = result;
                changePicture();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


}