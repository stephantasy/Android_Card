package com.example.barthste.tp3;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/********************************************************
 *	Stéphane Barthélemy 				                *
 /*******************************************************/

public class Fenetre_3 extends AppCompatActivity {

    private static final int NB_DATA_PER_STUDENT = 4;  // Nombre de données par étudiant

    private int student_id;                     // Id de l'étudiant sélectionné
    private ArrayList<Integer> notes;           // Notes

    private ImageView image;                    // Image de l'étudiant
    private TextView name;                      // Nom de l'étudiant
    private TextView code;                      // Code permanent de l'étudiant
    private TextView email;                     // Adresse courriel de l'étudiant
    private TextView is_student;                // SI l'étudiant est inscrit au cours IFT1135
    private EditText la_note;                   // Note courante de la présentation

    // Enumération des données sur un étudiant
    private enum Data_Id{
        IMAGE(0),
        CODE(1),
        EMAIL(2),
        IS_STUDENT(3);

        private final int id;
        Data_Id(int id) { this.id = id; }
        public int getValue() { return id; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenetre_3);

        // On reçoit toutes les infos à mettre dans la carte d'affaire
        Intent intent = getIntent();
        student_id = intent.getIntExtra("student_id", -1);
        notes = intent.getIntegerArrayListExtra("notes");

        // Widget de l'activité
        image = (ImageView)findViewById(R.id.iv_f3_image);
        name = (TextView)findViewById(R.id.tv_f3_name);
        code = (TextView)findViewById(R.id.tv_f3_code);
        email = (TextView)findViewById(R.id.tv_f3_email);
        is_student = (TextView)findViewById(R.id.tv_f3_is_student);
        la_note = (EditText)findViewById(R.id.et_f3_note);

        // Filtre pour permettre de mettre qu'une note entre 0 et 3.
        // On ne peut mettre qu'un chiffre entier. Ainsi, les notes permisent sont : 0, 1, 2 ou 3.
        InputFilter noteInputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,Spanned dest, int dstart, int dend) {
                // 1 chiffre seulement
                if (source.length() > 1 || dest.length() > 0){
                    return "";
                }
                // Un chiffre entre 0 et 3
                int value = Integer.parseInt(source.toString());
                if(value < 0 || value > 3){
                    return "";
                }
                return null;    // On laisse ce qu'il y a dans le TextField
            }
        };
        // On applique le filtre
        la_note.setFilters(new InputFilter[] {noteInputFilter});

        // On rempli les Widgets
        if(student_id >= 0){
            // On lit le fichier avec AsynchTask
            new FileReader().execute(student_id, NB_DATA_PER_STUDENT);
        }else{
            name.setText("### ERROR ### Bad student_id");
        }
    }


    /**
     * Retour vers la fenêtre 2b
     * @param v : View
     */
    public void clickBack(View v){

        // Si une note a été entrée, on la récupère
        if(!la_note.getText().toString().equals("")){
            int note = Integer.parseInt(la_note.getText().toString());
            notes.set(student_id, note);
        }

        // On renvoie les notes à la Fenêtre 2b
        Intent intent = new Intent(this, Fenetre_2b.class);
        intent.putExtra("notes", notes);
        startActivity(intent);
    }


    /**
     *  Lecture du fichier contenant les informations sur les étudiants
     */
    public class FileReader extends AsyncTask<Integer, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Integer... values) {

            ArrayList<String> texte = new ArrayList<>();    // Data a renvoyer
            int student_id = values[0];                     // Id de l'étudiant dont on veut les données
            int nbDataPerStudent = values[1];               // Nombre de données pour un étudiant
            int line_nb = 0;                                // Numéro de la ligne en cours de lecture
            int line_from = nbDataPerStudent * student_id;  // Ligne à partir de laquelle on prend les données
            int line_to = line_from + nbDataPerStudent - 1; // Dernière ligne des données

            try {
                // Lecture du fichier
                InputStream is = getResources().openRawResource(R.raw.student_data);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                // Lecture de la 1ère ligne
                String line = br.readLine();

                while (line != null){
                    // On ne garde que les données de l'étudiant sélectionné
                    if(line_nb >= line_from) {
                        texte.add(line);
                    }
                    // Si on a toutes les données, on quitte
                    if(line_nb >= line_to){
                        break;
                    }
                    // Ligne suivante
                    line = br.readLine();
                    line_nb++;
                }
                br.close();

            } catch (FileNotFoundException e) {
                Log.d("Tag1", "### ERROR ### FileReader => Fichier introuvable");
            } catch (IOException e) {
                Log.d("Tag2", "### ERROR ### FileReader => Problème de lecture");
            }

            return texte;
        }

        @Override
        protected void onPostExecute(ArrayList<String> data) {
            super.onPostExecute(data);

            // On affiche l'image
            int id = getResources().getIdentifier(data.get(Data_Id.IMAGE.getValue()), "drawable", getPackageName());
            image.setImageResource(id);

            // On affiche le nom
            String[] student_list = getResources().getStringArray(R.array.student_list);
            name.setText(student_list[student_id]);

            // On affiche le code étudiant
            code.setText(data.get(Data_Id.CODE.getValue()));

            // On affiche l'adresse courriel
            email.setText(data.get(Data_Id.EMAIL.getValue()));

            // On affiche s'il est étudiant d'IFT1135
            if(data.get(Data_Id.IS_STUDENT.getValue()).equals("true")){
                is_student.setText(R.string.studentIFT1135);
            }else{
                is_student.setText(R.string.notStudentIFT1135);
            }

            // Si une note avait déjà été donnée précédemment, on l'affiche
            int note = notes.get(student_id);
            if(note >= 0) {
                la_note.setText(Integer.toString(note));
            }
        }
    }
}
