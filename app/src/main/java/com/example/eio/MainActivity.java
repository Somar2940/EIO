package com.example.eio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import androidx.core.content.FileProvider;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static android.app.PendingIntent.getActivity;

public class MainActivity extends Activity {
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private final static int RESULT_CAMERA = 1001;
    private ImageView imageView;
    private Uri cameraUri;
    private FileNameString fnm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preference = getSharedPreferences("Preference Name", MODE_PRIVATE);
        editor = preference.edit();
        if (preference.getBoolean("Launched", false)==false) {
            //初回起動時の処理
            AlertDialog.Builder firstbuilder = new AlertDialog.Builder(MainActivity.this);
            firstbuilder.setMessage("アカウント設定からGmailの\n情報を更新してご利用ください")
                    .setTitle("System Message (初回起動時のみ)")
                    .setPositiveButton("OK",null);
            firstbuilder.show();
            //プリファレンスの書き変え
            editor.putBoolean("Launched", true);
            editor.commit();
        }

        fnm = (FileNameString) this.getApplication();

        imageView = findViewById(R.id.image_view);

        Button intent_button = findViewById(R.id.intent_button);
        intent_button.setOnClickListener(onClick_button_mail);

        Button cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(onClick_button_camera);

        Button account_button = findViewById(R.id.account_button);
        account_button.setOnClickListener(onClick_button_account);

    }
    private View.OnClickListener onClick_button_mail = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //カメラを使ったか否か
            if(cameraUri != null && isExternalStorageReadable()) {
                Intent intent = new Intent(getApplication(), MailSend.class);
                startActivity(intent);
            }else{
                //ダイアログを表示
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("カメラ起動し撮影してください")
                        .setTitle("System Message")
                        .setPositiveButton("OK",null);
                builder.show();
            }
        }
    };
    private View.OnClickListener onClick_button_camera = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isExternalStorageWritable()){
                cameraIntent();
            }
        }
    };
    private View.OnClickListener onClick_button_account = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent accountintent = new Intent(getApplication(), account.class);
            startActivity(accountintent);
        }
    };
    private void cameraIntent() {
        Context context = getApplicationContext();
        // 保存先のフォルダー
        //pass:storage/sdcard/data/com.example.eio/files/DCIM/CameraIntent_xxxxxxxx.jpg
        File cFolder = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        Log.d("log", "path: " + String.valueOf(cFolder));

        String fileDate = new SimpleDateFormat(
                "ddHHmmss", Locale.JAPAN).format(new Date());

        //FileNameStringのfiletimeにfileDateを代入
        fnm.setfiletime(fileDate);

        // ファイル名
        String fileName = String.format("CameraIntent_%s.jpg", fileDate);

        File cameraFile = new File(cFolder, fileName);

        cameraUri = FileProvider.getUriForFile(
                MainActivity.this,
                context.getPackageName() + ".fileprovider",
                cameraFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, RESULT_CAMERA);

        Log.d("debug","startActivityForResult()");
    }

    //サムネイル画像表示
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RESULT_CAMERA) {
            if(cameraUri != null && isExternalStorageReadable()){
                imageView.setImageURI(cameraUri);
            }
            else{
                Log.d("debug","cameraUri == null");
            }
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
}
