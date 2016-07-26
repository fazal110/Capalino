package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fazal.capalino.AppConstants.Utils;
import com.example.fazal.capalino.CustomViews.CustomCheckBox;
import com.example.fazal.capalino.CustomViews.CustomEditText_Book;
import com.example.fazal.capalino.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String PACKAGE = "com.example.fazal.capalino";
    private CustomEditText_Book password_et;
    private CustomEditText_Book email_et;
    private Context context = this;
    private CustomCheckBox rememberme;
    private Utils utils;
    private ProgressDialog pb;
    private boolean isactivated;
    private String activationcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }

    private void init() {
        //deleteDatabase("CapalinoDataBase.sqlite");
        View mainview = findViewById(R.id.logindialog);
        mainview.getBackground().setAlpha(200);
        
        password_et = (CustomEditText_Book) findViewById(R.id.password);
        email_et = (CustomEditText_Book)findViewById(R.id.email);
        rememberme = (CustomCheckBox) findViewById(R.id.remember_check);
        utils = new Utils(LoginActivity.this);
        remember_check();
            email_et.setText(utils.getdata("email"));
        password_et.setText(utils.getdata("pass"));
        if(utils.getdata("ischecked").equalsIgnoreCase("true")) {
            rememberme.setChecked(true);
            startActivity(new Intent(LoginActivity.this, Splash.class));
            finish();
        }else {
            rememberme.setChecked(false);
        }


    }

    private void remember_check() {
        rememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    utils.savedata("email", email_et.getText().toString());
                    utils.savedata("pass", password_et.getText().toString());
                    utils.savedata("ischecked", isChecked + "");
                } else {
                    utils.savedata("email","");
                    utils.savedata("pass","");
                    utils.savedata("ischecked","false");
                }
            }
        });
    }

    public void RegisterClick(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void LoginClick(View view){
        Login();
    }

    private void Login() {
        showPB("Loading...");
        if(email_et.length()>0 && password_et.length()>0) {
            String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/getLogin.php?UserEmailAddress=" + email_et.getText().toString() + "&UserPassword=" + password_et.getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.equalsIgnoreCase("Login Failed")) {
                        checkAccount();

                    }
                    else {
                        hidePB();
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Email & Password is incorrect, please try again.")
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
                    hidePB();
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
        }else {
            hidePB();
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Email & Password Required to proceed further...")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    private void checkAccount() {
        try{
            String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/getLogin.php?UserEmailAddress=" + email_et.getText().toString() + "&UserPassword=" + password_et.getText().toString();
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    try{
                        HttpClient httpclient = new DefaultHttpClient();
                        //showPB("Loading....");
                        HttpPost httppost = new HttpPost(params[0]);

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response = httpclient.execute(httppost,
                                responseHandler);

                        Log.i("Response", "Response : " + response);
                        JSONArray jsonarray = new JSONArray(response);
                        JSONObject jsonobj = jsonarray.getJSONObject(0);
                        if(jsonobj.has("UserID")) {
                            if (jsonobj.getString("UserID") != null) {
                                activationcode = jsonobj.getString("UserID");
                                return activationcode;
                            }
                        }
                        activationcode = jsonobj.getString("UserStatusCode");

                        return activationcode;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return activationcode;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(!s.equalsIgnoreCase("0")){
                        isactivated = true;
                        hidePB();
                        if(isactivated) {
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            i.putExtra("islogin","yes");
                            utils.savedata("Userid",s);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                        }
                    }else {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Account is not activated.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }.execute(url,"","");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void generateHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());


                String hashkey = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                hashkey = hashkey+".";
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(utils.getdata("ischecked").equalsIgnoreCase("true"))
        rememberme.setChecked(true);
        else
            rememberme.setChecked(false);
        email_et.setText(utils.getdata("email"));
        password_et.setText(utils.getdata("pass"));
    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(LoginActivity.this);
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
}
