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

import com.example.doanandroid.R;
import com.example.doanandroid.UpdateActivityTS;
import com.example.doanandroid.model.ThongSoCauThu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdapterThongsoCT extends BaseAdapter {

    Activity context;
    ArrayList<ThongSoCauThu> list;

    public AdapterThongsoCT(Activity context, ArrayList<ThongSoCauThu> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_row_tsct, null);
        TextView txtTen = (TextView) row.findViewById(R.id.txtTen);
        TextView txtSoao = (TextView) row.findViewById(R.id.txtSoao);
        TextView txtVitri = (TextView) row.findViewById(R.id.txtVitri);
        TextView txtTran = (TextView) row.findViewById(R.id.txtTran);
        TextView txtBanthang = (TextView) row.findViewById(R.id.txtBanthang);
        TextView txtThevang = (TextView) row.findViewById(R.id.txtThevang);
        TextView txtThedo = (TextView) row.findViewById(R.id.txtThedo);
        ImageButton ibtnSua = (ImageButton) row.findViewById(R.id.ibtnSua);

        final ThongSoCauThu thongSoCauThu = list.get(i);
        txtTen.setText(thongSoCauThu.tenct);
        txtSoao.setText(thongSoCauThu.soao+"");
        txtVitri.setText(thongSoCauThu.vitri);
        txtTran.setText(thongSoCauThu.sotran+"");
        txtBanthang.setText(thongSoCauThu.banthang+"");
        txtThevang.setText(thongSoCauThu.thevang+"");
        txtThedo.setText(thongSoCauThu.thedo+"");

        ibtnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UpdateActivityTS.class);
                i.putExtra("SOAO", thongSoCauThu.soao);
                context.startActivity(i);
            }
        });
        return row;
    }
    public void sortListByJerseyNumber() {
        Collections.sort(list, new Comparator<ThongSoCauThu>() {
            @Override
            public int compare(ThongSoCauThu cauThu1, ThongSoCauThu cauThu2) {
                return Integer.compare(cauThu1.soao, cauThu2.soao);
            }
        });
        notifyDataSetChanged();
    }
}
