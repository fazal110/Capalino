package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fazal.capalino.CustomViews.CustomTextView;
import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.DataStorage.Data;
import com.example.fazal.capalino.Fragments.MessagingFragments;
import com.example.fazal.capalino.JavaBeen.ListData_Agency;
import com.example.fazal.capalino.JavaBeen.ListData_RFP;
import com.example.fazal.capalino.JavaBeen.ListData_Track;
import com.example.fazal.capalino.JavaBeen.ListData_track_comnt;
import com.example.fazal.capalino.JavaBeen.ViewHolder_Agency;
import com.example.fazal.capalino.JavaBeen.ViewHolder_RfpList;
import com.example.fazal.capalino.JavaBeen.ViewHolder_Track;
import com.example.fazal.capalino.JavaBeen.ViewHolder_TrackComment;
import com.example.fazal.capalino.R;

import java.util.ArrayList;
import java.util.List;

public class TrackActivity extends FragmentActivity {

    private ListView lv;
    private ArrayList<ListData_Track> list;
    private ListView lv1;
    private ArrayList<ListData_track_comnt> list_cmnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        init();
    }

    //BackClick Event
    public void BackClick(View view){
        finish();
    }

    private void init() {
        lv = (ListView) findViewById(R.id.list_tracking);
        list = new ArrayList<ListData_Track>();
        populatelist();
    }

    private void populatelist() {


        ListData_RFP listData_rfp = (ListData_RFP) getIntent().getSerializableExtra("list_data");
        list.add(new ListData_Track(listData_rfp.getHeader(), listData_rfp.getRating(), listData_rfp.getTitle(),
                listData_rfp.getAgency(), listData_rfp.getPublic_date()));
        CustomListAdapter adapter = new CustomListAdapter(TrackActivity.this,R.layout.list_track_row,list);
        lv.setAdapter(adapter);
    }

    public void MessagingClick(View view){
        getSupportFragmentManager().beginTransaction().add(R.id.container_track,new MessagingFragments()).addToBackStack("tag").commit();
    }

    public class CustomListAdapter extends ArrayAdapter<ListData_Track> {

        public CustomListAdapter(Context context, int resource, List<ListData_Track> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try{
                ViewHolder_Track viewHolder;
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_track_row, null);
                    viewHolder = new ViewHolder_Track(convertView);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder_Track) convertView.getTag();
                }

                ListData_Track data = getItem(position);
                viewHolder.title.setText(data.getTitle());
                viewHolder.Agency.setText(data.getAgency());
                viewHolder.track_started_date.setText(data.getTrack_started_date());
                viewHolder.ratingbar.setRating((float) data.getRating());


            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }


    }



    //Footer Click Listener
    public void BrowseClick(View view){
        Intent i = new Intent(TrackActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(TrackActivity.this,SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(TrackActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void HomeClick(View view){
        Intent i = new Intent(TrackActivity.this,HomeActivity.class);
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
