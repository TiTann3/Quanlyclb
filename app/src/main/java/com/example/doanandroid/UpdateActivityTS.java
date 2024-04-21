package com.example.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanandroid.Database.DatabaseHelper;
import com.example.doanandroid.model.ThanhVien;

public class UpdateActivityTS extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";

    int id = -1;

    EditText edtSotran, edtBanthang, edtThevang, edtThedo;
    Button btnLuuTS, btnHuyTS;
    ImageView GoBackIcon, imgAvatar;
    TextView txtTencauthu, txtVitriCauthu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ts);
        addControls();
        addEvent();
        initUI();
    }



    private void initUI() {
        Intent i = getIntent();
        id = i.getIntExtra("SOAO", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT a.soao, a.vitri, a.sotran, a.banthang, a.thevang, a.thedo, b.hoten, b.anh " +
                " FROM THONGSOCAUTHU a, THANHVIEN b " +
                "WHERE a.matv = b.matv AND soao = ?", new String[]{id + ""});
        cursor.moveToFirst();
        String vitri = cursor.getString(1);
        int sotran = cursor.getInt(2);
        int banthang = cursor.getInt(3);
        int thevang = cursor.getInt(4);
        int thedo = cursor.getInt(5);
        String ten = cursor.getString(6);
        byte[] anh = cursor.getBlob(7);

        edtSotran.setText(sotran+"");
        edtBanthang.setText(banthang+"");
        edtThevang.setText(thevang+"");
        edtThedo.setText(thedo+"");
        txtTencauthu.setText(ten);
        txtVitriCauthu.setText(vitri);
        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);
        imgAvatar.setImageBitmap(bitmap);
    }

    private void addControls() {
        btnHuyTS = (Button) findViewById(R.id.btnHuyTS);
        btnLuuTS = (Button) findViewById(R.id.btnLuuTS);
        edtSotran = (EditText) findViewById(R.id.edtSotran);
        edtBanthang = (EditText) findViewById(R.id.edtBanthang);
        edtThevang = (EditText) findViewById(R.id.edtThevang);
        edtThedo = (EditText) findViewById(R.id.edtThedo);
        GoBackIcon = (ImageView) findViewById(R.id.GoBackIcon);
        txtTencauthu = (TextView) findViewById(R.id.txtTencauthu);
        txtVitriCauthu = (TextView) findViewById(R.id.txtVitriCauthu);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
    }

    private void addEvent() {
        btnLuuTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveConfirmationDialog();
            }
        });
        btnHuyTS.setOnClickListener(new View.OnClickListener() {
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

    private void update(){
        if(validateInput()) {
            int sotran = Integer.parseInt(edtSotran.getText().toString());
            int banthang = Integer.parseInt(edtBanthang.getText().toString());
            int thevang = Integer.parseInt(edtThevang.getText().toString());
            int thedo = Integer.parseInt(edtThedo.getText().toString());

            ContentValues contentValues = new ContentValues();
            contentValues.put("sotran", sotran);
            contentValues.put("banthang", banthang);
            contentValues.put("thevang", thevang);
            contentValues.put("thedo", thedo);

            SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
            database.update("THONGSOCAUTHU", contentValues, "soao = ?", new String[]{id + ""});
            Intent i = new Intent();
            setResult(Activity.RESULT_OK, i);
            Toast.makeText(UpdateActivityTS.this,"Sửa Thành Công",Toast.LENGTH_SHORT).show();
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

    private boolean validateInput() {
        boolean isValid = true;

        String sotran = edtSotran.getText().toString().trim();
        String banthang = edtBanthang.getText().toString().trim();
        String thevang = edtThevang.getText().toString().trim();
        String thedo = edtThedo.getText().toString().trim();

        if(sotran.isEmpty() || banthang.isEmpty() || thevang.isEmpty() || thedo.isEmpty()){
            isValid = false;
        } else {
            try{
                int sotranValue = Integer.parseInt(sotran);
                int banthangValue = Integer.parseInt(banthang);
                int thevangValue = Integer.parseInt(thevang);
                int thedoValue = Integer.parseInt(thedo);
            } catch (NumberFormatException e){
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