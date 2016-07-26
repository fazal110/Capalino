package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fazal.capalino.CustomViews.CustomButton;
import com.example.fazal.capalino.R;

import java.lang.reflect.InvocationTargetException;

public class ShowPDFActivity extends Activity {

    private WebView wv;
    private String url;
    private LinearLayout layout;
    private CustomButton emailbtn;
    private CustomButton donebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        init();
    }

    private void init() {
        wv = (WebView) findViewById(R.id.pdfview);
        donebtn = (CustomButton) findViewById(R.id.donebtn);
        layout = (LinearLayout) findViewById(R.id.layout_header);
        donebtn.setVisibility(View.VISIBLE);
        showpdf();
        OpenLink();
        PlayVideo();
    }

    public void DoneClick(View view){
        finish();
    }

    public void layoutClick(View view){

    }

    private void showpdf() {
        try {
            Intent i = getIntent();
            if(i.getStringExtra("status").equalsIgnoreCase("pdf")) {
                url = i.getStringExtra("url");
                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setPluginState(WebSettings.PluginState.ON);


                wv.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                wv.loadUrl("https://docs.google.com/viewer?url=" + url);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void OpenLink() {
        if(getIntent().getStringExtra("status").equalsIgnoreCase("url")) {
            WebSettings settings = wv.getSettings();
            settings.setJavaScriptEnabled(true);


            //The default value is true for API level android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 and below,
            //and false for API level android.os.Build.VERSION_CODES.JELLY_BEAN and above.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                settings.setAllowUniversalAccessFromFileURLs(true);

            settings.setBuiltInZoomControls(true);
            wv.setWebChromeClient(new WebChromeClient());
            wv.loadUrl(getIntent().getStringExtra("url"));
        }
    }

    private void PlayVideo(){
        if(getIntent().getStringExtra("status").equalsIgnoreCase("Video")){
            url = getIntent().getStringExtra("url");
            wv.getSettings().setJavaScriptEnabled(true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                wv.getSettings().setAllowUniversalAccessFromFileURLs(true);

            wv.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            wv.setWebChromeClient(new WebChromeClient());
            wv.loadUrl(url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(wv, (Object[]) null);

        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch(NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch(InvocationTargetException ite) {
            ite.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    public void BrowseClick(View view){
        Intent i = new Intent(ShowPDFActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(ShowPDFActivity.this,SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(ShowPDFActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void HomeClick(View view){

        try{
            Intent i = new Intent(ShowPDFActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
