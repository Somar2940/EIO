package com.example.eio;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


public class MailSend extends Activity implements LocationListener {
    private FileNameString fnm;
    private LocationManager manager;
    String LonLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailsend);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final EditText editTextmailsentence = findViewById(R.id.editTextmailsentence);
        fnm = (FileNameString) this.getApplication();
        Button button = findViewById(R.id.mailsend_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String save_account_name = fnm.getAccountname();
                String save_account_pass = fnm.getAccountpass();
                String mail_title = LonLat;
                asyncTask a = new asyncTask();
                String mail_sentence_text = editTextmailsentence.getText().toString();
                a.execute(save_account_name, save_account_pass, "EIOアプリより情報提供　"+mail_title, mail_sentence_text);
                Intent intent = new Intent(getApplication(), TYScreen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, this);
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (manager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.removeUpdates(this);
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        LonLat = "緯度：" + location.getLatitude() + "　経度：" + location.getLongitude();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private class asyncTask extends android.os.AsyncTask{

        protected String account;
        protected String password;
        protected String title;
        protected String text;

        @Override
        protected Object doInBackground(Object... obj){
            account=(String)obj[0];
            password=(String)obj[1];
            title=(String)obj[2];
            text=(String)obj[3];

            java.util.Properties properties = new java.util.Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.socketFactory.post", "465");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            final javax.mail.Message msg = new javax.mail.internet.MimeMessage(javax.mail.Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(account,password);
                }
            }));

            try {
                msg.setFrom(new javax.mail.internet.InternetAddress(account + "@gmail.com"));
                //自分自身にメールを送信
                msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse("min04gande@gmail.com"));
                msg.setSubject(title);//タイトルをセットする
                msg.setText(text);//テキストをセットする

                // 添付ファイルをする場合はこれを使う
                final MimeBodyPart txtPart = new MimeBodyPart();
                txtPart.setText(text, "utf-8");
                final MimeBodyPart filePart = new MimeBodyPart();
                String filetime = fnm.getfiletime();
                String pathname = "/sdcard/Android/data/com.example.eio/files/DCIM/CameraIntent_"+filetime+".jpg";
                File file = new File(pathname);
                FileDataSource fds = new FileDataSource(file);
                DataHandler data = new DataHandler(fds);
                filePart.setDataHandler(data);
                filePart.setFileName(MimeUtility.encodeWord("problem"));


                final Multipart mp = new MimeMultipart();
                mp.addBodyPart(txtPart);
                mp.addBodyPart(filePart); //添付ファイルをする場合はこれ
                msg.setContent(mp);

                javax.mail.Transport.send(msg);//ここで送信する

            } catch (Exception e) {
                return (Object)e.toString();
            }

            return (Object)"送信が完了しました";

        }
        @Override
        protected void onPostExecute(Object obj) {
            //画面にメッセージを表示する
            Toast.makeText(MailSend.this,(String)obj,Toast.LENGTH_LONG).show();
        }
    }
}