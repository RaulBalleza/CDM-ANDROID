package tech.raulballeza.p_intermedio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "restaurantes.json";
    private static final int REQUEST_ID_SEND_SMS = 102;

    //Codigos de permisos
    private static final int REQUEST_ID_READ_PHONE_STATE = 103;
    private static final int REQUEST_ID_INTERNET = 104;
    //Numero a donde se va a enviar el SMS
    private static final String PREF_USER_MOBILE_PHONE = "8311632213";
    static String URL_API = "https://raulballeza.tech";
    RequestQueue consulta;
    //Variables globales auxiliares
    int Mesa;
    int last_id;
    JSONArray productos = null;
    boolean[] checked;
    //Contexto
    Context context;
    //String de la orden
    StringBuilder orden;
    //Lista de Restaurantes
    ArrayList<Restaurente> restaurenteArrayList;
    //Lista de menu
    ListView restauranteMenu;
    Restaurente res;
    RestaurantMenuAdapter adapter;
    //Botones
    Button btn_scan, btn_ordenar;
    //Restaurante escaneado
    //TextViews
    TextView txt_mesa, txt_restaurante, txt_total; //txt_prueba;
    private String URL_API_PRODUCTOS = "";
    private String URL_API_RESTAURANTES = URL_API + "/api/restaurants";
    private String URL_API_PEDIDOS = URL_API + "/api/new/pedido";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, activity_registro.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissionOnly();

        Toolbar toolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        //Inicializacion de TextViews
        txt_mesa = (TextView) findViewById(R.id.txt_mesa);
        txt_total = (TextView) findViewById(R.id.txt_total);

        //txt_prueba = (TextView) findViewById(R.id.txt_prueba);
        txt_restaurante = (TextView) findViewById(R.id.txt_restaurante);

        //Inicializando Lista de menu
        restauranteMenu = (ListView) findViewById(R.id.list_menu);

        //Inicializacion de botones
        btn_ordenar = (Button) findViewById(R.id.btn_ordenar);
        btn_ordenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = adapter.getmChecked();
                int contadorChechados = 0;
                orden = new StringBuilder("Los clientes de la " + txt_mesa.getText() + " han ordenado: \n");
                Double total = 0d;
                for (int i = 0; i < checked.length; i++) {
                    if (checked[i]) {
                        orden.append(adapter.getItemCantidad(i)).append(" " + adapter.getItem(i).getKey()).append(" = $" + adapter.getItem(i).getValue() * adapter.getItemCantidad(i) + ",").append("\n");
                        contadorChechados++;
                    }
                    total = total + adapter.getItem(i).getValue() * adapter.getItemCantidad(i);
                }
                orden.append("Total: $" + total);
                //Toast.makeText(getApplicationContext(), orden.toString(), Toast.LENGTH_SHORT).show();
                //SmsHelper.sendDebugSms(PREF_USER_MOBILE_PHONE, orden.toString());
                //Toast.makeText(getApplicationContext(), "Enviando SMS", Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", "CHECADOS: "+contadorChechados);

                if (contadorChechados>0){
                    post_pedido(total);
                    txt_total.setText("Total: $"+total);
                }
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
        //load_restaurant_menu("221099");
/*
        try {
            load_restaurants();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }*/
    }

    private void post_productos() {
        String nombre;
        int id;
        URL_API_PRODUCTOS = URL_API + "/api/pedidohasproductos";
        for (int j = 0; j < productos.length(); j++) {
            JSONObject producto = null;
            try {
                producto = productos.getJSONObject(j);
                nombre = producto.getString("nombre");
                id = producto.getInt("id");

                for (int i = 0; i < checked.length; i++) {
                    Map<String, Object> parametros = new HashMap<>();

                    if (checked[i]) {
                        if (adapter.getItem(i).getKey().equals(nombre)) {
                            Log.d("DEBUG", "INSERTANDO PRODUCTOS PEDIDOS");

                            Log.d("VALUES", String.valueOf(last_id));
                            Log.d("VALUES", String.valueOf(id));
                            Log.d("VALUES", String.valueOf(adapter.getItemCantidad(i)));
                            parametros.put("id_pedido", String.valueOf(last_id));
                            parametros.put("id_producto", String.valueOf(id));
                            parametros.put("cantidad", String.valueOf(adapter.getItemCantidad(i)));

                            CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, URL_API_PRODUCTOS, parametros,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("DEBUG", response.toString());

                                            //Toast.makeText(context, "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("DEBUG", error.toString());
                                    Toast.makeText(context, "OPERACION FALLIDA", Toast.LENGTH_SHORT).show();
                                }
                            });
                            if (adapter.getItemCantidad(i) > 0) {
                                consulta.add(customJsonRequest);
                            }

                        }
                        //orden.append(adapter.getItemCantidad(i)).append(" " + adapter.getItem(i).getKey()).append(" = $" + adapter.getItem(i).getValue() * adapter.getItemCantidad(i) + ",").append("\n");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /*
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
                String code = restaurante.get("code");
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
    */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                //Toast.makeText(this, "CODIGO ESCANDEADO CON EXITO.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Mostrando menú...", Toast.LENGTH_SHORT).show();
                //int restaurante = Integer.parseInt(contents.split("-")[0]);
                String restaurante = contents.split("-")[0];

                int mesa = Integer.parseInt(contents.split("-")[1]);
                Mesa = mesa;
                txt_mesa.setText("Mesa: " + mesa);
                //load_restaurant_menu(restaurante);
                cargar_lista(restaurante);
            } else if (resultCode == RESULT_CANCELED) {
                //Handle cancel
            }
        }
    }

    private void post_pedido(final Double total) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("mesa", String.valueOf(Mesa));
        parametros.put("total", String.valueOf(total));
        parametros.put("estado", "Sin completar");
        parametros.put("codigo_restaurant", res.getCode().toString());
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, URL_API_PEDIDOS, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        try {
                            Log.d("DEBUG", "ID: " + response.getInt("id"));
                            last_id = response.getInt("id");
                            post_productos();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(context, "SU PEDIDO SE AH REALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG", error.toString());
                Toast.makeText(context, "OPERACION FALLIDA", Toast.LENGTH_SHORT).show();
            }
        });
        consulta.add(customJsonRequest);

        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_API_PEDIDOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("DEBUG", response.toString());
                Toast.makeText(context, "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG", "BAD RESPONSE");
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return super.getParams();
            }
        };
        consulta.add(stringRequest);*/
    }

    private void load_restaurant_menu(String id) {
       /* for (Restaurente restaurante : restaurenteArrayList) {
            if (restaurante.code == id)
                restaurenteScan = restaurante;
        }
        txt_restaurante.setText(String.format("Restaurante: %s", restaurenteScan.getName()));
        //txt_prueba.setText(restaurenteScan.toString());
        adapter = new RestaurantMenuAdapter(restaurenteScan.menu);
        restauranteMenu.setAdapter(adapter);*/
    }

    public void cargar_productos() {
        //String url_menu = URL_API + "/api/" + res.getCode() + "/productos";
        URL_API_PRODUCTOS = URL_API + "/api/" + res.getCode() + "/productos";
        JsonArrayRequest productosJSON = new JsonArrayRequest(URL_API_PRODUCTOS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                productos = response;
                //Guardando cada uno de los registros de archivos en el objeto array de la clase Archivo.
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject producto = null;
                    try {
                        producto = response.getJSONObject(i);
                        Double precio = producto.getDouble("precio");
                        String nombre = producto.getString("nombre");
                        String estado = producto.getString("estado");
                        Log.d("DEBUG", precio.toString());
                        if (Objects.equals(estado, "Activo")) {
                            res.addToMenu(producto.getString("nombre"), producto.getDouble("precio"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), res.toString(), Toast.LENGTH_LONG).show();

                adapter = new RestaurantMenuAdapter(res.menu);
                restauranteMenu.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
                Log.e("Error", error.toString());
            }
        }
        );
        consulta = Volley.newRequestQueue(this);
        consulta.add(productosJSON);
    }

    //Función encargada de cargar la lista con datos obtenidos desde la base de datos remota cdm_archivos.
    public void cargar_lista(final String codigo) {
        //String url_restaurante = URL_API + "/api/restaurants";
        //URL_API_RESTAURANTES = URL_API+"/api/restaurantes";
        JsonArrayRequest arregloJson = new JsonArrayRequest(URL_API_RESTAURANTES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Guardando cada uno de los registros de archivos en el objeto array de la clase Archivo.
                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject restaurante = null;
                    try {
                        restaurante = response.getJSONObject(i);
                        String code = restaurante.getString("codigo");
                        String nombre = restaurante.getString("nombre");
                        //Log.e("Error", "CODE: "+code);
                        //Log.e("Error", "CODIGO: "+codigo);

                        if (code.toLowerCase().toString().equals(codigo.toLowerCase().toString())) {
                            //Log.e("Error", "RESTAURANTE ENCONTRADO");
                            txt_restaurante.setText(String.format("Restaurante: %s", nombre));

                            res = new Restaurente(code, nombre);
                            cargar_productos();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
                Log.e("Error", error.toString());
            }
        }
        );

        //Toast.makeText(getApplicationContext(), res.toString(), Toast.LENGTH_LONG).show();

        consulta = Volley.newRequestQueue(this);
        consulta.add(arregloJson);
    }

    private void askPermissionOnly() {
        this.askPermission(REQUEST_ID_INTERNET, Manifest.permission.INTERNET);
        //this.askPermission(REQUEST_ID_SEND_SMS, Manifest.permission.SEND_SMS);
        //this.askPermission(REQUEST_ID_READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);

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
                case REQUEST_ID_INTERNET: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(getApplicationContext(), "Permission Internet Concedido!", Toast.LENGTH_SHORT).show();
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
