package tech.raulballeza.practica1;
/*
* Estadísticas de Faltas:
* Considerando un listado de entrada en una pantalla,
* con fecha asignada por el profesor,
* se pueda generar estadisticas por grupo y por individuo de las asistencias de un grupo.
* Se pueden crear varios grupos y agregar alumnos
*
* */
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context context;
    static ArrayList<Grupo> Grupos;
    Button btn_addGroup;
    ListView list_groups;
    ArrayAdapter<Grupo> adapter_groups;



    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Grupos = new ArrayList<>();
        Grupos.add(new Grupo("A102"));
        Grupos.get(0).add(new Alumno("Raul Balleza"));
        Grupos.get(0).add(new Alumno("Alejandro Lopez"));


        list_groups = (ListView) findViewById(R.id.list_gruops);
        adapter_groups = new ArrayAdapter<Grupo>(context,android.R.layout.simple_list_item_1,Grupos);
        list_groups.setAdapter(adapter_groups);

        list_groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(),((TextView) view).getText() + "- Posicion: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, GrupoActivity.class);
                intent.putExtra("Grupo",position);
                startActivity(intent);

            }
        });

        btn_addGroup = (Button) findViewById(R.id.btn_addGroup);
        btn_addGroup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog_groups = new Dialog(context);
                        dialog_groups.setContentView(R.layout.add_group);
                        dialog_groups.setTitle("Modificar/eliminar artículo");

                        final EditText nombre= (EditText)  dialog_groups.findViewById(R.id.editTextGroupName);

                        Button dialog_btn_saveGroup = (Button) dialog_groups.findViewById(R.id.btn_saveGroup);
                        dialog_btn_saveGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Grupos.add(new Grupo(nombre.getText().toString()));
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
                }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Grupo>>() {}.getType();
        Grupos = gson.fromJson(json, type);
        if (Grupos == null) {
            Grupos = new ArrayList<>();
        }

        adapter_groups = new ArrayAdapter<Grupo>(context,android.R.layout.simple_list_item_1,Grupos);
        list_groups.setAdapter(adapter_groups);

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Grupos);
        editor.putString("task list", json);
        editor.apply();
    }
}