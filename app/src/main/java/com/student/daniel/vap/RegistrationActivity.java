package com.student.daniel.vap;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RegistrationActivity extends AppCompatActivity {
    private EditText password;

    private EditText confirmPassword;
    private Button registraciaButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracia);
        if (check()) {

            password = (EditText) findViewById(R.id.heslo);

            confirmPassword = (EditText) findViewById(R.id.hesloznova);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean check() {

        String ret = "";

        try {
            InputStream inputStream = getApplicationContext().openFileInput("ok.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", R.string.fileNotFound + ": " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", R.string.cannotRead + ": " + e.toString());
        }

        if (ret.equals("")) {
            return true;
        }
        return false;
    }


    public void Registration(View view) {
        if (TextUtils.isEmpty(password.getText())){
            password.setError(getString(R.string.empty));
            password.clearComposingText();
           // confirmPassword.setErrorEnabled(false);

        } else {
            if (TextUtils.isEmpty(confirmPassword.getText())) {
                confirmPassword.setError(getString(R.string.empty));
            //    password.setErrorEnabled(false);
                confirmPassword.clearComposingText();
            } else {

                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(this,R.string.diff, Toast.LENGTH_SHORT).show();
                    confirmPassword.clearComposingText();
                    password.clearComposingText();
                } else {
                    writeToFile("ok", "1");
                    writeToFile("login", password.getText().toString());

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }


    }

    public void writeToFile(String filename, String input) {

        try {
            FileOutputStream fileout = openFileOutput(filename + ".txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(input);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
