package jvpg.cgodin.qc.ca.projetpldl;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jvpg.cgodin.qc.ca.projetpldl.entities.Utilisateur;

public class CreerUtilisateurCaptchaActivity extends AppCompatActivity {
    List<Utilisateur> listeUtil = new ArrayList<Utilisateur>();
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/creer/";
    String fluxJSON = "";

    TextView tvNoTicket;
    ImageView imgCaptcha;
    EditText etCaptcha;
    EditText etNoTicket;

    Button btnConfirmerCaptcha;

    String captcha;
    String et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_utilisateur_captcha);

        tvNoTicket = (TextView) findViewById(R.id.tvNoTicket);
        imgCaptcha = (ImageView) findViewById(R.id.imgCaptcha);
        etCaptcha = (EditText) findViewById(R.id.etCaptcha);
        etNoTicket = (EditText) findViewById(R.id.etNoTicket);
        btnConfirmerCaptcha = (Button) findViewById(R.id.btnConfirmeCaptcha);

        tvNoTicket.setText(getIntent().getStringExtra("noTicketDEL"));



        byte[] decodedString = Base64.decode(getIntent().getStringExtra("captchaImageFlux")+"", Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imgCaptcha.setImageBitmap(decodedByte);


        btnConfirmerCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new LoginActivity.GGDownloadTask().execute("test");
                new GGDownloadTask().execute("test");
                //Log.i("LoginActivity!",fluxJSONDEL);
            }
        });
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

            Toast.makeText(CreerUtilisateurCaptchaActivity.this, fluxJSON, Toast.LENGTH_SHORT).show();
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(url+etNoTicket.getText().toString()+"/"+etCaptcha.getText().toString());
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
