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

import jvpg.cgodin.qc.ca.projetpldl.Public.dummy.DummyContent;
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
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ListePubliqueContenuDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ListePubliqueContenuListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private List<Musique> musiquesDansListe = new ArrayList<Musique>();
    int idListe;
    String fluxJSON = "";
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecture/voirListeDeLecture/";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listepubliquecontenu_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            idListe = Integer.parseInt(extras.getString("idListe"));
            Log.i("List content", idListe+"");
        }

        new GGDownloadTask().execute("test");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.listepubliquecontenu_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, musiquesDansListe, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ListePubliqueContenuListActivity mParentActivity;
        private final List<Musique> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Musique item = (Musique) view.getTag();
                /*if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ListePubliqueContenuDetailFragment.ARG_ITEM_ID, item.id);
                    ListePubliqueContenuDetailFragment fragment = new ListePubliqueContenuDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.listepubliquecontenu_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ListePubliqueContenuDetailActivity.class);
                    intent.putExtra(ListePubliqueContenuDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }*/

                //DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                //Musique item = (Musique) view.getTag();
                String item = (String) view.getTag().toString();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(MusiquePubliqueDetailFragment.ARG_ITEM_ID, item);
                    MusiquePubliqueDetailFragment fragment = new MusiquePubliqueDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.musique_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MusiquePubliqueDetailActivity.class);
                    intent.putExtra(MusiquePubliqueDetailFragment.ARG_ITEM_ID, item);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ListePubliqueContenuListActivity parent,
                                      List<Musique> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listepubliquecontenu_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position).getTitre() + " - " + mValues.get(position).getArtiste());

            holder.itemView.setTag(mValues.get(position).getId());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
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
                musiquesDansListe.add(m);

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
            fillList();
            Log.i("JSON",fluxJSON);
            View recyclerView = findViewById(R.id.listepubliquecontenu_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(url+idListe);
                Log.i("getJSON",url+idListe);
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
