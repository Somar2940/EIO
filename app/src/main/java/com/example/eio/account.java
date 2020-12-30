package com.example.eio;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class account extends Activity{
    private FileNameString fnm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsetting);

        fnm = (FileNameString) this.getApplication();

        final EditText editText_account = findViewById(R.id.editTextTextPersonName);
        final EditText editText_password = findViewById(R.id.editTextTextPassword);

        Button account_set_button = findViewById(R.id.button_accountset);
        account_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // エディットテキストのテキストを取得
                String account_text = editText_account.getText().toString();
                String password_text = editText_password.getText().toString();
                // 取得したテキストを TextView に張り付ける
                fnm.setAccount(account_text,password_text);
                AlertDialog.Builder builder = new AlertDialog.Builder(account.this);
                builder.setMessage("アカウント情報をアプリに保存しました")
                        .setTitle("System Message")
                        .setPositiveButton("OK",null);
                builder.show();
            }
        });
    }
}
