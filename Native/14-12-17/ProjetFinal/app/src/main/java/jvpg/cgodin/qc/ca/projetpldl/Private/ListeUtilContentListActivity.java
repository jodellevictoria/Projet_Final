package jvpg.cgodin.qc.ca.projetpldl.Private;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jvpg.cgodin.qc.ca.projetpldl.Public.ListePubliqueContenuListActivity;
import jvpg.cgodin.qc.ca.projetpldl.Public.MusiquePubliqueDetailActivity;
import jvpg.cgodin.qc.ca.projetpldl.Public.MusiquePubliqueDetailFragment;
import jvpg.cgodin.qc.ca.projetpldl.R;

import jvpg.cgodin.qc.ca.projetpldl.Private.dummy.DummyContent;
import jvpg.cgodin.qc.ca.projetpldl.entities.ListeDeLecture;
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
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ListeUtilContentDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ListeUtilContentListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private List<Musique> musiques = new ArrayList<Musique>();

    public Musique musicSelected = null;

    String urlTicket = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecture/voirUneListeDeLectureALui/";
    String params = "";
    String fluxJSONTicket = "";
    String fluxJSON = "";

    public static int idListe;

    int cle;
    int noTicket;
    String chaineConfirmation;

    String urlCOPY = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecturemusiques/copierListeeALaListe/";
    String urlTicketCOPY = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String paramsCOPY = "";
    String fluxJSONTicketCOPY = "";
    String fluxJSONCOPY = "";
    int cleCOPY;
    int noTicketCOPY;
    String chaineConfirmationCOPY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listeutilcontent_list);

        Bundle extras = getIntent().getExtras();
        new GetNameOfPlaylist().execute("test");

        if (extras != null) {
            idListe = Integer.parseInt(extras.getString("idListe"));
            Log.i("List content", idListe+"");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEditList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ModifierListActivity.class);
                startActivity(i);
                //i.putExtra("idListe",idListe);
                //adapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fabCopyList);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListeUtilContentListActivity.this);

                builder.setTitle("Copier toute la liste vers une autre liste");
                builder.setMessage("Toutes les musiques dans liste de lecture seront copiés vers une nouvelle liste de lecture " +
                        "avec le même nom et \"- Copie\". Êtes-vous sûr(e) de vouloir copier la liste?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        new GGDownloadTaskTicketCOPY().execute("test");
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        new GGDownloadTaskTicket().execute("test");

        if (findViewById(R.id.listeutilcontent_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        /*View recyclerView = findViewById(R.id.listeutilcontent_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);*/
    }

    public class GetNameOfPlaylist extends AsyncTask<String, Integer, String> {

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
            try {
                JSONObject jObj = new JSONObject(fluxJSON);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle(jObj.getString("nom"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("JSON",fluxJSON);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL("http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecture/"+idListe);
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, musiques, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ListeUtilContentListActivity mParentActivity;
        private final List<Musique> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ListeUtilContentDetailFragment.ARG_ITEM_ID, item.id);
                    ListeUtilContentDetailFragment fragment = new ListeUtilContentDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.listeutilcontent_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ListeUtilContentDetailActivity.class);
                    intent.putExtra(ListeUtilContentDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }*/
                String item = (String) view.getTag().toString();

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(MusiquePubliqueDetailFragment.ARG_ITEM_ID, item);
                    arguments.putString(MusiquePubliqueDetailFragment.ARG_ACTION_NAME,MusiquePubliqueDetailFragment.ARG_ACTION_LIST);
                    MusiquePubliqueDetailFragment fragment = new MusiquePubliqueDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.musique_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MusiquePubliqueDetailActivity.class);
                    intent.putExtra(MusiquePubliqueDetailFragment.ARG_ITEM_ID, item);
                    intent.putExtra(MusiquePubliqueDetailFragment.ARG_ACTION_NAME,MusiquePubliqueDetailFragment.ARG_ACTION_LIST);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ListeUtilContentListActivity parent,
                                      List<Musique> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listeutilcontent_list_content, parent, false);
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
                mContentView = (TextView) view.findViewById(R.id.musiqueInList);
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
                musiques.add(m);

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
            params =  noTicket + "/" + chaineConfirmation + "/" + idListe + "/" + utilConnecte.getId();
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
            View recyclerView = findViewById(R.id.listeutilcontent_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(url+params);
                Log.i("getJSON",url+params);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0){
            recreate();
        }
    }

    public class GGDownloadTaskCOPY extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            paramsCOPY =  noTicketCOPY + "/" + chaineConfirmationCOPY + "/" + utilConnecte.getId() + "/" + idListe;
        }

        //cette méthode prend en argument un tableau illimité de chaines de caractères
        @Override
        protected String doInBackground(String... params) {
            String resultString = null;
            resultString = getJSON();
            fluxJSONCOPY = resultString;
            return resultString;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //fillList();
            Log.i("JSON",fluxJSONCOPY);
            View recyclerView = findViewById(R.id.listeutilcontent_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(urlCOPY+paramsCOPY);
                Log.i("getJSON",urlCOPY+paramsCOPY);
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

    public void useTicketCOPY(){
        try {
            JSONObject jObj = new JSONObject(fluxJSONTicketCOPY);

            cleCOPY = jObj.getInt("cle");
            noTicketCOPY = jObj.getInt("noTicket");

            if(noTicketCOPY != -1){
                String cleMDP = cleCOPY + utilConnecte.getMotDePasse();
                try {
                    MessageDigest m= null;
                    m = MessageDigest.getInstance("MD5");
                    m.update(cleMDP.getBytes(),0,cleMDP.length());
                    chaineConfirmationCOPY = new BigInteger(1,m.digest()).toString(16);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                new GGDownloadTaskCOPY().execute("test");

            }
            else{
                Toast.makeText(this, "Impossible de générer un ticket", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GGDownloadTaskTicketCOPY extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            paramsCOPY = utilConnecte.getCourriel();
        }

        //cette méthode prend en argument un tableau illimité de chaines de caractères
        @Override
        protected String doInBackground(String... params) {
            String resultString = null;
            resultString = getJSON();
            fluxJSONTicketCOPY = resultString;
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
            useTicketCOPY();
            Log.i("JSON",fluxJSONTicketCOPY);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(urlTicketCOPY+paramsCOPY);
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
