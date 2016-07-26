package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fazal.capalino.AppConstants.Constants;
import com.example.fazal.capalino.AppConstants.Utils;
import com.example.fazal.capalino.Database.DataBaseHelper;
import com.example.fazal.capalino.JavaBeen.ListData_Agency;
import com.example.fazal.capalino.JavaBeen.ViewHolder_Agency;
import com.example.fazal.capalino.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CapablitiesSearchActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private ArrayList<Boolean> list_check;
    private int count;
    private ArrayList<Integer> positionArray;
    private TextView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capablities_search);
        init();
    }

    //BackClick Event
    public void BackClick(View view) {
        Intent intentback = new Intent();

        if (count > 0)
            intentback.putExtra("count", count + " selected");
        SendDataBack(intentback);
        finish();
    }

    private void init() {
        header = (TextView) findViewById(R.id.headertext);
        if(getIntent().getStringExtra("header")!=null)
        header.setText(getIntent().getStringExtra("header"));
        lv = (ListView) findViewById(R.id.list_capabilities_search_lv);
        populatelist();
    }

    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();
            setupList();
            positionArray = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                list_check.add(i, false);
                positionArray.add(-1);
            }

            for (int i = 0; i < list.size(); i++) {
                list_check.set(i, false);
            }

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {

                        positionArray.set(position, position);
                        list_check.set(position, !list_check.get(position));
                        CustomListAdapter adapter = new CustomListAdapter(CapablitiesSearchActivity.this, R.layout.activity_agency, list);
                        lv.setAdapter(adapter);

                        //Intent intentback = new Intent();
                        //SendDataBack(intentback, list, position);

                        //finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            CustomListAdapter adapter = new CustomListAdapter(CapablitiesSearchActivity.this, R.layout.activity_agency, list);
            lv.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupList() {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(CapablitiesSearchActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();

            switch (getIntent().getStringExtra("status")) {
                case "Advertising": {
                    Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CapabilitiesMaster", false);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 100)
                                list.add(new ListData_Agency(cursor.getString(3)));
                        }
                    }
                    break;
                }

                case "HVConstruction": {
                    Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CapabilitiesMaster", false);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 300 || cursor.getInt(1) == 400)
                                list.add(new ListData_Agency(cursor.getString(3)));
                        }
                    }

                    break;
                }

                case "Architecture": {
                    Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CapabilitiesMaster", false);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 200)
                                list.add(new ListData_Agency(cursor.getString(3)));
                        }
                    }
                    break;
                }

                case "Envoirnmental": {
                    Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CapabilitiesMaster", false);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 500)
                                list.add(new ListData_Agency(cursor.getString(3)));
                        }
                    }
                    break;
                }

                case "Facilities": {
                    list.add(new ListData_Agency("Facilities1"));
                    list.add(new ListData_Agency("Facilities2"));
                    list.add(new ListData_Agency("Facilities3"));
                    list.add(new ListData_Agency("Facilities4"));
                    list.add(new ListData_Agency("Facilities5"));
                    list.add(new ListData_Agency("Facilities6"));
                    list.add(new ListData_Agency("Facilities7"));
                    list.add(new ListData_Agency("General Supplies"));
                    list.add(new ListData_Agency("Other"));
                    break;
                }

                case "GeneralMaintainance": {
                    Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CapabilitiesMaster", false);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 700)
                                list.add(new ListData_Agency(cursor.getString(3)));
                        }
                    }
                    break;
                }

                case "Security": {
                    Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CapabilitiesMaster", false);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 800)
                                list.add(new ListData_Agency(cursor.getString(3)));
                        }
                    }
                    break;
                }

                case "IT": {
                    Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CapabilitiesMaster", false);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 200)
                                list.add(new ListData_Agency(cursor.getString(3)));
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateClick(View view) {
        for (int i = 0; i < list_check.size(); i++) {
            if (list_check.get(i)) {
                count++;
            }
        }
    }

    private void SendDataBack(Intent intentback) {
        try {
            switch (getIntent().getStringExtra("status")) {
                case "Advertising": {
                    //intentback.putExtra("Advertisingvalues", list.get(position).getTitle());
                    setResult(Constants.request_advertising, intentback);
                    break;
                }

                case "HConstruction": {
                    //intentback.putExtra("HConstructionvalues", list.get(position).getTitle());
                    setResult(Constants.request_horizontal_constructions, intentback);
                    break;
                }

                case "Architecture": {
                    //intentback.putExtra("Architecturevalues", list.get(position).getTitle());
                    setResult(Constants.request_architectural, intentback);
                    break;
                }

                case "VConstruction": {
                    //intentback.putExtra("VConstructionvalues", list.get(position).getTitle());
                    setResult(Constants.request_vertical_constructions, intentback);
                    break;
                }

                case "Envoirnmental": {
                    //intentback.putExtra("Envoirnmentalvalues", list.get(position).getTitle());
                    setResult(Constants.request_envoirnmental, intentback);
                    break;
                }

                case "Facilities": {
                    //intentback.putExtra("Facilitiesvalues", list.get(position).getTitle());
                    setResult(Constants.request_facilities, intentback);
                    break;
                }

                case "GeneralMaintainance": {
                    //intentback.putExtra("GeneralMaintainancevalues", list.get(position).getTitle());
                    setResult(Constants.request_generalmaintainance, intentback);
                    break;
                }

                case "Security": {
                    //intentback.putExtra("Securityvalues", list.get(position).getTitle());
                    setResult(Constants.request_security, intentback);
                    break;
                }

                case "IT": {
                    //intentback.putExtra("ITvalues", list.get(position).getTitle());
                    setResult(Constants.request_IT, intentback);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CustomListAdapter extends ArrayAdapter<ListData_Agency> {

        public CustomListAdapter(Context context, int resource, List<ListData_Agency> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {

                ViewHolder_Agency viewHolder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_agency_row, null);
                    viewHolder = new ViewHolder_Agency(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder_Agency) convertView.getTag();
                }

                ListData_Agency data = getItem(position);
                viewHolder.title.setText(data.getTitle());

                if (positionArray.get(position) != -1) {
                    if (!list_check.get(position)) {
                        positionArray.set(position, -1);
                    } else
                        list_check.set(position, true);
                }


                if (list_check.get(position) == true) {
                    viewHolder.checkbox.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    //Footer tab Click Listener
    public void HomeClick(View view) {
        try {
            Intent i = new Intent(CapablitiesSearchActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void BrowseClick(View view) {
        try {
            Intent i = new Intent(CapablitiesSearchActivity.this, BrowseActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ResourceClick(View view) {
        Intent i = new Intent(CapablitiesSearchActivity.this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view) {
        Intent i = new Intent(CapablitiesSearchActivity.this, TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
