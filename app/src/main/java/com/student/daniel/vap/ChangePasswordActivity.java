package com.student.daniel.vap;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmNewPassword = (EditText) findViewById(R.id.hesloznova);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private boolean check() {

        String ret = "";

        try {
            InputStream inputStream = getApplicationContext().openFileInput("login.txt");

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

        if (oldPassword.getText().toString().equals(ret)) {
            return true;
        }
        return false;
    }

    public void changePassword(View view) {
        if (TextUtils.isEmpty(oldPassword.getText())) {
            oldPassword.setError(getString(R.string.empty));

        } else {
            if (TextUtils.isEmpty(newPassword.getText())) {
                newPassword.setError(getString(R.string.empty));

            } else {
                if (TextUtils.isEmpty(confirmNewPassword.getText())) {
                    confirmNewPassword.setError(getString(R.string.empty));

                } else {

                    if (!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())) {
                        Toast.makeText(this, R.string.diff, Toast.LENGTH_SHORT).show();
                    } else {
                        if (readFile("login")) {
                            writeToFile("login",newPassword.getText().toString());
                            Toast.makeText(this, R.string.succes, Toast.LENGTH_SHORT).show();
                            finish();


                        } else {
                            Toast.makeText(this, R.string.badOldPassword, Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            }
        }
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


        if (oldPassword.getText().toString().equals(s)) {
            return true;
        }
        return false;

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
