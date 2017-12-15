package jvpg.cgodin.qc.ca.projetpldl.Public;

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
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jvpg.cgodin.qc.ca.projetpldl.R;
import jvpg.cgodin.qc.ca.projetpldl.entities.Musique;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Musiques. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MusiquePubliqueDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MusiquePubliqueListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/musiquesPubliques";
    private List<Musique> musiquesPubliques = new ArrayList<Musique>();
    String fluxJSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musique_list);

        new GGDownloadTask().execute("test");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Musiques publiques");

        if (findViewById(R.id.musique_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        /*View recyclerView = findViewById(R.id.musique_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);*/
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, musiquesPubliques, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final MusiquePubliqueListActivity mParentActivity;
        private final List<Musique> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                //Musique item = (Musique) view.getTag();
                String item = (String) view.getTag().toString();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(MusiquePubliqueDetailFragment.ARG_ITEM_ID, item);
                    arguments.putString(MusiquePubliqueDetailFragment.ARG_ACTION_NAME,MusiquePubliqueDetailFragment.ARG_ACTION_PUBLIC);
                    MusiquePubliqueDetailFragment fragment = new MusiquePubliqueDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.musique_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MusiquePubliqueDetailActivity.class);
                    intent.putExtra(MusiquePubliqueDetailFragment.ARG_ITEM_ID, item);
                    intent.putExtra(MusiquePubliqueDetailFragment.ARG_ACTION_NAME,MusiquePubliqueDetailFragment.ARG_ACTION_PUBLIC);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(MusiquePubliqueListActivity parent,
                                      List<Musique> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.musique_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getTitre() + " - " + mValues.get(position).getArtiste());
            //holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position).getId());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
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
                musiquesPubliques.add(m);

                Log.i("Musique",m.toString());
            }

            //Toast.makeText(LoginActivity.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            View recyclerView = findViewById(R.id.musique_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
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
}
