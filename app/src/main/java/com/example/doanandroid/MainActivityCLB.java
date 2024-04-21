package com.example.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.doanandroid.Adapter.AdapterCLB;
import com.example.doanandroid.Database.DatabaseHelper;
import com.example.doanandroid.model.DanhSachCLB;

import java.util.ArrayList;

public class MainActivityCLB extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";
    int YOUR_REQUEST_CODE = 123;
    SQLiteDatabase database;

    ListView listView;
    ArrayList<DanhSachCLB> list;
    AdapterCLB adapter;
    ImageView GoBackIcon, ibtnAddCLB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_clb);

        addControls();
        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt("listViewIndex");
            int top = savedInstanceState.getInt("listViewTop");

            listView.setSelectionFromTop(index, top);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

        outState.putInt("listViewIndex", index);
        outState.putInt("listViewTop", top);
    }

    protected void onResume() {
        super.onResume();
        readData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE && resultCode == RESULT_OK) {
            readData();
        }
    }

    private void addControls() {
        GoBackIcon = (ImageView) findViewById(R.id.GoBackIcon);
        GoBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ibtnAddCLB = (ImageView) findViewById(R.id.ibtnAddCLB);
        ibtnAddCLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityCLB.this, AddActivityCLB.class);
                startActivity(i);
            }
        });
        listView = (ListView) findViewById(R.id.listViewBXH);
        list = new ArrayList<>();
        adapter = new AdapterCLB(this, list);
        listView.setAdapter(adapter);
    }

    private void readData() {
        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM DANHSACHCLB", null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idclb = cursor.getInt(0);
            String tenclb = cursor.getString(1);
            byte[] anh = cursor.getBlob(2);
            list.add(new DanhSachCLB(idclb, tenclb, anh));
        }
        cursor.close();
        database.close();
        adapter.notifyDataSetChanged();

    }
}