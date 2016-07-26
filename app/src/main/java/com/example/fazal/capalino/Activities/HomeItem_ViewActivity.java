package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fazal.capalino.CustomViews.CustomTextView_Bold;
import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.Database.DataBaseHelper;
import com.example.fazal.capalino.JavaBeen.ListData;
import com.example.fazal.capalino.R;

public class HomeItem_ViewActivity extends Activity {

    private CustomTextView_Bold header;
    private ImageView icon;
    private Context context = this;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_item__view);
        init();
    }

    //BackClick Event
    public void BackClick(View view){
        finish();
        startActivity(new Intent(this,HomeActivity.class));
    }

    public void UrlClick(View view){

        new AlertDialog.Builder(context)
                .setTitle("Alert!")
                .setMessage("You are being redirected to an external site. Tap on the top on the screen and click Done to return to the app")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //String url = ((CustomTextView_Book) findViewById(R.id.link_content)).getText().toString();
                        Intent i = new Intent(HomeItem_ViewActivity.this,ShowPDFActivity.class);
                        i.putExtra("status","url");
                        i.putExtra("url",url);
                        startActivity(i);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();


    }

    private void init() {
        try{
            header = (CustomTextView_Bold) findViewById(R.id.headertext);
            icon = (ImageView) findViewById(R.id.icon_img);

            header.setText(getIntent().getStringExtra("headertext"));
            icon.setImageResource(getIntent().getIntExtra("image", 0));
            setdata();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setdata() {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeItem_ViewActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "ContentMaster", false);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    if(String.valueOf(cursor.getInt(0)).equalsIgnoreCase(getIntent().getStringExtra("ContentID"))) {
                        ((CustomTextView_Bold)findViewById(R.id.text_header)).setText(cursor.getString(2));
                        ((CustomTextView_Book)findViewById(R.id.time_duration)).setText(cursor.getString(10));
                        ((CustomTextView_Book) findViewById(R.id.longdescrip)).setText(cursor.getString(4));
                        url = cursor.getString(5);
                        SpannableString content = new SpannableString("More Information");
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                        ((CustomTextView_Book) findViewById(R.id.link_content)).setText(content);
                        ((CustomTextView_Book) findViewById(R.id.longdescrip)).setMovementMethod(new ScrollingMovementMethod());
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void BrowseClick(View view){
        Intent i = new Intent(HomeItem_ViewActivity.this,BrowseActivity.class);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(HomeItem_ViewActivity.this,SettingsActivity.class);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(HomeItem_ViewActivity.this,ResourceActivity.class);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(HomeItem_ViewActivity.this,TrackList.class);
        startActivity(i);
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
