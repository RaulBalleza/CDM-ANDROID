package tech.raulballeza.p_recuperacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceHolder;

public class MainActivity extends AppCompatActivity {

    MySurfaceView MSV;
    SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MSV = (MySurfaceView) findViewById(R.id.Surface);
        surfaceHolder = MSV.getHolder();

    }
}