package com.example.doanandroid;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanandroid.Adapter.AdapterThanhVien;
import com.example.doanandroid.Database.DatabaseHelper;
import com.example.doanandroid.model.ThanhVien;

import java.util.ArrayList;

public class MainActivityTV extends AppCompatActivity {
    final String DATABASE_NAME = "Quanly_CLB";
    int YOUR_REQUEST_CODE = 123;
    SQLiteDatabase database;

    ListView listView;
    ArrayList<ThanhVien> list;
    AdapterThanhVien adapter;
    ImageButton ibtnAdd;
    SearchView searchViewTV;
    ImageView GoBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhvien);

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
        ibtnAdd = findViewById(R.id.ibtnAdd);
        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityTV.this, AddActivityTV.class);
                startActivityForResult(i, YOUR_REQUEST_CODE);
            }
        });
        GoBackIcon = (ImageView)findViewById(R.id.GoBackIcon);
        GoBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchViewTV = findViewById(R.id.searchViewTV);
        searchViewTV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResults(newText);
                return true;
            }
        });
        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new AdapterThanhVien(this, list);
        listView.setAdapter(adapter);
    }

    private void readData(){
        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM THANHVIEN", null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int matv = cursor.getInt(0);
            String hoten = cursor.getString(1);
            String ngaysinh = cursor.getString(2);
            String chucvu = cursor.getString(3);
            String quoctich = cursor.getString(4);
            byte[] anh = cursor.getBlob(5);
            list.add(new ThanhVien(matv, hoten, ngaysinh, chucvu, quoctich, anh));
        }
        adapter.notifyDataSetChanged();
        database.close();
    }

    private void performSearch(String query) {
        ArrayList<ThanhVien> searchResults = new ArrayList<>();
        for (ThanhVien thanhVien : list) {
            if (thanhVien.getHoten().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(thanhVien);
            }
        }
        adapter = new AdapterThanhVien(MainActivityTV.this, searchResults);
        listView.setAdapter(adapter);
    }

    private void filterResults(String newText) {
        if (newText.isEmpty()) {
            adapter = new AdapterThanhVien(MainActivityTV.this, list);
            listView.setAdapter(adapter);
            return;
        }

        ArrayList<ThanhVien> filteredList = new ArrayList<>();
        for (ThanhVien thanhVien : list) {
            if (thanhVien.getHoten().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(thanhVien);
            }
        }
        adapter = new AdapterThanhVien(MainActivityTV.this, filteredList);
        listView.setAdapter(adapter);
    }
}
