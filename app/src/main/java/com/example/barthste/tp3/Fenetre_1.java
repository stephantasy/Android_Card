package com.example.barthste.tp3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/********************************************************
 *	Stéphane Barthélemy 				                *
 /*******************************************************/

public class Fenetre_1 extends AppCompatActivity {

    private static final String APPLI_PASSWORD = "12345";  // Password de l'application
    private EditText userName, password;            // Champs Nom et Password
    private SharedPreferences sp;                   // Shared Preferences
    private ArrayList<Integer> notes;               // Liste des notes attribuées à la présentation des étudiants
    private TextView noteFinale;                    // Liste des notes temporaire


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenetre_1);

        // Shared Preferences (entre toutes les activités de l'applicaiton)
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        // User Name, Password, Moyenne
        userName = (EditText)findViewById(R.id.et_f1_UserName);
        password = (EditText)findViewById(R.id.et_f1_Password);
        noteFinale = (TextView) findViewById(R.id.tv_f1_moyenne);

        // Notes
        notes = new ArrayList<>();

        // Initialize SharedPreferences
        initSharedPreferences();

        // Est-ce qu'on est applelé par une autre activité ? Si oui, on reçoit des données
        Intent intent = getIntent();
        if(intent != null) {

            // On récupère l'Id de la fenêtre ayant appelée l'activité
            String caller = intent.getStringExtra("caller");

            // Les notes viennent de la Fenêtre 2b
            notes = intent.getIntegerArrayListExtra("notes");

            // Si on est pas appelé par la Fenêtre 2b, alors notes est NULL
            if(notes == null){
                notes = new ArrayList<>();
            }

            //  Si on ne vient pas de la Fenêtre 2b, on initialise les notes des étudiants
            if(notes.size() <= 0){
                // On compte le nombre d'étudiants
                int nbStudent = getResources().getStringArray(R.array.student_list).length;

                // On initialise les notes à -1
                for(int i = 0 ; i < nbStudent ; i++){
                    notes.add(-1);
                }
            }

            // Si Caller est null, c'est qu'on vient de lancer l'application
            if(caller != null) {

                // On affiche le nom du User
                userName.setText(sp.getString("userName",""));

                // Si on est applelée par la Fenêtre 2b
                if (caller.equals("F2b")) {
                    // On affiche le Password pour pouvoir retourner dans la Fenêtre 2b
                    password.setText(sp.getString("password", ""));

                    // On affiche le total des notes qu'on a reçue
                    int nbNote = 0;
                    int totalNote = 0;
                    for ( int note : notes) {
                        // On ignore les notes non remplie, càd = -1
                        if(note >= 0){
                            nbNote++;
                            totalNote += note;
                        }
                    }

                    // On affiche le résultat
                    if(nbNote > 0){
                        // Total des notes
                        noteFinale.setText(getText(R.string.moyenne) + " " + totalNote);

                        // Pour faire la moyenne
                        //float moy = (float)totalNote / nbNote;
                        //moyenne.setText(getText(R.string.moyenne) + " " + String.format("%.2f", moy));
                    }
                }
                // Si on est applelée par la Fenêtre 2a
                else if (caller.equals("F2a")) {
                    // On ne récupère aucune information !
                    // En effet, puisque l'utilisateur s'est trompé dans le mot de passe,
                    // On vide tous les champs de la fenêtre 1 (résultat des notes compris) !
                }
            }

        }
    }


    /**
     * Initialisation des sharedPreferences
     * Si le mot de passe n'est pas encore sauvegardé, on le place dans les SharedPreferences.
     */
    private void initSharedPreferences() {

        // Conteneur par défaut
        String testPwd = sp.getString("password","");

        // Si le password est vide, on l'enregistre
        if(testPwd.equals(""))
        {
            //Pour définir ou modifier les SharedPreferences, il faut une référence sur un
            //Editor.  Celle-ci est retournée par la méthode edit
            SharedPreferences.Editor editeur = sp.edit();

            //Pour définir ou modifier une SharedPreference, on utilise une méthode putType avec
            //une clé et une valeur du type en question.
            editeur.putString("password", APPLI_PASSWORD);

            //La méthode apply est exécutée pour effectuer le changement.
            editeur.apply();
        }
    }


    /**
     * On sauve le User Name pour l'afficher dans les autres Fenêtre
     * @param userName : Nom de l'utilisateur
     */
    private void saveUsername(String userName) {
        SharedPreferences.Editor editeur = sp.edit();
        editeur.putString("userName", userName);
        editeur.apply();
    }


    /**
     * Click sur "Apply"
     * @param v : View
     */
    public void clickSubmit(View v){

        // On mémorise le User Name
        saveUsername(userName.getText().toString());

        // On récupère le bon Password dans les ShareedPreferences
        String pwd = sp.getString("password", "#wrongPassword");

        // Password ok ?
        if(pwd.equals(password.getText().toString())){
            // On appel la Fenêtre 2b en lui passant les notes
            Intent intent = new Intent(this, Fenetre_2b.class);
            intent.putExtra("caller", "F1");       // On indique qui on est
            intent.putIntegerArrayListExtra("notes", notes);
            startActivity(intent);
        }else{
            // Appel de la Fenêtre 2a, indiquant que le Password n'est pas bon
            Intent intent = new Intent(this, Fenetre_2a.class);
            intent.putExtra("caller", "F1");       // On indique qui on est
            startActivity(intent);
        }
    }

}
