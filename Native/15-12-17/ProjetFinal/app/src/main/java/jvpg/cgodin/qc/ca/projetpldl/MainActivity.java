package jvpg.cgodin.qc.ca.projetpldl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jvpg.cgodin.qc.ca.projetpldl.Private.ListesUtilActivity;
import jvpg.cgodin.qc.ca.projetpldl.Private.MusiquesUtilListActivity;
import jvpg.cgodin.qc.ca.projetpldl.Private.SearchYTActivity;
import jvpg.cgodin.qc.ca.projetpldl.Public.ListesPubliquesActivity;
import jvpg.cgodin.qc.ca.projetpldl.Public.MusiquePubliqueListActivity;
import jvpg.cgodin.qc.ca.projetpldl.entities.ListeDeLecture;
import jvpg.cgodin.qc.ca.projetpldl.entities.Utilisateur;

public class MainActivity extends AppCompatActivity {

    public static Utilisateur utilConnecte = null;

    Button btnLogin;
    Button btnMusiquesPubliques;
    Button btnListesPubliques;
    Button btnMusiquesUtil;
    Button btnListesUtil;
    Button btnModifierUtil;
    Button btnSearchYT;
    ImageView imageView;

    TextView welcomeText;
    TextView infoText;


    String fluxJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnConnexion);
        btnMusiquesPubliques = (Button) findViewById(R.id.btnMusiquesPubliques);
        btnListesPubliques = (Button) findViewById(R.id.btnListesPubliques);

        btnSearchYT = (Button) findViewById(R.id.btnSearchYT);
        btnMusiquesUtil = (Button) findViewById(R.id.btnVoirMesMusiques);
        btnListesUtil = (Button) findViewById(R.id.btnVoirMesListes);
        btnModifierUtil = (Button) findViewById(R.id.btnModifierProfil);
        imageView = (ImageView) findViewById(R.id.imageView);
        welcomeText = (TextView) findViewById(R.id.txtWelcome);
        infoText = (TextView) findViewById(R.id.infoPrives);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(utilConnecte != null){
                utilConnecte = null;
                Toast.makeText(getApplicationContext(), "Vous n'êtes plus connecté", Toast.LENGTH_SHORT).show();

                changeAppearance();
            }
            else{
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(i,1);

                //changeAppearance();
            }
            }
        });

        btnMusiquesPubliques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MusiquePubliqueListActivity.class);
            startActivity(i);
            }
        });

        btnListesPubliques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), ListesPubliquesActivity.class);
            startActivity(i);
            }
        });

        btnSearchYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchYTActivity.class);
                startActivity(i);
            }
        });

        btnMusiquesUtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MusiquesUtilListActivity.class);
                startActivity(i);
            }
        });

        btnListesUtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListesUtilActivity.class);
                startActivity(i);
            }
        });

        btnModifierUtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ModifierUtilisateurActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            /*if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }else{

            }*/

            changeAppearance();
        }
    }

    public void changeAppearance(){
        Log.i("changeAppearance()","dans methode");
        if(utilConnecte != null){
            Log.i("changeAppearance()","utilisateur is connected()");
            btnLogin.setText("Déconnecter");
            welcomeText.setText("Bienvenue " + utilConnecte.getAlias());
            infoText.setText("");
            btnSearchYT.setEnabled(true);
            btnMusiquesUtil.setEnabled(true);
            btnListesUtil.setEnabled(true);
            btnModifierUtil.setEnabled(true);
            new GGDownloadAvatar().execute("test");
            imageView.setVisibility(View.VISIBLE);
        }
        else{
            Log.i("changeAppearance()","utilisateur is not connected");
            btnLogin.setText("Se connecter");
            welcomeText.setText("Bienvenue");
            infoText.setText("Veuillez vous connecter pour utiliser les fonctionalités suivantes");
            btnSearchYT.setEnabled(false);
            btnMusiquesUtil.setEnabled(false);
            btnListesUtil.setEnabled(false);
            btnModifierUtil.setEnabled(false);

            imageView.setVisibility(View.INVISIBLE);
        }
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
                imageView.setImageBitmap(decodedByte);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL("http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/avatar/"+utilConnecte.getAvatar());
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
