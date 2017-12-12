package jvpg.cgodin.qc.ca.projetpldl.Public;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import jvpg.cgodin.qc.ca.projetpldl.R;
import jvpg.cgodin.qc.ca.projetpldl.dummy.DummyContent;
import jvpg.cgodin.qc.ca.projetpldl.entities.Musique;

/**
 * A fragment representing a single Musique detail screen.
 * This fragment is either contained in a {@link MusiquePubliqueListActivity}
 * in two-pane mode (on tablets) or a {@link MusiquePubliqueDetailActivity}
 * on handsets.
 */
public class MusiquePubliqueDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;
    private Musique musique;
    private String idMusique;

    TextView txtTitre;
    TextView txtArtiste;
    TextView txtProprietaire;
    ToggleButton togglePublique;
    ToggleButton toggleActive;
    VideoView videoMusique;

    private String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/musiquePublique/";
    //private List<Musique> musiquesPubliques = new ArrayList<Musique>();
    String fluxJSON = "";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MusiquePubliqueDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            idMusique = getArguments().getString(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            new GGDownloadTask().execute("test");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.musique_detail, container, false);

        // Show the dummy content as text in a TextView.
        /*if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.musique_detail)).setText(mItem.details);
        }*/

        txtArtiste = (TextView) rootView.findViewById(R.id.txtTitre);
        txtTitre = (TextView) rootView.findViewById(R.id.txtArtiste);
        txtProprietaire = (TextView) rootView.findViewById(R.id.txtProprietaire);
        toggleActive = (ToggleButton) rootView.findViewById(R.id.toggleActive);
        togglePublique = (ToggleButton) rootView.findViewById(R.id.togglePublique);
        videoMusique = (VideoView) rootView.findViewById(R.id.videoMusique);

        //new GGDownloadTask().execute("test");
        Log.i("MusiqueDetail", (musique!=null)+"");
        /*if(musique!=null){

            ((TextView) rootView.findViewById(R.id.txtTitre)).setText(musique.getTitre());
            ((TextView) rootView.findViewById(R.id.txtArtiste)).setText(musique.getArtiste());
            ((TextView) rootView.findViewById(R.id.txtProprietaire)).setText("Propriétaire: " + musique.getProprietaire());
            ((ToggleButton) rootView.findViewById(R.id.togglePublique)).setChecked(musique.isPublique());
            ((ToggleButton) rootView.findViewById(R.id.toggleActive)).setChecked(musique.isActive());
        }*/

        Log.i("MusiqueDetail", idMusique);

        return rootView;
    }

    /*int id;
            int proprietaire;
            String titre;
            String artiste;
            String musique;
            String vignette;
            boolean publique;
            boolean active;
            Date date;*/

    public void loadData(){
        Log.i("LoadData","inMethod");
        try {
            JSONObject jObj = new JSONObject(fluxJSON);

            musique = new Musique();
            musique.setId(jObj.getInt("id"));
            musique.setProprietaire(jObj.getInt("proprietaire"));
            musique.setTitre(jObj.getString("titre"));
            musique.setArtiste(jObj.getString("artiste"));
            musique.setMusique(jObj.getString("musique"));
            musique.setVignette(jObj.getString("vignette"));
            musique.setPublique(jObj.getBoolean("publique"));
            musique.setActive(jObj.getBoolean("active"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(musique!=null){

            txtTitre.setText(musique.getTitre());
            txtArtiste.setText(musique.getArtiste());
            txtProprietaire.setText("Propriétaire: " + musique.getProprietaire());
            togglePublique.setChecked(musique.isPublique());
            toggleActive.setChecked(musique.isActive());
            videoMusique.setVideoURI(Uri.parse(musique.getMusique()));
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
            //fillList();
            //View recyclerView = findViewById(R.id.musique_list);
            //assert recyclerView != null;
            //setupRecyclerView((RecyclerView) recyclerView);
            loadData();
            Log.i("JSON",fluxJSON);
        }

        public String getJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                URL u = new URL(url+idMusique);
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
