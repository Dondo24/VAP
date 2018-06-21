package com.student.daniel.vap;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {
    private EditText passwordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        passwordLogin = (EditText) findViewById(R.id.hesloPrihlasenie);
    }


    public boolean readFile(String file) {
        String s = "";
        try {
            FileInputStream fileIn = openFileInput(file + ".txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[100];
            int charRead;
            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (passwordLogin.getText().toString().equals(s) ) {
            return true;
        }
        return false;

    }

    public void webView(View view) {
        Intent intent = new Intent(LoginActivity.this, Web.class);
        startActivity(intent);
    }

    public void login(View view) {

            if (TextUtils.isEmpty(passwordLogin.getText())) {
                passwordLogin.setError(getString(R.string.empty));


            } else {
                if (readFile("login")) {

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {


                    Toast.makeText(this,R.string.badPasswoerd, Toast.LENGTH_SHORT).show();
                }
            }


        }
}
