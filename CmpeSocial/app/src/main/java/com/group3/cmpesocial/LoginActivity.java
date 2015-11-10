package com.group3.cmpesocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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
        if (emailEditText.getText() == null || emailEditText.getText().length() == 0 ||
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

        Ion.with(this)
                .load("http://54.148.86.208:8080/cmpesocial/api/login")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.i(TAG, "got result");
                        if (e != null) {
                            Log.i(TAG, "error " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "an error occured", Toast.LENGTH_LONG).show();
                        }else if (result != null) {
                            Toast.makeText(getApplicationContext(), "login done", Toast.LENGTH_LONG).show();

                            String type = trimQuotes(result.get("type").toString());
                            if (type.equals("SUCCESS")) {
                                JsonObject user = result.getAsJsonObject("user");
                                int user_id = Integer.parseInt(user.get("id").toString());
                                String name = trimQuotes(user.get("name").toString());
                                String surname = trimQuotes(user.get("surname").toString());
                                String password = trimQuotes(user.get("password").toString());
                                String email = trimQuotes(user.get("email").toString());
                                Log.i(TAG, "" + user_id + " " + name + " " + surname + " " + email + " " + password);

                                SharedPreferences.Editor editor = getSharedPreferences("prefsCMPE", MODE_PRIVATE).edit();
                                editor.putBoolean("user_exists", true);
                                editor.putInt("user_id", user_id);
                                editor.putString("name", name);
                                editor.putString("surname", surname);
                                editor.putString("email", email);
                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else if (type.equals("WRONG_PASSWORD")) {
                                Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_LONG).show();
                                passwordEditText.setText("");
                            }

                        } else {
                            Log.i(TAG, "result empty");
                            emptyFields();
                            Toast.makeText(getApplicationContext(), "result empty", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    protected String trimQuotes(String s){
        if (s.charAt(0) == '\"' && s.charAt(s.length()-1) == '\"')
            return s.substring(1, s.length() - 1);
        else
            return s;
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

}
