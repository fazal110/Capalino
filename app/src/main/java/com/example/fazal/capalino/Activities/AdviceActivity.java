package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fazal.capalino.AppConstants.Utils;
import com.example.fazal.capalino.CustomViews.CustomEditText_Book;
import com.example.fazal.capalino.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdviceActivity extends Activity {

    private CustomEditText_Book full_name;
    private Context context = this;
    private CustomEditText_Book email;
    private CustomEditText_Book phno;
    private CustomEditText_Book calltime;
    private CustomEditText_Book comment;
    private ProgressDialog pb;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        init();
    }

    private void init() {
        utils = new Utils(AdviceActivity.this);

        phno = (CustomEditText_Book) findViewById(R.id.phone_no);
        calltime = (CustomEditText_Book) findViewById(R.id.call_time);
        comment = (CustomEditText_Book) findViewById(R.id.comment);
    }

    //BackClick Event
    public void BackClick(View view){
        finish();
        startActivity(new Intent(this,BrowseActivity.class));
    }

    public void SendClick(View view){
        try{
            if(phno.length()==0 || calltime.length()==0 || comment.length()==0) {
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("All the input field must be filled to further proceed...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else {
                SendRequestToServer();
            }
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void SendRequestToServer() {
        try{
            showPB("Loading...");
            String fullname = utils.getdata("fullname");
            String mail = utils.getdata("email");
            String ph_no = phno.getText().toString();
            String call_time = calltime.getText().toString();
            String commnt = comment.getText().toString();
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy E 'at' hh:mm a");
            Date date = new Date();


            String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/addadviceRequest.php?FullName="+fullname+
                    "&EmailAddress="+mail+"&PhoneNumber="+ph_no+"&TimeToCall="+call_time+
                    "&Comments="+commnt+"&RequestAddedDateTime="+dateformat.format(date);
            url = url.replace(" ","%20");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equalsIgnoreCase("Records Added.")) {
                        hidePB();
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Data has been sent to the server")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       startActivity(new Intent(AdviceActivity.this,REFListingActivity.class));
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                    else {
                        hidePB();
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Check Your Internet Conection, Please try again.")
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
                    //hidePB();
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Check your Internet Connection")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                }
            });

            Volley.newRequestQueue(this).add(stringRequest);

        }catch (Exception e){
                e.printStackTrace();
            }

    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(AdviceActivity.this);
                pb.setMessage(message);
                pb.show();
            }
        });

    }

    void hidePB() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pb != null && pb.isShowing())
                    pb.dismiss();
            }
        });

    }

    //Footer Tab Event Click
    public void HomeClick(View view){

        try{
            Intent i = new Intent(AdviceActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void SettingsClick(View view){
        try {
            Intent i = new Intent(AdviceActivity.this, SettingsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void TrackClick(View view){
        try {
            Intent i = new Intent(AdviceActivity.this, TrackList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

}
