package com.group3.cmpesocial;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = LoginActivity.class.getSimpleName();

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    public void login(View v) {
        if (!isNetworkConnected()){
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }
        else if (emailEditText.getText() == null || emailEditText.getText().length() == 0 ||
                passwordEditText.getText() == null || passwordEditText.getText().length() == 0) {
            Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_LONG).show();
            return;
        }

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        ionLoginRequest(email, password);
    }

    protected void ionLoginRequest(String email, String password){
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);

        Log.i(TAG, json.toString());

        int result = API.login(json, getApplicationContext());

        if (result == API.ERROR){
            Toast.makeText(getApplicationContext(), "an error occured, please try again", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (result == API.WRONG_PASSWORD){
            Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_LONG).show();
            passwordEditText.setText("");
        }else if (result == API.RESULT_EMPTY){
            emptyFields();
            Toast.makeText(getApplicationContext(), "result empty", Toast.LENGTH_LONG).show();
        }

    }

    /*
     * Empty the fields in case of an unsuccessful login attempt
     */
    protected void emptyFields(){
        emailEditText.setText("");
        passwordEditText.setText("");
    }

    public void signup(View v) {
        Toast.makeText(this, "signup", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
