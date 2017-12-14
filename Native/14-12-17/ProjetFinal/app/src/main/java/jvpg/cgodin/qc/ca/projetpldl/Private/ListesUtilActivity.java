package jvpg.cgodin.qc.ca.projetpldl.Private;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;

import jvpg.cgodin.qc.ca.projetpldl.Public.ListePubliqueContenuListActivity;
import jvpg.cgodin.qc.ca.projetpldl.Public.ListesPubliquesActivity;
import jvpg.cgodin.qc.ca.projetpldl.R;
import jvpg.cgodin.qc.ca.projetpldl.entities.ListeDeLecture;

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

public class ListesUtilActivity extends AppCompatActivity {

    List<ListeDeLecture> listes = new ArrayList<ListeDeLecture>();
    //{noTicket}/{chaineConfirmation}/{idUtil}
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecture/voirListeDeLectureALui/";
    String urlTicket = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String params = "";
    String fluxJSONTicket = "";
    String fluxJSON = "";
    int cle;
    int noTicket;
    String chaineConfirmation;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listes_util);
        //ListeDeLecture listelool= new ListeDeLecture();
        //listelool.setNom("LOL");
        //listesPubliques.add(listelool);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddPlaylist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddListeActivity.class);
                startActivity(i);
            }
        });

        new GGDownloadTaskTicket().execute("test");

        recyclerView = (RecyclerView) findViewById(R.id.listesPubliques);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlanetAdapter(listes,getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    public void fillList(){
        try {
            listes = new ArrayList<ListeDeLecture>();
            JSONArray listesJSON = new JSONArray(fluxJSON);
             /*this.id = id;
            this.proprietaire = proprietaire;
            this.nom = nom;
            this.publique = publique;
            this.active = active;
            this.date = date;*/

            for(int i = 0; i < listesJSON.length(); i++){
                JSONObject liste = listesJSON.getJSONObject(i);
                ListeDeLecture l = new ListeDeLecture();
                l.setId(liste.getInt("id"));
                l.setProprietaire(liste.getInt("proprietaire"));
                l.setNom(liste.getString("nom"));
                l.setPublique(liste.getBoolean("publique"));
                l.setActive(liste.getBoolean("active"));
                listes.add(l);
                Log.i("Liste",l.toString());
            }

            //Toast.makeText(LoginActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
        recyclerView = (RecyclerView) findViewById(R.id.listesPubliques);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlanetAdapter(listes,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params = noTicket + "/" + chaineConfirmation + "/" + utilConnecte.getId();
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
            fillList();
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

    public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder> {

        List<ListeDeLecture> planetList;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = (String) view.getTag().toString();
                Intent intent = new Intent(getApplicationContext(), ListeUtilContentListActivity.class);
                intent.putExtra("idListe", item);
                intent.putExtra(ChooseListeActivity.ARG_ACTION_NAME, ChooseListeActivity.ARG_ACTION_ADD);

                startActivity(intent);
            }
        };

        public PlanetAdapter(List<ListeDeLecture> planetList, Context context) {
            this.planetList = planetList;
        }

        @Override
        public PlanetAdapter.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listedelecture_item,parent,false);
            PlanetAdapter.PlanetViewHolder viewHolder=new PlanetAdapter.PlanetViewHolder(v);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(PlanetAdapter.PlanetViewHolder holder, int position) {
            //holder.image.setImageResource(R.drawable.planetimage);
            holder.infoFilms.setText(planetList.get(position).getNom());
            holder.infoFilms.setOnClickListener(mOnClickListener);
            holder.infoFilms.setTag(planetList.get(position).getId());

        }

        @Override
        public int getItemCount() {
            return planetList.size();
        }

        public  class PlanetViewHolder extends RecyclerView.ViewHolder{

            protected TextView infoFilms;


            public PlanetViewHolder(View itemView) {
                super(itemView);
                infoFilms= (TextView) itemView.findViewById(R.id.listeNom);
            }
        }
    }
}
