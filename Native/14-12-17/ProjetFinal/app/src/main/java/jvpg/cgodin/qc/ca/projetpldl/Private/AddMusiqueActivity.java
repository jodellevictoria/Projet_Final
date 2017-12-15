package jvpg.cgodin.qc.ca.projetpldl.Private;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jvpg.cgodin.qc.ca.projetpldl.ModifierUtilisateurActivity;
import jvpg.cgodin.qc.ca.projetpldl.R;

import static jvpg.cgodin.qc.ca.projetpldl.MainActivity.utilConnecte;

public class AddMusiqueActivity extends AppCompatActivity {

    String url = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/musiques/creerMusique/";
    String urlTicket = "http://424v.cgodin.qc.ca:8086/ProjetPLDL/webresources/utilisateurs/GetTicket/";
    String fluxJSON = "";
    String fluxJSONTicket="";
    int noTicket;
    int cle;
    String chaineConfirmation;
    String params = "";


    ImageView imgVignette;
    TextView txtURL;
    EditText addMusiqueTitre;
    EditText addMusiqueArtiste;
    ToggleButton togglePublique;
    ToggleButton toggleActif;
    Button btnSave;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_musique);

        imgVignette = (ImageView) findViewById(R.id.imgVignette);
        txtURL = (TextView) findViewById(R.id.txtURL);
        addMusiqueTitre = (EditText) findViewById(R.id.addMusiqueTitre);
        addMusiqueArtiste = (EditText) findViewById(R.id.addMusiqueArtiste);
        togglePublique = (ToggleButton) findViewById(R.id.togglePublique);
        toggleActif = (ToggleButton) findViewById(R.id.toggleActif);
        btnSave = (Button) findViewById(R.id.btnSave);

        byte[] decodedString = Base64.decode(getIntent().getStringExtra("vignetteEncoded"), Base64.DEFAULT);
        Log.e("biggggggggggggg",getIntent().getStringExtra("vignette")+"");
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imgVignette.setImageBitmap(decodedByte);

        txtURL.setText((getIntent().getStringExtra("musique")));
        addMusiqueTitre.setText((getIntent().getStringExtra("titre")));


        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglePublique);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new LoginActivity.GGDownloadTask().execute("test");
                new GGDownloadTaskTicket().execute("test");
                ///Log.i("LoginActivity!",fluxJSON);
            }
        });


    }

    public void doAction(){
        Toast.makeText(AddMusiqueActivity.this,fluxJSON , Toast.LENGTH_SHORT).show();
    }

    public class GGDownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*ImageView imgVignette;
            TextView txtURL;
            EditText addMusiqueTitre;
            EditText addMusiqueArtiste;
            ToggleButton togglePublique;
            ToggleButton toggleActif;
            Button btnSave;*/


            String titreTempo=addMusiqueTitre.getText().toString();
            String artisteTempo=addMusiqueArtiste.getText().toString();
            String txtURLTempo=txtURL.getText().toString();
            String vignetteTempo=getIntent().getStringExtra("vignette").toString();


            try {
                titreTempo = URLEncoder.encode(titreTempo, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                artisteTempo = URLEncoder.encode(artisteTempo, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                txtURLTempo = URLEncoder.encode(txtURLTempo, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                vignetteTempo = URLEncoder.encode(vignetteTempo, "UTF-8")+"";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }





            /*vignetteTempo = vignetteTempo.replaceAll("\\+", "Password1");
            vignetteTempo = vignetteTempo.replaceAll("/", "Password2");
            vignetteTempo = vignetteTempo.replaceAll("=", "Password3");
            vignetteTempo = vignetteTempo.replaceAll(" ", "");*/

            String lol = "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword22wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB" +
                    "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword2wAARCABaAHgDASIA" +
                    "AhEBAxEBPassword28QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoLPassword28QAtRAAAgEDAwIEAwUFBAQA" +
                    "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3" +
                    "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm" +
                    "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4Password1Tl5ufo6erx8vP09fb3Password1Pn6Password28QAHwEA" +
                    "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoLPassword28QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx" +
                    "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK" +
                    "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3" +
                    "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3Password1Pn6Password29oADAMBAAIRAxEAPwDPassword1ATae" +
                    "53Y6DGM8nvn0wf06k00ckh224Password22c5656H2H5Password1xr7muNC0Password14BPassword1zhUbgY8tgDye2Password2rgDGf5Fs4lx4a" +
                    "kRdPassword2lIRzjMWM4yO7gdvXPucgn8Password2o8d4KV08FKLbS9Password1qr297ZOlG2ze17u17Nn9W4v6KHEOFg60eJ" +
                    "Fjl3hkju7Sd988vomrXbvdJtMPassword1M9jen6jPassword2GjY3pPassword1oPassword2xr6wltTECjQx5HZoPassword1evUbmPfrgexyOaoGz" +
                    "hHXbPassword2wB8enPassword2Av8Password19dq4rw8ldYOXTX23Password2ANoPassword1zWvlu3r8vVPassword1j1jKVPassword2wDjJE7Wu1ktrN3taPassword1dOPassword134r" +
                    "VtmnPassword1zX4BPassword2ZP8bQPassword1Pv8AhpX47eJfg7Lo0fhPassword1fwTF4c8C634tPassword2wCEwt2Password24SWTxHYFtO0jU10rUrf7" +
                    "LoMGjSaoEgmfVZpTmPTpAfqBfgTPassword2AMEpZZ7OOT9uL4oJbvDPqd1Mfgl4uDW9oTZx2HhdRPassword2wgmW8R" +
                    "PLqDySavhvD66Zo2sDcrT6EHPassword1N7jRbZlkkjijRPassword1OkPYls9DnoBjn6EkNnmZreOEGPy0wDwMHuTnn" +
                    "cc53ceg3ZyQM9tDiGjWvy4VPVfFJud7zPassword1T2tePassword1nuq7adPassword2lsf4N5nljl9YzhNe44tU1bVyVlrb7PW" +
                    "7bbu24uPassword23xpHwoPassword24Jc6p8Cvh9pnin9pbUvCPassword2xq0rQfifdPassword1PfEfhTwd8bdZ0zXvEKPassword1KvE4Password1GWnjTt" +
                    "ePassword1FK6YNPPassword2wCEbHhRblNIGlEyPq5uNfNyu6tzSPhPPassword2wAEirPRPassword2E9lePassword2tReN9Uu9X8XaJpGgPassword1INTPassword1H" +
                    "nxWPassword2tTwh4LXwpqp1rXbHRNA8A6RpXiTVD4iurQo2rkfLZ6ZDH4fFuuva3qH52tYQOrsEjDjHPle5" +
                    "JG3JA6rPassword1J7nFc3NbRRfuthDgdTk92PTJzjA5z3HHUnoo5tCq2lFqyV7JqPassword1sl6293R67rS92eRjvD" +
                    "qvgE39d003T97e1nqtbW0d073vbX9DbD4If8Eq1ttCl1r9sT4jLd3cPassword1qw63p2gPassword1BfF2o22g2lidT" +
                    "1DT7xtc1L4C6PPassword2aP9qrBYeFV8vR1dNbvB4k3f8I6n9h1y3gf4GPassword28E4rjRLBfiHPassword12b4ittaGmatLq" +
                    "l34TPassword1GnxJbTBqi65qB0SzsrDVPg08mB4dhsWlLaxjWNc1HVlzo0XheJfEPwJcWUstrdXcUBa2tZb" +
                    "SGecRkPassword1SbzeLMMMn7Password22NwOe5GD1PZeEfB3h7WPDHiPassword2xHrPassword2jDQvDp0zTifD2nTym41fXvEB1HQf8A" +
                    "QBomnudSsdMbTrPassword1Password2lOsvGujq9oVycMx9COITjzON9rauy1ktLNLaO26uPassword1kW38dicnWGrexeLXSza" +
                    "s38VtG3dPassword2PrZttKPassword2cPassword2tIPassword1EP2avC2qPassword1D1Password2ZsPassword1Jvib4j6Rqfhv7X4vHinQb3SdQ8MeJPassword2t8obRkebwv" +
                    "4UTUYPassword2soDefHpYUNBKQ5Vkr5n2N6fqP8a6m0gjcMChycc5b1OMYORnjvPassword1JJOOtstO89EjjT68E55" +
                    "kGfvAjPassword1XXJzzXJUzGnh1K8ZWWPassword2vWWml9W7LT5bNXtKXvZVwVXzS6w2Mj07tdU3u2lovRN3fuyZ5Z" +
                    "kDgNjHXgnufUPassword15796CZD3zjocKM19Gab4c8yNmMewDHWEnuwPassword1pGRkc9zzgE11V74QvNO03TdSeKw" +
                    "e2vYSYfIvtGurv8A4Password2tT08f2hp9hIdR03P8AZ95Password2yFgCQByeCfKqcTUIyaWGbatrzu2vMtH7Nr7P" +
                    "ePassword1107tr7nCPassword1CeOq0XXrZu4rssmqOyTlq1Kqu11ZXd31TPkcgdCPassword2TPassword2Zor6Rk0d5GbMCEk9omGcFPassword1c" +
                    "KRjoOPassword2TPJIYEqv8AWehPassword2z6l6Password129P7n5Password1l92tF4JZhLWOcNx6PPassword1xa2qvJXPassword2jLovz1s7n2JJ8NbmJD" +
                    "5YJ9MkE8lhjlycHqTnPassword16OOtYx8E6ys6wQxyOZJhDwF5O7A43HtzjnoeCcgfTa6RfKCNnz8c8DnLZ" +
                    "zjr27Password1gyMVe0vTbzPassword10NP863Password2AOXyzPassword2SPassword2bjrPassword2AFJPassword17kg1Password2JdDiPassword1suZyxNJ6JXlyyWnNrdSvdrXta1" +
                    "9YuPassword2Password2QlmfgBwPassword2VoN4LB5zl70T3sldrRuNPassword12i20SsuZHzPasswordAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword22wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB" +
                    "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword2wAARCABaAHgDASIA" +
                    "AhEBAxEBPassword28QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoLPassword28QAtRAAAgEDAwIEAwUFBAQA" +
                    "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3" +
                    "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm" +
                    "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4Password1Tl5ufo6erx8vP09fb3Password1Pn6Password28QAHwEA" +
                    "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoLPassword28QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx" +
                    "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSEl" +
                    "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3" +
                    "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3Password1Pn6Password29oADAMBAAIRAxEAPwDPassword1ATae" +
                    "53Y6DGM8nvn0wf06k00ckh224Password22c5656H2H5Password1xr7muNC0Password14BPassword1zhUbgY8tgDye2Password2rgDGf5Fs4lx4a" +
                    "kRdPassword2lIRzjMWM4yO7gdvXPucgn8Password2o8d4KV08FKLbS9Password1qr297ZOlG2ze17u17Nn9W4v6KHEOFg60eJ" +
                    "Fjl3hkju7Sd988vomrXbvdJtMPassword1M9jen6jPassword2GjY3pPassword1oPassword2xr6wltTECjQx5HZoPassword1evUbmPfrgexyOaoGz" +
                    "hHXbPassword2wB8enPassword2Av8Password19dq4rw8ldYOXTX23Password2ANoPassword1zWvlu3r8vVPassword1j1jKVPassword2wDjJE7Wu1ktrN3taPassword1dOPassword134r" +
                    "VtmnPassword1zX4BPassword2ZP8bQPassword1Pv8AhpX47eJfg7Lo0fhPassword1fwTF4c8C634tPassword2wCEwt2Password24SWTxHYFtO0jU10rUrf7" +
                    "LoMGjSaoEgmfVZpTmPTpAfqBfgTPassword2AMEpZZ7OOT9uL4oJbvDPqd1Mfgl4uDW9oTZx2HhdRPassword2wgmW8R" +
                    "PLqDySavhvD66Zo2sDcrT6EHPassword1N7jRbZlkkjijRPassword1OkPYls9DnoBjn6EkNnmZreOEGPy0wDwMHuTnn" +
                    "cc53ceg3ZyQM9tDiGjWvy4VPVfFJud7zPassword1T2tePassword1nuq7adPassword2lsf4N5nljl9YzhNe44tU1bVyVlrb7PW" +
                    "7bbu24uPassword23xpHwoPassword24Jc6p8Cvh9pnin9pbUvCPassword2xq0rQfifdPassword1PfEfhTwd8bdZ0zXvEKPassword1KvE4Password1GWnjTt" +
                    "ePassword1FK6YNPPassword2wCEbHhRblNIGlEyPq5uNfNyu6tzSPhPPassword2wAEirPRPassword2E9lePassword2tReN9Uu9X8XaJpGgPassword1INTPassword1H" +
                    "nxWPassword2tTwh4LXwpqp1rXbHRNA8A6RpXiTVD4iurQo2rkfLZ6ZDH4fFuuva3qH52tYQOrsEjDjHPle5" +
                    "JG3JA6rPassword1J7nFc3NbRRfuthDgdTk92PTJzjA5z3HHUnoo5tCq2lFqyV7JqPassword1sl6293R67rS92eRjvD" +
                    "qvgE39d003T97e1nqtbW0d073vbX9DbD4If8Eq1ttCl1r9sT4jLd3cPassword1qw63p2gPassword1BfF2o22g2lidT" +
                    "1DT7xtc1L4C6PPassword2aP9qrBYeFV8vR1dNbvB4k3f8I6n9h1y3gf4GPassword28E4rjRLBfiHPassword12b4ittaGmatLq" +
                    "l34TPassword1GnxJbTBqi65qB0SzsrDVPg08mB4dhsWlLaxjWNc1HVlzo0XheJfEPwJcWUstrdXcUBa2tZb" +
                    "SGecRkPassword1SbzeLMMMn7Password22NwOe5GD1PZeEfB3h7WPDHiPassword2xHrPassword2jDQvDp0zTifD2nTym41fXvEB1HQf8A" +
                    "QBomnudSsdMbTrPassword1Password2lOsvGujq9oVycMx9COITjzON9rauy1ktLNLaO26uPassword1kW38dicnWGrexeLXSza" +
                    "s38VtG3dPassword2PrZttKPassword2cPassword2tIPassword1EP2avC2qPassword1D1Password2ZsPassword1Jvib4j6Rqfhv7X4vHinQb3SdQ8MeJPassword2t8obRkebwv" +
                    "4UTUYPassword2soDefHpYUNBKQ5Vkr5n2N6fqP8a6m0gjcMChycc5b1OMYORnjvPassword1JJOOtstO89EjjT68E55" +
                    "kGfvAjPassword1XXJzzXJUzGnh1K8ZWWPassword2vWWml9W7LT5bNXtKXvZVwVXzS6w2Mj07tdU3u2lovRN3fuyZ5Z" +
                    "kDgNjHXgnufUPassword15796CZD3zjocKM19Gab4c8yNmMewDHWEnuwPassword1pGRkc9zzgE11V74QvNO03TdSeKw" +
                    "e2vYSYfIvtGurv8A4Password2tT08f2hp9hIdR03P8AZ95Password2yFgCQByeCfKqcTUIyaWGbatrzu2vMtH7Nr7P" +
                    "ePassword1107tr7nCPassword1CeOq0XXrZu4rssmqOyTlq1Kqu11ZXd31TPkcgdCPassword2TPassword2Zor6Rk0d5GbMCEk9omGcFPassword1c" +
                    "KRjoOPassword2TPJIYEqv8AWehPassword2z6l6Password129P7n5Password1l92tF4JZhLWOcNx6PPassword1xa2qvJXPassword2jLovz1s7n2JJ8NbmJD" +
                    "5YJ9MkE8lhjlycHqTnPassword16OOtYx8E6ys6wQxyOZJhDwF5O7A43HtzjnoeCcgfTa6RfKCNnz8c8DnLZ" +
                    "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword22wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB" +
                    "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword2wAARCABaAHgDASIA" +
                    "AhEBAxEBPassword28QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoLPassword28QAtRAAAgEDAwIEAwUFBAQA" +
                    "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3" +
                    "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm" +
                    "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4Password1Tl5ufo6erx8vP09fb3Password1Pn6Password28QAHwEA" +
                    "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoLPassword28QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx" +
                    "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK" +
                    "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3" +
                    "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3Password1Pn6Password29oADAMBAAIRAxEAPwDPassword1ATae" +
                    "53Y6DGM8nvn0wf06k00ckh224Password22c5656H2H5Password1xr7muNC0Password14BPassword1zhUbgY8tgDye2Password2rgDGf5Fs4lx4a" +
                    "kRdPassword2lIRzjMWM4yO7gdvXPucgn8Password2o8d4KV08FKLbS9Password1qr297ZOlG2ze17u17Nn9W4v6KHEOFg60eJ" +
                    "Fjl3hkju7Sd988vomrXbvdJtMPassword1M9jen6jPassword2GjY3pPassword1oPassword2xr6wltTECjQx5HZoPassword1evUbmPfrgexyOaoGz" +
                    "hHXbPassword2wB8enPassword2Av8Password19dq4rw8ldYOXTX23Password2ANoPassword1zWvlu3r8vVPassword1j1jKVPassword2wDjJE7Wu1ktrN3taPassword1dOPassword134r" +
                    "VtmnPassword1zX4BPassword2ZP8bQPassword1Pv8AhpX47eJfg7Lo0fhPassword1fwTF4c8C634tPassword2wCEwt2Password24SWTxHYFtO0jU10rUrf7" +
                    "LoMGjSaoEgmfVZpTmPTpAfqBfgTPassword2AMEpZZ7OOT9uL4oJbvDPqd1Mfgl4uDW9oTZx2HhdRPassword2wgmW8R" +
                    "PLqDySavhvD66Zo2sDcrT6EHPassword1N7jRbZlkkjijRPassword1OkPYls9DnoBjn6EkNnmZreOEGPy0wDwMHuTnn" +
                    "cc53ceg3ZyQM9tDiGjWvy4VPVfFJud7zPassword1T2tePassword1nuq7adPassword2lsf4N5nljl9YzhNe44tU1bVyVlrb7PW" +
                    "7bbu24uPassword23xpHwoPassword24Jc6p8Cvh9pnin9pbUvCPassword2xq0rQfifdPassword1PfEfhTwd8bdZ0zXvEKPassword1KvE4Password1GWnjTt" +
                    "ePassword1FK6YNPPassword2wCEbHhRblNIGlEyPq5uNfNyu6tzSPhPPassword2wAEirPRPassword2E9lePassword2tReN9Uu9X8XaJpGgPassword1INTPassword1H" +
                    "nxWPassword2tTwh4LXwpqp1rXbHRNA8A6RpXiTVD4iurQo2rkfLZ6ZDH4fFuuva3qH52tYQOrsEjDjHPle5" +
                    "JG3JA6rPassword1J7nFc3NbRRfuthDgdTk92PTJzjA5z3HHUnoo5tCq2lFqyV7JqPassword1sl6293R67rS92eRjvD" +
                    "qvgE39d003T97e1nqtbW0d073vbX9DbD4If8Eq1ttCl1r9sT4jLd3cPassword1qw63p2gPassword1BfF2o22g2lidT" +
                    "1DT7xtc1L4C6PPassword2aP9qrBYeFV8vR1dNbvB4k3f8I6n9h1y3gf4GPassword28E4rjRLBfiHPassword12b4ittaGmatLq" +
                    "l34TPassword1GnxJbTBqi65qB0SzsrDVPg08mB4dhsWlLaxjWNc1HVlzo0XheJfEPwJcWUstrdXcUBa2tZb" +
                    "SGecRkPassword1SbzeLMMMn7Password22NwOe5GD1PZeEfB3h7WPDHiPassword2xHrPassword2jDQvDp0zTifD2nTym41fXvEB1HQf8A" +
                    "QBomnudSsdMbTrPassword1Password2lOsvGujq9oVycMx9COITjzON9rauy1ktLNLaO26uPassword1kW38dicnWGrexeLXSza" +
                    "s38VtG3dPassword2PrZttKPassword2cPassword2tIPassword1EP2avC2qPassword1D1Password2ZsPassword1Jvib4j6Rqfhv7X4vHinQb3SdQ8MeJPassword2t8obRkebwv" +
                    "4UTUYPassword2soDefHpYUNBKQ5Vkr5n2N6fqP8a6m0gjcMChycc5b1OMYORnjvPassword1JJOOtstO89EjjT68E55" +
                    "kGfvAjPassword1XXJzzXJUzGnh1K8ZWWPassword2vWWml9W7LT5bNXtKXvZVwVXzS6w2Mj07tdU3u2lovRN3fuyZ5Z" +
                    "kDgNjHXgnufUPassword15796CZD3zjocKM19Gab4c8yNmMewDHWEnuwPassword1pGRkc9zzgE11V74QvNO03TdSeKw" +
                    "e2vYSYfIvtGurv8A4Password2tT08f2hp9hIdR03P8AZ95Password2yFgCQByeCfKqcTUIyaWGbatrzu2vMtH7Nr7P" +
                    "ePassword1107tr7nCPassword1CeOq0XXrZu4rssmqOyTlq1Kqu11ZXd31TPkcgdCPassword2TPassword2Zor6Rk0d5GbMCEk9omGcFPassword1c" +
                    "KRjoOPassword2TPJIYEqv8AWehPassword2z6l6Password129P7n5Password1l92tF4JZhLWOcNx6PPassword1xa2qvJXPassword2jLovz1s7n2JJ8NbmJD" +
                    "5YJ9MkE8lhjlycHqTnPassword16OOtYx8E6ys6wQxyOZJhDwF5O7A43HtzjnoeCcgfTa6RfKCNnz8c8DnLZ" +
                    "zjr27Password1gyMVe0vTbzPassword10NP863Password2AOXyzPassword2SPassword2bjrPassword2AFJPassword17kg1Password2JdDiPassword1suZyxNJ6JXlyyWnNrdSvdrXta1" +
                    "9YuPassword2Password2QlmfgBwPassword2VoN4LB5zl70T3sldrRuNPassword12i20SsuZHzPasswordAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword22wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB" +
                    "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword2wAARCABaAHgDASIA" +
                    "AhEBAxEBPassword28QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoLPassword28QAtRAAAgEDAwIEAwUFBAQA" +
                    "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3" +
                    "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm" +
                    "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4Password1Tl5ufo6erx8vP09fb3Password1Pn6Password28QAHwEA" +
                    "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoLPassword28QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx" +
                    "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSEl" +
                    "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3" +
                    "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3Password1Pn6Password29oADAMBAAIRAxEAPwDPassword1ATae" +
                    "53Y6DGM8nvn0wf06k00ckh224Password22c5656H2H5Password1xr7muNC0Password14BPassword1zhUbgY8tgDye2Password2rgDGf5Fs4lx4a" +
                    "kRdPassword2lIRzjMWM4yO7gdvXPucgn8Password2o8d4KV08FKLbS9Password1qr297ZOlG2ze17u17Nn9W4v6KHEOFg60eJ" +
                    "Fjl3hkju7Sd988vomrXbvdJtMPassword1M9jen6jPassword2GjY3pPassword1oPassword2xr6wltTECjQx5HZoPassword1evUbmPfrgexyOaoGz" +
                    "hHXbPassword2wB8enPassword2Av8Password19dq4rw8ldYOXTX23Password2ANoPassword1zWvlu3r8vVPassword1j1jKVPassword2wDjJE7Wu1ktrN3taPassword1dOPassword134r" +
                    "VtmnPassword1zX4BPassword2ZP8bQPassword1Pv8AhpX47eJfg7Lo0fhPassword1fwTF4c8C634tPassword2wCEwt2Password24SWTxHYFtO0jU10rUrf7" +
                    "LoMGjSaoEgmfVZpTmPTpAfqBfgTPassword2AMEpZZ7OOT9uL4oJbvDPqd1Mfgl4uDW9oTZx2HhdRPassword2wgmW8R" +
                    "PLqDySavhvD66Zo2sDcrT6EHPassword1N7jRbZlkkjijRPassword1OkPYls9DnoBjn6EkNnmZreOEGPy0wDwMHuTnn" +
                    "cc53ceg3ZyQM9tDiGjWvy4VPVfFJud7zPassword1T2tePassword1nuq7adPassword2lsf4N5nljl9YzhNe44tU1bVyVlrb7PW" +
                    "7bbu24uPassword23xpHwoPassword24Jc6p8Cvh9pnin9pbUvCPassword2xq0rQfifdPassword1PfEfhTwd8bdZ0zXvEKPassword1KvE4Password1GWnjTt" +
                    "ePassword1FK6YNPPassword2wCEbHhRblNIGlEyPq5uNfNyu6tzSPhPPassword2wAEirPRPassword2E9lePassword2tReN9Uu9X8XaJpGgPassword1INTPassword1H" +
                    "nxWPassword2tTwh4LXwpqp1rXbHRNA8A6RpXiTVD4iurQo2rkfLZ6ZDH4fFuuva3qH52tYQOrsEjDjHPle5" +
                    "JG3JA6rPassword1J7nFc3NbRRfuthDgdTk92PTJzjA5z3HHUnoo5tCq2lFqyV7JqPassword1sl6293R67rS92eRjvD" +
                    "qvgE39d003T97e1nqtbW0d073vbX9DbD4If8Eq1ttCl1r9sT4jLd3cPassword1qw63p2gPassword1BfF2o22g2lidT" +
                    "1DT7xtc1L4C6PPassword2aP9qrBYeFV8vR1dNbvB4k3f8I6n9h1y3gf4GPassword28E4rjRLBfiHPassword12b4ittaGmatLq" +
                    "l34TPassword1GnxJbTBqi65qB0SzsrDVPg08mB4dhsWlLaxjWNc1HVlzo0XheJfEPwJcWUstrdXcUBa2tZb" +
                    "SGecRkPassword1SbzeLMMMn7Password22NwOe5GD1PZeEfB3h7WPDHiPassword2xHrPassword2jDQvDp0zTifD2nTym41fXvEB1HQf8A" +
                    "QBomnudSsdMbTrPassword1Password2lOsvGujq9oVycMx9COITjzON9rauy1ktLNLaO26uPassword1kW38dicnWGrexeLXSza" +
                    "s38VtG3dPassword2PrZttKPassword2cPassword2tIPassword1EP2avC2qPassword1D1Password2ZsPassword1Jvib4j6Rqfhv7X4vHinQb3SdQ8MeJPassword2t8obRkebwv" +
                    "4UTUYPassword2soDefHpYUNBKQ5Vkr5n2N6fqP8a6m0gjcMChycc5b1OMYORnjvPassword1JJOOtstO89EjjT68E55" +
                    "kGfvAjPassword1XXJzzXJUzGnh1K8ZWWPassword2vWWml9W7LT5bNXtKXvZVwVXzS6w2Mj07tdU3u2lovRN3fuyZ5Z" +
                    "kDgNjHXgnufUPassword15796CZD3zjocKM19Gab4c8yNmMewDHWEnuwPassword1pGRkc9zzgE11V74QvNO03TdSeKw" +
                    "e2vYSYfIvtGurv8A4Password2tT08f2hp9hIdR03P8AZ95Password2yFgCQByeCfKqcTUIyaWGbatrzu2vMtH7Nr7P" +
                    "ePassword1107tr7nCPassword1CeOq0XXrZu4rssmqOyTlq1Kqu11ZXd31TPkcgdCPassword2TPassword2Zor6Rk0d5GbMCEk9omGcFPassword1c" +
                    "KRjoOPassword2TPJIYEqv8AWehPassword2z6l6Password129P7n5Password1l92tF4JZhLWOcNx6PPassword1xa2qvJXPassword2jLovz1s7n2JJ8NbmJD" +
                    "5YJ9MkE8lhjlycHqTnPassword16OOtYx8E6ys6wQxyOZJhDwF5O7A43HtzjnoeCcgfTa6RfKCNnz8c8DnLZ" +
                    "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword22wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB" +
                    "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword2wAARCABaAHgDASIA" +
                    "AhEBAxEBPassword28QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoLPassword28QAtRAAAgEDAwIEAwUFBAQA" +
                    "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3" +
                    "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm" +
                    "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4Password1Tl5ufo6erx8vP09fb3Password1Pn6Password28QAHwEA" +
                    "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoLPassword28QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx" +
                    "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK" +
                    "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3" +
                    "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3Password1Pn6Password29oADAMBAAIRAxEAPwDPassword1ATae" +
                    "53Y6DGM8nvn0wf06k00ckh224Password22c5656H2H5Password1xr7muNC0Password14BPassword1zhUbgY8tgDye2Password2rgDGf5Fs4lx4a" +
                    "kRdPassword2lIRzjMWM4yO7gdvXPucgn8Password2o8d4KV08FKLbS9Password1qr297ZOlG2ze17u17Nn9W4v6KHEOFg60eJ" +
                    "Fjl3hkju7Sd988vomrXbvdJtMPassword1M9jen6jPassword2GjY3pPassword1oPassword2xr6wltTECjQx5HZoPassword1evUbmPfrgexyOaoGz" +
                    "hHXbPassword2wB8enPassword2Av8Password19dq4rw8ldYOXTX23Password2ANoPassword1zWvlu3r8vVPassword1j1jKVPassword2wDjJE7Wu1ktrN3taPassword1dOPassword134r" +
                    "VtmnPassword1zX4BPassword2ZP8bQPassword1Pv8AhpX47eJfg7Lo0fhPassword1fwTF4c8C634tPassword2wCEwt2Password24SWTxHYFtO0jU10rUrf7" +
                    "LoMGjSaoEgmfVZpTmPTpAfqBfgTPassword2AMEpZZ7OOT9uL4oJbvDPqd1Mfgl4uDW9oTZx2HhdRPassword2wgmW8R" +
                    "PLqDySavhvD66Zo2sDcrT6EHPassword1N7jRbZlkkjijRPassword1OkPYls9DnoBjn6EkNnmZreOEGPy0wDwMHuTnn" +
                    "cc53ceg3ZyQM9tDiGjWvy4VPVfFJud7zPassword1T2tePassword1nuq7adPassword2lsf4N5nljl9YzhNe44tU1bVyVlrb7PW" +
                    "7bbu24uPassword23xpHwoPassword24Jc6p8Cvh9pnin9pbUvCPassword2xq0rQfifdPassword1PfEfhTwd8bdZ0zXvEKPassword1KvE4Password1GWnjTt" +
                    "ePassword1FK6YNPPassword2wCEbHhRblNIGlEyPq5uNfNyu6tzSPhPPassword2wAEirPRPassword2E9lePassword2tReN9Uu9X8XaJpGgPassword1INTPassword1H" +
                    "nxWPassword2tTwh4LXwpqp1rXbHRNA8A6RpXiTVD4iurQo2rkfLZ6ZDH4fFuuva3qH52tYQOrsEjDjHPle5" +
                    "JG3JA6rPassword1J7nFc3NbRRfuthDgdTk92PTJzjA5z3HHUnoo5tCq2lFqyV7JqPassword1sl6293R67rS92eRjvD" +
                    "qvgE39d003T97e1nqtbW0d073vbX9DbD4If8Eq1ttCl1r9sT4jLd3cPassword1qw63p2gPassword1BfF2o22g2lidT" +
                    "1DT7xtc1L4C6PPassword2aP9qrBYeFV8vR1dNbvB4k3f8I6n9h1y3gf4GPassword28E4rjRLBfiHPassword12b4ittaGmatLq" +
                    "l34TPassword1GnxJbTBqi65qB0SzsrDVPg08mB4dhsWlLaxjWNc1HVlzo0XheJfEPwJcWUstrdXcUBa2tZb" +
                    "SGecRkPassword1SbzeLMMMn7Password22NwOe5GD1PZeEfB3h7WPDHiPassword2xHrPassword2jDQvDp0zTifD2nTym41fXvEB1HQf8A" +
                    "QBomnudSsdMbTrPassword1Password2lOsvGujq9oVycMx9COITjzON9rauy1ktLNLaO26uPassword1kW38dicnWGrexeLXSza" +
                    "s38VtG3dPassword2PrZttKPassword2cPassword2tIPassword1EP2avC2qPassword1D1Password2ZsPassword1Jvib4j6Rqfhv7X4vHinQb3SdQ8MeJPassword2t8obRkebwv" +
                    "4UTUYPassword2soDefHpYUNBKQ5Vkr5n2N6fqP8a6m0gjcMChycc5b1OMYORnjvPassword1JJOOtstO89EjjT68E55" +
                    "kGfvAjPassword1XXJzzXJUzGnh1K8ZWWPassword2vWWml9W7LT5bNXtKXvZVwVXzS6w2Mj07tdU3u2lovRN3fuyZ5Z" +
                    "kDgNjHXgnufUPassword15796CZD3zjocKM19Gab4c8yNmMewDHWEnuwPassword1pGRkc9zzgE11V74QvNO03TdSeKw" +
                    "e2vYSYfIvtGurv8A4Password2tT08f2hp9hIdR03P8AZ95Password2yFgCQByeCfKqcTUIyaWGbatrzu2vMtH7Nr7P" +
                    "ePassword1107tr7nCPassword1CeOq0XXrZu4rssmqOyTlq1Kqu11ZXd31TPkcgdCPassword2TPassword2Zor6Rk0d5GbMCEk9omGcFPassword1c" +
                    "KRjoOPassword2TPJIYEqv8AWehPassword2z6l6Password129P7n5Password1l92tF4JZhLWOcNx6PPassword1xa2qvJXPassword2jLovz1s7n2JJ8NbmJD" +
                    "5YJ9MkE8lhjlycHqTnPassword16OOtYx8E6ys6wQxyOZJhDwF5O7A43HtzjnoeCcgfTa6RfKCNnz8c8DnLZ" +
                    "zjr27Password1gyMVe0vTbzPassword10NP863Password2AOXyzPassword2SPassword2bjrPassword2AFJPassword17kg1Password2JdDiPassword1suZyxNJ6JXlyyWnNrdSvdrXta1" +
                    "9YuPassword2Password2QlmfgBwPassword2VoN4LB5zl70T3sldrRuNPassword12i20SsuZHzPasswordAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword22wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB" +
                    "AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQHPassword2wAARCABaAHgDASIA" +
                    "AhEBAxEBPassword28QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoLPassword28QAtRAAAgEDAwIEAwUFBAQA" +
                    "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3" +
                    "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm" +
                    "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4Password1Tl5ufo6erx8vP09fb3Password1Pn6Password28QAHwEA" +
                    "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoLPassword28QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx" +
                    "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSEl" +
                    "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3" +
                    "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3Password1Pn6Password29oADAMBAAIRAxEAPwDPassword1ATae" +
                    "53Y6DGM8nvn0wf06k00ckh224Password22c5656H2H5Password1xr7muNC0Password14BPassword1zhUbgY8tgDye2Password2rgDGf5Fs4lx4a" +
                    "kRdPassword2lIRzjMWM4yO7gdvXPucgn8Password2o8d4KV08FKLbS9Password1qr297ZOlG2ze17u17Nn9W4v6KHEOFg60eJ" +
                    "Fjl3hkju7Sd988vomrXbvdJtMPassword1M9jen6jPassword2GjY3pPassword1oPassword2xr6wltTECjQx5HZoPassword1evUbmPfrgexyOaoGz" +
                    "hHXbPassword2wB8enPassword2Av8Password19dq4rw8ldYOXTX23Password2ANoPassword1zWvlu3r8vVPassword1j1jKVPassword2wDjJE7Wu1ktrN3taPassword1dOPassword134r" +
                    "VtmnPassword1zX4BPassword2ZP8bQPassword1Pv8AhpX47eJfg7Lo0fhPassword1fwTF4c8C634tPassword2wCEwt2Password24SWTxHYFtO0jU10rUrf7" +
                    "LoMGjSaoEgmfVZpTmPTpAfqBfgTPassword2AMEpZZ7OOT9uL4oJbvDPqd1Mfgl4uDW9oTZx2HhdRPassword2wgmW8R" +
                    "PLqDySavhvD66Zo2sDcrT6EHPassword1N7jRbZlkkjijRPassword1OkPYls9DnoBjn6EkNnmZreOEGPy0wDwMHuTnn" +
                    "cc53ceg3ZyQM9tDiGjWvy4VPVfFJud7zPassword1T2tePassword1nuq7adPassword2lsf4N5nljl9YzhNe44tU1bVyVlrb7PW" +
                    "7bbu24uPassword23xpHwoPassword24Jc6p8Cvh9pnin9pbUvCPassword2xq0rQfifdPassword1PfEfhTwd8bdZ0zXvEKPassword1KvE4Password1GWnjTt" +
                    "ePassword1FK6YNPPassword2wCEbHhRblNIGlEyPq5uNfNyu6tzSPhPPassword2wAEirPRPassword2E9lePassword2tReN9Uu9X8XaJpGgPassword1INTPassword1H" +
                    "nxWPassword2tTwh4LXwpqp1rXbHRNA8A6RpXiTVD4iurQo2rkfLZ6ZDH4fFuuva3qH52tYQOrsEjDjHPle5" +
                    "JG3JA6rPassword1J7nFc3NbRRfuthDgdTk92PTJzjA5z3HHUnoo5tCq2lFqyV7JqPassword1sl6293R67rS92eRjvD" +
                    "qvgE39d003T97e1nqtbW0d073vbX9DbD4If8Eq1ttCl1r9sT4jLd3cPassword1qw63p2gPassword1BfF2o22g2lidT" +
                    "1DT7xtc1L4C6PPassword2aP9qrBYeFV8vR1dNbvB4k3f8I6n9h1y3gf4GPassword28E4rjRLBfiHPassword12b4ittaGmatLq" +
                    "l34TPassword1GnxJbTBqi65qB0SzsrDVPg08mB4dhsWlLaxjWNc1HVlzo0XheJfEPwJcWUstrdXcUBa2tZb" +
                    "SGecRkPassword1SbzeLMMMn7Password22NwOe5GD1PZeEfB3h7WPDHiPassword2xHrPassword2jDQvDp0zTifD2nTym41fXvEB1HQf8A" +
                    "QBomnudSsdMbTrPassword1Password2lOsvGujq9oVycMx9COITjzON9rauy1ktLNLaO26uPassword1kW38dicnWGrexeLXSza" +
                    "s38VtG3dPassword2PrZttKPassword2cPassword2tIPassword1EP2avC2qPassword1D1Password2ZsPassword1Jvib4j6Rqfhv7X4vHinQb3SdQ8MeJPassword2t8obRkebwv" +
                    "4UTUYPassword2soDefHpYUNBKQ5Vkr5n2N6fqP8a6m0gjcMChycc5b1OMYORnjvPassword1JJOOtstO89EjjT68E55" +
                    "kGfvAjPassword1XXJzzXJUzGnh1K8ZWWPassword2vWWml9W7LT5bNXtKXvZVwVXzS6w2Mj07tdU3u2lovRN3fuyZ5Z" +
                    "kDgNjHXgnufUPassword15796CZD3zjocKM19Gab4c8yNmMewDHWEnuwPassword1pGRkc9zzgE11V74QvNO03TdSeKw" +
                    "e2vYSYfIvtGurv8A4Password2tT08f2hp9hIdR03P8AZ95Password2yFgCQByeCfKqcTUIyaWGbatrzu2vMtH7Nr7P" +
                    "ePassword1107tr7nCPassword1CeOq0XXrZu4rssmqOyTlq1Kqu11ZXd31TPkcgdCPassword2TPassword2Zor6Rk0d5GbMCEk9omGcFPassword1c" +
                    "KRjoOPassword2TPJIYEqv8AWehPassword2z6l6Password129P7n5Password1l92tF4JZhLWOcNx6PPassword1xa2qvJXPassword2jLovz1s7n2JJ8NbmJD" +
                    "5YJ9MkE8lhjlycHqTnPassword16OOtYx8E6ys6wQxyOZJhDwF5O7A43HtzjnoeCcgfTa6RfKCNnz8c8DnLZ" +
                    "zjr27Password1gyMVe0vTbzPassword10NP863Password2AOXyzPassword2SPassword2bjrPassword2AFJPassword17kg1Password2JdDiPassword1suZyxNJ6JXlyyWnNrdSvdrXta1" +
                    "9YuPassword2Password2QlmfgBwPassword2VoN4LB5zl70T3sldrRuNPassword12i20SsuZHzPassword";

            lol=vignetteTempo;


            //creerMusique/{noTicket}/{chaineConfirmation}/{idUser}/{titre}/{artiste}/{musique}/{vignette}/{publique}/{active}
            params = noTicket + "/" + chaineConfirmation + "/" + utilConnecte.getId()+"/"+titreTempo+"/"
                    +artisteTempo+"/" + txtURLTempo+"/"+vignetteTempo+"/"+((Boolean)togglePublique.isChecked()).toString()+"/"
                    +((Boolean)toggleActif.isChecked()).toString();
            Log.e("PARAMMMMMMMM2",params+"");
            Log.e("PARAMMMMMMMM",lol+"");
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
            doAction();
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
}