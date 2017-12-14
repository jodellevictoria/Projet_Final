package jvpg.cgodin.qc.ca.projetpldl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import jvpg.cgodin.qc.ca.projetpldl.Private.ListesUtilActivity;
import jvpg.cgodin.qc.ca.projetpldl.Private.MusiquesUtilListActivity;
import jvpg.cgodin.qc.ca.projetpldl.Private.SearchYTActivity;
import jvpg.cgodin.qc.ca.projetpldl.Public.ListesPubliquesActivity;
import jvpg.cgodin.qc.ca.projetpldl.Public.MusiquePubliqueListActivity;
import jvpg.cgodin.qc.ca.projetpldl.entities.Utilisateur;

public class MainActivity extends AppCompatActivity {

    public static Utilisateur utilConnecte = null;

    Button btnLogin;
    Button btnMusiquesPubliques;
    Button btnListesPubliques;
    Button btnMusiquesUtil;
    Button btnListesUtil;
    Button btnModifierUtil;
    Button btnSearchYT;

    TextView welcomeText;
    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnConnexion);
        btnMusiquesPubliques = (Button) findViewById(R.id.btnMusiquesPubliques);
        btnListesPubliques = (Button) findViewById(R.id.btnListesPubliques);

        btnMusiquesUtil = (Button) findViewById(R.id.btnVoirMesMusiques);
        btnListesUtil = (Button) findViewById(R.id.btnVoirMesListes);
        btnModifierUtil = (Button) findViewById(R.id.btnModifierProfil);

        welcomeText = (TextView) findViewById(R.id.txtWelcome);
        infoText = (TextView) findViewById(R.id.infoPrives);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(utilConnecte != null){
                utilConnecte = null;
                Toast.makeText(getApplicationContext(), "Vous n'êtes plus connecté", Toast.LENGTH_SHORT).show();

                changeAppearance();
            }
            else{
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(i,1);

                //changeAppearance();
            }
            }
        });

        btnMusiquesPubliques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MusiquePubliqueListActivity.class);
            startActivity(i);
            }
        });

        btnListesPubliques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), ListesPubliquesActivity.class);
            startActivity(i);
            }
        });

        btnSearchYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchYTActivity.class);
                startActivity(i);
            }
        });

        btnMusiquesUtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MusiquesUtilListActivity.class);
                startActivity(i);
            }
        });

        btnListesUtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListesUtilActivity.class);
                startActivity(i);
            }
        });

        btnModifierUtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ModifierUtilisateurActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            /*if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }else{

            }*/

            changeAppearance();
        }
    }

    public void changeAppearance(){
        Log.i("changeAppearance()","dans methode");
        if(utilConnecte != null){
            Log.i("changeAppearance()","utilisateur is connected()");
            btnLogin.setText("Déconnecter");
            welcomeText.setText("Bienvenue " + utilConnecte.getAlias());
            infoText.setText("");
            btnSearchYT.setEnabled(false);
            btnMusiquesUtil.setEnabled(true);
            btnListesUtil.setEnabled(true);
            btnModifierUtil.setEnabled(true);
        }
        else{
            Log.i("changeAppearance()","utilisateur is not connected");
            btnLogin.setText("Se connecter");
            welcomeText.setText("Bienvenue");
            infoText.setText("Veuillez vous connecter pour utiliser les fonctionalités suivantes");
            btnSearchYT.setEnabled(false);
            btnMusiquesUtil.setEnabled(false);
            btnListesUtil.setEnabled(false);
            btnModifierUtil.setEnabled(false);
        }
    }
}
