package com.example.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doanandroid.Database.DatabaseHelper;

import org.w3c.dom.Text;

public class MainChiTietCLB extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";

    ImageView GoBackIconCLB;
    TextView txtSotran, txtTranthang,txtTranhoa, txtTranthua, txtBanthang, txtBanthua, txtHieuso, txtDiem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chi_tiet_clb);
        addControls();
        addEvent();
        initUI();
    }

    private void initUI() {
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM CHITIETTRANDAU", null);

        int tongbanthanghome = 0;
        int tongbanthuahome = 0;
        int tongbanthangaway = 0;
        int tongbanthuaaway = 0;
        int sotranthang =0;
        int sotranhoa = 0;
        int sotranthua = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idhome = cursor.getInt(1);
                int idaway = cursor.getInt(2);
                int homegoal = cursor.getInt(3);
                int awaygoal = cursor.getInt(4);

                if (idhome == 14) {
                    if (homegoal > awaygoal) {
                        sotranthang++;
                    } else if (homegoal < awaygoal) {
                        sotranthua++;
                    } else {
                        sotranhoa++;
                    }
                }

                if (idaway == 14) {
                    if (homegoal < awaygoal) {
                        sotranthang++;
                    } else if (homegoal > awaygoal) {
                        sotranthua++;
                    } else {
                        sotranhoa++;
                    }
                }
                if(idhome == 14){
                    tongbanthanghome += homegoal;
                    tongbanthuahome += awaygoal;
                } else {
                    tongbanthangaway += awaygoal;
                    tongbanthuaaway += homegoal;
                }
                int tongbanthang = tongbanthanghome + tongbanthangaway;
                int tongbanthua = tongbanthuahome + tongbanthuaaway;
                txtSotran.setText(String.valueOf(sotranthang+sotranhoa+sotranthua));
                txtTranthang.setText(String.valueOf(sotranthang));
                txtTranhoa.setText(String.valueOf(sotranhoa));
                txtTranthua.setText(String.valueOf(sotranthua));
                txtBanthang.setText(String.valueOf(tongbanthang));
                txtBanthua.setText(String.valueOf(tongbanthua));
                txtHieuso.setText(String.valueOf(tongbanthang-tongbanthua));
                txtDiem.setText(String.valueOf((sotranthang*3)+(sotranhoa*1)));
            } while (cursor.moveToNext());
        }
        if (cursor == null || cursor.getCount() == 0) {
            txtSotran.setText("0");
            txtTranthang.setText("0");
            txtTranhoa.setText("0");
            txtTranthua.setText("0");
            txtBanthang.setText("0");
            txtBanthua.setText("0");
            txtHieuso.setText("0");
            txtDiem.setText("0");
        }
    }

    private void addControls() {
        txtSotran = (TextView) findViewById(R.id.txtSotranCLB);
        txtTranthang = (TextView) findViewById(R.id.txtTranthang);
        txtTranhoa = (TextView) findViewById(R.id.txtTranhoa);
        txtTranthua = (TextView) findViewById(R.id.txtTranthua);
        txtBanthang = (TextView) findViewById(R.id.txtBanthangCLB);
        txtBanthua = (TextView) findViewById(R.id.txtBanthuaCLB);
        txtHieuso = (TextView) findViewById(R.id.txtHieuso);
        txtDiem = (TextView) findViewById(R.id.txtDiem);
        GoBackIconCLB = (ImageView) findViewById(R.id.GoBackIconCLB);
    }

    private void addEvent() {
        GoBackIconCLB.setOnClickListener(new View.OnClickListener() {
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