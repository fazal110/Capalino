package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fazal.capalino.AppConstants.Constants;
import com.example.fazal.capalino.AppConstants.Utils;
import com.example.fazal.capalino.Database.DataBaseHelper;
import com.example.fazal.capalino.JavaBeen.ListData_Agency;
import com.example.fazal.capalino.JavaBeen.ViewHolder_Agency;
import com.example.fazal.capalino.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertificationActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private ArrayList<Boolean> list_check;
    private int count;
    private ArrayList<ListData_Agency> list_tick = new ArrayList<>();
    private ArrayList<Integer> position_Array_Certification = new ArrayList<>();
    private Context context = this;
    private String responsefromserver;
    private Utils utils;
    private AlertDialog levelDialog;
    private int imputSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        init();
        populatepopup();
    }

    private void populatepopup() {
        final CharSequence[] items = { " NEW YORK CITY ", " NEW YORK STATE " };

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Imput Type");

        builder.setSingleChoiceItems(items,imputSelection,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {
                            case 0:
                                imputSelection=0;
                                break;
                            case 1:
                                imputSelection=1;
                                break;

                        }

                    }
                });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();
    }


    public void BackClick(View view){
        Intent intentback = new Intent();

        intentback.putExtra("list_data",list_tick);
        if(count>0)
        intentback.putExtra("certification_count", count + " selected");
        intentback.putIntegerArrayListExtra("Certification_pos_array", position_Array_Certification);
        setResult(Constants.request_certification, intentback);
        finish();
    }

    public void UpdateClick(View view) {
        try {
            utils = new Utils(this);
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            String date = dateformat.format(new Date());
            for (int i = 0; i < position_Array_Certification.size(); i++) {
                if (position_Array_Certification.get(i) >= 0) {
                    String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") + "&SettingTypeID=" + getIntent().getIntExtra("SettingID", 0) + "&ActualTagID=" + (position_Array_Certification.get(i) + 1) + "&AddedDateTime=" + date;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            responsefromserver = response;
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

                    Volley.newRequestQueue(this).add(stringRequest);
                }
            }

            checkResponse();

            for(int i=0;i<list_check.size();i++){
                if(list_check.get(i)){
                    count++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkResponse() {
        if (responsefromserver.equalsIgnoreCase("Records Added.")) {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Data has been sent to the server")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Data has not been sent to the server, please try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void init() {
        lv = (ListView) findViewById(R.id.list_certifications_lv);
        populatelist();
        if(getIntent().getIntegerArrayListExtra("Certification_pos_array")!=null) {
            if (getIntent().getIntegerArrayListExtra("Certification_pos_array").size() > 0) {
                position_Array_Certification = getIntent().getIntegerArrayListExtra("Certification_pos_array");
                for(int i=0;i<list_check.size();i++){
                    if(position_Array_Certification.get(i)!=-1)
                        list_check.set(i,true);
                }
            }
        }else {
            for(int i=0;i<list_check.size();i++){
                position_Array_Certification.add(-1);
            }
        }
    }



    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();

            DataBaseHelper dataBaseHelper = new DataBaseHelper(CertificationActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CertificationTypeTags", false);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    list.add(new ListData_Agency(cursor.getString(2)));
                }
            }

            for (int i = 0; i < list.size(); i++) {
                list_check.add(i, false);
            }

            if(getIntent().getStringArrayExtra("output_certification")!=null) {
                String[] output_certification = getIntent().getStringArrayExtra("output_certification");
                for (int i = 0; i < list.size(); i++) {
                    for(int j=0;j<output_certification.length;j++) {
                        if (list.get(i).getTitle().equalsIgnoreCase(output_certification[j])) {
                            list_check.set(i, true);
                        }
                    }
                }

                if(list_tick.size()==0) {
                    for (int i = 0; i < output_certification.length; i++) {
                        list_tick.add(new ListData_Agency(output_certification[i]));
                    }
                }
            }

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        list_check.set(position, !list_check.get(position));
                        list_tick.add(list.get(position));
                        CustomListAdapter adapter = new CustomListAdapter(CertificationActivity.this, R.layout.activity_agency, list);
                        lv.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            CustomListAdapter adapter = new CustomListAdapter(CertificationActivity.this, R.layout.activity_agency, list);
            lv.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //Footer tab Click Listener
    public void HomeClick(View view){
        Intent i = new Intent(CertificationActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view){
        Intent i = new Intent(CertificationActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(CertificationActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(CertificationActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public class CustomListAdapter extends ArrayAdapter<ListData_Agency> {

        public CustomListAdapter(Context context, int resource, List<ListData_Agency> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try{

                ViewHolder_Agency viewHolder;
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_agency_row, null);
                    viewHolder = new ViewHolder_Agency(convertView);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder_Agency) convertView.getTag();
                }

                ListData_Agency data = getItem(position);
                viewHolder.title.setText(data.getTitle());

                if(position_Array_Certification.get(position)!=-1){
                    if(!list_check.get(position)){
                        position_Array_Certification.set(position,-1);
                    }else
                        list_check.set(position,true);
                }

                if(list_check.get(position)==true) {
                    viewHolder.checkbox.setVisibility(View.VISIBLE);
                    position_Array_Certification.set(position,position);
                }else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                    //list_tick.remove(position);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
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
