package tech.raulballeza.qrdecoder2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Button BT1, btn_limpiar;
    TextView tv_costo;
    ArrayList<String> productos;
    ListView lista_productos;
    float total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productos = new ArrayList<>();
        tv_costo = (TextView) findViewById(R.id.tv_costo);
        tv_costo.setText("$ "+total);

        lista_productos = (ListView) findViewById(R.id.lista_productos);

        BT1 = (Button) findViewById(R.id.button1);

        BT1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);

            }
        });

        btn_limpiar = (Button) findViewById(R.id.btn_limpiar);

        btn_limpiar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarProductos();
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                String producto = contents.split(",")[0];
                float costo = Float.parseFloat(contents.split(",")[1]);
                total =  total + costo;
                productos.add(producto);
                //TextView TV1 = (TextView) findViewById(R.id.textView1);
                //TV1.setText(contents);
                actualizarProductos();
            } else if (resultCode == RESULT_CANCELED) {
                //Handle cancel
            }
        }
    }

    private void actualizarProductos() {
        ArrayAdapter<String> productosAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productos);
        lista_productos.setAdapter(productosAdapter);
        tv_costo.setText(String.format("$ %s", total));
    }

    private void limpiarProductos() {
        productos = new ArrayList<>();
        lista_productos.setAdapter(null);
        total = 0;
        tv_costo.setText(String.format("$ %s", total));
    }

}
