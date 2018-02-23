package com.example.barthste.tp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/********************************************************
 *	Stéphane Barthélemy 				                *
 /*******************************************************/

public class Fenetre_2a extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenetre_2a);
    }

    /**
     * Retour à la fanêtre 1
     * @param v : View
     */
    public void clickBack(View v){
        // Retour à la Fenêtre 1
        // ATTENTION : Puisque l'utilisateur s'est trompé dans le mot de passe,
        // On vide tous les champs de la fenêtre 1 (résultat des notes compris) !
        Intent intent = new Intent(this, Fenetre_1.class);
        intent.putExtra("caller", "F2a");       // On indique qui on est
        startActivity(intent);
    }
}
