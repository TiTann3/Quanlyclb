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
import com.example.doanandroid.model.ThanhVien;

import java.util.ArrayList;
import java.util.List;

public class AddActivityTS extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";
    Spinner spnThanhvien;
    EditText edtSoao, edtVitri, edtSotran, edtBanthang, edtThevang, edtThedo;
    Button btnHuyAddTS, btnLuuAddTS;
    ImageView GoBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ts);

        addControls();
        addEvent();
    }

    private void addControls() {
        btnHuyAddTS = (Button) findViewById(R.id.btnHuyAddTS);
        btnLuuAddTS = (Button) findViewById(R.id.btnLuuAddTS);
        edtSoao = (EditText) findViewById(R.id.edtSoao);
        edtSotran = (EditText) findViewById(R.id.edtSotran);
        edtBanthang = (EditText) findViewById(R.id.edtBanthang);
        edtThevang = (EditText) findViewById(R.id.edtThevang);
        edtThedo = (EditText) findViewById(R.id.edtThedo);
        spnThanhvien = (Spinner) findViewById(R.id.spnThanhvien);
        GoBackIcon = (ImageView) findViewById(R.id.GoBackIcon);
        edtVitri = (EditText) findViewById(R.id.edtVitri);
    }

    public List<ThanhVien> getAllThanhViens() {
        List<ThanhVien> ThanhvienList = new ArrayList<>();
        SQLiteDatabase db = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = db.rawQuery("SELECT matv, hoten FROM THANHVIEN", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int maTV = cursor.getInt(cursor.getColumnIndex("matv"));
                @SuppressLint("Range") String hoTen = cursor.getString(cursor.getColumnIndex("hoten"));
                ThanhVien thanhVien = new ThanhVien(maTV, hoTen);
                ThanhvienList.add(thanhVien);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ThanhvienList;
    }

    public int getIdThanhVienFromSpinner(Spinner spinner) {
        int selectedPosition = spinner.getSelectedItemPosition();
        ThanhVien selectedHoten = (ThanhVien) spinner.getItemAtPosition(selectedPosition);
        return selectedHoten.getMatv();
    }
    private void addEvent() {
        List<ThanhVien> danhSachThanhhvien = getAllThanhViens();
        ArrayAdapter<ThanhVien> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachThanhhvien);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnThanhvien.setAdapter(adapter);
        btnLuuAddTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveConfirmationDialog();
            }
        });
        btnHuyAddTS.setOnClickListener(new View.OnClickListener() {
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
            int soaocheck = Integer.parseInt(edtSoao.getText().toString());
            if (checkJerseyNumberExistence(soaocheck)) {
                showJerseyNumberExistsDialog();
            } else {
                int soao = Integer.parseInt(edtSoao.getText().toString());
                String vitri = edtVitri.getText().toString();
                int sotran = Integer.parseInt(edtSotran.getText().toString());
                int banthang = Integer.parseInt(edtBanthang.getText().toString());
                int thevang = Integer.parseInt(edtThevang.getText().toString());
                int thedo = Integer.parseInt(edtThedo.getText().toString());
                int matv = getIdThanhVienFromSpinner(spnThanhvien);

                ContentValues contentValues = new ContentValues();
                contentValues.put("soao", soao);
                contentValues.put("vitri", vitri);
                contentValues.put("sotran", sotran);
                contentValues.put("banthang", banthang);
                contentValues.put("thevang", thevang);
                contentValues.put("thedo", thedo);
                contentValues.put("matv", matv);

                SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
                database.insert("THONGSOCAUTHU", null, contentValues);
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                Toast.makeText(AddActivityTS.this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                finish();
            }
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
    private boolean checkJerseyNumberExistence(int jerseyNumber) {
        SQLiteDatabase db = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = db.rawQuery("SELECT soao FROM THONGSOCAUTHU WHERE soao = ?", new String[]{String.valueOf(jerseyNumber)});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    private void showJerseyNumberExistsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi");
        builder.setMessage("Số áo này đã tồn tại. Vui lòng chọn số áo khác.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private boolean validateInput() {
        boolean isValid = true;
        String soao = edtSoao.getText().toString().trim();
        String vitri = edtVitri.getText().toString().trim();
        String sotran = edtSotran.getText().toString().trim();
        String banthang = edtBanthang.getText().toString().trim();
        String thevang = edtThevang.getText().toString().trim();
        String thedo = edtThedo.getText().toString().trim();
        if (soao.isEmpty() || vitri.isEmpty() || sotran.isEmpty() || banthang.isEmpty()
                || thevang.isEmpty() || thedo.isEmpty()) {
            isValid = false;
        } else {
            try {
                int soaoValue = Integer.parseInt(soao);
                int sotranValue = Integer.parseInt(sotran);
                int banthangValue = Integer.parseInt(banthang);
                int thevangValue = Integer.parseInt(thevang);
                int thedoValue = Integer.parseInt(thedo);
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