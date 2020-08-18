package tech.raulballeza.practica1;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GrupoActivity extends AppCompatActivity {

    Context context;
    ListView list_alumnos;
    ArrayList<Alumno> alumnos = new ArrayList<>();
    ArrayList<Asistencia> asistencias = new ArrayList<>();

    ArrayAdapter<Alumno> alumnoArrayAdapter;
    ArrayAdapter<Asistencia> asistenciaArrayAdapter;
    ArrayAdapter<Alumno> alumnoAsistenciaArrayAdapter;
    ArrayAdapter<Alumno> estadisticasArrayAdapter;

    Grupo grupo;
    SparseBooleanArray asistencia_lista;

    Button btn_asistencia;
    Button btn_verAsistencias;
    Button btn_addAlumno;
    Button btn_verEstadisticas;
    ArrayList<HashMap<String, String>> estadisticas;
    Boolean listas = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);

        context = this;
        grupo = (Grupo) MainActivity.Grupos.get((Integer) getIntent().getSerializableExtra("Grupo"));
        btn_asistencia = findViewById(R.id.btn_pasar);
        list_alumnos = findViewById(R.id.list_alumnos);
        btn_verAsistencias = findViewById(R.id.btn_verAsistencias);
        btn_addAlumno = findViewById(R.id.btn_addAlumno);
        btn_verEstadisticas = findViewById(R.id.btn_verEstadisticas);

        list_alumnos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        estadisticas = new ArrayList<HashMap<String, String>>();


        alumnoArrayAdapter = new ArrayAdapter<Alumno>(context, android.R.layout.simple_list_item_multiple_choice, grupo.alumnos);
        asistenciaArrayAdapter = new ArrayAdapter<Asistencia>(context, android.R.layout.simple_list_item_1, grupo.asistencias);


        list_alumnos.setAdapter(alumnoArrayAdapter);

        list_alumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!listas) {
                    CheckedTextView checkedTextView = (CheckedTextView) view;
                    if (checkedTextView.isChecked()) {
                        checkedTextView.setChecked(false);
                        Toast.makeText(getApplicationContext(), ((TextView) view).getText() + "- SELECCIONADO: " + position, Toast.LENGTH_SHORT).show();

                    } else {
                        checkedTextView.setChecked(true);
                        Toast.makeText(getApplicationContext(), ((TextView) view).getText() + "- DESSELECCIONADO: " + position, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Asistencia asistencia = grupo.asistencias.get(position);
                    alumnoAsistenciaArrayAdapter = new ArrayAdapter<Alumno>(context, android.R.layout.simple_list_item_1, asistencia.alumnos);
                    list_alumnos.setAdapter(alumnoAsistenciaArrayAdapter);
                }
            }
        });

        btn_verEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < grupo.alumnos.size(); i++) {
                    grupo.alumnos.get(i).setNumAsistencia(getStatisticsAlumno(grupo.alumnos.get(i)));
                }

                estadisticasArrayAdapter = new ArrayAdapter<Alumno>(context, android.R.layout.simple_list_item_2, grupo.alumnos);
            }
        });

        btn_addAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog_groups = new Dialog(context);
                dialog_groups.setContentView(R.layout.add_group);
                dialog_groups.setTitle("Modificar/eliminar artículo");

                final EditText nombre = (EditText) dialog_groups.findViewById(R.id.editTextGroupName);

                Button dialog_btn_saveGroup = (Button) dialog_groups.findViewById(R.id.btn_saveGroup);
                dialog_btn_saveGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        grupo.add(new Alumno(nombre.getText().toString()));
                        dialog_groups.dismiss();
                    }
                });

                Button dialog_btn_cancelGroup = (Button) dialog_groups.findViewById(R.id.btn_cancelGroup);
                dialog_btn_cancelGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_groups.dismiss();
                    }
                });

                dialog_groups.show();
            }
        });

        btn_asistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = list_alumnos.getCount();
                ArrayList<Alumno> auxAlumnArray = new ArrayList<>();
                asistencia_lista = list_alumnos.getCheckedItemPositions();
                if (asistencia_lista.size() != 0) {
                    for (int i = 0; i < len; i++) {
                        if (asistencia_lista.get(i)) {
                            grupo.alumnos.get(i).asistencia = true;
                            auxAlumnArray.add(grupo.alumnos.get(i));
                        }
                    }
                    Date d = new Date();
                    String date = (String) DateFormat.format("MMMM d, yyyy ", d.getTime());
                    Asistencia asistencia = new Asistencia(date, auxAlumnArray);
                    grupo.asistencias.add(asistencia);
                }else {
                    Toast.makeText(context, "Por favor, selecciona los alumnos antes de pasar lista", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_verAsistencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listas) {
                    list_alumnos.setAdapter(asistenciaArrayAdapter);
                    listas = !listas;
                    btn_verAsistencias.setText("Ver Alumnos");
                    btn_asistencia.setEnabled(false);

                } else {
                    list_alumnos.setAdapter(alumnoArrayAdapter);
                    listas = !listas;
                    btn_verAsistencias.setText("ver Asistencias");
                    btn_asistencia.setEnabled(true);
                }

            }
        });

    }

    private float getStatisticsAlumno(Alumno alumno) {

        int total_clases = grupo.asistencias.size();
        int asistencias_alumno = 0;

        for (int i = 0; i < total_clases; i++) {
            if (grupo.asistencias.get(i).alumnos.contains(alumno)) {
                asistencias_alumno++;
            }
        }
        return (asistencias_alumno * 100) / total_clases;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();

        //Alumnos
        String json = sharedPreferences.getString("alumnos" + grupo.nombre, null);
        Type type = new TypeToken<ArrayList<Alumno>>() {
        }.getType();
        alumnos = gson.fromJson(json, type);
        if (alumnos == null) {
            alumnos = new ArrayList<>();
        }
        grupo.alumnos = alumnos;
        alumnoArrayAdapter = new ArrayAdapter<Alumno>(context, android.R.layout.simple_list_item_multiple_choice, grupo.alumnos);
        asistenciaArrayAdapter.notifyDataSetChanged();
        list_alumnos.setAdapter(alumnoArrayAdapter);


        //Asistencias
        json = sharedPreferences.getString("asistencias" + grupo.nombre, null);
        type = new TypeToken<ArrayList<Asistencia>>() {
        }.getType();
        asistencias = gson.fromJson(json, type);
        if (asistencias == null) {
            asistencias = new ArrayList<>();
        }
        grupo.asistencias = asistencias;
        asistenciaArrayAdapter = new ArrayAdapter<Asistencia>(context, android.R.layout.simple_list_item_1, grupo.asistencias);
        asistenciaArrayAdapter.notifyDataSetChanged();
        //list_alumnos.setAdapter(asistenciaArrayAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alumnos);
        editor.putString("alumnos" + grupo.nombre, json);


        json = gson.toJson(asistencias);
        editor.putString("asistencias" + grupo.nombre, json);

        editor.apply();
    }
}
