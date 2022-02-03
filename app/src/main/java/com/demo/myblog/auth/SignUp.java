package com.demo.myblog.auth;

import androidx.appcompat.app.AppCompatActivity; //////////

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demo.myblog.MainActivity;
import com.demo.myblog.R;
import com.demo.myblog.volley.VolleySingleton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    // sign in URL
    private String appURL;

    // from signup form
    private String NAME, EMAIL, PASSWORD, CONFIRM_PASSWORD;
    MaterialEditText mName, mEmail, mPassword, mConfirmPassword;
    CheckBox mTerms;
    Activity mContext = this;
    Button mSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        appURL = "http://152.13.76.150/api/signup.php";
        mName = findViewById(R.id.text_Name);
        mEmail = findViewById(R.id.text_Email);
        mPassword = findViewById(R.id.text_Password);
        mConfirmPassword = findViewById(R.id.text_Confirm_Password);
        mTerms = findViewById(R.id.terms);
        mSignUp = findViewById(R.id.btn_signup);

        //set onClick listener for signup button
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // attempt sign up
                signUp();
            }
        });

    }

    private void signUp() { // account registration
        NAME = mName.getText().toString();
        EMAIL = mEmail.getText().toString();
        PASSWORD = mPassword.getText().toString();
        CONFIRM_PASSWORD = mConfirmPassword.getText().toString();

        if (NAME.isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Name cannot be empty");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        } else if (NAME.length() < 4) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Name cannot be less than 4 characters");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        }
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
        } else if (!PASSWORD.equals(CONFIRM_PASSWORD)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("Password and Confirm Password do not match");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        } else if (!mTerms.isChecked()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setMessage("You must accept the terms and conditions");
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
                public void onResponse(String response) { // registration is successful
                    // remove leading \r or \n
                    response = response.replaceAll("\\r\\n", "");
                    if (response.equals("true")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Success");
                        alert.setMessage("You have successfully created an account \n You can login now.");
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        alert.show();

                    } else { // not successful
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Error");
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
                    params.put("name", NAME);
                    params.put("email", EMAIL);
                    params.put("password", PASSWORD);
                    return params;
                }
            };
            VolleySingleton.getInstance().addRequestQueue(stringRequest);
        }

    }
}