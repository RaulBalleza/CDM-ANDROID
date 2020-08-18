package tech.raulballeza.lab33kotlin;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 27/05/15.
 */
public class ActividadSecundaria  extends Activity {

    String CadenaRecibida1;
    String CadenaRecibida2;

    private EditText EditTextCampo1;
    private EditText EditTextCampo2;
    private EditText EditTextCampo3;

    private Button button01;



    private ArrayList<Estudiambre> ListaEstudiantesModificada;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_secundaria);

        // Aqui es en donde se obtienen los datos pasados por otro formulario (de haberlos)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            EditTextCampo1 = (EditText) findViewById(R.id.et_01SEC);
            EditTextCampo2 = (EditText) findViewById(R.id.et_02SEC);
            EditTextCampo3 = (EditText) findViewById(R.id.et_03SEC);

            // Codigo para obtenr los campos del objeto "stuent"
            Estudiambre student = extras.getParcelable("student");
            CadenaRecibida1 = student.getId() + " - " + student.getName() + " - " + student.getGrade();
            EditTextCampo1.setText(CadenaRecibida1);


            //Codigo para obtenr los campos del objeto "UbicacionesObtenidas", que es un arreglo de Ubicaciones
            String Vacio = "";
            List<Estudiambre> UbicacionesObtenidas;
            UbicacionesObtenidas = getIntent().getParcelableArrayListExtra("student_list");
            for (Estudiambre v : UbicacionesObtenidas) {
                Vacio += v.getId() + " , " + v.getName() + " , " + v.getGrade() + "\n";
            }
            EditTextCampo2.setText(Vacio);

            // De la lista obtenida, obtener una sublista
            String Vacio2 = "";
            ListaEstudiantesModificada = new ArrayList<Estudiambre>();
            int i = 0;
            for (Estudiambre v : UbicacionesObtenidas) {
                if ((i % 2) == 0) {
                    Vacio2 += v.getId() + " , " + v.getName() + " , " + v.getGrade() + "\n";
                    ListaEstudiantesModificada.add(new Estudiambre(v.getId(), v.getName(), v.getGrade()));
                }
                i++;
            }
            EditTextCampo3.setText(Vacio2);


        }

        button01 = (Button) findViewById(R.id.botonsec);

        // Cuando se da click en el botón
        button01.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Terminar_Regresar_Valores();

            }
        });
    }

     void Terminar_Regresar_Valores () {
            Intent data = new Intent();
            data.putParcelableArrayListExtra("student_list_regreso", ListaEstudiantesModificada);
            setResult(RESULT_OK, data);
            finish();
     }



        @Override
        public void onBackPressed() {
            /*AlertDialog ad = adb.create();
            ad.setMessage("Interceptando el botón BACK");
            ad.show();
            Intent data = new Intent();
            data.putExtra("Parametro1",EditTextCampo1.getText().toString()+"-"+CampoApellidoP.getText().toString());
            data.putExtra("Parametro2","Procesamiento de Texto Hecho" );
            setResult(RESULT_OK,data);*/
            //finish();

            Terminar_Regresar_Valores();
        }






}
