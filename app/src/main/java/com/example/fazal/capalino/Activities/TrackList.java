package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fazal.capalino.CustomViews.CustomTextView_Bold;
import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.DataStorage.Data;
import com.example.fazal.capalino.Database.DataBaseHelper;
import com.example.fazal.capalino.JavaBeen.ListData;
import com.example.fazal.capalino.JavaBeen.ListData_Agency;
import com.example.fazal.capalino.JavaBeen.ListData_RFP;
import com.example.fazal.capalino.JavaBeen.ListData_Track;
import com.example.fazal.capalino.JavaBeen.ViewHolder_RfpList;
import com.example.fazal.capalino.JavaBeen.ViewHolder_Track;
import com.example.fazal.capalino.R;

import java.util.ArrayList;
import java.util.List;

public class TrackList extends Activity {

    private ListView lv_track_list;
    private Context context = this;
    private ArrayList<ListData_Track> list_track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);
        init();
    }

    private void init() {
        lv_track_list = (ListView) findViewById(R.id.list_tracking_item);
        list_track = new ArrayList<ListData_Track>();
        populateList();
    }

    private void populateList() {

        if(Data.listData_rfp.size()>0) {
            for(int i=0;i<Data.listData_rfp.size();i++) {
                list_track.add(new ListData_Track("Capalino+Company Match", Data.listData_rfp.get(i).getRating(),
                        Data.listData_rfp.get(i).getTitle(), Data.listData_rfp.get(i).getAgency(),  Data.listData_rfp.get(i).getPublic_date()));
            }



        }else {
            //label show
            getTrackeddata();

        }

        CustomListAdapter adapter = new CustomListAdapter(TrackList.this, R.layout.activity_track_list, list_track);
        lv_track_list.setAdapter(adapter);
//            lvClick();
        clickActionList();

    }

    private void getTrackeddata() {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(TrackList.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("","","TrackListing",false);
            list_track.clear();
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    list_track.add(new ListData_Track("Capalino+Company Match",cursor.getDouble(6),cursor.getString(1),
                            cursor.getString(2),cursor.getString(3)));
                }
            }else {
                ((CustomTextView_Bold) findViewById(R.id.label)).setVisibility(View.VISIBLE);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void clickActionList() {
        try{
             lv_track_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     Intent i = new Intent(TrackList.this, TrackActivity.class);
                     if(Data.listData_rfp.size()>0)
                     i.putExtra("list_data", Data.listData_rfp.get(position));
                     else {
                         i.putExtra("list_data",list_track.get(position));
                     }
                     startActivity(i);
                 }
             });
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    //BackClick Event
    public void BackClick(View view){
        finish();
        startActivity(new Intent(this,BrowseActivity.class));
    }


    public class CustomListAdapter extends ArrayAdapter<ListData_Track> {

        ArrayList<ListData_RFP> list_track = new ArrayList<ListData_RFP>();

        public CustomListAdapter(Context context, int resource, List<ListData_Track> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try {
                ViewHolder_Track viewHolder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_track_row, null);
                    viewHolder = new ViewHolder_Track(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder_Track) convertView.getTag();
                }

                ListData_Track data = getItem(position);
                setdata(viewHolder, data);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private void setdata(ViewHolder_Track viewHolder,ListData_Track data) {
            //viewHolder.header.setText(data.getHeader());
            viewHolder.title.setText(data.getTitle());
            viewHolder.Agency.setText(data.getAgency());
            viewHolder.track_started_date.setText(data.getTrack_started_date());

            viewHolder.ratingbar.setRating((float) data.getRating());

        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }
    }

        public void BrowseClick(View view){
        Intent i = new Intent(TrackList.this,BrowseActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(TrackList.this,SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(TrackList.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void HomeClick(View view){
        Intent i = new Intent(TrackList.this,HomeActivity.class);
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
