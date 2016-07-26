package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fazal.capalino.AppConstants.Utils;
import com.example.fazal.capalino.R;

/**
 * Created by Fazal on 7/26/2016.
 */
public class UpdateProfileActivity extends Activity {

    private Context context = this;
    private Utils utils;
    private TextView fname_et;
    private TextView lname_et;
    private TextView email_et;
    private TextView phno_et;
    private TextView address_et;
    private TextView city_et;
    private TextView state_et;
    private String responsefromserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_fragment);
        init();
    }

        public void BackClick(View view){
            finish();
            startActivity(new Intent(this,SettingsActivity.class));
        }


    private void init() {
        utils = new Utils(this);
        fname_et = (TextView) findViewById(R.id.name);
        lname_et = (TextView) findViewById(R.id.lastname);
        email_et = (TextView) findViewById(R.id.email);
        phno_et = (TextView) findViewById(R.id.phno);
        address_et = (TextView) findViewById(R.id.address);
        city_et = (TextView) findViewById(R.id.city);
        state_et = (TextView) findViewById(R.id.state);

        String fname = utils.getdata("fname");
        String lname = utils.getdata("lname");
        String email = utils.getdata("email");
        fname_et.setText(fname);
        lname_et.setText(lname);
        email_et.setText(email);
    }

    public void UpdateClick(View view) {
        if (fname_et.getText().length() > 0 && lname_et.getText().length() > 0 && email_et.getText().length() > 0) {
            String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/updateUserProfile.php?UserEmailAddress=" + email_et.getText().toString() +
                    "&UserFirstName=" + fname_et.getText().toString() + "&UserLastName=" + lname_et.getText().toString() +
                    "&UserMobilePhone=" + phno_et.getText().toString() + "&UserAddressLine1=" + address_et.getText().toString() +
                    "&UserCity=" + city_et.getText().toString() + "&UserState=" + state_et.getText().toString();

            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, params[0], new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Records Updated.")) {

                                responsefromserver = response;
                            } else {
                                new AlertDialog.Builder(context)
                                        .setTitle("Alert!")
                                        .setMessage("There is a problem with getting response from server,please try again")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("Check your Internet Connection")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            utils.savedata("fname", fname_et.getText().toString());
                                            utils.savedata("lname", lname_et.getText().toString());
                                            utils.savedata("email", email_et.getText().toString());
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                        }
                    });

                    Volley.newRequestQueue(UpdateProfileActivity.this).add(stringRequest);
                    return responsefromserver;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (s != null) {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Data Updated")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }


            }.execute(url, "", "");
        }else {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("All the Field required to proceed further")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    //Footer Click Listener
    public void HomeClick(View view){
        Intent i = new Intent(UpdateProfileActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view){
        Intent i = new Intent(UpdateProfileActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(UpdateProfileActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(UpdateProfileActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
}
