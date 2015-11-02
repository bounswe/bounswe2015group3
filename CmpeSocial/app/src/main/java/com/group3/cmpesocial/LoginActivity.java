package com.group3.cmpesocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v){
        Toast.makeText(this, "login", Toast.LENGTH_LONG).show();
        EditText emailEditText = (EditText) findViewById(R.id.emailEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        if (emailEditText.getText() == null || emailEditText.getText().length() == 0 ||
                passwordEditText.getText() == null || passwordEditText.getText().length() == 0){
            Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_LONG).show();
            return;
        }

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.i("email", "" + email);
        Log.i("password", "" + password);
    }

    public void signup(View v){
        Toast.makeText(this, "signup", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void noLogin(View v){
        Toast.makeText(this, "noLogin", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("user", -1);
        startActivity(intent);
        finish();
    }
}
