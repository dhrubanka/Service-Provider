package com.bankaspace.serviceproviderbeta;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Enquiries extends Fragment {
    private static final String URL_ENQ = "http://babydchere.96.lt/enqlist.php";

    //a list to store all the products
    List<category> categoryList;

    //the recyclerview
    RecyclerView recyclerView;
    categoryAdapter adapter;
    String userid;


    public Enquiries() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enq, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //initializing the productlist
        categoryList = new ArrayList<>();
        User user=SharedPrefManager.getInstance(getContext()).getUser();
        userid=user.getId();
        load();
        return rootView;
    }

    private void load() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ///
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ENQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray array = jsonObj.getJSONArray("records");

                            //traversing through all the object
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject categor = array.getJSONObject(i);

                                category catt = new category(
                                        categor.getString("enq_no"),
                                        categor.getInt("userid"),
                                        categor.getString("name"),
                                        categor.getString("email_client"),
                                        categor.getString("date"),
                                        categor.getString("time")

                                );

                                //adding the product to product list
                                categoryList.add(catt);
                                adapter = new categoryAdapter(getActivity(), categoryList, new categoryAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(category item) {
                                        String enq=item.getEnq();

                                        Intent intent=new Intent(getActivity(), DetailMessage.class);
                                        intent.putExtra("userid",userid);
                                        intent.putExtra("enq",enq);
                                        startActivity(intent);
                                    }
                                });
                                recyclerView.setAdapter(adapter);
                                progressDialog.dismiss();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);

                return params;
            }
        };

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
