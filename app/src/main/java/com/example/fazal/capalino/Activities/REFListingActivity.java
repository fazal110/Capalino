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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fazal.capalino.AppConstants.Utils;
import com.example.fazal.capalino.CustomViews.CustomTextView;
import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.DataStorage.Data;
import com.example.fazal.capalino.Database.DataBaseHelper;
import com.example.fazal.capalino.Database.DatabaseBeen.TrackingData;
import com.example.fazal.capalino.JavaBeen.ListData;
import com.example.fazal.capalino.JavaBeen.ListData_RFP;
import com.example.fazal.capalino.JavaBeen.ViewHolder;
import com.example.fazal.capalino.JavaBeen.ViewHolder_RfpList;
import com.example.fazal.capalino.R;

import java.lang.reflect.ReflectPermission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class REFListingActivity extends Activity {

    private ListView lv;
    private CustomListAdapter adapter;
    private ArrayList<ListData_RFP> list;
    private Context context = this;
    private Utils utils;
    private String responsefromserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflisting);
        init();
    }

    private void init() {
        lv = (ListView) findViewById(R.id.list_rfp_lv);
        populatelist();
    }

    private void populatelist() {
        try{
            list = new ArrayList<ListData_RFP>();
            DataBaseHelper dataBaseHelper = new DataBaseHelper(REFListingActivity.this);
            dataBaseHelper.openDataBase();
            if(getIntent().getStringExtra("contact_value")!=null) {
                String contract_value = getIntent().getStringExtra("contact_value");
                Cursor cursor = dataBaseHelper.getDataFromProcurementMaster(contract_value);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                            list.add(new ListData_RFP("Capalino+Company Match", 2.0, cursor.getString(5), cursor.getString(3),
                                    cursor.getString(17), cursor.getString(8)));
                    }
                }
            }
            adapter = new CustomListAdapter(REFListingActivity.this,R.layout.activity_reflisting,list);
            lv.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Footer Tab Event Listener

    public void HomeClick(View view){
        Intent i = new Intent(REFListingActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(REFListingActivity.this,SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(REFListingActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(REFListingActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BackClick(View view){
        finish();
        startActivity(new Intent(this,BrowseActivity.class));
    }

    public class CustomListAdapter extends ArrayAdapter<ListData_RFP> {

        ArrayList<ListData_RFP> list_track = new ArrayList<ListData_RFP>();
        private boolean ispresent;

        public CustomListAdapter(Context context, int resource, List<ListData_RFP> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

           try{
               ViewHolder_RfpList viewHolder;
               if(convertView==null) {
                   convertView = getLayoutInflater().inflate(R.layout.list_rfp_row, null);
                   viewHolder = new ViewHolder_RfpList(convertView);
                   convertView.setTag(viewHolder);
               }else {
                   viewHolder = (ViewHolder_RfpList) convertView.getTag();
               }

               ListData_RFP data = getItem(position);
               setdata(viewHolder, data);

               getTrackeddata();
               onClick(viewHolder,position);


           }catch (Exception e){
               e.printStackTrace();
           }
            return convertView;
        }

        private void getTrackeddata() {
            try{
                DataBaseHelper dataBaseHelper = new DataBaseHelper(REFListingActivity.this);
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
                Cursor cursor = dataBaseHelper.getDataFromDB("","","TrackListing",false);
                if(cursor.getCount()>0){
                    list_track.clear();
                    while (cursor.moveToNext()){
                        list_track.add(new ListData_RFP("Capalino+Company Match",cursor.getDouble(6),cursor.getString(1),
                                cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                    }
                }

                Data.listData_rfp = list_track;
                }catch (Exception e){
                    e.printStackTrace();
                }

        }

        private void onClick(final ViewHolder_RfpList viewHolder, final int position) {
            viewHolder.track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        utils = new Utils(REFListingActivity.this);
                        //deleteDatabase("CapalinoDataBase.sqlite");
                        final DataBaseHelper dataBaseHelper = new DataBaseHelper(REFListingActivity.this);
                        dataBaseHelper.createDataBase();
                        dataBaseHelper.openDataBase();


                        //String trackdate = viewHolder.getPublic_date().getText().toString();
                        final ListData_RFP data_rfp = getItem(position);

                        /*if(Data.listData_rfp!=null && Data.listData_rfp.size()>0) {
                            list_track = Data.listData_rfp;
                        }*/

                        for(ListData_RFP listData_rfp : list_track ){
                            if(listData_rfp.getTitle().contains(data_rfp.getTitle()) && !ispresent){
                                ispresent = true;
                            }
                        }

                        if(ispresent){
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("This RFP is already being tracked.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else {
                            list_track.add(new ListData_RFP(data_rfp.getHeader(), data_rfp.getRating(), data_rfp.getTitle(),
                                    data_rfp.getAgency(), data_rfp.getPublic_date(), data_rfp.getDue_date()));
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("This RFP is now bieng tracked.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            boolean isInserted = dataBaseHelper.InsertUserProcurmentTracking(new TrackingData(data_rfp.getTitle(),data_rfp.getAgency(),data_rfp.getPublic_date(),
                                                    data_rfp.getDue_date(),utils.getdata("Userid"),data_rfp.getRating()));
                                            SendDataToTheServer(position);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }


                        if(list_track.size()==0){
                            list_track.add(new ListData_RFP(data_rfp.getHeader(), data_rfp.getRating(), data_rfp.getTitle(),
                                    data_rfp.getAgency(), data_rfp.getPublic_date(), data_rfp.getDue_date()));

                            boolean isInserted = dataBaseHelper.InsertUserProcurmentTracking(new TrackingData(data_rfp.getTitle(),data_rfp.getAgency(),data_rfp.getPublic_date(),
                                    data_rfp.getDue_date(),utils.getdata("Userid"),data_rfp.getRating()));
                            SendDataToTheServer(position);

                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("This RFP is now bieng tracked.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SendDataToTheServer(position);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                        Data.listData_rfp = list_track;



                        }catch (Exception e){
                            e.printStackTrace();
                        }
                }
            });

            viewHolder.advice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(REFListingActivity.this,AdviceActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            });
        }



        private void SendDataToTheServer(int position) {
            try {
                utils = new Utils(REFListingActivity.this);
                        String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/addTrackingRFP.php?ProcurementTitle=" +list_track.get(position).getTitle()+
                                "&AgencyTitle="+list_track.get(position).getAgency()+"&TrackDate="+list_track.get(position).getPublic_date()+"&ProposalDeadLine="
                                +list_track.get(position).getDue_date()+"&UserID="+utils.getdata("Userid");
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

                        Volley.newRequestQueue(REFListingActivity.this).add(stringRequest);

                checkResponse();
            }catch (Exception e){
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


        private void setdata(ViewHolder_RfpList viewHolder,ListData_RFP data) {
            viewHolder.title.setText(data.getTitle());
            viewHolder.Agency.setText(data.getAgency());
            viewHolder.public_date.setText(data.getPublic_date());
            viewHolder.due_date.setText(data.getDue_date());
            viewHolder.ratingbar.setRating((float) data.getRating());

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
