package jvpg.cgodin.qc.ca.projetpldl;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jvpg.cgodin.qc.ca.projetpldl.entities.Utilisateur;

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

public class LoginActivity extends AppCompatActivity {

    EditText loginCourriel;
    EditText loginMotDePasse;
    Button btnLogin;
    Button btnCreerCompte;
    String urlTicket = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/validerUtilisateur/";
    String params = "";
    String fluxJSONTicket = "";
    String fluxJSON = "";

    String passwordMD5;
    int cle;
    int noTicket;
    String chaineConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginCourriel = (EditText) findViewById(R.id.loginEmail);
        loginMotDePasse = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCreerCompte = (Button) findViewById(R.id.btnCreerCompte);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new GGDownloadTask().execute("test");
                new GGDownloadTaskTicket().execute("test");

                try {
                    MessageDigest m= null;
                    m = MessageDigest.getInstance("MD5");
                    m.update(loginMotDePasse.getText().toString().getBytes(),0,loginMotDePasse.getText().toString().length());
                    //System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
                    passwordMD5 = new BigInteger(1,m.digest()).toString(16);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                Log.i("LoginActivity!",fluxJSON);

            }
        });

        btnCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreerUtilisateurActivity.class);
                startActivity(intent);
            }
        });


    }

    public void useTicket(){
        try {
            JSONObject jObj = new JSONObject(fluxJSONTicket);

            cle = jObj.getInt("cle");
            noTicket = jObj.getInt("noTicket");

            if(noTicket != -1){
                String cleMDP = cle + passwordMD5;
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
                Toast.makeText(LoginActivity.this, "L'utilisateur n'existe pas dans la base de données", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doAction(){
        try {
            JSONObject jObj = new JSONObject(fluxJSON);

            switch (jObj.getInt("result")){
                case 1:
                    //"utilisateur":{"courriel"u1@gmail.ca,"motdepasse":"2ac9cb7dc02b3c0083eb70898e549b63",
                    // "alias":"Utilisateur 1","avatar":"10","actif":true,"date":Tue Oct 20 19:00:00 EDT 2015}}
                    JSONObject jsonUtil = jObj.getJSONObject("utilisateur");
                    Utilisateur util = new Utilisateur();
                    util.setId(jsonUtil.getInt("id"));
                    util.setCourriel(loginCourriel.getText().toString());
                    util.setMotDePasse(passwordMD5);
                    util.setAlias(jsonUtil.getString("alias"));
                    util.setAvatar(jsonUtil.getString("avatar"));
                    util.setActif(jsonUtil.getBoolean("actif"));
                    utilConnecte = util;
                    Log.i("Login", utilConnecte.toString());

                    Toast.makeText(LoginActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("result",1);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                    break;
                default:

                    Toast.makeText(LoginActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    break;
                /*case -2:

                    Toast.makeText(LoginActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    break;*/
            }


            /*if(utilConnecte != null){
                finish();
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params = noTicket + "/" + chaineConfirmation + "/" + loginCourriel.getText().toString() + "/" + passwordMD5;
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
            doAction();
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

    public class GGDownloadTaskTicket extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params = loginCourriel.getText().toString();
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
