package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fazal.capalino.CustomViews.CustomButton;
import com.example.fazal.capalino.CustomViews.CustomEditText_Book;
import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivationActivity extends Activity {

    private CustomEditText_Book email_et;
    private CustomEditText_Book activation_et;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        init();
    }

    private void init() {
        View view = findViewById(R.id.activationdialog);
        view.getBackground().setAlpha(200);
        if(getIntent().getStringExtra("email")!=null)
        email_et = (CustomEditText_Book)findViewById(R.id.email);
        if(getIntent().getStringExtra("email")!=null)
        email_et.setText(getIntent().getStringExtra("email"));
        activation_et = (CustomEditText_Book) findViewById(R.id.activation_code);

    }

    public void SubmitClick(View view){
        try{
            check_activationcode();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void check_activationcode() {
        Thread check_thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    final String email = getIntent().getStringExtra("email");
                    final String password = getIntent().getStringExtra("pass");
                    String url = "https://celeritas-solutions.com/cds/capalinoapp/apis/getActivationCode.php?UserEmailAddress=" + email + "&UserPassword=" + password;

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    JSONArray jsonarray = new JSONArray(response);
                    JSONObject jsonobj = jsonarray.getJSONObject(0);
                    String activation_code = jsonobj.getString("ActivationCode");
                    if (activation_code.equalsIgnoreCase(activation_et.getText().toString())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateRecord(email,password);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        check_thread.start();
    }

    private void updateRecord(String email, String password) {
        String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/updateStatusCode.php?UserEmailAddress="+email+"&UserPassword="+password;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              AlertDialog dialog =  new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("This Agreement and any operating rules for Capalino's website established  by Capalino" +
                                " constitute the entire agreement of the parties with respect to the subject matter hereof," +
                                " and supersede all previous written or oral agreements between the parties with respect to such subject matter." +
                                " This Agreement shall be construed in accordance with the laws of the State of New York," +
                                " without regard to its conflict of laws rules.")
                        .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                //updateRecord(email,password);

                                new android.app.AlertDialog.Builder(context)
                                        .setTitle("Alert!")
                                        .setMessage("Your account has been activated. You are redirecting to a login page, " +
                                                "Please sign in to access MWBE Connect NY.")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                Intent i = new Intent(ActivationActivity.this, LoginActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(i);
                                            }
                                        })
                                        .show();
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                Typeface face=Typeface.createFromAsset(getAssets(), "gotham_book.otf");
                textView.setTypeface(face);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);

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
