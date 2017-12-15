package jvpg.cgodin.qc.ca.projetpldl.Private;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import jvpg.cgodin.qc.ca.projetpldl.ModifierUtilisateurActivity;
import jvpg.cgodin.qc.ca.projetpldl.R;
import jvpg.cgodin.qc.ca.projetpldl.entities.Avatar;
import jvpg.cgodin.qc.ca.projetpldl.entities.ListeDeLecture;
import jvpg.cgodin.qc.ca.projetpldl.entities.Musique;

public class SelectAvatarActivity extends AppCompatActivity {

    private String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/avatar/";
    private List<Avatar> avatars = new ArrayList<Avatar>();
    String fluxJSON = "";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);


        new GGDownloadTask().execute("test");

        recyclerView = (RecyclerView) findViewById(R.id.listesAvatar);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlanetAdapter(avatars,getApplicationContext());
        recyclerView.setAdapter(adapter);


    }
    public void fillList(){
        Log.i("MusiqueList", fluxJSON);
        try {
            JSONArray avatarsJSON = new JSONArray(fluxJSON);

            /*int id;
            int proprietaire;
            String titre;
            String artiste;
            String musique;
            String vignette;
            boolean publique;
            boolean active;
            Date date;*/

            for(int i = 0; i < avatarsJSON.length(); i++){
                JSONObject av = avatarsJSON.getJSONObject(i);
                Avatar a = new Avatar();
                a.setAvatar(av.getString("avatar"));
                a.setNom(av.getString("nom"));
                a.setId(av.getInt("id"));
                avatars.add(a);
                //Log.i("Musique",m.toString());
            }

            //Toast.makeText(LoginActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter.notifyDataSetChanged();
        recyclerView = (RecyclerView) findViewById(R.id.listesAvatar);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlanetAdapter(avatars,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //params = loginCourriel.getText().toString() + "/" + loginMotDePasse.getText().toString();
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
            fillList();
            Log.i("JSON",fluxJSON);
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

        List<Avatar> planetList;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String item = (String) view.getTag().toString();
                //ModifierUtilisateurActivity.avatarChosen = Integer.parseInt(item);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",Integer.parseInt(item));
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        };

        public PlanetAdapter(List<Avatar> planetList, Context context) {
            this.planetList = planetList;
        }

        @Override
        public PlanetAdapter.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.avatar_item,parent,false);
            PlanetAdapter.PlanetViewHolder viewHolder=new PlanetAdapter.PlanetViewHolder(v);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(PlanetAdapter.PlanetViewHolder holder, int position) {
            //holder.image.setImageResource(R.drawable.planetimage);
            holder.nomAvatar.setText(planetList.get(position).getNom());

            holder.nomAvatar.setOnClickListener(mOnClickListener);
            holder.nomAvatar.setTag(planetList.get(position).getId());
            byte[] decodedString = Base64.decode(planetList.get(position).getAvatar(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgAvatar.setImageBitmap(decodedByte);
        }

        @Override
        public int getItemCount() {
            return planetList.size();
        }

        public  class PlanetViewHolder extends RecyclerView.ViewHolder{

            protected TextView nomAvatar;
            protected ImageView imgAvatar;

            public PlanetViewHolder(View itemView) {
                super(itemView);
                nomAvatar= (TextView) itemView.findViewById(R.id.nomAvatar);
                imgAvatar= (ImageView) itemView.findViewById(R.id.imgAvatar);
            }
        }
    }
}