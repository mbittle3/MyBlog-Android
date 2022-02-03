package com.demo.myblog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.myblog.auth.SignUp;
import com.demo.myblog.profile.UserProfile;
import com.demo.myblog.storage.SharedPrefManager;
import com.demo.myblog.volley.VolleySingleton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    // web application URL - login URL
    String appURL = "http://152.13.76.150/api/login.php";
    // from login form
    private String EMAIL, PASSWORD;

    // from form
    MaterialEditText mEmail, mPassword;
    Button mButton;
    Activity mContext = this;
    TextView mCreateAcct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set form varaibles
        mEmail = findViewById(R.id.text_Email);
        mPassword = findViewById(R.id.text_Password);
        mCreateAcct = findViewById(R.id.lb_CreateAccount);
        mButton = findViewById(R.id.btn_login);

        // set on Click listener for create account
        mCreateAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        // set onClick listener for login button
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when button is clicked, attempt login
                login();
            }
        });

    }

    private void login() { // login function
        // extract strings
        EMAIL = mEmail.getText().toString();
        PASSWORD = mPassword.getText().toString();

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
        } else if (PASSWORD.isEmpty()) { // check if password empty
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Password cannot be empty");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        } else if (PASSWORD.length() < 8) { // check password length
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Password length must not be less than 8 characters");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, appURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) { // successfully logged in
                    // remove leading \r or \n
                    response = response.replaceAll("\\r\\n", "");
                    if (response.equals("true")) {
                        Intent intent = new Intent(mContext, Dashboard.class);
                        //intent.putExtra("email", EMAIL);

                        // save email
                        SharedPrefManager sharedPrefManager = new SharedPrefManager();
                        sharedPrefManager.saveEmail(getApplicationContext(), EMAIL);
                        Log.d(TAG, "email saved" + EMAIL);

                        // launch UserProfile  activity
                        startActivity(intent);
                    } else { // login failed
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setMessage(response);
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

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", EMAIL);
                    params.put("password", PASSWORD);
                    return params;
                }
            };
            // send request
            VolleySingleton.getInstance().addRequestQueue(stringRequest);
        }

    }

}