package com.example.doanandroid.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanandroid.AddActivityTV;
import com.example.doanandroid.R;
import com.example.doanandroid.UpdateActivityCTTD;
import com.example.doanandroid.model.ChiTietTranDau;

import java.util.ArrayList;

public class AdapterChitiettrandau extends BaseAdapter {

    Activity context;
    ArrayList<ChiTietTranDau> list;

    public AdapterChitiettrandau(Activity context, ArrayList<ChiTietTranDau> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_row_cttd, null);
        TextView txtNgaythidau = (TextView) row.findViewById(R.id.txtNgaythidau);
        TextView txtHome = (TextView) row.findViewById(R.id.txtHome);
        TextView txtAway = (TextView) row.findViewById(R.id.txtAway);
        TextView txtGoalhome = (TextView) row.findViewById(R.id.txtGoalhome);
        TextView txtGoalaway = (TextView) row.findViewById(R.id.txtGoalaway);
        ImageButton ibtnSuaCTTD = (ImageButton) row.findViewById(R.id.ibtnSuaCTTD);

        final ChiTietTranDau chiTietTranDau = list.get(i);
        txtNgaythidau.setText(chiTietTranDau.ngaythidau);
        txtHome.setText(chiTietTranDau.tenclb_Home);
        txtGoalhome.setText(chiTietTranDau.goalhome+"");
        txtGoalaway.setText(chiTietTranDau.goalaway+"");
        txtAway.setText(chiTietTranDau.tenclb_Away);
        ibtnSuaCTTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UpdateActivityCTTD.class);
                i.putExtra("IDTRANDAU", chiTietTranDau.idtrandau);
                context.startActivity(i);
            }
        });

        return row;
    }
}
