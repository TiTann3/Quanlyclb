package com.example.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doanandroid.Database.DatabaseHelper;
import com.example.doanandroid.model.DanhSachCLB;

import java.util.ArrayList;
import java.util.List;

public class AddActivityCTTD extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";

    Spinner spnHome, spnAway;
    EditText edtHomegoal, edtAwaygoal, edtNgaythidau;
    Button btnHuyAddTD, btnLuuAddTD;
    ImageView GoBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cttd);

        addControls();
        addEvents();

    }

    private void addControls() {
        btnHuyAddTD = (Button) findViewById(R.id.btnHuyAddTD);
        btnLuuAddTD = (Button) findViewById(R.id.btnLuuAddTD);
        spnHome = (Spinner) findViewById(R.id.spnUdHome);
        spnAway = (Spinner) findViewById(R.id.spnAway);
        edtHomegoal = (EditText) findViewById(R.id.edtHomegoal);
        edtAwaygoal = (EditText) findViewById(R.id.edtAwaygoal);
        edtNgaythidau = (EditText) findViewById(R.id.edtNgaythidau);
        GoBackIcon = (ImageView) findViewById(R.id.GoBackIcon);
    }
    public List<DanhSachCLB> getAllCLBs() {
        List<DanhSachCLB> clbList = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = db.rawQuery("SELECT IDCLB, TENCLB FROM DANHSACHCLB", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int idCLB = cursor.getInt(cursor.getColumnIndex("IDCLB"));
                @SuppressLint("Range") String tenCLB = cursor.getString(cursor.getColumnIndex("TENCLB"));
                DanhSachCLB clb = new DanhSachCLB(idCLB, tenCLB);
                clbList.add(clb);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return clbList;
    }

    public int getIdHomeFromSpinner(Spinner spinner) {
        int selectedPosition = spinner.getSelectedItemPosition();
        DanhSachCLB selectedCLB = (DanhSachCLB) spinner.getItemAtPosition(selectedPosition);
        return selectedCLB.getIdclb();
    }

    public int getIdAwayFromSpinner(Spinner spinner) {
        int selectedPosition = spinner.getSelectedItemPosition();
        DanhSachCLB selectedCLB = (DanhSachCLB) spinner.getItemAtPosition(selectedPosition);
        return selectedCLB.getIdclb();
    }
    private void addEvents() {
        List<DanhSachCLB> danhSachCLB = getAllCLBs();
        ArrayAdapter<DanhSachCLB> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachCLB);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnHome.setAdapter(adapter);
        spnAway.setAdapter(adapter);
        btnLuuAddTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveConfirmationDialog();
            }
        });
        btnHuyAddTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelConfirmationDialog();
            }
        });
        GoBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelConfirmationDialog();
            }
        });
    }

    private void insert() {
        if(validateInput()) {
            int idHome = getIdHomeFromSpinner(spnHome);
            int idAway = getIdAwayFromSpinner(spnAway);
            int homegoal = Integer.parseInt(edtHomegoal.getText().toString());
            int awaygoal = Integer.parseInt(edtAwaygoal.getText().toString());
            String ngaythidau = edtNgaythidau.getText().toString();

            ContentValues contentValues = new ContentValues();
            contentValues.put("IDHOME", idHome);
            contentValues.put("IDAWAY", idAway);
            contentValues.put("HOMEGOAL", homegoal);
            contentValues.put("AWAYGOAL", awaygoal);
            contentValues.put("NGAYTHIDAU", ngaythidau);

            SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
            database.insert("CHITIETTRANDAU", null, contentValues);
            Intent i = new Intent();
            setResult(Activity.RESULT_OK, i);
            Toast.makeText(AddActivityCTTD.this,"Thêm Thành Công",Toast.LENGTH_SHORT).show();
            finish();
        }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Lỗi");
                builder.setMessage("Vui lòng nhập đầy đủ thông tin và đúng định dạng");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
        }
    }
    private boolean validateInput() {
        boolean isValid = true;

        String homeGoal = edtHomegoal.getText().toString().trim();
        String awayGoal = edtAwaygoal.getText().toString().trim();
        String ngayThiDau = edtNgaythidau.getText().toString().trim();

        if ((homeGoal.isEmpty() || awayGoal.isEmpty() || ngayThiDau.isEmpty()
                || spnHome.getSelectedItem() == null || spnAway.getSelectedItem() == null)
                && (spnHome.getSelectedItem() == spnAway.getSelectedItem())) {
            isValid = false;
        } else {
            try {
                int homeGoalValue = Integer.parseInt(homeGoal);
                int awayGoalValue = Integer.parseInt(awayGoal);
            } catch (NumberFormatException e) {
                isValid = false;
            }
        }
        return isValid;
    }
    private void cancel() {
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    private void showSaveConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận lưu dữ liệu");
        builder.setMessage("Bạn có chắc muốn lưu thông tin này không?");

        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                insert();
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
    private void showCancelConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận hủy bỏ");
        builder.setMessage("Bạn có chắc muốn hủy bỏ thao tác này không?");

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancel();
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}