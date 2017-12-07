package jvpg.cgodin.qc.ca.projetpldl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jvpg.cgodin.qc.ca.projetpldl.entities.Utilisateur;

public class MainActivity extends AppCompatActivity {

    public static Utilisateur utilConnecte = null;

    Button btnLogin;
    Button btnMusiquesPubliques;
    Button btnListesPubliques;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnConnexion);
        btnMusiquesPubliques = (Button) findViewById(R.id.btnMusiquesPubliques);
        btnListesPubliques = (Button) findViewById(R.id.btnListesPubliques);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
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
    }
}
