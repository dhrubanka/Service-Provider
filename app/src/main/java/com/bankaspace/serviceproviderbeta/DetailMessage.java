package com.bankaspace.serviceproviderbeta;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bankaspace.serviceproviderbeta.Networks.HTTP_Post_Callback;
import com.bankaspace.serviceproviderbeta.Networks.Post_to_Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailMessage extends AppCompatActivity {
    TextView nameClient,emailClient,phoneClient,messageClient;
    String userid;
    String URL= "http://babydchere.96.lt/clientdetails.php";
    String UID;
    String enq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);
        nameClient=findViewById(R.id.nameClient);
        emailClient=findViewById(R.id.emailClient);
        phoneClient=findViewById(R.id.phoneClient);
        messageClient=findViewById(R.id.messageClient);

        try {

             UID = getIntent().getExtras().getString("userid");
             enq=getIntent().getExtras().getString("enq");

        } catch (NullPointerException e ) {
        }


        load();


    }

        public void load(){
         //diaglog progress
         final ProgressDialog dialog= new ProgressDialog(this);
         dialog.setMessage("Logging ");
         dialog.show();

         Map<String,String> params=new HashMap<>();
         params.put("userid",UID);
         params.put("enq",enq);

         new Post_to_Server(this, params).getJson(URL, new HTTP_Post_Callback() {
             @Override
             public void onSuccess(String string) {
                 try {
                     JSONObject job = new JSONObject(string);
                     int SUCCESS = job.getInt("SUCCESS");


                     if (SUCCESS == 1) {
                         dialog.dismiss();
                         JSONObject userJson = job.getJSONObject("user");
                         Message message = new Message(
                                 userJson.getString("name"),
                                 userJson.getString("email_client"),
                                 userJson.getString("phone"),
                                 userJson.getString("message"),
                                 userJson.getString("date"),
                                 userJson.getString("time")
                                );

                         nameClient.setText(message.getName());
                         emailClient.setText(message.getEmail());
                         phoneClient.setText(message.getPhone());
                         messageClient.setText(message.getMessage());


                     } else {
                         dialog.dismiss();
                         Toast.makeText(DetailMessage.this, "Error", Toast.LENGTH_SHORT).show();
                     }
                 }


                catch (JSONException ex){
                 dialog.cancel();
                 Toast.makeText(DetailMessage.this, "Error", Toast.LENGTH_SHORT).show();
             }
         }

         @Override
         public void onError(VolleyError error) {

         }
     });


}



}

