package jvpg.cgodin.qc.ca.projetpldl.Public;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

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

import jvpg.cgodin.qc.ca.projetpldl.Config;
import jvpg.cgodin.qc.ca.projetpldl.Private.ChooseListeActivity;
import jvpg.cgodin.qc.ca.projetpldl.Private.ListeUtilContentListActivity;
import jvpg.cgodin.qc.ca.projetpldl.Private.ModifierMusiqueActivity;
import jvpg.cgodin.qc.ca.projetpldl.Private.MusiquesUtilListActivity;
import jvpg.cgodin.qc.ca.projetpldl.R;
import jvpg.cgodin.qc.ca.projetpldl.Test;
import jvpg.cgodin.qc.ca.projetpldl.dummy.DummyContent;
import jvpg.cgodin.qc.ca.projetpldl.entities.Musique;

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

/**
 * A fragment representing a single Musique detail screen.
 * This fragment is either contained in a {@link MusiquePubliqueListActivity}
 * in two-pane mode (on tablets) or a {@link MusiquePubliqueDetailActivity}
 * on handsets.
 */


public class MusiquePubliqueDetailFragment extends Fragment {



    public static final String ARG_ITEM_ID = "item_id";

    public static final String ARG_ACTION_PUBLIC = "PUBLIC";
    public static final String ARG_ACTION_UTIL = "UTIL";
    public static final String ARG_ACTION_LIST = "LIST";


    public static final String ARG_ACTION_NAME = "action_name";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;
    private Musique musique;
    private String idMusique;
    private String action;

    TextView txtTitre;
    TextView txtArtiste;
    TextView txtProprietaire;
    ImageView imgVignette;
    YouTubePlayerSupportFragment mYoutubePlayerFragment;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    private String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/musiquePublique/";
    String urlTicket = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String params = "";
    String paramsService = "";
    String fluxJSONTicket = "";
    String fluxJSON = "";

    int cle;
    int noTicket;
    String chaineConfirmation;


    Button btnEdit;
    Button btnAddToPlaylist;
    Button btnRemoveFromPlaylist;
    Button btnCopyToList;
    Button btnTransferToList;

    LinearLayout lnPlaylistActions;
    LinearLayout lnModifications;


    private String urlDEL = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecturemusiques/supprimerMusiqueALaListe/";
    String urlTicketDEL = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String paramsDEL = "";
    String paramsServiceDEL = "";
    String fluxJSONTicketDEL = "";
    String fluxJSONDEL = "";

    int cleDEL;
    int noTicketDEL;
    String chaineConfirmationDEL;

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

            action = getArguments().getString(ARG_ACTION_NAME);
            Log.i("Action",action);
            Activity activity = this.getActivity();

            switch(action){
                case ARG_ACTION_UTIL:
                    doUtilAction();
                    break;
                case ARG_ACTION_PUBLIC:
                    doPublicAction();
                    break;
                case ARG_ACTION_LIST:
                    doListAction();
                    break;
            }


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

        txtArtiste = (TextView) rootView.findViewById(R.id.txtArtiste);
        txtTitre = (TextView) rootView.findViewById(R.id.txtTitre);
        txtProprietaire = (TextView) rootView.findViewById(R.id.txtProprietaire);
        imgVignette = (ImageView) rootView.findViewById(R.id.imgVignette);
        //videoYoutubeMusique = (YouTubePlayerView) rootView.findViewById(R.id.videoYoutubeMusique);

        btnEdit = (Button) rootView.findViewById(R.id.btnEdit);
        btnAddToPlaylist = (Button) rootView.findViewById(R.id.btnAddToPlaylist);
        btnRemoveFromPlaylist = (Button) rootView.findViewById(R.id.btnRemoveFromPlaylist);
        btnCopyToList = (Button) rootView.findViewById(R.id.btnCopyToList);
        btnTransferToList = (Button) rootView.findViewById(R.id.btnTransferToList);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ModifierMusiqueActivity.class);
                intent.putExtra("idMusique", musique.getId());
                startActivity(intent);
            }
        });

        btnAddToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChooseListeActivity.class);
                intent.putExtra("idMusique", musique.getId());
                intent.putExtra(ChooseListeActivity.ARG_ACTION_NAME, ChooseListeActivity.ARG_ACTION_ADD);
                startActivity(intent);
            }
        });

        btnRemoveFromPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Retirer la musique de la liste");
                builder.setMessage("Êtes-vous sûr(e) de vouloir retirer " + txtTitre.getText().toString() +" de la liste de lecture?");

                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        new GGDownloadTaskTicketDEL().execute("test");
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {

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

        btnCopyToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChooseListeActivity.class);
                intent.putExtra("idMusique", musique.getId());
                intent.putExtra(ChooseListeActivity.ARG_ACTION_NAME, ChooseListeActivity.ARG_ACTION_COPY);
                startActivity(intent);
            }
        });

        btnTransferToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChooseListeActivity.class);
                intent.putExtra("idMusique", musique.getId());
                intent.putExtra(ChooseListeActivity.ARG_ACTION_NAME, ChooseListeActivity.ARG_ACTION_TRANSFER);
                startActivity(intent);
            }
        });



        lnPlaylistActions = (LinearLayout) rootView.findViewById(R.id.lnPlaylistActions);
        lnModifications = (LinearLayout) rootView.findViewById(R.id.lnModifications);

        mYoutubePlayerFragment = new YouTubePlayerSupportFragment();
        //mYoutubePlayerFragment.initialize(youtubeKey, this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_youtube_player, mYoutubePlayerFragment);
        fragmentTransaction.commit();

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

            byte[] decodedString = Base64.decode(musique.getVignette(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgVignette.setImageBitmap(decodedByte);

            onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.loadVideo(musique.getMusique().substring(musique.getMusique().lastIndexOf("/") + 1));
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            };

            mYoutubePlayerFragment.initialize(Config.YOUTUBE_API_KEY,onInitializedListener);

            switch(action){
                case ARG_ACTION_UTIL:
                    btnAddToPlaylist.setEnabled(true);
                    lnPlaylistActions.setVisibility(View.INVISIBLE);
                    lnModifications.setVisibility(View.VISIBLE);
                    break;
                case ARG_ACTION_PUBLIC:
                    if(utilConnecte == null) {
                        btnAddToPlaylist.setEnabled(false);
                        lnModifications.setVisibility(View.INVISIBLE);
                    }
                    else {
                        btnAddToPlaylist.setEnabled(true);

                        if(musique.getProprietaire() == utilConnecte.getId())
                            lnModifications.setVisibility(View.VISIBLE);
                        else
                            lnModifications.setVisibility(View.INVISIBLE);
                    }

                    lnPlaylistActions.setVisibility(View.INVISIBLE);
                    break;
                case ARG_ACTION_LIST:
                    btnAddToPlaylist.setEnabled(true);
                    lnPlaylistActions.setVisibility(View.VISIBLE);

                    if(musique.getProprietaire() == utilConnecte.getId())
                        lnModifications.setVisibility(View.VISIBLE);
                    else
                        lnModifications.setVisibility(View.INVISIBLE);
                    break;
            }
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
            //params = loginCourriel.getText().toString() + "/" + loginMotDePasse.getText().toString();
            paramsService = action.equals(ARG_ACTION_PUBLIC) ? idMusique : noTicket + "/" + chaineConfirmation + "/" + utilConnecte.getId() + "/" + idMusique;

        }

        //cette méthode prend en argument un tableau illimité de chaines de caractères
        @Override
        protected String doInBackground(String... params) {
            String resultString = null;
            resultString = getJSON();
            fluxJSON = resultString;

            Log.i("ParamsService",paramsService);
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
                URL u = new URL(url+paramsService);
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

    private void doPublicAction(){
        url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/musiquePublique/";

        new GGDownloadTask().execute("test");
    }

    private void doListAction(){
        url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/listesdelecture/consulterMusiqueDansListe/";

        new GGDownloadTaskTicket().execute("test");
    }

    private void doUtilAction(){
        //{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}
        url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/consulterMusique/";

        new GGDownloadTaskTicket().execute("test");
    }


    public void useTicketDEL(){
        try {
            JSONObject jObj = new JSONObject(fluxJSONTicketDEL);

            cleDEL = jObj.getInt("cle");
            noTicketDEL = jObj.getInt("noTicket");

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
                new GGDownloadTaskDEL().execute("test");

            }
            else{
                Toast.makeText(getContext(), "Impossible de générer un ticket", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GGDownloadTaskTicketDEL extends AsyncTask<String, Integer, String> {

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
            useTicketDEL();
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


    public class GGDownloadTaskDEL extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //paramsDEL = loginCourriel.getText().toString() + "/" + loginMotDePasse.getText().toString();
            paramsServiceDEL = noTicketDEL + "/" + chaineConfirmationDEL + "/" + utilConnecte.getId() + "/" + idMusique + "/" + ListeUtilContentListActivity.idListe;

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

            Log.i("JSONDEL", fluxJSONDEL);
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

    }
}