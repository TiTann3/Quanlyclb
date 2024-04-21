package com.example.doanandroid;

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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanandroid.Database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddActivityTV extends AppCompatActivity {

    final String DATABASE_NAME = "Quanly_CLB";
    final int RESQUEST_TAKE_PHOTO = 123;
    final int RESQUEST_CHOOSE_PHOTO = 321;

    Button btnChonhinh, btnChuphinh, btnLuu, btnHuy;
    EditText edtHoten, edtNgaysinh, edtChucvu, edtQuoctich;
    ImageView imgAvatarAdd, GoBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tv);

        addControls();
        addEvents();
    }

    private void addControls() {
        btnChonhinh = (Button) findViewById(R.id.btnChonhinh);
        btnChuphinh = (Button) findViewById(R.id.btnChuphinh);
        btnLuu = (Button) findViewById(R.id.btnLuu);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        edtHoten = (EditText) findViewById(R.id.edtHoten);
        edtNgaysinh = (EditText) findViewById(R.id.edtNgaysinh);
        edtChucvu = (EditText) findViewById(R.id.edtChucvu);
        edtQuoctich = (EditText) findViewById(R.id.edtQuoctich);
        imgAvatarAdd = (ImageView) findViewById(R.id.imgAvatarAdd);
        GoBackIcon = (ImageView) findViewById(R.id.GoBackIcon);
    }

    private void addEvents(){
        btnChonhinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        btnChuphinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveConfirmationDialog();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
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

    private void takePicture(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, RESQUEST_TAKE_PHOTO);
    }

    private void choosePicture(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, RESQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgAvatarAdd.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgAvatarAdd.setImageBitmap(bitmap);
            }
        }
    }

    private void insert(){
        if (validateInput()) {
            String hoten = edtHoten.getText().toString();
            String ngaysinh = edtNgaysinh.getText().toString();
            String chucvu = edtChucvu.getText().toString();
            String quoctich = edtQuoctich.getText().toString();

            BitmapDrawable drawable = (BitmapDrawable) imgAvatarAdd.getDrawable();
            if (drawable != null) {
                byte[] anh = getByteArrayFromImageView(imgAvatarAdd);

                ContentValues contentValues = new ContentValues();
                contentValues.put("hoten", hoten);
                contentValues.put("ngaysinh", ngaysinh);
                contentValues.put("chucvu", chucvu);
                contentValues.put("quoctich", quoctich);
                contentValues.put("anh", anh);

                SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
                database.insert("ThanhVien", null, contentValues);
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                Toast.makeText(AddActivityTV.this,"Thêm Thành Công",Toast.LENGTH_SHORT).show();
                finish();
            } else {
                showErrorMessage("Vui lòng chọn ảnh đại diện");
            }
        } else {
            showErrorMessage("Vui lòng nhập đầy đủ thông tin");
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
    private boolean validateInput() {
        boolean isValid = true;

        if (edtHoten.getText().toString().trim().isEmpty()
                || edtNgaysinh.getText().toString().trim().isEmpty()
                || edtChucvu.getText().toString().trim().isEmpty()
                || edtQuoctich.getText().toString().trim().isEmpty()
                || imgAvatarAdd.getDrawable() == null) {
            isValid = false;
        }

        return isValid;
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