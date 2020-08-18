package tech.raulballeza.lab33kotlin;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends ActionBarActivity {

    private Button button01;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private TextView tv1;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        et1  = (EditText) findViewById(R.id.et_01);
        et2  = (EditText) findViewById(R.id.et_02);
        et3  = (EditText) findViewById(R.id.et_03);
        et4  = (EditText) findViewById(R.id.et_04);
        tv1 = (TextView) findViewById(R.id.textview_01);
        tv2 = (TextView) findViewById(R.id.textview_02);

        addListenerOnButton();
    }

    private void addListenerOnButton() {
        // TODO Auto-generated method stub

        button01  = (Button) findViewById(R.id.boton01_FPrincipal);
        button01.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                String CopiaMensaje1 = et1.getText().toString();
                String CopiaMensaje2 = et2.getText().toString();
                String CopiaMensaje3 = et3.getText().toString();

                Intent intent = new Intent(MainActivity.this, ActividadSecundaria.class);
                intent.putExtra("student", new Estudiambre(CopiaMensaje1,CopiaMensaje2,CopiaMensaje3));

                // INITIALIZE NEW ARRAYLIST AND POPULATE
                Random r = new Random();
                int i1 = r.nextInt(10)+2;
                ArrayList<Estudiambre> ListaEstudiantes= new ArrayList<Estudiambre>();
                int i;
                for (i=0; i<i1; i++) {
                    ListaEstudiantes.add(new Estudiambre(""+i, "Nombre "+i, "Direccion "+(i*2)));
                }

                // EMBED INTO INTENT
                intent.putParcelableArrayListExtra("student_list", ListaEstudiantes);

                // Si se desea que la actividad hija regrese cadenas
                int CodigoPeticion;
                CodigoPeticion=2;
                startActivityForResult (intent,CodigoPeticion);
            }
        });

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2)
        {
            if (data != null)
            {

                String Cadena="";
                List<Estudiambre> UbicacionesObtenidasReg;
                UbicacionesObtenidasReg = data.getParcelableArrayListExtra("student_list_regreso");
                for (Estudiambre v : UbicacionesObtenidasReg) {
                    //Cadena+=v.getGeoPoint_lat()+" , " + v.getGeoPoint_long() + " , " + v.getName() + " , " + v.getAddress() + "\n";
                    Cadena+=v.toString() + "\n";
                }
                et4.setText(Cadena);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
