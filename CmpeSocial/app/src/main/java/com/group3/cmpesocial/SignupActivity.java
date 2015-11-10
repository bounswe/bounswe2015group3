package com.group3.cmpesocial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SignupActivity extends AppCompatActivity {

    private static String TAG = SignupActivity.class.getSimpleName();

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Spinner roleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        roleSpinner = (Spinner) findViewById(R.id.roleSpinner);

        String[] roles = {"Undergraduate", "Graduate", "Alumni", "Faculty Member", "Staff"};
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,roles);
        roleSpinner.setAdapter(adapter);
    }

    public void signup(View v){
        Toast.makeText(this, "sign up", Toast.LENGTH_LONG).show();

        if (firstNameEditText.getText() == null || firstNameEditText.getText().length() == 0 ||
                lastNameEditText.getText() == null || lastNameEditText.getText().length() == 0 ||
                emailEditText.getText() == null || emailEditText.getText().length() == 0 ||
                passwordEditText.getText() == null || passwordEditText.getText().length() == 0 ||
                confirmPasswordEditText.getText() == null || confirmPasswordEditText.getText().length() == 0) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        final String firstName = firstNameEditText.getText().toString().trim();
        final String lastName = lastNameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (firstName.matches(".*\\d.*") || lastName.matches(".*\\d.*")){
            Toast.makeText(this, "Invalid name", Toast.LENGTH_LONG).show();
            return;
        }
        if (!email.contains("@")){
            Toast.makeText(this, "Invalid e-mail address", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG).show();
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
            return;
        }

        ionSignupRequest(firstName, lastName, email, password);
    }

    public void ionSignupRequest(String firstName, String lastName, String email, String password){
        JsonObject json = new JsonObject();
        json.addProperty("name", firstName);
        json.addProperty("surname", lastName);
        json.addProperty("email", email);
        json.addProperty("password", password);

        Log.i(TAG,json.toString());

        Ion.with(this)
                .load("http://54.148.86.208:8080/cmpesocial/api/signup")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.i(TAG, "got result");
                        if(e != null)
                            Log.i(TAG, "error " + e.getMessage());
                        if(result != null)
                            Toast.makeText(getApplicationContext(), "signup done", Toast.LENGTH_LONG).show();
                        else
                            Log.i(TAG, "result empty");
                    }
                });
    }

}
