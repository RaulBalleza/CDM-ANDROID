package tech.raulballeza.lab33kotlin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : ActivityCompat() {
    private var button01: Button? = null
    private var et1: EditText? = null
    private var et2: EditText? = null
    private var et3: EditText? = null
    private var et4: EditText? = null
    private var tv1: TextView? = null
    private var tv2: TextView? = null
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        et1 = findViewById(R.id.et_01) as EditText?
        et2 = findViewById(R.id.et_02) as EditText?
        et3 = findViewById(R.id.et_03) as EditText?
        et4 = findViewById(R.id.et_04) as EditText?
        tv1 = findViewById(R.id.textview_01) as TextView?
        tv2 = findViewById(R.id.textview_02) as TextView?
        addListenerOnButton()
    }

    private fun addListenerOnButton() { // TODO Auto-generated method stub
        button01 = findViewById(R.id.boton01_FPrincipal) as Button?
        button01!!.setOnClickListener {
            val CopiaMensaje1 = et1!!.text.toString()
            val CopiaMensaje2 = et2!!.text.toString()
            val CopiaMensaje3 = et3!!.text.toString()
            val intent = Intent(this@MainActivity, ActividadSecundaria::class.java)
            intent.putExtra("student", Estudiambre(CopiaMensaje1, CopiaMensaje2, CopiaMensaje3))
            // INITIALIZE NEW ARRAYLIST AND POPULATE
            val r = Random()
            val i1 = r.nextInt(10) + 2
            val ListaEstudiantes =
                ArrayList<Estudiambre>()
            var i: Int
            i = 0
            while (i < i1) {
                ListaEstudiantes.add(Estudiambre("" + i, "Nombre $i", "Direccion " + i * 2))
                i++
            }
            // EMBED INTO INTENT
            intent.putParcelableArrayListExtra("student_list", ListaEstudiantes)
            // Si se desea que la actividad hija regrese cadenas
            val CodigoPeticion: Int
            CodigoPeticion = 2
            startActivityForResult(intent, CodigoPeticion)
        }
    }

    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (data != null) {
                var Cadena = ""
                val UbicacionesObtenidasReg: List<Estudiambre>
                UbicacionesObtenidasReg = data.getParcelableArrayListExtra("student_list_regreso")
                for (v in UbicacionesObtenidasReg) { //Cadena+=v.getGeoPoint_lat()+" , " + v.getGeoPoint_long() + " , " + v.getName() + " , " + v.getAddress() + "\n";
                    Cadena += v.toString() + "\n"
                }
                et4!!.setText(Cadena)
            }
        }
    }

    fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}