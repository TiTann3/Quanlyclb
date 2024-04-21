package com.example.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doanandroid.Database.DatabaseHelper;

public class MainChiTietCauThu extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";

    int id = -1;


    ImageView imgAvatarChitiet, GoBackIconChitiet;
    TextView txtMatvChitiet, txtTenChitiet, txtNgaysinhChitiet, txtQuoctichChitiet, txtSoaoChitiet, txtVitriChitiet,
            txtSotranChitiet, txtSobanChitiet, txtSothevangChitiet, txtSothedoChitiet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chi_tiet_cau_thu);
        addControls();
        addEvent();
        initUI();
    }

    private void initUI() {
        Intent i = getIntent();
        id = i.getIntExtra("MATV", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT a.matv, a.hoten, a.ngaysinh, a.quoctich, b.soao, b.vitri, b.sotran, b.banthang, " +
                "b.thevang, b.thedo, a.anh FROM THANHVIEN a, THONGSOCAUTHU b WHERE a.matv = b.matv And b.matv = ?", new String[]{id + ""});

        if (cursor != null && cursor.moveToFirst()) {
            int matv = cursor.getInt(0);
            String ten = cursor.getString(1);
            String ngaysinh = cursor.getString(2);
            String quoctich = cursor.getString(3);
            int soao = cursor.isNull(4) ? 0 : cursor.getInt(4);
            String vitri = cursor.isNull(5) ? "" : cursor.getString(5);
            int sotran = cursor.isNull(6) ? 0 : cursor.getInt(6);
            int banthang = cursor.isNull(7) ? 0 : cursor.getInt(7);
            int thevang = cursor.isNull(8) ? 0 : cursor.getInt(8);
            int thedo = cursor.isNull(9) ? 0 : cursor.getInt(9);
            byte[] anh = cursor.getBlob(10);

            Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);
            imgAvatarChitiet.setImageBitmap(bitmap);

            txtMatvChitiet.setText(String.valueOf(matv));
            txtTenChitiet.setText(ten);
            txtNgaysinhChitiet.setText(ngaysinh);
            txtQuoctichChitiet.setText(quoctich);
            txtSoaoChitiet.setText(String.valueOf(soao));
            txtVitriChitiet.setText(vitri);
            txtSotranChitiet.setText(String.valueOf(sotran));
            txtSobanChitiet.setText(String.valueOf(banthang));
            txtSothevangChitiet.setText(String.valueOf(thevang));
            txtSothedoChitiet.setText(String.valueOf(thedo));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Không tìm thấy thông tin thành viên.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private void addControls() {
        GoBackIconChitiet = (ImageView) findViewById(R.id.GoBackIconChitiet);
        imgAvatarChitiet = (ImageView) findViewById(R.id.imgAvatarChiTiet);
        txtMatvChitiet = (TextView) findViewById(R.id.txtMatvChiTiet);
        txtTenChitiet = (TextView) findViewById(R.id.txtTenChitiet);
        txtNgaysinhChitiet = (TextView) findViewById(R.id.txtNgaysinhChitiet);
        txtQuoctichChitiet = (TextView) findViewById(R.id.txtQuoctichChitiet);
        txtSoaoChitiet = (TextView) findViewById(R.id.txtSoaoChitiet);
        txtVitriChitiet = (TextView) findViewById(R.id.txtVitriChitiet);
        txtSotranChitiet = (TextView) findViewById(R.id.txtSotranChitiet);
        txtSobanChitiet = (TextView) findViewById(R.id.txtSobanChitiet);
        txtSothevangChitiet = (TextView) findViewById(R.id.txtSothevangChitiet);
        txtSothedoChitiet = (TextView) findViewById(R.id.txtSothedoChitiet);
    }

    private void addEvent() {
        GoBackIconChitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }
    private void cancel() {
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        finish();
    }

}