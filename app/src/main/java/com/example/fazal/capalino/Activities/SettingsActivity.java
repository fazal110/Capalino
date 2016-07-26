package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fazal.capalino.AppConstants.Constants;
import com.example.fazal.capalino.CustomViews.CustomEditText_Book;
import com.example.fazal.capalino.Database.DataBaseHelper;
import com.example.fazal.capalino.Database.DatabaseBeen.SettingsModel;
import com.example.fazal.capalino.JavaBeen.ListData_Agency;
import com.example.fazal.capalino.R;

import java.util.ArrayList;

public class SettingsActivity extends Activity {

    private ArrayList<Integer> positionArray;
    private ArrayList<Integer> positionArray_contractvalue;
    private ArrayList<Integer> position_Array;
    private Context context = this;
    private String Geographic_Name="";
    private int count_geographic;
    private String[] output_geographic;
    private String[] output_contractvalue;
    private String Certification_Name="";
    private String[] output_certification;
    private DataBaseHelper dataBaseHelper;
    private int count_certification;
    private String target_contract_value = "";
    private String capablities;
    private int count_contractvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    private void init() {
        try{
            //dataBaseHelper.delete("SettingsMaster");
            dataBaseHelper = new DataBaseHelper(SettingsActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("","","SettingsMaster",false);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    Geographic_Name = cursor.getString(1);
                    target_contract_value = cursor.getString(2);
                    Certification_Name = cursor.getString(3);
                    output_geographic = Geographic_Name.split(",");
                    output_contractvalue = target_contract_value.split(":");
                    output_certification = Certification_Name.split(",");
                    count_geographic = output_geographic.length;
                    count_contractvalue = output_contractvalue.length;
                    count_certification = output_certification.length;
                    capablities = cursor.getString(4);

                }
            }

            setupEditText();

            }catch (Exception e){
                e.printStackTrace();
            }

    }

    private void setupEditText() {
        if(count_geographic==0){
            ((CustomEditText_Book) findViewById(R.id.geographic_coverage)).setText("# Selected");
        }else {
            ((CustomEditText_Book) findViewById(R.id.geographic_coverage)).setText(count_geographic + " Selected");
        }

        if(count_certification==0){
            ((CustomEditText_Book) findViewById(R.id.certifications)).setText("# Selected");
        }else {
            ((CustomEditText_Book) findViewById(R.id.certifications)).setText(count_certification + " Selected");
        }

        if(count_contractvalue==0){
            ((CustomEditText_Book) findViewById(R.id.target_contract_value)).setText("# Selected");
        }else {
            ((CustomEditText_Book) findViewById(R.id.target_contract_value)).setText(count_contractvalue + " Selected");
        }

        //((CustomEditText_Book) findViewById(R.id.target_contract_value)).setText(target_contract_value);
        ((CustomEditText_Book) findViewById(R.id.capability)).setText(capablities);
    }

    //EditText Click Listener
    public void GeographicCoverageClick(View view){
       try{
           Intent i = new Intent(SettingsActivity.this,GeographicCoverageActivity.class);
           i.putIntegerArrayListExtra("geographic_pos_array", positionArray);
           i.putExtra("SettingID",1);
           if(output_geographic!=null) {
               if (output_geographic.length > 0) {
                   i.putExtra("output_geographic", output_geographic);
               }
           }
           startActivityForResult(i, Constants.request_geographic_coverage);
           }catch (Exception e){
               e.printStackTrace();
           }
    }

    public void ContractClick(View view){
        try{
            Intent i = new Intent(SettingsActivity.this,TargetContractActivity.class);
            i.putIntegerArrayListExtra("contractvalue_pos_array", positionArray_contractvalue);
            i.putExtra("SettingID", 2);
            if(output_contractvalue!=null){
                if(output_contractvalue.length > 0){
                    i.putExtra("output_contract",output_contractvalue);
                }
            }
            startActivityForResult(i, Constants.request_target_contract_value);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    public void CertificationActivity(View view){
        try{
            Intent i = new Intent(SettingsActivity.this,CertificationActivity.class);
            i.putIntegerArrayListExtra("Certification_pos_array", position_Array);
            i.putExtra("SettingID",3);
            if(output_certification!=null)
                if(output_certification.length>0){
                    i.putExtra("output_certification",output_certification);
                }
            startActivityForResult(i, Constants.request_certification);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    public void CapablitiesClick(View view){
        Intent i = new Intent(SettingsActivity.this,CapablitiesActivity.class);
        startActivityForResult(i, Constants.request_capabilites);
    }

    public void UpdateProfileClick(View view){
        Intent i = new Intent(this,UpdateProfileActivity.class);
        startActivity(i);
    }

    public void UpdateClick(View view){
        try{
            String geographic_coverage = ((CustomEditText_Book) findViewById(R.id.geographic_coverage)).getText().toString();
            //String target_contract_value = ((CustomEditText_Book) findViewById(R.id.target_contract_value)).getText().toString();
            String certifications = ((CustomEditText_Book) findViewById(R.id.certifications)).getText().toString();
            String capability = ((CustomEditText_Book) findViewById(R.id.capability)).getText().toString();

            if(dataBaseHelper==null){
                dataBaseHelper = new DataBaseHelper(SettingsActivity.this);
                dataBaseHelper.openDataBase();
            }

            //if(geographic_coverage.length()>0 && target_contract_value.length()>0 && certifications.length()>0 && capability.length()>0) {
                boolean isInserted = dataBaseHelper.InsertSettingsMaster(new SettingsModel(Geographic_Name, target_contract_value,
                        Certification_Name, capability));
                if (isInserted) {

                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Data has been updated...")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

    }



    //Footer Click Listener
    public void HomeClick(View view){
        Intent i = new Intent(SettingsActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view){
        Intent i = new Intent(SettingsActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(SettingsActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(SettingsActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
        switch (requestCode) {
                case Constants.request_geographic_coverage: {
                    if(data.getStringExtra("geographic_count")!=null) {
                        positionArray = data.getIntegerArrayListExtra("geographic_pos_array");
                        ((CustomEditText_Book) findViewById(R.id.geographic_coverage)).setText(data.getStringExtra("geographic_count"));
                        if(data.getSerializableExtra("list_data")!=null){
                            ArrayList<ListData_Agency> list = (ArrayList<ListData_Agency>) data.getSerializableExtra("list_data");
                            Geographic_Name="";
                            for(int i = 0;i<list.size();i++){
                                /*if(i==list.size()-1){
                                    Geographic_Name += list.get(i).getTitle();
                                    return;
                                }*/
                                Geographic_Name += list.get(i).getTitle()+",";
                            }
                        }
                    }
                    break;
                }

                case Constants.request_target_contract_value:{
                    /*if(data.getStringExtra("target_contract_value")!=null) {
                        target_contract_value = data.getStringExtra("target_contract_value");
                        ((CustomEditText_Book) findViewById(R.id.target_contract_value)).setText(target_contract_value);
                        break;
                    }*/

                    if(data.getStringExtra("contract_count")!=null) {
                        positionArray = data.getIntegerArrayListExtra("contractvalue_pos_array");
                        ((CustomEditText_Book) findViewById(R.id.target_contract_value)).setText(data.getStringExtra("contract_count"));
                        if(data.getSerializableExtra("list_data")!=null){
                            ArrayList<ListData_Agency> list = (ArrayList<ListData_Agency>) data.getSerializableExtra("list_data");
                            target_contract_value="";
                            for(int i = 0;i<list.size();i++){
                                /*if(i==list.size()-1){
                                    Geographic_Name += list.get(i).getTitle();
                                    return;
                                }*/
                                if(!list.get(i).getTitle().equalsIgnoreCase("null") && list.get(i).getTitle()!=null)
                                target_contract_value += list.get(i).getTitle()+":";
                            }
                        }
                    }
                    break;
                }

            case Constants.request_certification:{
                if(data.getStringExtra("certification_count")!=null){
                    position_Array = data.getIntegerArrayListExtra("Certification_pos_array");
                    ((CustomEditText_Book)findViewById(R.id.certifications)).setText(data.getStringExtra("certification_count"));
                    if(data.getSerializableExtra("list_data")!=null){
                        ArrayList<ListData_Agency> list = (ArrayList<ListData_Agency>) data.getSerializableExtra("list_data");
                        Certification_Name = "";
                        for(int i = 0;i<list.size();i++){
                            if(!list.get(i).getTitle().equalsIgnoreCase(""))
                            Certification_Name += list.get(i).getTitle()+",";
                        }
                    }
                    break;
                }
            }

            case Constants.request_capabilites:{
                if(data.getIntExtra("count", 0)!=0){
                    ((CustomEditText_Book)findViewById(R.id.capability)).setText(data.getIntExtra("count",0)+" Selected");
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
