package com.example.doanandroid;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanandroid.Database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";
    ImageView GoBackIcon;
    Button btnLogin;
    EditText edtUser, edtPasswordLogin;
    TextView txtSignup, txtNoAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
        addEvent();
    }

    private void addControls() {
        GoBackIcon = (ImageView) findViewById(R.id.GoBackIcon);
        btnLogin = (Button) findViewById(R.id.btnDangnhap);
        edtUser = (EditText) findViewById(R.id.edtUser);
        edtPasswordLogin = (EditText) findViewById(R.id.edtPasswordLogin);
        txtSignup = (TextView) findViewById(R.id.txtSigup);
        txtNoAccount = (TextView) findViewById(R.id.txtNoAccount);
    }

    private void addEvent(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUser.getText().toString().trim();
                String password = edtPasswordLogin.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    showEmptyFieldsDialog();
                } else {
                    DatabaseHelper db = new DatabaseHelper(LoginActivity.this);
                    boolean loginSuccess = checkLogin(username, password);

                    if (loginSuccess) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this,"Đăng Nhập Thành Công",Toast.LENGTH_SHORT).show();
                        finish();
                        edtUser.setText("");
                        edtPasswordLogin.setText("");
                    } else {
                        showLoginFailedDialog();
                    }
                }
            }
        });
        GoBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitConfirmationDialog();
            }
        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        txtNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void showLoginFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng nhập không thành công")
                .setMessage("Đăng nhập không thành công. Vui lòng kiểm tra lại thông tin.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận thoát ứng dụng")
                .setMessage("Bạn có chắc chắn muốn thoát ứng dụng không?")
                .setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void showEmptyFieldsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi đăng nhập")
                .setMessage("Vui lòng nhập tên đăng nhập và mật khẩu.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public boolean checkLogin(String tendangnhap, String matkhau) {
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM TAIKHOAN WHERE tentaikhoan=? AND matkhau=?", new String[]{tendangnhap, matkhau});
        return cursor.getCount() > 0;
    }

}
