package com.example.fazal.capalino.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fazal.capalino.Activities.BrowseActivity;
import com.example.fazal.capalino.Activities.HomeActivity;
import com.example.fazal.capalino.Activities.ResourceActivity;
import com.example.fazal.capalino.Activities.SettingsActivity;
import com.example.fazal.capalino.Activities.TrackActivity;
import com.example.fazal.capalino.AppConstants.Utils;
import com.example.fazal.capalino.CustomViews.CustomButton;
import com.example.fazal.capalino.JavaBeen.ListData_Track;
import com.example.fazal.capalino.JavaBeen.ListData_track_comnt;
import com.example.fazal.capalino.JavaBeen.ViewHolder_TrackComment;
import com.example.fazal.capalino.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Fazal on 6/28/2016.
 */
public class MessagingFragments extends Fragment {

    private ListView lv;
    private ArrayList<ListData_track_comnt> list_cmnt;
    private Context context;
    private EditText requesttext;
    private Utils utils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.messaging_fragment,container,false);
        init(rootview);
        return rootview;
    }

    public void BackClick(View view){
        getActivity().getSupportFragmentManager().beginTransaction().remove(new MessagingFragments()).commit();
    }

    private void init(View view) {
        context = getActivity();
        utils = new Utils(getActivity());
        lv = (ListView) view.findViewById(R.id.list_tracking_cmnt);
        requesttext = (EditText) view.findViewById(R.id.request_text_et);
        list_cmnt = new ArrayList<ListData_track_comnt>();
        populatelist(view);
    }

    private void populatelist(View view) {
        getData();
        /*list_cmnt.add(new ListData_track_comnt("Do i need to file a Broker certification", R.drawable.person));
        list_cmnt.add(new ListData_track_comnt("Yes - this is required for contract over", R.drawable.bulb));
        list_cmnt.add(new ListData_track_comnt("Thank you!", R.drawable.person));*/
       /* CustomListAdapterComment adaptercomment = new CustomListAdapterComment(getActivity(),R.layout.list_track_row,list_cmnt);
        lv.setAdapter(adaptercomment);*/
        SendClick(view);
    }

    private void getData() {

        Thread getData_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/getUserRequests.php?UserID=" +utils.getdata("Userid");
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(url);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    String RequestText = null;
                    JSONArray jsonarray = new JSONArray(response);
                    for(int i=0;i<jsonarray.length();i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        RequestText = jsonobj.getString("RequestText");
                        list_cmnt.add(new ListData_track_comnt(RequestText, R.drawable.person));
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomListAdapterComment adaptercomment = new CustomListAdapterComment(getActivity(),R.layout.list_track_row,list_cmnt);
                            lv.setAdapter(adaptercomment);
                        }
                    });

                }catch (Exception e){
                        e.printStackTrace();
                    }
            }
        });
        getData_thread.start();

/*
        String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/getUserRequests.php?UserID=" +utils.getdata("Userid");
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    String RequestText = null;
                    JSONArray jsonarray = new JSONArray(response);
                    for(int i=0;i<jsonarray.length();i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        RequestText = jsonobj.getString("RequestText");
                        list_cmnt.add(new ListData_track_comnt(RequestText, R.drawable.person));
                    }

                    return RequestText;
                }catch (Exception e){
                    e.printStackTrace();
                    return "";
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(!s.equalsIgnoreCase("")){
                    CustomListAdapterComment adaptercomment = new CustomListAdapterComment(getActivity(),R.layout.list_track_row,list_cmnt);
                    lv.setAdapter(adaptercomment);
                }
            }
        }.execute(url,"","");
*/
    }

    private void SendClick(View view) {
        ((CustomButton)view.findViewById(R.id.sendbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils = new Utils(getActivity());
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
                Date date = new Date();
                String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/addUserRequest.php?RequestText="+requesttext.getText().toString()+
                        "&UserID="+utils.getdata("Userid")+"&RequestAddedDateTime="+dateformat.format(date);
                url = url.replace(" ","%20");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Records Added.")) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("Record Added Successfully")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();

                        }
                        else {
                            //hidePB();
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("No Record Added, Please Try Again.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("No Record Added, Please Try Again.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }
                });

                Volley.newRequestQueue(getActivity()).add(stringRequest);
            }
        });
    }


    //Footer Click Listener
    public void BrowseClick(View view){
        Intent i = new Intent(getActivity(),BrowseActivity.class);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(getActivity(),SettingsActivity.class);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(getActivity(),ResourceActivity.class);
        startActivity(i);
    }

    public void HomeClick(View view){
        Intent i = new Intent(getActivity(),HomeActivity.class);
        startActivity(i);

    }

    public class CustomListAdapterComment extends ArrayAdapter<ListData_track_comnt> {

        public CustomListAdapterComment(Context context, int resource, List<ListData_track_comnt> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try{
                ViewHolder_TrackComment viewHolder;
                if(convertView==null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.list_track_cmnt_row, null);
                    viewHolder = new ViewHolder_TrackComment(convertView);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder_TrackComment) convertView.getTag();
                }

                ListData_track_comnt data = getItem(position);
                viewHolder.title.setText(data.getTitle());
                viewHolder.image.setImageResource(data.getImage());

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }


    }
}
