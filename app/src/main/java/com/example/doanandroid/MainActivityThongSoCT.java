package com.example.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.doanandroid.Adapter.AdapterThongsoCT;
import com.example.doanandroid.Database.DatabaseHelper;
import com.example.doanandroid.model.ThongSoCauThu;

import java.util.ArrayList;

public class MainActivityThongSoCT extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";
    int YOUR_REQUEST_CODE = 123;
    SQLiteDatabase database;

    ListView listView;
    ArrayList<ThongSoCauThu> list;
    AdapterThongsoCT adapter;
    ImageButton ibtnAddTS;
    ImageView GoBackIcon;
    SearchView searchViewTSCT;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_thong_so_ct);

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
        ibtnAddTS = (ImageButton) findViewById(R.id.ibtnAddTS);
        ibtnAddTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityThongSoCT.this, AddActivityTS.class);
                startActivityForResult(i, YOUR_REQUEST_CODE);
            }
        });

        GoBackIcon = (ImageView) findViewById(R.id.GoBackIcon);
        GoBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchViewTSCT = findViewById(R.id.searchViewTSCT);
        searchViewTSCT.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        btnReset = (Button) findViewById(R.id.btnResetValues);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetValues();
            }
        });

        listView = (ListView) findViewById(R.id.listViewTSCT);
        list = new ArrayList<>();
        adapter = new AdapterThongsoCT(this, list);
        listView.setAdapter(adapter);

    }

    private void performSearch(String query) {
        ArrayList<ThongSoCauThu> searchResults = new ArrayList<>();
        for (ThongSoCauThu thongSo : list) {
            if (thongSo.getTenct().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(thongSo);
            }
        }
        adapter = new AdapterThongsoCT(MainActivityThongSoCT.this, searchResults);
        listView.setAdapter(adapter);
    }

    private void filterResults(String newText) {
        if (newText.isEmpty()) {
            adapter = new AdapterThongsoCT(MainActivityThongSoCT.this, list);
            listView.setAdapter(adapter);
            return;
        }

        ArrayList<ThongSoCauThu> filteredList = new ArrayList<>();
        for (ThongSoCauThu thongSo : list) {
            if (thongSo.getTenct().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(thongSo);
            }
        }
        adapter = new AdapterThongsoCT(MainActivityThongSoCT.this, filteredList);
        listView.setAdapter(adapter);
    }

    private void readData() {
        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT a.hoten, b.soao, b.vitri, b.sotran, b.banthang, b.thevang, b.thedo" +
                " FROM THANHVIEN a, THONGSOCAUTHU b Where a.matv = b.matv", null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String hoten = cursor.getString(0);
            int soao = cursor.getInt(1);
            String vitri = cursor.getString(2);
            int sotran = cursor.getInt(3);
            int banthang = cursor.getInt(4);
            int thevang = cursor.getInt(5);
            int thedo = cursor.getInt(6);
            list.add(new ThongSoCauThu(hoten, soao, vitri, sotran, banthang, thevang, thedo));
        }
        cursor.close();
        database.close();
        adapter.sortListByJerseyNumber();
        adapter.notifyDataSetChanged();
    }
    private void resetValues() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận reset giá trị");
        builder.setMessage("Bạn có chắc chắn muốn reset giá trị về 0?");
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                performResetValues();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void performResetValues() {
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);

        ContentValues contentValues = new ContentValues();
        contentValues.put("sotran", 0);
        contentValues.put("banthang", 0);
        contentValues.put("thevang", 0);
        contentValues.put("thedo", 0);

        database.update("THONGSOCAUTHU", contentValues, null, null);

        database.close();

        for (ThongSoCauThu thongSo : list) {
            thongSo.setSotran(0);
            thongSo.setBanthang(0);
            thongSo.setThevang(0);
            thongSo.setThedo(0);
        }

        adapter.notifyDataSetChanged();
    }
}