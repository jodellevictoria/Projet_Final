package jvpg.cgodin.qc.ca.projetpldl.Private;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jvpg.cgodin.qc.ca.projetpldl.LoginActivity;
import jvpg.cgodin.qc.ca.projetpldl.Public.MusiquePubliqueDetailActivity;
import jvpg.cgodin.qc.ca.projetpldl.Public.MusiquePubliqueDetailFragment;
import jvpg.cgodin.qc.ca.projetpldl.R;

import jvpg.cgodin.qc.ca.projetpldl.Private.dummy.DummyContent;
import jvpg.cgodin.qc.ca.projetpldl.entities.Musique;

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

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

/**
 * An activity representing a list of Musique. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MusiquesUtilDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MusiquesUtilListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private List<Musique> musiquesUtil = new ArrayList<Musique>();

    String urlTicket = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/voirMusiquesALui/";
    String params = "";
    String fluxJSONTicket = "";
    String fluxJSON = "";

    int cle;
    int noTicket;
    String chaineConfirmation;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiquesutil_list);

        new GGDownloadTaskTicket().execute("test");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.musiquesutil_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        /*View recyclerView = findViewById(R.id.musiquesutil_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);*/
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, musiquesUtil, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final MusiquesUtilListActivity mParentActivity;
        private final List<Musique> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(MusiquesUtilDetailFragment.ARG_ITEM_ID, item.id);
                    MusiquesUtilDetailFragment fragment = new MusiquesUtilDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.musiquesutil_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MusiquesUtilDetailActivity.class);
                    intent.putExtra(MusiquesUtilDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }*/

                String item = (String) view.getTag().toString();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(MusiquePubliqueDetailFragment.ARG_ITEM_ID, item);
                    arguments.putString(MusiquePubliqueDetailFragment.ARG_ACTION_NAME,MusiquePubliqueDetailFragment.ARG_ACTION_UTIL);
                    MusiquePubliqueDetailFragment fragment = new MusiquePubliqueDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.musique_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MusiquePubliqueDetailActivity.class);
                    intent.putExtra(MusiquePubliqueDetailFragment.ARG_ITEM_ID, item);
                    intent.putExtra(MusiquePubliqueDetailFragment.ARG_ACTION_NAME,MusiquePubliqueDetailFragment.ARG_ACTION_UTIL);
                    Log.i("Test",MusiquePubliqueDetailFragment.ARG_ACTION_NAME +" /"+MusiquePubliqueDetailFragment.ARG_ACTION_UTIL);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(MusiquesUtilListActivity parent,
                                      List<Musique> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.musiquesutil_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.titreArtiste.setText(mValues.get(position).getTitre() + " - " + mValues.get(position).getArtiste());

            holder.itemView.setTag(mValues.get(position).getId());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView titreArtiste;

            ViewHolder(View view) {
                super(view);
                titreArtiste = (TextView) view.findViewById(R.id.titreArtisteMusique);
            }
        }
    }

    public void fillList(){
        Log.i("MusiqueList", fluxJSON);
        try {
            JSONArray musiquesJSON = new JSONArray(fluxJSON);

            /*int id;
            int proprietaire;
            String titre;
            String artiste;
            String musique;
            String vignette;
            boolean publique;
            boolean active;
            Date date;*/

            for(int i = 0; i < musiquesJSON.length(); i++){
                JSONObject musique = musiquesJSON.getJSONObject(i);
                Musique m = new Musique();
                m.setId(musique.getInt("id"));
                m.setProprietaire(musique.getInt("proprietaire"));
                m.setTitre(musique.getString("titre"));
                m.setArtiste(musique.getString("artiste"));
                m.setMusique(musique.getString("musique"));
                m.setVignette(musique.getString("vignette"));
                m.setPublique(musique.getBoolean("publique"));
                m.setActive(musique.getBoolean("active"));
                //m.setDate(new Date(musique.getString("date")));
                musiquesUtil.add(m);

                Log.i("Musique",m.toString());
            }

            //Toast.makeText(LoginActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
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
            //Toast.makeText(LoginActivity.this, "Fin de l'exécution du traitement en arrière-plan", Toast.LENGTH_SHORT).show();
            //doAction();
            fillList();
            View recyclerView = findViewById(R.id.musiquesutil_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
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
}
