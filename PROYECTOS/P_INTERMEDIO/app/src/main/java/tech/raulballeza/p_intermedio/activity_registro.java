package tech.raulballeza.p_intermedio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_registro extends AppCompatActivity {
    TextView url;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        url = (TextView) findViewById(R.id.txt_url);
        url.setText(MainActivity.URL_API);

        save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.URL_API = url.getText().toString();
                finish();
            }
        });
    }
}