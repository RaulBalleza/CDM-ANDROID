package tech.raulballeza.lab15;

/*
Requisitos:
* ANDROID STUDIO ACTUALIZADO!!
* Tener CONEXION A INTERNET
*
Paso 1:
Agregar la siguiente linea al final de la seccion dependencies
del archivo build.graddle (Module:app)
    // Libreria requerida para el explorador de archivos
    // En los mas recientes se sustituye "compile" por "implementation"

Paso 2:
Sincronizar proyecto => Se conecta a internet y descarga los archivo necesarios de los repositorios

Paso 3:
Agregar el sigueintei import
import com.obsez.android.lib.filechooser.ChooserDialog;
Si no hubo ningún problema, esa linea no marcará error

// Documentación:
https://github.com/hedzr/android-file-chooser


 */

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    TextView TV1;
    ImageView IV;
    Button B1, B2, B3, B4;

    MainActivity MA;
    String[] imagenes;
    ArrayAdapter<String> imagenesArrayAdapter;
    ListView lista;
    public static String ruta;
    public static String ultimaRuta;
    static String startingDir = Environment.getExternalStorageDirectory().toString();

    View.OnClickListener ListenerComun = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonFolder:
                    String input = ruta;
                    int index = input.lastIndexOf("/");
                    if (index > 0) {
                        input = input.substring(0, index); // or index + 1 to keep slash
                        Toast.makeText(MainActivity.this, "FOLDER: " + input, Toast.LENGTH_SHORT).show();
                        showImageList(input);
                    }
                    break;
                case R.id.btn_limpiar:
                    lista.setAdapter(null);
                    TV1.setText("Sin directorio seleccionado");
                    break;
                case R.id.btn_guardar:
                    new ChooserDialog(MainActivity.this)
                            .withFilter(true, false)
                            .withStartFile(startingDir)
                            // to handle the result(s)
                            .withChosenListener(new ChooserDialog.Result() {
                                @Override
                                public void onChoosePath(String path, File pathFile) {
                                    Toast.makeText(MainActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                    TV1.setText("Ruta Directorio Seleccionado : \n" + path);

                                    saveAnalysis(path);
                                }
                            })
                            .build()
                            .show();
                    break;
                /*case R.id.Boton01:
                    //Toast.makeText(getApplicationContext(),"Boton1",Toast.LENGTH_SHORT).show();
                    // Escoger un TXT
                    new ChooserDialog(MainActivity.this)
                            .withFilter(false, false, "txt", "TXT")
                            .withStartFile(startingDir)
                            .withResources(R.string.title_choose, R.string.title_choose, R.string.dialog_cancel)
                            .withChosenListener(new ChooserDialog.Result() {
                                @Override
                                public void onChoosePath(String path, File pathFile) {
                                    Toast.makeText(MainActivity.this, "FILE: " + path, Toast.LENGTH_SHORT).show();
                                    TV1.setText("Ruta Archivo Seleccionado : \n" + path);
                                }
                            })
                            .build()
                            .show();
                    break;
                case R.id.Boton02:
                    //Toast.makeText(getApplicationContext(),"Boton2",Toast.LENGTH_SHORT).show();
                    // Escoger una imagen
                    new ChooserDialog(MainActivity.this)
                            .withFilter(false, false, "jpg", "jpeg", "png")
                            .withStartFile(startingDir)
                            .withResources(R.string.title_choose, R.string.title_choose, R.string.dialog_cancel)
                            .withChosenListener(new ChooserDialog.Result() {
                                @Override
                                public void onChoosePath(String path, File pathFile) {
                                    Toast.makeText(MainActivity.this, "FILE: " + path, Toast.LENGTH_SHORT).show();
                                    TV1.setText("Ruta Archivo Seleccionado : \n" + path);
                                }
                            })
                            .build()
                            .show();
                    break;*/
                case R.id.Boton03:
                    //Toast.makeText(getApplicationContext(),"Boton3",Toast.LENGTH_SHORT).show();
                    // Escoger un Directorio
                    new ChooserDialog(MainActivity.this)
                            .withFilter(true, false)
                            .withStartFile(startingDir)
                            // to handle the result(s)
                            .withChosenListener(new ChooserDialog.Result() {
                                @Override
                                public void onChoosePath(String path, File pathFile) {
                                    Toast.makeText(MainActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                    TV1.setText("Ruta Directorio Seleccionado : \n" + path);

                                    showImageList(path);
                                }
                            })
                            .build()
                            .show();
                    break;
                default:
                    break;
            }
        }
    };

    private void saveAnalysis(String path) {
        try {
            File root = new File(path, "Analisis.txt");
            FileWriter writer = new FileWriter(root);
            String body = "ANALISIS DE DIRECTORIO\n";
            for (String data : imagenes) {
                body = body + data + ",";
            }

            writer.append(body);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showImageList(String path) {
        File file = new File(path);

        if (file.exists()) {
            //String[] fileListVideo = file.list();
            imagenes = file.list();
            //Collections.addAll(imagenes, fileListVideo);
        }
        ultimaRuta = ruta;
        ruta = path;
        laodImages(path);
    }

    private void laodImages(String path) {
        imagenesArrayAdapter = new MyListAdapter(this, imagenes);
        lista.setAdapter(imagenesArrayAdapter);

        /*Toast.makeText(MainActivity.this, path+"/"+imagenes[0], Toast.LENGTH_SHORT).show();
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),path+"/"+imagenes[0]);
        IV.setImageDrawable(bitmapDrawable);*/
    }

    @Override
    public void onBackPressed() {
        showImageList(ultimaRuta);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissionOnly();

        MA = this;
        ruta = startingDir;
        TV1 = (TextView) findViewById(R.id.TV1);
        B1 = (Button) findViewById(R.id.btn_limpiar);
        B2 = (Button) findViewById(R.id.btn_guardar);
        B3 = (Button) findViewById(R.id.Boton03);
        B4 = findViewById(R.id.buttonFolder);
        //IV = findViewById(R.id.avatar);
        lista = findViewById(R.id.lista);
        B1.setOnClickListener(ListenerComun);
        B2.setOnClickListener(ListenerComun);
        B3.setOnClickListener(ListenerComun);
        B4.setOnClickListener(ListenerComun);
        //imagenes = new ArrayList<>();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDirectory(imagenes[position])) {
                    Toast.makeText(MainActivity.this, "DIRECTORIO: " + ruta + "/" + imagenes[position], Toast.LENGTH_SHORT).show();
                    showImageList(ruta + "/" + imagenes[position]);
                }
            }
        });

    }

    static boolean isDirectory(String imagen) {
        File file = new File(ruta + "/" + imagen);
        return file.isDirectory();
    }

    private void askPermissionOnly() {
        this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        this.askPermission(REQUEST_ID_READ_PERMISSION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);

    }


    // With Android Level >= 23, you have to ask the user
    // for permission with device (For example read/write data on the device).
    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = checkSelfPermission(permissionName);


            if (permission != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }

    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_ID_READ_PERMISSION: {
                    if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Permission Lectura Concedido!", Toast.LENGTH_SHORT).show();
                    }
                }
                case REQUEST_ID_WRITE_PERMISSION: {
                    if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Permission Escritura Concedido!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }
}
