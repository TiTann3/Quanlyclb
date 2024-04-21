package com.example.doanandroid.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.doanandroid.Database.DatabaseHelper;
import com.example.doanandroid.R;
import com.example.doanandroid.UpdateActivityCLB;
import com.example.doanandroid.model.DanhSachCLB;
import com.example.doanandroid.model.ThanhVien;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdapterCLB extends BaseAdapter {

    Activity context;
    ArrayList<DanhSachCLB> list;

    public AdapterCLB(Activity context, ArrayList<DanhSachCLB> list){
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
        View row = inflater.inflate(R.layout.listview_row_clb, null);
        ImageView imgClb = (ImageView) row.findViewById(R.id.imgClb);
        TextView txtTT = (TextView) row.findViewById(R.id.txtTT);
        TextView txtClb = (TextView) row.findViewById(R.id.txtClb);
        ImageButton ibtnUpdate = (ImageButton) row.findViewById(R.id.ibtnUpdate);
        ImageButton ibtnDelete = (ImageButton) row.findViewById(R.id.ibtnDelete);


        final DanhSachCLB danhSachCLB = list.get(i);
        int position = i+1;
        txtTT.setText(position + "");
        txtClb.setText(danhSachCLB.tenclb);

        Bitmap bmClb = BitmapFactory.decodeByteArray(danhSachCLB.anh, 0, danhSachCLB.anh.length);
        imgClb.setImageBitmap(bmClb);

        ibtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UpdateActivityCLB.class);
                i.putExtra("IDCLB", danhSachCLB.idclb);
                context.startActivity(i);
            }
        });
        ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa clb này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(danhSachCLB.idclb);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }
    public void delete(int idClb) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(context, "Quanly_CLB");
        int deletedRows = database.delete("danhsachclb", "idclb = ?", new String[]{String.valueOf(idClb)});
        if (deletedRows > 0) {
            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            list.clear();
            Cursor cursor = database.rawQuery("SELECT * FROM danhsachclb", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String tenclb = cursor.getString(1);
                byte[] anh = cursor.getBlob(2);
                list.add(new DanhSachCLB(id, tenclb, anh));
            }
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
