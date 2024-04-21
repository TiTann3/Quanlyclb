package com.example.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doanandroid.Database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddActivityCLB extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";

    final int RESQUEST_CHOOSE_PHOTO = 321;

    EditText edtAddTenClb;
    ImageView imgAvatarAddClb, GoBackIcon;
    Button btnLuuCLB, btnHuyCLB, btnChonhinhCLB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clb);
        addControls();
        addEvent();
    }

    private void addControls() {
        btnChonhinhCLB = (Button) findViewById(R.id.btnChonhinhCLB);
        btnLuuCLB = (Button) findViewById(R.id.btnLuuCLB);
        btnHuyCLB = (Button) findViewById(R.id.btnHuyCLB);
        edtAddTenClb = (EditText) findViewById(R.id.edtAddTenClb);
        imgAvatarAddClb = (ImageView) findViewById(R.id.imgAvatarAddCLB);
        GoBackIcon = (ImageView) findViewById(R.id.GoBackIconAddClb);
    }

    private void addEvent() {
        btnChonhinhCLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        btnLuuCLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveConfirmationDialog();
            }
        });
        btnHuyCLB.setOnClickListener(new View.OnClickListener() {
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
    private void insert(){
        if (validateInput()) {
            String tenClb = edtAddTenClb.getText().toString();
            BitmapDrawable drawable = (BitmapDrawable) imgAvatarAddClb.getDrawable();
            if (drawable != null) {
                byte[] anh = getByteArrayFromImageView(imgAvatarAddClb);
                ContentValues contentValues = new ContentValues();
                contentValues.put("tenclb", tenClb);
                contentValues.put("anh", anh);

                SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
                database.insert("danhsachclb", null, contentValues);
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                Toast.makeText(AddActivityCLB.this,"Thêm Thành Công",Toast.LENGTH_SHORT).show();
                finish();
            } else {
                showErrorMessage("Vui lòng chọn ảnh đại diện");
            }
        } else {
            showErrorMessage("Vui lòng nhập đầy đủ thông tin");
        }
    }
    private boolean validateInput() {
        boolean isValid = true;

        if (edtAddTenClb.getText().toString().trim().isEmpty()
                || imgAvatarAddClb.getDrawable() == null) {
            isValid = false;
        }
        return isValid;
    }
    private void choosePicture(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, RESQUEST_CHOOSE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESQUEST_CHOOSE_PHOTO) {
            if (data != null) {
                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgAvatarAddClb.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void showErrorMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void cancel(){
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        finish();
    }
    private byte[] getByteArrayFromImageView(ImageView imgv){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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