package com.example.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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

import com.example.doanandroid.Adapter.AdapterChitiettrandau;
import com.example.doanandroid.Database.DatabaseHelper;
import com.example.doanandroid.model.ChiTietTranDau;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivityChitietTD extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";
    int YOUR_REQUEST_CODE = 123;
    SQLiteDatabase database;
    ListView listView;
    ArrayList<ChiTietTranDau> list;
    AdapterChitiettrandau adapter;
    ImageButton ibtnThemTD;
    ImageView GoBackIcon;
    SearchView searchViewCTTD;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chitiet_td);
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
        ibtnThemTD = (ImageButton) findViewById(R.id.ibtnThemTD);
        ibtnThemTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityChitietTD.this, AddActivityCTTD.class);
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

        searchViewCTTD = findViewById(R.id.searchViewCTTD);
        searchViewCTTD.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllMatches();
            }
        });


        listView = (ListView) findViewById(R.id.listViewChitietTD);
        list = new ArrayList<>();
        adapter = new AdapterChitiettrandau(this, list);
        listView.setAdapter(adapter);

    }

    private void performSearch(String query) {
        ArrayList<ChiTietTranDau> searchResults = new ArrayList<>();
        for (ChiTietTranDau tranDau : list) {
            if (tranDau.getTenclb_Home().toLowerCase().contains(query.toLowerCase()) ||
                    tranDau.getTenclb_Away().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(tranDau);
            }
        }
        adapter = new AdapterChitiettrandau(MainActivityChitietTD.this, searchResults);
        listView.setAdapter(adapter);
    }

    private void filterResults(String newText) {
        if (newText.isEmpty()) {
            adapter = new AdapterChitiettrandau(MainActivityChitietTD.this, list);
            listView.setAdapter(adapter);
            return;
        }

        ArrayList<ChiTietTranDau> filteredList = new ArrayList<>();
        for (ChiTietTranDau tranDau : list) {
            if (tranDau.getTenclb_Home().toLowerCase().contains(newText.toLowerCase()) ||
                    tranDau.getTenclb_Away().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(tranDau);
            }
        }
        adapter = new AdapterChitiettrandau(MainActivityChitietTD.this, filteredList);
        listView.setAdapter(adapter);
    }

    private void readData() {
        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT a.idtrandau, b1.tenclb AS tenclb_home, b2.tenclb AS tenclb_away, a.homegoal, a.awaygoal, a.ngaythidau " +
                "FROM CHITIETTRANDAU a " +
                "INNER JOIN DANHSACHCLB b1 ON a.idhome = b1.idclb " +
                "INNER JOIN DANHSACHCLB b2 ON a.idaway = b2.idclb", null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idtrandau = cursor.getInt(0);
            String tenclb_home = cursor.getString(1);
            String tenclb_away = cursor.getString(2);
            int homegoal = cursor.getInt(3);
            int awaygoal = cursor.getInt(4);
            String ngaythidau = cursor.getString(5);
            list.add(new ChiTietTranDau(idtrandau, tenclb_home, tenclb_away, homegoal, awaygoal, ngaythidau));
        }
        cursor.close();
        database.close();
        adapter.notifyDataSetChanged();
    }

    private void deleteAllMatches() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa toàn bộ trận đấu");
        builder.setMessage("Bạn có chắc chắn muốn xóa toàn bộ trận đấu?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                performDeleteAllMatches();
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

    private void performDeleteAllMatches() {

        database = DatabaseHelper.initDatabase(this, DATABASE_NAME);

        database.delete("CHITIETTRANDAU", null, null);

        database.close();

        readData();
    }

}