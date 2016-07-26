package com.example.fazal.capalino.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fazal.capalino.AppConstants.Constants;
import com.example.fazal.capalino.CustomViews.CustomEditText_Book;
import com.example.fazal.capalino.R;

public class BrowseActivity extends Activity {

    private Switch switchbtn;
    private CustomEditText_Book agency;
    private CustomEditText_Book procurment;
    private CustomEditText_Book contractvalue;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        switchbtn = (Switch) findViewById(R.id.switchbtn);
        //switchbtn.setShowText(true);
        switchbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchColor(isChecked);
            }
        });

        agency = (CustomEditText_Book) findViewById(R.id.agency);

        procurment = (CustomEditText_Book) findViewById(R.id.procurement);

        contractvalue = (CustomEditText_Book) findViewById(R.id.contract);

    }

    private void switchColor(boolean checked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            switchbtn.getThumbDrawable().setColorFilter(checked ? Color.parseColor("#2b3f04") : Color.parseColor("#909090"), PorterDuff.Mode.MULTIPLY);
            switchbtn.getTrackDrawable().setColorFilter(!checked ? Color.parseColor("#909090") : Color.parseColor("#2b3f04"), PorterDuff.Mode.MULTIPLY);
        }
    }

    public void BrowseButtonClick(View view){
        try{
            String contract_value = contractvalue.getText().toString();
            if(contract_value!=null && !contract_value.equalsIgnoreCase("")) {
                Intent i = new Intent(BrowseActivity.this,REFListingActivity.class);
                i.putExtra("contact_value",contract_value);
                startActivity(i);
            }else {
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Target Contract value must be filled to proceed further...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void AgencyClick(View view){
        try{
            Intent i = new Intent(BrowseActivity.this,AgencyActivity.class);
            startActivityForResult(i, Constants.request_agency);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ProcurementClick(View view){

        try{
            Intent i = new Intent(BrowseActivity.this,ProcurementTypeActivity.class);
            startActivityForResult(i, Constants.request_procurement_type);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void ContractClick(View view){

        try{
            Intent i = new Intent(BrowseActivity.this,ContractValueActivity.class);
            startActivityForResult(i, Constants.request_contract_value);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //Footer Tab Click Events

    public void HomeClick(View view){

        try{
            Intent i = new Intent(BrowseActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void SettingsClick(View view){

        try{
            Intent i = new Intent(BrowseActivity.this,SettingsActivity.class);
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
            Intent i = new Intent(BrowseActivity.this,TrackList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ResourceClick(View view){
        Intent i = new Intent(BrowseActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case Constants.request_agency:{
                if(data!=null){
                    if(data.getStringExtra("agency")!=null){
                        agency.setText(data.getStringExtra("agency"));
                    }
                }
                break;
            }

            case Constants.request_contract_value:{
                if(data!=null){
                    if(data.getStringExtra("contractvalue")!=null){
                        contractvalue.setText(data.getStringExtra("contractvalue"));
                    }
                }
                break;
            }

            case Constants.request_procurement_type:{
                if(data!=null){
                    if(data.getStringExtra("procurmentType")!=null){
                        procurment.setText(data.getStringExtra("procurmentType"));
                    }
                }
                break;
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
