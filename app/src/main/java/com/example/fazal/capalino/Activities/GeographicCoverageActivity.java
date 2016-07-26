package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class GeographicCoverageActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private List<Boolean> list_check;
    private int count;
    private boolean[] list_check_restore;
    private ArrayList<Integer> positionArray = new ArrayList<>();
    private String[] val;
    private String[] key;
    private Utils utils;
    private ArrayList<ListData_Agency> list_tick = new ArrayList<>();
    private Context context = this;
    private String responsefromserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geographic_coverage);
        init();
    }

    public void BackClick(View view) {
        Intent intentback = new Intent();

        utils = new Utils(GeographicCoverageActivity.this);

        intentback.putExtra("list_data", list_tick);
        if(count>0)
        intentback.putExtra("geographic_count", count + " selected");
        intentback.putIntegerArrayListExtra("geographic_pos_array", positionArray);
        setResult(Constants.request_geographic_coverage, intentback);
        finish();
    }

    public void UpdateClick(View view) {
        try {
            utils = new Utils(this);
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            String date = dateformat.format(new Date());
            for (int i = 0; i < positionArray.size(); i++) {
                if (positionArray.get(i) >= 0) {
                    String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") + "&SettingTypeID=" + getIntent().getIntExtra("SettingID", 0) + "&ActualTagID=" + (positionArray.get(i) + 1) + "&AddedDateTime=" + date;
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

            for (int i = 0; i < list_check.size(); i++) {
                if (list_check.get(i)) {
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
        try {
            lv = (ListView) findViewById(R.id.list_geographic_coverage_lv);
            populatelist();
            if (getIntent().getIntegerArrayListExtra("geographic_pos_array") != null) {
                positionArray = getIntent().getIntegerArrayListExtra("geographic_pos_array");
                for (int i = 0; i < list_check.size(); i++) {
                    if (getIntent().getIntegerArrayListExtra("geographic_pos_array").size() > 0) {
                        if (positionArray.get(i) != -1)
                            list_check.set(i, true);
                    }
                }
            } else {
                for (int i = 0; i < list_check.size(); i++) {
                    positionArray.add(-1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();

            DataBaseHelper dataBaseHelper = new DataBaseHelper(GeographicCoverageActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            list.add(new ListData_Agency("NY-All NYC"));
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "GeopraphyTags", false);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    list.add(new ListData_Agency(cursor.getString(2)));
                }
            }

            for (int i = 0; i < list.size(); i++) {
                list_check.add(i, false);
            }

            if (getIntent().getStringArrayExtra("output_geographic") != null) {
                String[] output_geographic = getIntent().getStringArrayExtra("output_geographic");
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < output_geographic.length; j++) {
                        if (list.get(i).getTitle().equalsIgnoreCase(output_geographic[j])) {
                            list_check.set(i, true);
                        }
                    }
                }
                if (list_tick.size() == 0) {
                    for (int i = 0; i < output_geographic.length; i++) {
                        list_tick.add(new ListData_Agency(output_geographic[i]));

                    }
                }
            }

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        list_check.set(position, !list_check.get(position));
                        list_tick.add(list.get(position));
                        CustomListAdapter adapter = new CustomListAdapter(GeographicCoverageActivity.this, R.layout.activity_agency, list);
                        lv.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            CustomListAdapter adapter = new CustomListAdapter(GeographicCoverageActivity.this, R.layout.activity_agency, list);
            lv.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Footer tab Click Listener
    public void HomeClick(View view) {
        Intent i = new Intent(GeographicCoverageActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view) {
        Intent i = new Intent(GeographicCoverageActivity.this, BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view) {
        Intent i = new Intent(GeographicCoverageActivity.this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view) {
        Intent i = new Intent(GeographicCoverageActivity.this, TrackList.class);
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
                    positionArray.set(position, position);
                } else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                    //list_tick.remove(position);

                }

            } catch (Exception e) {
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
