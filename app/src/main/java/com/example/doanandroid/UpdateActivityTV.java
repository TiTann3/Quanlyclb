package com.example.doanandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

public class UpdateActivityTV extends AppCompatActivity {
    final String DATABASE_NAME = "Quanly_CLB";
    final int RESQUEST_TAKE_PHOTO = 123;
    final int RESQUEST_CHOOSE_PHOTO = 321;

    int id = -1;
    Button btnChonhinh, btnChuphinh, btnLuu, btnHuy;
    EditText edtHoten, edtNgaysinh, edtChucvu, edtQuoctich;
    ImageView imgAvatarUpdate, GoBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tv);

        addControls();
        addEvents();
        initUI();
    }

    private void initUI() {
        Intent i = getIntent();
        id = i.getIntExtra("MATV", -1);
        SQLiteDatabase database = DatabaseHelper.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM THANHVIEN Where matv = ?", new String[]{id + ""});
        cursor.moveToFirst();
        String hoten = cursor.getString(1);
        String ngaysinh = cursor.getString(2);
        String chucvu = cursor.getString(3);
        String quoctich = cursor.getString(4);
        byte[] anh = cursor.getBlob(5);

        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);
        imgAvatarUpdate.setImageBitmap(bitmap);
        edtHoten.setText(hoten);
        edtNgaysinh.setText(ngaysinh);
        edtChucvu.setText(chucvu);
        edtQuoctich.setText(quoctich);
    }

    private void addControls() {
        btnChonhinh = (Button) findViewById(R.id.btnChonhinh);
        btnChuphinh = (Button) findViewById(R.id.btnChuphinht);
        btnLuu = (Button) findViewById(R.id.btnLuut);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        edtHoten = (EditText) findViewById(R.id.edtHoten);
        edtNgaysinh = (EditText) findViewById(R.id.edtNgaysinht);
        edtChucvu = (EditText) findViewById(R.id.edtChucvu);
        edtQuoctich = (EditText) findViewById(R.id.edtQuoctich);
        imgAvatarUpdate = (ImageView) findViewById(R.id.imgAvatarAdd);
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
                    imgAvatarUpdate.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgAvatarUpdate.setImageBitmap(bitmap);
            }
        }
    }

    private void update(){
        if (validateInput()) {
            String hoten = edtHoten.getText().toString();
            String ngaysinh = edtNgaysinh.getText().toString();
            String chucvu = edtChucvu.getText().toString();
            String quoctich = edtQuoctich.getText().toString();

            BitmapDrawable drawable = (BitmapDrawable) imgAvatarUpdate.getDrawable();
            if (drawable != null) {
                byte[] anh = getByteArrayFromImageView(imgAvatarUpdate);
                ContentValues contentValues = new ContentValues();
                contentValues.put("hoten", hoten);
                contentValues.put("ngaysinh", ngaysinh);
                contentValues.put("chucvu", chucvu);
                contentValues.put("quoctich", quoctich);
                contentValues.put("anh", anh);

                SQLiteDatabase database = DatabaseHelper.initDatabase(this, "Quanly_CLB");
                database.update("ThanhVien", contentValues, "matv = ?", new String[]{id + ""});
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                Toast.makeText(UpdateActivityTV.this,"Sửa Thành Công",Toast.LENGTH_SHORT).show();
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

        if (edtHoten.getText().toString().trim().isEmpty()
                || edtNgaysinh.getText().toString().trim().isEmpty()
                || edtChucvu.getText().toString().trim().isEmpty()
                || edtQuoctich.getText().toString().trim().isEmpty()
                || imgAvatarUpdate.getDrawable() == null) {
            isValid = false;
        }

        return isValid;
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