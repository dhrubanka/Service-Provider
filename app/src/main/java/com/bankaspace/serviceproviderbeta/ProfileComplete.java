package com.bankaspace.serviceproviderbeta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.EventLogTags;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import com.android.volley.VolleyError;
import com.bankaspace.serviceproviderbeta.Networks.HTTP_Post_Callback;
import com.bankaspace.serviceproviderbeta.Networks.Post_to_Server;
import android.provider.MediaStore;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Base64;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import static com.bankaspace.serviceproviderbeta.Urls.URL_PROFILECOMPLETE;


public class ProfileComplete extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText Editownername, EditOwnerNo, EditPricing, EditDescription, EditWhatsapp, Editlocality,editaddress;
    TextView Category;
    Spinner spinner;
    Button btn;
    Bitmap bitmap;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_complete);

        //binding views
        imageView = (ImageView)findViewById(R.id.imageView);
        Editownername = (EditText) findViewById(R.id.editOwnername);
        EditOwnerNo = (EditText) findViewById(R.id.editOwnerNumber);
        EditPricing = (EditText) findViewById(R.id.editPricing);
        EditDescription = (EditText) findViewById(R.id.description);
        Category = (TextView) findViewById(R.id.category);
        spinner = (Spinner) findViewById(R.id.service_cat_spinner);
        EditWhatsapp=(EditText)findViewById(R.id.editWhatsappNumber);
        btn = (Button) findViewById(R.id.ButtonSubmit);
        Editlocality=(EditText)findViewById(R.id.locality);
        editaddress=(EditText)findViewById(R.id.address);


        Button btn_upload = (Button) findViewById(R.id.btn_upload);
        // upload listener
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.service_category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //submit btn listener
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterNow();
            }
        });


    }

    String cat;

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        cat = (String) parent.getItemAtPosition(pos);
        Category.setText(cat);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    String ConvertImage;
    //image encoding
    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imageView.setImageBitmap(bitmap);

                ByteArrayOutputStream byteArrayOutputStreamObject ;

                byteArrayOutputStreamObject = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

                byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

                ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    String username, userno, pricing, description, whatsappno,locality,address;



    private void setValues() {

        username = Editownername.getText().toString().trim();
        userno = EditOwnerNo.getText().toString().trim();
        pricing = EditPricing.getText().toString().trim();
        description = EditDescription.getText().toString().trim();
        whatsappno = EditWhatsapp.getText().toString().trim();
        locality=Editlocality.getText().toString().trim();
        address=editaddress.getText().toString().trim();

    }

    private boolean checkEmpty() {
        boolean ok = true;


        if (TextUtils.isEmpty(username)) {
            Editownername.setError("Please enter username");
            EditOwnerNo.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(userno)) {
            EditOwnerNo.setError("Please enter your number");
            EditOwnerNo.requestFocus();
            return false;
        }


        if (TextUtils.isEmpty(pricing)) {
            EditPricing.setError("Enter a price range");
            EditPricing.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            EditDescription.setError("Enter your businness profile description");
            EditDescription.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(whatsappno)) {
            EditWhatsapp.setError("Enter your businness whatsapp no");
            EditWhatsapp.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(locality)) {
            Editlocality.setError("Enter your locality");
            Editlocality.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            editaddress.setError("Enter your address");
            editaddress.requestFocus();
            return false;
        }

        return ok;
    }


    private void RegisterNow() {
        setValues();
        if (checkEmpty() == false) {
            return;
        }
        String emaile;
        try {
             emaile = getIntent().getExtras().getString("emaile");
        } catch (NullPointerException e ) {
            emaile = "something_else";
            Toast.makeText(ProfileComplete.this, "STFU", Toast.LENGTH_SHORT).show();
        }
        String profilepic_name="Profilename" +emaile;

            final ProgressDialog dialog= new ProgressDialog(this);
            dialog.setMessage("Wait.. in progress ");
            dialog.show();


            Map<String, String> params = new HashMap<>();
            params.put("category", cat);
            params.put("ownername", username);
            params.put("ownerno", userno);
            params.put("pricing", pricing);
            params.put("description", description);
            params.put("whatsappno", whatsappno);
            params.put("email",emaile);
            params.put("image_name", profilepic_name);
            params.put("encoded_string",ConvertImage);
            params.put("locality",locality);
            params.put("address",address);

            new Post_to_Server(this, params).getJson(URL_PROFILECOMPLETE, new HTTP_Post_Callback() {
                @Override
                public void onSuccess(String string) {
                    try {
                        JSONObject job = new JSONObject(string);
                        int SUCCESS = job.getInt("SUCCESS");
                        if (SUCCESS == 1) {
                            dialog.dismiss();
                            Toast.makeText(ProfileComplete.this, "Profile Completed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), TabActivity.class));

                        } else {

                                dialog.dismiss();
                                Toast.makeText(ProfileComplete.this, "Not registered!" , Toast.LENGTH_SHORT).show();
                            }

                    } catch (JSONException ex) {

                        Toast.makeText(ProfileComplete.this, "Network Error", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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
        Toast.makeText(this, "Complete your Profile", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    }


