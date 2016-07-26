package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fazal.capalino.AppConstants.Constants;
import com.example.fazal.capalino.CustomViews.CustomEditText_Book;
import com.example.fazal.capalino.R;

public class CapablitiesActivity extends Activity {

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capablities);
    }

    //BackClick Event
    public void BackClick(View view){
        Intent intentback = new Intent();
        intentback.putExtra("count", count);
        setResult(Constants.request_capabilites, intentback);
        finish();

    }

    //Edit Text Click Listener
    public void AdvertisingClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,CapablitiesSearchActivity.class);
        i.putExtra("status", "Advertising");
        i.putExtra("header","Advertising, Graphic Arts...");
        startActivityForResult(i, Constants.request_advertising);
    }

    public void HConstructionClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,CapablitiesSearchActivity.class);
        i.putExtra("status","HVConstruction");
        i.putExtra("header","Construction");
        startActivityForResult(i, Constants.request_horizontal_constructions);
    }

    public void ArchitecturalClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,CapablitiesSearchActivity.class);
        i.putExtra("status","Architecture");
        i.putExtra("header","Architectural Engineering");
        startActivityForResult(i, Constants.request_architectural);
    }

    public void EnvoirnmentalClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,CapablitiesSearchActivity.class);
        i.putExtra("status","Envoirnmental");
        i.putExtra("header","Environmental");
        startActivityForResult(i, Constants.request_envoirnmental);
    }

    public void FacilitiesClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,CapablitiesSearchActivity.class);
        i.putExtra("status","Facilities");
        i.putExtra("header","Facilities Maintenance");
        startActivityForResult(i, Constants.request_facilities);
    }

    public void GeneralMaintainanceClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,CapablitiesSearchActivity.class);
        i.putExtra("status","GeneralMaintainance");
        i.putExtra("header","General maintenance");
        startActivityForResult(i, Constants.request_generalmaintainance);
    }

    public void SecurityClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,CapablitiesSearchActivity.class);
        i.putExtra("status","Security");
        i.putExtra("header","Safety and Security");
        startActivityForResult(i, Constants.request_security);
    }

    public void ITClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,CapablitiesSearchActivity.class);
        i.putExtra("status","IT");
        i.putExtra("header","Information Technology (IT)");
        startActivityForResult(i, Constants.request_IT);
    }

    //Footer Tab Click Events

    public void HomeClick(View view){

        try{
            Intent i = new Intent(CapablitiesActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void BrowseClick(View view){
        try {
            Intent i = new Intent(CapablitiesActivity.this, BrowseActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void TrackClick(View view){

        try{
            Intent i = new Intent(CapablitiesActivity.this,TrackList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ResourceClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            switch (requestCode){
                case Constants.request_advertising:{
                    if(data.getStringExtra("count")!=null){
                        ((CustomEditText_Book)findViewById(R.id.Advertising_)).setText(data.getStringExtra("count"));
                        count++;
                        break;
                    }
                }

                case Constants.request_horizontal_constructions:{
                    if(data.getStringExtra("count")!=null){
                        ((CustomEditText_Book)findViewById(R.id.hconstruction)).setText(data.getStringExtra("count"));
                        count++;
                        break;
                    }
                }

                case Constants.request_architectural: {
                    if (data.getStringExtra("count") != null) {
                        ((CustomEditText_Book) findViewById(R.id.Architectural)).setText(data.getStringExtra("count"));
                        count++;
                        break;
                    }
                }

                case Constants.request_envoirnmental:{
                    if(data.getStringExtra("count")!=null){
                        ((CustomEditText_Book)findViewById(R.id.Enviornmental)).setText(data.getStringExtra("count"));
                        count++;
                        break;
                    }
                }

                case Constants.request_facilities:{
                    if(data.getStringExtra("count")!=null){
                        ((CustomEditText_Book)findViewById(R.id.Maintainance)).setText(data.getStringExtra("count"));
                        count++;
                        break;
                    }
                }

                case Constants.request_generalmaintainance:{
                    if(data.getStringExtra("count")!=null){
                        ((CustomEditText_Book)findViewById(R.id.General_maintenance)).setText(data.getStringExtra("count"));
                        count++;
                        break;
                    }
                }

                case Constants.request_security:{
                    if(data.getStringExtra("count")!=null){
                        ((CustomEditText_Book)findViewById(R.id.security)).setText(data.getStringExtra("count"));
                        count++;
                        break;
                    }
                }

                case Constants.request_IT:{
                    if(data.getStringExtra("count")!=null){
                        ((CustomEditText_Book)findViewById(R.id.Information_technology)).setText(data.getStringExtra("count"));
                        count++;
                        break;
                    }
                }
            }
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
