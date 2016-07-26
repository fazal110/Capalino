package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fazal.capalino.AppConstants.Utils;
import com.example.fazal.capalino.CustomViews.CustomEditText_Book;
import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.PasswordValidator;
import com.example.fazal.capalino.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RegisterActivity extends Activity {

    private CustomEditText_Book email_et;
    private boolean ischecked = false;
    private ProgressDialog pb;
    private CustomEditText_Book fname_et;
    private CustomEditText_Book lastname_et;
    private CustomEditText_Book company_et;
    private CustomEditText_Book password_et;
    private String email;
    private int activation_code;
    private Context context = this;
    private String password;
    private CustomEditText_Book retype_pass;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        utils = new Utils(RegisterActivity.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = findViewById(R.id.registerdialog);
        view.getBackground().setAlpha(200);
        fname_et = (CustomEditText_Book) findViewById(R.id.name);
        lastname_et = (CustomEditText_Book) findViewById(R.id.lastname);
        email_et = (CustomEditText_Book) findViewById(R.id.email);
        company_et = (CustomEditText_Book) findViewById(R.id.company_name);
        password_et = (CustomEditText_Book) findViewById(R.id.password);
        retype_pass = (CustomEditText_Book) findViewById(R.id.repassword);


    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean validate() {
        try {
            if (fname_et.length() == 0 || lastname_et.length() == 0 || email_et.length() == 0 ||
                    company_et.length() == 0 || password_et.length() == 0 || retype_pass.length() == 0) {
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("All the field must be required to proceed further...")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return false;
            } else {
                if (!password_et.getText().toString().equalsIgnoreCase(retype_pass.getText().toString())) {
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Password & retype password must be same")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                    return false;
                }
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void checkEmail(){
        Thread thread_check_email = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String email = email_et.getText().toString().trim();
                    utils.savedata("fullname",fname_et.getText().toString());
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");

                    String link = "https://celeritas-solutions.com/cds/capalinoapp/apis/getEmail.php?UserEmailAddress="+email;
                    HttpPost httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    JSONArray jsonarray = new JSONArray(response);
                    JSONObject jsonobj = jsonarray.getJSONObject(0);
                    if(jsonobj.getString("Number").equalsIgnoreCase("1")) {
                        ischecked = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                final Toast toast = Toast.makeText(getApplicationContext(), "Email already exists, Try with different email", Toast.LENGTH_LONG);
                                toast.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 15000);
                            }
                        });
                    }else{
                      registration();
                    }



                    //hidePB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread_check_email.start();
    }

    private void registration() {
        Thread thread_activate = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    showPB("Registration is in progress...");
                    String fname = fname_et.getText().toString();
                    String lname = lastname_et.getText().toString();
                    email = email_et.getText().toString();
                    String company = company_et.getText().toString();
                    password = password_et.getText().toString();
                    Random rnd = new Random();
                    activation_code = 1000000 + rnd.nextInt(9000000);
                    utils.savedata("fname",fname);
                    utils.savedata("lname",lname);
                    utils.savedata("email",email);
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MMM-dd HH:mm a");
                    Date date = new Date();
                    String url = "https://celeritas-solutions.com/cds/capalinoapp/apis/registerUser.php?UserFirstName="+fname+
                            "&UserLastName="+lname+"&UserEmailAddress="+email+"&UserCompany="+company+"&UserPassword="+password+
                            "&UserRegisteredDate="+dateformat.format(date)+"&ActivationCode="+String.valueOf(activation_code);
                    url = url.replace(" ","%20");

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    if(response.equalsIgnoreCase("Records Added.Passwords Added.")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activation();
                            }
                        });
                    }

                    Log.i("Response", "Response : " + response);


                    //hidePB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread_activate.start();
    }

    public void activation(){
        Thread thread_activate = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String message = "Thank you for joining the Capalino community. Please see the activation code below which should be entered into the Join In screen. Note down and activate your account. Activation Code: "+activation_code;
                    message = message.replace(" ","%20");
                    String link = "https://celeritas-solutions.com/cds/capalinoapp/apis/emailCapalino.php?toemail="+email+"&messageemail="+message;
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    if(response.equalsIgnoreCase("1")){


                        //Toast.makeText(getApplicationContext(),"Activation code send to your email, Check your email & activate your account",Toast.LENGTH_LONG).show();

                    }


                    //hidePB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread_activate.start();

        new AlertDialog.Builder(context)
                .setTitle("Alert!")
                .setMessage("Thank you for registering! Activation code has been sent to your email. Kindly use that to activate your account. You will now be redirected...")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(RegisterActivity.this, ActivationActivity.class);
                i.putExtra("email", email);
                i.putExtra("pass", password);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finish();
            }
        }, 10000);


    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(RegisterActivity.this);
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

    //BackClick Event
    public void BackClick(View view){
        finish();
    }

    public void ActivateClick(View view){
        try{
            if(validate()) {
                PasswordValidator passwordValidator = new PasswordValidator();
                if(isValidEmail(email_et.getText().toString()) && passwordValidator.validate(password_et.getText().toString())) {
                    checkEmail();
                }

                if(!passwordValidator.validate(password_et.getText().toString()))
                {
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Invalid Email And Password, Password must contain atleast 6 character including uppercase, lowercase, number and special characters ")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                if(!isValidEmail(email_et.getText().toString())){
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Invalid Email!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
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
