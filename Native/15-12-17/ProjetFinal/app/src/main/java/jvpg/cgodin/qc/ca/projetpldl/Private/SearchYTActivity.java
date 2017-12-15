/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jvpg.cgodin.qc.ca.projetpldl.Private;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jvpg.cgodin.qc.ca.projetpldl.R;
import jvpg.cgodin.qc.ca.projetpldl.entities.SearchBarItem;


/**
 * Simple one-activity app that takes a search term via the Action Bar
 * and uses it as a query to search the contacts database via the Contactables
 * table.
 */
public class SearchYTActivity extends AppCompatActivity {

    List<SearchBarItem> lstSearchBarItem = new ArrayList<SearchBarItem>();
    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/creerMusique";
    //String youtubeUrl ="https://www.googleapis.com/youtube/v3/search AIzaSyCWK6nOn8VzfC5B51wwkv3ZXqLsl8gVTzc ";
    String youtubeKey = "AIzaSyCWK6nOn8VzfC5B51wwkv3ZXqLsl8gVTzc";
    String youtubeUrl = "https://www.googleapis.com/youtube/v3/search?";
    //part=snippet&q=eminem&type=video&key=<key>
    String imgUrl;
    String fluxJSON = "";
    String motRechercher;

    Context context ;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Bitmap bitmap;
    private List<String> mURLs = new LinkedList<String>();
    int compteurTempo=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbar_list);

        context=this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.searchbar, menu);
        Toast.makeText(SearchYTActivity.this, "Entrez ce que vous chercher dans la barre de recherche...", Toast.LENGTH_SHORT).show();

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                              @Override
                                              public boolean onQueryTextSubmit(String query) {
                                                  Toast.makeText(SearchYTActivity.this, query, Toast.LENGTH_SHORT).show();
                                                  motRechercher = query;
                                                  new GGDownloadTask().execute("test");


                                                  recyclerView = (RecyclerView) findViewById(R.id.searchbar_list);
                                                  layoutManager = new LinearLayoutManager(context);
                                                  recyclerView.setLayoutManager(layoutManager);

                                                  adapter = new PlanetAdapter(lstSearchBarItem,getApplicationContext());
                                                  recyclerView.setAdapter(adapter);

                                                  return true;
                                              }
                                              @Override
                                              public boolean onQueryTextChange(String newText) {
                                                  //Toast.makeText(MainActivity2.this, newText, Toast.LENGTH_SHORT).show();
                                                  return false;
                                              }
                                          }
        );
        return true;
    }


    public void fillList(){

        lstSearchBarItem = new ArrayList<SearchBarItem>();

        JSONObject responseJSON= null;
        try {
            responseJSON = new JSONObject(fluxJSON);

            JSONArray items = responseJSON.getJSONArray("items");


            for(int i=0;i<items.length();i++){

                SearchBarItem s = new SearchBarItem();
                //s.setMusique("https://www.youtube.com/watch?v="+items.getJSONObject(i).getJSONObject("id").getString("videoId"));
                //OUUUUUUUUU
                s.setMusique("http://www.youtube.com/embed/"+items.getJSONObject(i).getJSONObject("id").getString("videoId"));
                s.setTitre(items.getJSONObject(i).getJSONObject("snippet").get("title")+"");
                s.setVignette(items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url"));
                s.setVignetteEncoded("null");
                //Toast.makeText(MainActivity2.this, "Item "+i+" : ", Toast.LENGTH_SHORT).show();
                // Toast.makeText(MainActivity2.this, "Title : "+items.getJSONObject(i).getJSONObject("snippet").get("title"), Toast.LENGTH_SHORT).show();
                // Toast.makeText(MainActivity2.this, "Description : "+items.getJSONObject(i).getJSONObject("snippet").get("description"), Toast.LENGTH_SHORT).show();
                // Toast.makeText(MainActivity2.this, "thumbnails : "+items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url"), Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity2.this, "URL : https://www.youtube.com/watch?v="+items.getJSONObject(i).getJSONObject("id").getString("videoId"), Toast.LENGTH_SHORT).show();
                mURLs.add(items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url")+"");
                lstSearchBarItem.add(s);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loadNext();

        adapter.notifyDataSetChanged();
        recyclerView = (RecyclerView) findViewById(R.id.searchbar_list);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlanetAdapter(lstSearchBarItem,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
    private void loadNext() {

        if(mURLs.size()!=0)
        {
            String url = mURLs.remove(mURLs.size()-1);
            if (url != null) {
                new LoadImage().execute(url);
            }
        }
    }


    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                //img.setImageBitmap(image);

                SearchBarItem s = new SearchBarItem();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                //Toast.makeText(MainActivity2.this, encodedImage, Toast.LENGTH_SHORT).show();
                lstSearchBarItem.get((lstSearchBarItem.size()-1)-compteurTempo).setVignetteEncoded(encodedImage);
                compteurTempo++;
                if(compteurTempo==lstSearchBarItem.size())
                {
                    compteurTempo=0;
                }

            }else{

                Toast.makeText(SearchYTActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
            loadNext();
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
            resultString = getYoutubeJSON();
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

        public String getYoutubeJSON() {
            HttpURLConnection c = null;
            String resultat = "";
            try {
                String motRechercherTempo=motRechercher;

                try {
                    motRechercherTempo = URLEncoder.encode(motRechercherTempo, "UTF-8")+"";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                URL u = new URL(youtubeUrl + "part=snippet&maxResults=20&q="+motRechercherTempo+"&type=video&key="+youtubeKey);
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

        List<SearchBarItem> planetList;

        public PlanetAdapter(List<SearchBarItem> planetList, Context context) {
            this.planetList = planetList;
        }

        @Override
        public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.searchbar_list_content,parent,false);
            PlanetViewHolder viewHolder=new PlanetViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PlanetViewHolder holder, int position) {


            byte[] decodedString = Base64.decode(lstSearchBarItem.get(position).getVignetteEncoded(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.ivVignette.setImageBitmap(decodedByte);

            holder.tvTitre.setText(lstSearchBarItem.get(position).getTitre());

            holder.btnAjouter.setTag(lstSearchBarItem.get(position));
            holder.btnAjouter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AddMusiqueActivity.class);
                    SearchBarItem s = (SearchBarItem)v.getTag();
                    intent.putExtra("titre",s.getTitre());
                    intent.putExtra("musique",s.getMusique());
                    intent.putExtra("vignette",s.getVignette());
                    intent.putExtra("vignetteEncoded",s.getVignetteEncoded());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return planetList.size();
        }

        public  class PlanetViewHolder extends RecyclerView.ViewHolder{

            protected ImageView ivVignette;
            protected TextView tvTitre;
            protected Button btnAjouter;


            public PlanetViewHolder(View itemView) {
                super(itemView);
                ivVignette= (ImageView) itemView.findViewById(R.id.ivVignette);
                tvTitre= (TextView) itemView.findViewById(R.id.tvTitre);
                btnAjouter= (Button) itemView.findViewById(R.id.btnAjouter);
            }
        }
    }
}