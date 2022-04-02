package com.lleans.spp_kelompok_2.ui.utils.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lleans.spp_kelompok_2.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private final Context context;
    private final List<SpinnerInterface> list;
    private final boolean focused;

    public SpinnerAdapter(Context context, List<SpinnerInterface> list, boolean focused) {
        this.context = context;
        this.list = list;
        this.focused = focused;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_spinner, parent, false);
        int primary = context.getResources().getColor(R.color.red);

        TextView text = view.findViewById(R.id.text);

        if (focused) {
            text.setTextColor(primary);
        }
        text.setText(list.get(position).getName());

        return view;
    }
}
