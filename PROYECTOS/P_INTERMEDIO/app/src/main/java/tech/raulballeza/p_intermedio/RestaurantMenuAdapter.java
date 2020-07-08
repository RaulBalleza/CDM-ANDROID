package tech.raulballeza.p_intermedio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class RestaurantMenuAdapter extends BaseAdapter {
    private final ArrayList mData;

    public boolean[] getmChecked() {
        return mChecked;
    }
    public int[] cantidades;
    private boolean[] mChecked;
    private View result;

    public RestaurantMenuAdapter(Map<String, Double> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        mChecked = new boolean[getCount()];
        cantidades = new int[getCount()];
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, Double> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    public long getItemCantidad(int position) {
        return cantidades[position];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_menu, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, Double> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((ImageView) result.findViewById(R.id.iv_checked)).setBackgroundResource(R.drawable.ic_baseline_check_box_outline_blank_24);
        ((TextView) result.findViewById(R.id.txt_producto)).setText(item.getKey());
        ((TextView) result.findViewById(R.id.txt_precio)).setText(item.getValue().toString());
        //((TextView) result.findViewById(R.id.txt_cantidad)).setText("0");
        final TextView cantidad = ((TextView) result.findViewById(R.id.txt_cantidad));
        ((Button) result.findViewById((R.id.btn_aumentarCantidad))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(cantidad.getText().toString());
                a = a+1;
                cantidades[position] = a;
                cantidad.setText(Integer.toString(a));
            }
        });
        ((Button) result.findViewById((R.id.btn_disminuirCantidad))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(cantidad.getText().toString());
                if (a>=0)
                    if (a<=0)
                        a=0;
                   else
                        a = a-1;

                cantidades[position] = a;
                cantidad.setText(Integer.toString(a));
            }
        });
        CheckBox cBox = (CheckBox) result.findViewById(R.id.checkbox_ordenar);
        cBox.setTag(position);
        cBox.setChecked(mChecked[position]);
        cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mChecked[(Integer)buttonView.getTag()] = isChecked;
            }
        });
        return result;
    }
}