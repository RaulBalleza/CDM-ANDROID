package tech.raulballeza.lab33kotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.util.*

/**
 * Created by marco on 27/05/15.
 */
class ActividadSecundaria : Activity() {
    var CadenaRecibida1: String? = null
    var CadenaRecibida2: String? = null
    private var EditTextCampo1: EditText? = null
    private var EditTextCampo2: EditText? = null
    private var EditTextCampo3: EditText? = null
    private var button01: Button? = null
    private var ListaEstudiantesModificada: ArrayList<Estudiambre>? =
        null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_secundaria)
        // Aqui es en donde se obtienen los datos pasados por otro formulario (de haberlos)
        val extras = intent.extras
        if (extras != null) {
            EditTextCampo1 = findViewById<View>(R.id.et_01SEC) as EditText
            EditTextCampo2 = findViewById<View>(R.id.et_02SEC) as EditText
            EditTextCampo3 = findViewById<View>(R.id.et_03SEC) as EditText
            // Codigo para obtenr los campos del objeto "stuent"
            val student: Estudiambre = extras.getParcelable("student")
            CadenaRecibida1 =
                student.getId() + " - " + student.getName() + " - " + student.getGrade()
            EditTextCampo1!!.setText(CadenaRecibida1)
            //Codigo para obtenr los campos del objeto "UbicacionesObtenidas", que es un arreglo de Ubicaciones
            var Vacio = ""
            val UbicacionesObtenidas: List<Estudiambre>
            UbicacionesObtenidas = intent.getParcelableArrayListExtra("student_list")
            for (v in UbicacionesObtenidas) {
                Vacio += v.getId() + " , " + v.getName() + " , " + v.getGrade() + "\n"
            }
            EditTextCampo2!!.setText(Vacio)
            // De la lista obtenida, obtener una sublista
            var Vacio2 = ""
            ListaEstudiantesModificada =
                ArrayList()
            var i = 0
            for (v in UbicacionesObtenidas) {
                if (i % 2 == 0) {
                    Vacio2 += v.getId() + " , " + v.getName() + " , " + v.getGrade() + "\n"
                    ListaEstudiantesModificada!!.add(
                        Estudiambre(
                            v.getId(),
                            v.getName(),
                            v.getGrade()
                        )
                    )
                }
                i++
            }
            EditTextCampo3!!.setText(Vacio2)
        }
        button01 = findViewById<View>(R.id.botonsec) as Button
        // Cuando se da click en el botón
        button01!!.setOnClickListener { Terminar_Regresar_Valores() }
    }

    fun Terminar_Regresar_Valores() {
        val data = Intent()
        data.putParcelableArrayListExtra("student_list_regreso", ListaEstudiantesModificada)
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onBackPressed() { /*AlertDialog ad = adb.create();
            ad.setMessage("Interceptando el botón BACK");
            ad.show();
            Intent data = new Intent();
            data.putExtra("Parametro1",EditTextCampo1.getText().toString()+"-"+CampoApellidoP.getText().toString());
            data.putExtra("Parametro2","Procesamiento de Texto Hecho" );
            setResult(RESULT_OK,data);*/
//finish();
        Terminar_Regresar_Valores()
    }
}