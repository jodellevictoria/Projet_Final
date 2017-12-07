package jvpg.cgodin.qc.ca.projetpldl;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.List;

import jvpg.cgodin.qc.ca.projetpldl.entities.ListeDeLecture;

public class ListesPubliquesActivity extends AppCompatActivity {
    List<ListeDeLecture> listesPubliques = new ArrayList<ListeDeLecture>();
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecture/voirListeDeLectures";
    String fluxJSON = "";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listes_publiques);
        //ListeDeLecture listelool= new ListeDeLecture();
        //listelool.setNom("LOL");
        //listesPubliques.add(listelool);

        new GGDownloadTask().execute("test");

        recyclerView = (RecyclerView) findViewById(R.id.listesPubliques);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlanetAdapter(listesPubliques,getApplicationContext());
        recyclerView.setAdapter(adapter);

    }

    public void fillList(){
        try {
            listesPubliques = new ArrayList<ListeDeLecture>();
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
                listesPubliques.add(l);
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

        adapter = new PlanetAdapter(listesPubliques,getApplicationContext());
        recyclerView.setAdapter(adapter);
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
            fillList();
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(url);
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

        public PlanetAdapter(List<ListeDeLecture> planetList, Context context) {
            this.planetList = planetList;
        }

        @Override
        public PlanetAdapter.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listedelecture_item,parent,false);
            PlanetViewHolder viewHolder=new PlanetViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PlanetAdapter.PlanetViewHolder holder, int position) {
            //holder.image.setImageResource(R.drawable.planetimage);
            holder.infoFilms.setText(planetList.get(position).getNom());



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
