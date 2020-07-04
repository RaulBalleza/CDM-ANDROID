package tech.raulballeza.p_intermedio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "restaurantes.json";

    //Codigos de permisos
    private static final int REQUEST_ID_SEND_SMS = 102;
    private static final int REQUEST_ID_READ_PHONE_STATE = 103;

    //Numero a donde se va a enviar el SMS
    private static final String PREF_USER_MOBILE_PHONE = "8311632213";


    //Contexto
    Context context;
    //String de la orden
    StringBuilder orden;
    //Lista de Restaurantes
    ArrayList<Restaurente> restaurenteArrayList;
    //Lista de menu
    ListView restauranteMenu;
    //Restaurante escaneado
    Restaurente restaurenteScan;
    RestaurantMenuAdapter adapter;
    //Botones
    Button btn_scan, btn_ordenar;
    //TextViews
    TextView txt_mesa, txt_restaurante; //txt_prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissionOnly();

        context = this;

        //Inicializacion de TextViews
        txt_mesa = (TextView) findViewById(R.id.txt_mesa);
        //txt_prueba = (TextView) findViewById(R.id.txt_prueba);
        txt_restaurante = (TextView) findViewById(R.id.txt_restaurante);

        //Inicializando Lista de menu
        restauranteMenu = (ListView) findViewById(R.id.list_menu);

        //Inicializacion de botones
        btn_ordenar = (Button) findViewById(R.id.btn_ordenar);
        btn_ordenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] checked = adapter.getmChecked();
                orden = new StringBuilder("Los clientes de la " + txt_mesa.getText() + " han ordenado: \n");
                for (int i = 0; i < checked.length; i++) {
                    if (checked[i]) {
                        orden.append(adapter.getItem(i).getKey()).append(" ").append(adapter.getItem(i).getValue()).append("\n");
                    }
                }
                SmsHelper.sendDebugSms(PREF_USER_MOBILE_PHONE, orden.toString());
                Toast.makeText(getApplicationContext(), "Enviando SMS", Toast.LENGTH_SHORT).show();
            }
        });

        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });

        try {
            load_restaurants();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void load_restaurants() throws JSONException, IOException {
        //File file = new File(context.getFilesDir(), FILE_NAME);
        //FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.restaurants)));
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        // This responce will have Json Format String
        String in = stringBuilder.toString();
        //Toast.makeText(context, "Codigo: " + in, Toast.LENGTH_SHORT).show();


        restaurenteArrayList = new ArrayList<>();
        JSONObject reader = new JSONObject(in);
        JSONArray restaurantes = reader.getJSONArray("restaurants");
        for (int i = 0; i < restaurantes.length(); i++) {
            JSONObject restaurante = restaurantes.getJSONObject(i);
            int code = restaurante.getInt("code");
            //Toast.makeText(context, "Codigo: " + code, Toast.LENGTH_SHORT).show();
            String name = restaurante.getString("name");
            //Toast.makeText(context, "Nombre: " + name, Toast.LENGTH_SHORT).show();
            Restaurente restaurenteObj = new Restaurente(code, name);
            JSONArray menu = restaurante.getJSONArray("menu");
            for (int j = 0; j < menu.length(); j++) {
                JSONObject producto = menu.getJSONObject(j);
                restaurenteObj.addToMenu(producto.getString("producto"), producto.getDouble("precio"));
            }
            restaurenteArrayList.add(restaurenteObj);


        }
        Toast.makeText(this, "Restaurantes cargados correctamente", Toast.LENGTH_SHORT).show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                //Toast.makeText(this, "CODIGO ESCANDEADO CON EXITO.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Mostrando menú...", Toast.LENGTH_SHORT).show();
                int restaurante = Integer.parseInt(contents.split("-")[0]);
                int mesa = Integer.parseInt(contents.split("-")[1]);
                txt_mesa.setText("Mesa: " + mesa);
                load_restaurant_menu(restaurante);
            } else if (resultCode == RESULT_CANCELED) {
                //Handle cancel
            }
        }
    }

    private void load_restaurant_menu(int id) {
        for (Restaurente restaurante : restaurenteArrayList) {
            if (restaurante.code == id)
                restaurenteScan = restaurante;
        }
        txt_restaurante.setText(String.format("Restaurante: %s", restaurenteScan.getName()));
        //txt_prueba.setText(restaurenteScan.toString());
        adapter = new RestaurantMenuAdapter(restaurenteScan.menu);
        restauranteMenu.setAdapter(adapter);
    }

    private void askPermissionOnly() {
        this.askPermission(REQUEST_ID_SEND_SMS, Manifest.permission.SEND_SMS);
        this.askPermission(REQUEST_ID_READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);

    }

    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(new String[]{permissionName}, requestId);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_ID_SEND_SMS: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(getApplicationContext(), "Permission Escritura Concedido!", Toast.LENGTH_SHORT).show();
                }
                break;
                case REQUEST_ID_READ_PHONE_STATE: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(getApplicationContext(), "Permission Lectura Concedido!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        } else
            Toast.makeText(getApplicationContext(), "Permission Cancelled!" + requestCode, Toast.LENGTH_SHORT).show();
    }
}
