package tech.raulballeza.p_intermedio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    private boolean[] mChecked;
    private View result;

    public RestaurantMenuAdapter(Map<String, Double> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        mChecked = new boolean[getCount()];
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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