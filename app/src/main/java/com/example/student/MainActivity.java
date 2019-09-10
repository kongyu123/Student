package com.example.student;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtuserName;
    private EditText mEtpassword;
    private CheckBox mCb_yh;
    private Button btnLogin, btnExit, readIn, readOut;
    private ImageView imageView;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtuserName = findViewById(R.id.et_username);
        mEtpassword = findViewById(R.id.et_password);
        mCb_yh = findViewById(R.id.cb_yh);
        btnLogin = findViewById(R.id.btn_login);
        btnExit = findViewById(R.id.btn_exit);
        imageView = findViewById(R.id.imageView);
        readIn = findViewById(R.id.readIn);
        readOut = findViewById(R.id.readOut);


        readIn.setOnClickListener((View.OnClickListener) this);
        readOut.setOnClickListener((View.OnClickListener) this);

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if ("xixi".equals(mEtuserName.getText().toString()) && "123".equals(mEtpassword.getText().toString())) {
//
//                    Intent intent = new Intent(MainActivity.this, DataStorageActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.readIn:
                saveToSD("abc.jpeg");
                break;
            case R.id.readOut:
                break;
        }
    }


//SD卡：外部的公有文件需要申请运行时权限
    private void saveToSD(String s) {
        //申请写的SD权限，要求Android版本大于6.0的（Buid.）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            return;
            }


        String  path = Environment.getExternalStoragePublicDirectory("").getPath()
                + File.separator+Environment.DIRECTORY_PICTURES;
            File file = new File(path,filename);
            try {
                if(file.createNewFile()){
//            2.获取ImageView的Bitmap图片对象
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();;
                    Bitmap bitmap  = drawable.getBitmap();
//            3.将Bitmap对象写入SD卡
            FileOutputStream outputStream = new FileOutputStream(file);
//            带缓冲
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);

            //4.关闭请求的资源
            outputStream.flush();
            outputStream.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
       }
    }
//申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"权限申请被拒绝",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode){
            case 1:
                saveToSD("abc.jpeg");
                break;

        }
    }
}
