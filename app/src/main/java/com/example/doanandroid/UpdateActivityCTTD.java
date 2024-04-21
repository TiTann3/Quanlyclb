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

public class UpdateActivityCTTD extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";

    int id = -1;

    Spinner spnUdHome, spnUdAway;
    EditText edtUdHomegoal, edtUdAwaygoal, edtUdNgaythidau;
    Button btnUdHuy, btnUdLuu;
    ImageView GoBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cttd);
        addControls();
        addEvent();
        initUI();
    }

    private void initUI() {
        Intent i = getIntent();
        id = i.getIntExtra("IDTRANDAU", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM CHITIETTRANDAU WHERE idtrandau = ?", new String[]{id+""});
        cursor.moveToFirst();
        int idhome = cursor.getInt(1);
        int idaway = cursor.getInt(2);
        int goalhome = cursor.getInt(3);
        int goalaway = cursor.getInt(4);
        String ngaythidau = cursor.getString(5);

        spnUdHome.setSelection(idhome-1);
        spnUdAway.setSelection(idaway-1);
        edtUdHomegoal.setText(goalhome+"");
        edtUdAwaygoal.setText(goalaway+"");
        edtUdNgaythidau.setText(ngaythidau);
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

    // Hàm để lấy idhome từ Spinner đã chọn
    public int getIdHomeFromSpinner(Spinner spinner) {
        // Lấy vị trí của item đã chọn trong Spinner
        int selectedPosition = spinner.getSelectedItemPosition();

        // Lấy dữ liệu từ Spinner theo vị trí đã chọn
        DanhSachCLB selectedCLB = (DanhSachCLB) spinner.getItemAtPosition(selectedPosition);

        // Trả về id của CLB đã chọn từ Spinner
        return selectedCLB.getIdclb();
    }

    // Hàm để lấy idaway từ Spinner đã chọn
    public int getIdAwayFromSpinner(Spinner spinner) {
        int selectedPosition = spinner.getSelectedItemPosition();
        DanhSachCLB selectedCLB = (DanhSachCLB) spinner.getItemAtPosition(selectedPosition);
        return selectedCLB.getIdclb();
    }

    private void addEvent() {
        List<DanhSachCLB> danhSachCLB = getAllCLBs();
        ArrayAdapter<DanhSachCLB> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachCLB);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnUdHome.setAdapter(adapter);
        spnUdAway.setAdapter(adapter);
        btnUdLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveConfirmationDialog();
            }
        });
        btnUdHuy.setOnClickListener(new View.OnClickListener() {
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

    private void addControls() {
        btnUdHuy = (Button) findViewById(R.id.btnHuyUdTD);
        btnUdLuu = (Button) findViewById(R.id.btnLuuUdTD);
        edtUdHomegoal = (EditText) findViewById(R.id.edtUdHomegoal);
        edtUdAwaygoal = (EditText) findViewById(R.id.edtUdAwaygoal);
        edtUdNgaythidau = (EditText) findViewById(R.id.edtUdNgaythidau);
        spnUdHome = (Spinner) findViewById(R.id.spnUdHome);
        spnUdAway = (Spinner) findViewById(R.id.spnUdAway);
        GoBackIcon = (ImageView) findViewById(R.id.GoBackIcon);
    }

    private void update(){
        if(validateInput()) {
            int idhome = getIdHomeFromSpinner(spnUdHome);
            int idaway = getIdAwayFromSpinner(spnUdAway);
            int goalhome = Integer.parseInt(edtUdHomegoal.getText().toString());
            int goalaway = Integer.parseInt(edtUdAwaygoal.getText().toString());
            String ngaythidau = edtUdNgaythidau.getText().toString();

            ContentValues contentValues = new ContentValues();
            contentValues.put("IDHOME", idhome);
            contentValues.put("IDAWAY", idaway);
            contentValues.put("HOMEGOAL", goalhome);
            contentValues.put("AWAYGOAL", goalaway);
            contentValues.put("NGAYTHIDAU", ngaythidau);

            SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
            database.update("CHITIETTRANDAU", contentValues, "IDTRANDAU = ?", new String[]{id + ""});
            Intent i = new Intent();
            setResult(Activity.RESULT_OK, i);
            Toast.makeText(UpdateActivityCTTD.this,"Sửa Thành Công",Toast.LENGTH_SHORT).show();
            finish();
        } else {
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

    private boolean validateInput(){
        boolean isValid = true;

        String homeGoal = edtUdHomegoal.getText().toString().trim();
        String awayGoal = edtUdAwaygoal.getText().toString().trim();
        String ngayThiDau = edtUdNgaythidau.getText().toString().trim();

        if ((homeGoal.isEmpty() || awayGoal.isEmpty() || ngayThiDau.isEmpty()
                || spnUdHome.getSelectedItem() == null || spnUdAway.getSelectedItem() == null)
                && (spnUdHome.getSelectedItem() == spnUdAway.getSelectedItem())) {
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

    private void cancel(){
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
                update(); // Thực hiện lưu dữ liệu nếu người dùng chọn "Lưu"
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); // Đóng dialog nếu người dùng chọn "Hủy"
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
                cancel(); // Thực hiện hủy bỏ nếu người dùng chọn "Đồng ý"
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); // Đóng dialog nếu người dùng chọn "Không"
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}