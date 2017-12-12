package jvpg.cgodin.qc.ca.projetpldl;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jvpg.cgodin.qc.ca.projetpldl.entities.Utilisateur;

public class CreerUtilisateurActivity extends AppCompatActivity {
    List<Utilisateur> listeUtil = new ArrayList<Utilisateur>();
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/creer/";
    String fluxJSON = "";

    /*private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;*/

    EditText etNom;
    EditText etCourriel;
    EditText etPassword;
    EditText etAvatar;
    Button btnCreerCompte;

    String captcha;
    String noTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_utilisateur);


        etNom = (EditText) findViewById(R.id.etNom);
        etCourriel = (EditText) findViewById(R.id.etCourriel);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etAvatar = (EditText) findViewById(R.id.etAvatar);
        btnCreerCompte = (Button) findViewById(R.id.btnModifierCompte2);



        btnCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new LoginActivity.GGDownloadTask().execute("test");
                new GGDownloadTask().execute("test");
                Log.i("LoginActivity!",fluxJSON);
            }
        });
    }


    public void CreateUser(){

        //Utilisateur u;
        String messageErreur="succes";



        try {
            JSONObject jObj = new JSONObject(fluxJSON);

            if(jObj.getString("captcha").compareTo("-1")==0){
                messageErreur="un utilisateur avec le meme courriel existe déjà";
            }
            else if(jObj.getString("captcha").compareTo("-2")==0){
                messageErreur="un utilisateur avec le meme alias existe déjà";
            }
            else if(jObj.getString("captcha").compareTo("-3")==0){
                messageErreur="entréé un numero de 1 a 36 pour l'avatar";
            }
            else{
                captcha=jObj.getString("captchaImageFlux");
                noTicket=jObj.getString("numTicket");

                Intent intent = new Intent(getApplicationContext(), CreerUtilisateurCaptchaActivity.class);
                intent.putExtra("noTicket",noTicket);
                intent.putExtra("captchaImageFlux",captcha);
                intent.putExtra("courriel",etCourriel.getText().toString());
                intent.putExtra("motDePasse",etPassword.getText().toString());
                intent.putExtra("nom",etNom.getText().toString());
                intent.putExtra("avatar",etAvatar.getText().toString());
                startActivity(intent);
            }
            Toast.makeText(CreerUtilisateurActivity.this, messageErreur, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

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
            CreateUser();
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(url+etNom.getText().toString()+"/"+etCourriel.getText().toString()+"/"+etPassword.getText().toString()+"/"+etAvatar.getText().toString());
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
}
