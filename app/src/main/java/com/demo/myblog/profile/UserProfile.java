package com.demo.myblog.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.myblog.R;
import com.demo.myblog.storage.SharedPrefManager;
import com.demo.myblog.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {
    private String ID, NAME, EMAIL, CREATED_DATE;
    Activity mContext = this;
    // web application URL - login URL
    String appURL;
    TextView mId, mName, mEmail, mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mId = findViewById(R.id.text_id);
        mName = findViewById(R.id.text_name);
        mEmail = findViewById(R.id.text_email);
        mDate = findViewById(R.id.text_date);

        Intent data = getIntent();
        //EMAIL = data.getStringExtra("email");
        // get email from shared preference
        SharedPrefManager sharedPrefManager = new SharedPrefManager();
        EMAIL = sharedPrefManager.getEmail(mContext);
        appURL = "http://152.13.76.150/api/getUserDetail.php?email="+EMAIL;

        getUserDetail();

    }

    // fetch user data
    private void getUserDetail() {
        if (EMAIL.isEmpty()) { // check if email empty
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Email cannot be empty");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, appURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) { // successfully logged in
                    try { // get JSON data from server
                        JSONObject jsonObject = new JSONObject(response);
                        ID = jsonObject.getString("id");
                        NAME = jsonObject.getString("name");
                        EMAIL = jsonObject.getString("email");
                        CREATED_DATE = jsonObject.getString("created_date");

                        mId.setText(ID);
                        mName.setText(NAME);
                        mEmail.setText(EMAIL);
                        mDate.setText(CREATED_DATE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder alert;
                    // Volley Network response error
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 400:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage("The request could not be understood by the server due to malformed syntax");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                            case 404:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage("The server has not found anything matching the Request-URI");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                            case 403:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage("The server understood the request, but is refusing to fulfill it");
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                alert.show();
                                break;

                        }
                    } else { // null
                        alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Error");
                        alert.setMessage("There is an error. Please try again");
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alert.show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Accept", "Application/json; charset=UTF-8");
                    return params;
                }
            };
            // send request
            VolleySingleton.getInstance().addRequestQueue(stringRequest);
        }

    }
}
