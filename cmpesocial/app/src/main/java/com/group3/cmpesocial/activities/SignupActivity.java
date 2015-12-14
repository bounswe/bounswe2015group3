package com.group3.cmpesocial.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.User;

public class SignupActivity extends AppCompatActivity {

    private static String TAG = SignupActivity.class.getSimpleName();

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Spinner roleSpinner;
    private int type = 0;

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
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = User.UNDERGRADUATE;
            }
        });
    }

    public void signup(View v){
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

        JsonObject json = new JsonObject();
        json.addProperty("name", firstName);
        json.addProperty("surname", lastName);
        json.addProperty("email", email);
        json.addProperty("password", password);
        json.addProperty("type", type);

        Log.i(TAG,json.toString());

        int result = API.signup(json, getApplicationContext());
        if(result == API.ERROR){
            Toast.makeText(this, "something went wrong, try again", Toast.LENGTH_SHORT).show();
        }else if (result == API.SUCCESS){
            Toast.makeText(getApplicationContext(), "Signup successful, please wait for your confirmation email", Toast.LENGTH_LONG).show();
        }else if(result == API.RESULT_EMPTY){
            Log.i(TAG, "result empty");
            Toast.makeText(this, "something went wrong, try again", Toast.LENGTH_SHORT).show();
        }
    }

}
