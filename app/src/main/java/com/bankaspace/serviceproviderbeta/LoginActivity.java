package com.bankaspace.serviceproviderbeta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bankaspace.serviceproviderbeta.Networks.HTTP_Post_Callback;
import com.bankaspace.serviceproviderbeta.Networks.Post_to_Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.bankaspace.serviceproviderbeta.Urls.URL_LOGIN;
import static com.bankaspace.serviceproviderbeta.Urls.URL_REGISTER;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, TabActivity.class));
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);


        //if user presses on login
        //calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterNow();
            }
        });

        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    String email,password;
    private void setvalues() {
        //first getting the values
        email = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
    }
    private boolean checkEmpty(){
        boolean ok = true;
        //validating inputs
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return false;
        }
return ok;

    }
    private void RegisterNow(){
        setvalues();
        if(checkEmpty()==false)
        {return;}
        //diaglog progress
        final ProgressDialog dialog= new ProgressDialog(this);
        dialog.setMessage("Logging ");
        dialog.show();

        Map<String, String> params = new HashMap<>();

        params.put("email", email);
        params.put("password", password);

        new Post_to_Server(this, params).getJson(URL_LOGIN, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {
                try {
                    JSONObject job = new JSONObject(string);
                    int SUCCESS = job.getInt("SUCCESS");
                    if (SUCCESS == 3) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();

                    } else {

                        if (SUCCESS == 1) {
                            dialog.dismiss();
                            JSONObject userJson = job.getJSONObject("user");
                            User user = new User(
                                    userJson.getString("userid"),
                                    userJson.getString("username"),
                                    userJson.getString("email"),
                                    userJson.getString("gender"),
                                    userJson.getString("category"),
                                    userJson.getString("fullname"),
                                    userJson.getString("personalno"),
                                    userJson.getString("pricing"),
                                    userJson.getString("description"),
                                    userJson.getString("whatsapp"),
                                    userJson.getString("picpath"),
                                    userJson.getString("locality"),
                                    userJson.getString("address")
                                    );
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //starting the profile activity
                            finish();
                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,TabActivity.class);

                            startActivity(intent);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "User not register", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                catch (JSONException ex){
                    dialog.cancel();
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });


    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
