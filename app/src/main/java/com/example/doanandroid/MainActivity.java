
//Nguyễn Thanh Tân      21CTHA0455      CCNTT21F

package com.example.doanandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanandroid.Database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    ImageButton ibtnThanhvien, ibtnDanhsachCLB, ibtnThongso, ibtnTrandau, ibtnXemchitiet, ibtnThoat;
    ImageView imgShare, imgFacebook, imgTwitter, imgYoutube, imgInstagram, imgUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        addControls();
    }

    private void Init() {
        ibtnThanhvien =(ImageButton) findViewById(R.id.ibtnThanhvien);
        ibtnDanhsachCLB = (ImageButton) findViewById(R.id.ibtnDanhsachCLB);
        ibtnTrandau = (ImageButton) findViewById(R.id.ibtnTrandau);
        ibtnThongso = (ImageButton) findViewById(R.id.ibtnThongso);
        ibtnXemchitiet = (ImageButton) findViewById(R.id.ibtnXemchitiet);
        ibtnThoat = (ImageButton) findViewById(R.id.ibtnThoat);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        imgFacebook = (ImageView) findViewById(R.id.imgFacebook);
        imgTwitter = (ImageView) findViewById(R.id.imgTwitter);
        imgYoutube = (ImageView) findViewById(R.id.imgYoutube);
        imgInstagram = (ImageView) findViewById(R.id.imgInstagram);
        imgUser = (ImageView) findViewById(R.id.imgUser);
    }

    private void addControls() {
        ibtnThanhvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MainActivityTV.class);
                startActivity(i);
            }
        });
        ibtnDanhsachCLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MainActivityCLB.class);
                startActivity(i);
            }
        });
        ibtnTrandau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MainActivityChitietTD.class);
                startActivity(i);
            }
        });
        ibtnThongso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MainActivityThongSoCT.class);
                startActivity(i);
            }
        });
        ibtnXemchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MainChiTietCLB.class);
                startActivity(i);
            }
        });
        ibtnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showExitConfirmationDialog();
            }
        });
        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/manchesterunited";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Không tìm thấy trình duyệt web.")
                            .setTitle("Thông báo")
                            .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://twitter.com/ManUtd_AR";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Không tìm thấy trình duyệt web.")
                            .setTitle("Thông báo")
                            .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        imgYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.youtube.com/channel/UC6yW44UGJJBvYTlfC7CRg2Q";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Không tìm thấy trình duyệt web.")
                            .setTitle("Thông báo")
                            .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        imgInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.instagram.com/manchesterunited/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Không tìm thấy trình duyệt web.")
                            .setTitle("Thông báo")
                            .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAccountInfoDialog();
            }
        });
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn Back lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

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
    private void showAccountInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.account_infor_dialog, null);
        builder.setView(dialogView);

        SQLiteDatabase database = DatabaseHelper.initDatabase(this, "Quanly_CLB");
        Cursor cursor = database.rawQuery("SELECT * FROM TAIKHOAN", null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("tentaikhoan"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
            ImageView imageViewAvatar = dialogView.findViewById(R.id.imgAvatardetail);
            TextView textViewUsername = dialogView.findViewById(R.id.txtUsername);
            TextView textViewEmail = dialogView.findViewById(R.id.txtEmaildetail);
            Button buttonLogout = dialogView.findViewById(R.id.btnLogout);
            textViewUsername.setText("Tên đăng nhập: " + username);
            textViewEmail.setText("Email: " + email);

            AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.TOP | Gravity.END;
            layoutParams.y = 100;
            window.setAttributes(layoutParams);

            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Xác nhận đăng xuất")
                            .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    Toast.makeText(MainActivity.this,"Đăng xuất Thành Công",Toast.LENGTH_SHORT).show();
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
            });
            dialog.show();
            cursor.close();
        }
    }
}