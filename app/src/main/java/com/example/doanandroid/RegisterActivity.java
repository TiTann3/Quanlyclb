package com.example.doanandroid;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanandroid.Database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    final String DATABASE_NAME = "Quanly_CLB";

    Button btnDangky;
    ImageView GoBackIcon;
    TextView LogInbtn;
    EditText edtTendangnhap, edtEmail, edtPassword;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addEvent();
        addControls();



    }

    private void addEvent() {
        btnDangky = findViewById(R.id.btnDangky);
        GoBackIcon = (ImageView) findViewById(R.id.GoBackIcon);
        LogInbtn = (TextView) findViewById(R.id.LogInbtn);
        edtTendangnhap = (EditText) findViewById(R.id.edtTendangnhap);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    private void addControls() {
        databaseHelper = new DatabaseHelper(this);
        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtTendangnhap.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    boolean inserted = addTaiKhoan(username, email, password);

                    if (inserted) {
                        showRegistrationSuccessDialog();
                    } else {
                        showRegistrationFailedDialog();
                    }
                } else {
                    showFillAllFieldsDialog();
                }
            }
        });
        GoBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void showRegistrationSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng ký thành công")
                .setMessage("Tài khoản của bạn đã được đăng ký thành công.")
                .setPositiveButton("Đăng nhập ngay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent ds = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(ds);
                        finish();
                    }
                })
                .setNegativeButton("Ở lại", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        edtTendangnhap.setText("");
                        edtEmail.setText("");
                        edtPassword.setText("");
                        edtTendangnhap.requestFocus();
                    }
                })
                .show();
    }

    private void showRegistrationFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng ký không thành công")
                .setMessage("Đã xảy ra lỗi khi đăng ký tài khoản. Vui lòng thử lại sau.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showFillAllFieldsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo")
                .setMessage("Vui lòng điền đầy đủ thông tin để đăng ký.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public boolean addTaiKhoan(String tenTaiKhoan, String email, String matKhau) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME );
        ContentValues values = new ContentValues();
        values.put("tentaikhoan", tenTaiKhoan);
        values.put("email", email);
        values.put("matkhau", matKhau);

        long result = database.insert("TAIKHOAN", null, values);
        return result != -1;
    }

    public void LognIn(View view) {
        Intent ds = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(ds);
        finish();
    }
}
