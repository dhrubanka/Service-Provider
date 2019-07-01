package com.bankaspace.serviceproviderbeta;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;




import com.android.volley.VolleyError;
import com.bankaspace.serviceproviderbeta.Networks.HTTP_Post_Callback;
import com.bankaspace.serviceproviderbeta.Networks.Post_to_Server;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.bankaspace.serviceproviderbeta.Urls.URL_REGISTER;

public class MainActivity extends AppCompatActivity {


    EditText editTextUsername, editTextEmail, editTextPassword;
    RadioGroup radioGroupGender;
    ProgressBar progressBar;
    Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, TabActivity.class));
            return;
        }

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        radioGroupGender = (RadioGroup) findViewById(R.id.radioGender);
        btn_register = (Button) findViewById(R.id.buttonRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterNow();
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                finish();

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


    }

    String username, email, password, gender;




    private void setValues() {

        username = editTextUsername.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

    }

    private boolean checkEmpty(){
        boolean ok = true;


        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return false;
        }

        return ok;
    }


    private void RegisterNow(){
        setValues();
        if(checkEmpty()==false){return;}
        final ProgressDialog dialog= new ProgressDialog(this);
        dialog.setMessage("Wait.. in progress ");
        dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);
        params.put("gender", gender);
        new Post_to_Server(this, params).getJson(URL_REGISTER, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject job = new JSONObject(string);
                    int SUCCESS = job.getInt("SUCCESS");
                    if (SUCCESS == 2) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "User Already registered", Toast.LENGTH_SHORT).show();

                    } else {

                        if (SUCCESS == 1) {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,ProfileComplete.class);
                            intent.putExtra("emaile",email);
                            startActivity(intent);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                catch (JSONException ex){
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError error) {
                dialog.dismiss();

            }
        });


    }

}
