package com.example.barthste.tp3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/********************************************************
 *	Stéphane Barthélemy 				                *
 /*******************************************************/

public class Fenetre_2b extends AppCompatActivity {

    private TextView tv_name;                   // Nom de l'utilisateur
    private ArrayList<Integer> notes;           // Notes
    private ListView studentList;               // Liste des étudiants

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenetre_2b);

        // Nom du User (récupéré dans les SharedPreferences)
        tv_name = (TextView)findViewById(R.id.tv_f2b_UserName);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        tv_name.setText(sp.getString("userName", "#NameError"));

        // On reçoit les notes déjà données (si elles existent, sinon elles valent -1)
        Intent intent = getIntent();
        notes = intent.getIntegerArrayListExtra("notes");

        // Paramètre de la ListView
        studentList = (ListView)findViewById(R.id.lv_f2b);
        studentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // On rempli la listView avec le nom des étudiants
        String[] student_list = getResources().getStringArray(R.array.student_list);
        studentList.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, student_list));

        // Listener pour la ListView
        studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickList(position);
            }
        });

    }


    /**
     * Demande d'affichage de la carte d'affaire de l'étudiant sélectionné
     * @param student_id : Identifiant de l'étudiant sélectionné dans la liste
     */
    private void clickList(int student_id){

        // On affiche la Fenêtre 3, présentant la carte d'affaire
        Intent intent = new Intent(this, Fenetre_3.class);
        intent.putExtra("caller", "F2b");           // On indique qui on est
        intent.putExtra("student_id", student_id);  // On envoie l'Id de l'étudiant sélectionné
        intent.putExtra("notes", notes);            // On envoie les notes
        startActivity(intent);
    }


    /**
     * Retour à la Fenêtre 1
     * @param v : View
     */
    public void clickBack(View v){
        // On affiche la Fenêtre 1
        Intent intent = new Intent(this, Fenetre_1.class);
        intent.putExtra("caller", "F2b");       // On indique qui on est
        intent.putExtra("notes", notes);        // On envoie les notes
        startActivity(intent);

    }
}
