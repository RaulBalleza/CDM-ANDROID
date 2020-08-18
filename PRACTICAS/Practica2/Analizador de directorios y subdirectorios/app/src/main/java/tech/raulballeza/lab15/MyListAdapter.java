package tech.raulballeza.lab15;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static tech.raulballeza.lab15.MainActivity.ruta;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    String[] imagenes;
    /*private final String[] maintitle;
    private final String[] subtitle;
    private final Bitmap[] imgid;*/

    public MyListAdapter(Activity context, String[] imagenes) {
        super(context, R.layout.mylist, imagenes);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.imagenes=imagenes;
        /*this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.imgid=imgid;*/

    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.nombre);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.peso);

        if (MainActivity.isDirectory(imagenes[position])){
            titleText.setText(imagenes[position]);
            imageView.setImageResource(R.drawable.folder);
        }else{
            BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(),ruta+"/"+imagenes[position]);
            double tamano=0;
            try{
                tamano = sizeOf(bitmapDrawable.getBitmap())/1024;
                imageView.setImageDrawable(bitmapDrawable);

            }catch (Exception e){
                e.printStackTrace();
            }

            titleText.setText(imagenes[position]);
            //imageView.setImageResource(R.drawable.ic_launcher_background);
            subtitleText.setText(tamano+" Kb");
        }


        return rowView;

    };

    public static int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else if (Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
            return data.getByteCount();
        } else{
            return data.getAllocationByteCount();
        }
    }
}