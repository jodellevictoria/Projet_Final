package jvpg.cgodin.qc.ca.projetpldl;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

import jvpg.cgodin.qc.ca.projetpldl.dummy.DummyContent;
import jvpg.cgodin.qc.ca.projetpldl.entities.Musique;

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

/**
 * Created by jodel on 2017-12-14.
 */

public class Test {
/*
    private DummyContent.DummyItem mItem;
    private Musique musique;
    private String idMusique;

    private String urlDEL = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/musiquePublique/";
    String urlTicketDEL = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String paramsDEL = "";
    String paramsServiceDEL = "";
    String fluxJSONTicketDEL = "";
    String fluxJSONDEL = "";

    int cleDEL;
    int noTicketDEL;
    String chaineConfirmationDEL;

    public void useTicket(){
        try {
            JSONObject jObj = new JSONObject(fluxJSONTicketDEL);

            cleDEL = jObj.getInt("cleDEL");
            noTicketDEL = jObj.getInt("noTicketDEL");

            if(noTicketDEL != -1){
                String cleMDP = cleDEL + utilConnecte.getMotDePasse();
                try {
                    MessageDigest m= null;
                    m = MessageDigest.getInstance("MD5");
                    m.update(cleMDP.getBytes(),0,cleMDP.length());
                    chaineConfirmationDEL = new BigInteger(1,m.digest()).toString(16);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                new GGDownloadTask().execute("test");

            }
            else{
                Toast.makeText(getContext(), "Impossible de générer un ticket", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GGDownloadTaskTicket extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            paramsDEL = utilConnecte.getCourriel();
        }

        //cette méthode prend en argument un tableau illimité de chaines de caractères
        @Override
        protected String doInBackground(String... params) {
            String resultString = null;
            resultString = getJSON();
            fluxJSONTicketDEL = resultString;
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
            Log.i("JSON", fluxJSONTicketDEL);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(urlTicketDEL + paramsDEL);
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


    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //paramsDEL = loginCourriel.getText().toString() + "/" + loginMotDePasse.getText().toString();
            //paramsServiceDEL = action.equals(ARG_ACTION_PUBLIC) ? idMusique : noTicketDEL + "/" + chaineConfirmationDEL + "/" + utilConnecte.getId() + "/" + idMusique;

        }

        //cette méthode prend en argument un tableau illimité de chaines de caractères
        @Override
        protected String doInBackground(String... params) {
            String resultString = null;
            resultString = getJSON();
            fluxJSONDEL = resultString;

            Log.i("ParamsService", paramsServiceDEL);
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
            //fillList();
            //View recyclerView = findViewById(R.id.musique_list);
            //assert recyclerView != null;
            //setupRecyclerView((RecyclerView) recyclerView);

            Log.i("JSON", fluxJSONDEL);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(urlDEL + paramsServiceDEL);
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

    }*/
}
