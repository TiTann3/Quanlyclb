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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.doanandroid.Database.DatabaseHelper;
import com.example.doanandroid.MainChiTietCauThu;
import com.example.doanandroid.R;
import com.example.doanandroid.UpdateActivityTV;
import com.example.doanandroid.model.ThanhVien;

import java.util.ArrayList;

public class AdapterThanhVien extends BaseAdapter {

    Activity context;
    ArrayList<ThanhVien> list;

    public AdapterThanhVien(Activity context, ArrayList<ThanhVien> list) {
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
        View row = inflater.inflate(R.layout.listview_row_tv, null);
        ImageView imgAvatar = (ImageView) row.findViewById(R.id.imgAvatar);
        TextView txtMatv = (TextView) row.findViewById(R.id.txtMatv);
        TextView txtHoten = (TextView) row.findViewById(R.id.txtHoten);
        TextView txtNgaysinh = (TextView) row.findViewById(R.id.txtNgaysinh);
        TextView txtChucvu = (TextView) row.findViewById(R.id.txtChucvu);
        TextView txtQuoctich = (TextView) row.findViewById(R.id.txtQuoctich);
        Button btnSua = (Button) row.findViewById(R.id.btnSua);
        Button btnXoa = (Button) row.findViewById(R.id.btnXoa);
        LinearLayout linerClick = (LinearLayout) row.findViewById(R.id.linerClick);

        final ThanhVien thanhVien = list.get(i);
        int position = i+1;
        txtMatv.setText(position + "");
        txtHoten.setText(thanhVien.hoten);
        txtNgaysinh.setText(thanhVien.ngaysinh);
        txtChucvu.setText(thanhVien.chucvu);
        txtQuoctich.setText(thanhVien.quoctich);

        Bitmap bmAvatar = BitmapFactory.decodeByteArray(thanhVien.anh, 0, thanhVien.anh.length);
        imgAvatar.setImageBitmap(bmAvatar);

        linerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MainChiTietCauThu.class);
                i.putExtra("MATV", thanhVien.matv);
                context.startActivity(i);
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UpdateActivityTV.class);
                i.putExtra("MATV", thanhVien.matv);
                context.startActivity(i);
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa thành viên này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(thanhVien.matv);
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

    public void delete(int idThanhvien) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(context, "Quanly_CLB");
        int deletedRows = database.delete("ThanhVien", "matv = ?", new String[]{String.valueOf(idThanhvien)});
        if (deletedRows > 0) {
            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            list.clear();
            Cursor cursor = database.rawQuery("SELECT * FROM THANHVIEN", null);
            while (cursor.moveToNext()) {
                int matv = cursor.getInt(0);
                String hoten = cursor.getString(1);
                String ngaysinh = cursor.getString(2);
                String chucvu = cursor.getString(3);
                String quoctich = cursor.getString(4);
                byte[] anh = cursor.getBlob(5);
                list.add(new ThanhVien(matv, hoten, ngaysinh, chucvu, quoctich, anh));
            }
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }

}
