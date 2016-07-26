package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fazal.capalino.AppConstants.Constants;
import com.example.fazal.capalino.AppConstants.Utils;
import com.example.fazal.capalino.DataStorage.Data;
import com.example.fazal.capalino.Database.DataBaseHelper;
import com.example.fazal.capalino.JavaBeen.ContentMasterModel;
import com.example.fazal.capalino.JavaBeen.ListData;
import com.example.fazal.capalino.JavaBeen.ViewHolder;
import com.example.fazal.capalino.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData> list_data;
    private CustomListAdapter adapter;
    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    private boolean isvisibleswipe;
    private ArrayList<Boolean> isvisible = new ArrayList<>();
    ArrayList<Integer> pos = new ArrayList<>();
    private int currentpos;
    private static final int MIN_DISTANCE = 100;
    private SwipeGestureListener gesturelistener;
    private int image;
    private Context context = this;
    private String post;
    private Utils utils;
    private JSONObject jsonobj;
    private String date;
    private boolean isinserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initalize_facebook();
        init();

    }

    private void initalize_facebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public void logoutClick(View view){
        new AlertDialog.Builder(context)
                .setTitle("Alert!")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        utils.savedata("email","");
                        utils.savedata("fname","");
                        utils.savedata("lname","");
                        utils.savedata("pass", "");
                        utils.savedata("ischecked", "false");
                        finish();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    private void init() {
        //deleteDatabase("CapalinoDataBase.sqlite");
        //LastUpdateDate();
        utils = new Utils(HomeActivity.this);
        lv = (ListView) findViewById(R.id.list_lv);
        if(getIntent().getStringExtra("islogin")!=null){
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("This Agreement and any operating rules for Capalino's website established by Capalino" +
                            " constitute the entire agreement of the parties with respect to the subject matter hereof, and supersede" +
                            " all previous written or oral agreements between the parties with respect to such subject matter. This Agreement" +
                            " shall be construed in accordance with the laws of the State of New York, without regard to its conflict of laws rules.")
                    .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            Typeface face=Typeface.createFromAsset(getAssets(), "gotham_book.otf");
            textView.setTypeface(face);
        }
        list_data = new ArrayList<ListData>();
        populateList();
        setuplistinit();
    }

    private void LastUpdateDate(final String date) {
        try{

            Thread thread_update = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
                    dataBaseHelper.createDataBase();
                    dataBaseHelper.openDataBase();
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");

                    String link = "https://celeritas-solutions.com/cds/capalinoapp/apis/isNewContent.php?lastUpdateSql="+date;
                    HttpPost httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                        final String response = httpclient.execute(httppost,
                                responseHandler);


                    Log.i("Response", "Response : " + response);
                    if(!response.equalsIgnoreCase(" Both are Equal")) {
                        dataBaseHelper.delete("ContentMaster");
                        JSONArray jsonarray = new JSONArray(response);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobj = jsonarray.getJSONObject(i);
                            String lastupdate = jsonobj.getString("LASTUPDATE");
                            int contentid = Integer.parseInt(jsonobj.getString("ContentID"));
                            if (!lastupdate.equalsIgnoreCase(date)) {
                                    String contenttype = jsonobj.getString("ContentType");
                                    SetupImageIconFromServer(contenttype);
                                    String ContentTitle = jsonobj.getString("ContentTitle");
                                    String ContentLongDescription = jsonobj.getString("ContentLongDescription");
                                    String ContentReferenceURL = jsonobj.getString("ContentReferenceURL");
                                    String ContentPostedByUser = jsonobj.getString("ContentPostedByUser");
                                    String ContentPostedDate = jsonobj.getString("ContentPostedDate");

                                    String contentShortDescription = jsonobj.getString("ContentShortDescription");
                                    String ContentRelevantDateTime = jsonobj.getString("ContentRelevantDateTime");
                                    String ContentExpirationDate = jsonobj.getString("ContentExpirationDate");
                                    String ContentStatusCode = jsonobj.getString("ContentStatusCode");

                                    ContentTitle = ContentTitle.replace("(", "");
                                    ContentTitle = ContentTitle.replace(")","");
                                    ContentTitle = ContentTitle.replace("'","");

                                    contentShortDescription = contentShortDescription.replace("(","");
                                    contentShortDescription = contentShortDescription.replace(")","");
                                    contentShortDescription = contentShortDescription.replace("'","");

                                    ContentLongDescription = ContentLongDescription.replace("(","");
                                    ContentLongDescription = ContentLongDescription.replace(")","");
                                    ContentLongDescription = ContentLongDescription.replace("'","");


                                    //list_data.add(new ListData(image, contentShortDescription, ContentRelevantDateTime));
                                    ContentMasterModel been = new ContentMasterModel(contenttype, ContentTitle, contentShortDescription,
                                            ContentLongDescription, ContentReferenceURL, ContentPostedByUser, ContentPostedDate,
                                            ContentStatusCode, ContentExpirationDate, ContentRelevantDateTime, lastupdate);
                                    isinserted = dataBaseHelper.InsertContentMaster(been);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (isinserted)
                                {
                                    //Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_LONG).show();
                                    populateList();
                                    Log.d("IsItemAdded", "Added");
                                }
                            }
                        });
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread_update.start();


            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void SetupImageIconFromServer(String contenttype) {
        switch (contenttype){
            case "1":{
                image = R.drawable.icon2;
                break;
            }

            case "2":{
                image = R.drawable.icon5;
                break;
            }

            case "3":{
                image = R.drawable.icon1;
                break;
            }

            case "4":{
                image = R.drawable.icon4;
                break;
            }

            case "5":{
                image = R.drawable.icon3;
                break;
            }
        }
    }

    private void setuplistinit() {
        for(int i=0;i<16;i++){
            isvisible.add(i,false);
        }
    }

    private void populateList() {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "ContentMaster", false);
            list_data.clear();
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    setupimageicon(cursor);
                    list_data.add(new ListData(image, cursor.getString(3), cursor.getString(10)));
                    date = cursor.getString(11);
                }
            }
            LastUpdateDate(date);
            adapter = new CustomListAdapter(HomeActivity.this,R.layout.activity_home,list_data);
            lv.setAdapter(adapter);
            listclick();
            swipelist(lv);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void listclick() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HomeActivity.this,HomeItem_ViewActivity.class);
                switch (list_data.get(position).getImage()){
                    case R.drawable.icon1:{
                        i.putExtra("headertext","EXPERT");
                        i.putExtra("image",R.drawable.icon1);
                        i.putExtra("ContentID",String.valueOf(position + 1));
                        break;
                    }

                    case R.drawable.icon2:{
                        i.putExtra("headertext","EVENT");
                        i.putExtra("image",R.drawable.icon2);
                        i.putExtra("ContentID",String.valueOf(position + 1));
                        break;
                    }

                    case R.drawable.icon3:{
                        i.putExtra("headertext","ALERT");
                        i.putExtra("image",R.drawable.icon3);
                        i.putExtra("ContentID",String.valueOf(position + 1));
                        break;
                    }

                    case R.drawable.icon4:{
                        i.putExtra("headertext","NEWS");
                        i.putExtra("image",R.drawable.icon4);
                        i.putExtra("ContentID",String.valueOf(position + 1));
                        break;
                    }

                    case R.drawable.icon5:{
                        i.putExtra("headertext","ANNOUNCEMENTS");
                        i.putExtra("image",R.drawable.icon5);
                        i.putExtra("ContentID",String.valueOf(position + 1));
                        break;
                    }
                }

                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(i);


            }
        });
    }

    private void setupimageicon(Cursor cursor) {
        try{
            switch (cursor.getString(1)){
                case "1":{
                    image = R.drawable.icon2;
                    break;
                }

                case "2":{
                    image = R.drawable.icon5;
                    break;
                }

                case "3":{
                    image = R.drawable.icon1;
                    break;
                }

                case "4":{
                    image = R.drawable.icon4;
                    break;
                }

                case "5":{
                    image = R.drawable.icon3;
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void swipelist(final ListView lv) {
        gesturelistener = new SwipeGestureListener(HomeActivity.this);
        lv.setOnTouchListener(gesturelistener);
    }

    //Fotter Tab Functions

    public void BrowseClick(View view){
        Intent i = new Intent(HomeActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(HomeActivity.this,SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(HomeActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(HomeActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    //Custom List Adapter
    public class CustomListAdapter extends ArrayAdapter<ListData>{

        public CustomListAdapter(Context context, int resource, List<ListData> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try{
                pos.add(position);
                currentpos = position;
                ViewHolder viewHolder;
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_row, null);
                    if(isvisible.get(position)){
                        View view = convertView.findViewById(R.id.swipe_layout);
                        view.setVisibility(View.VISIBLE);
                        onClick(view,position);
                        isvisibleswipe = false;
                        isvisible.set(position,false);
                    }
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                    convertView.setTag(position);
                }else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                ListData data = getItem(position);
                viewHolder.text.setText(data.getText());
                viewHolder.time.setText(data.getTime());
                viewHolder.image_icon.setImageResource(data.getImage());
            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }

        private void onClick(final View view, final int position) {
            ((ImageView)view.findViewById(R.id.fb)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //senddatatofb();
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://developers.facebook.com"))
                            .build();

                    ShareDialog shareDialog = new ShareDialog(HomeActivity.this);
                    shareDialog.show(content, ShareDialog.Mode.NATIVE);

                    view.setVisibility(View.GONE);
                }
            });

            ((ImageView)view.findViewById(R.id.linkdin)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    senddatatolinkdin(view);
                }
            });

            ((ImageView)view.findViewById(R.id.twitter)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    senddatatwitter(view);
                }
            });

            ((ImageView)view.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.GONE);
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   delete(position);
                                    isvisible.set(pos.get(position), false);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            ((ImageView)view.findViewById(R.id.message)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EmailClick(position,view);
                }
            });


        }

        private void delete(int pos) {
            try{
                DataBaseHelper databaseHelper = new DataBaseHelper(HomeActivity.this);
                databaseHelper.createDataBase();
                databaseHelper.openDataBase();
                Cursor cursor = databaseHelper.getDataFromDB("", "", "ContentMaster", false);
                if(cursor.getCount()>0){
                    while (cursor.moveToNext()){
                        if(list_data.get(pos).getText().equalsIgnoreCase(cursor.getString(3)))
                            databaseHelper.deleteRecord(cursor.getInt(0));
                    }
                }

                list_data.remove(pos);
                adapter = new CustomListAdapter(HomeActivity.this,R.layout.activity_home,list_data);
                lv.setAdapter(adapter);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        public void senddatatofb() {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://post"));
            final PackageManager packageManager = HomeActivity.this.getPackageManager();
            final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.isEmpty()) {

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/?mds=%2Fmessage%2Fcompose%2fdialog%2F&mdf=1"));
            }
            startActivity(intent);
        }
    }

    public void senddatatolinkdin(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://m//share"));
        final PackageManager packageManager = this.getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/m/share"));
        }
        view.setVisibility(View.GONE);
        startActivity(intent);

    }

    public void senddatatwitter(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://m//share"));
        final PackageManager packageManager = this.getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mobile.twitter.com/compose/tweet"));
        }
        view.setVisibility(View.GONE);
        startActivity(intent);
    }

    public void EmailClick(int position,View view){
        try{

            getPost(position);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Capalino+Company Share");
            intent.putExtra(Intent.EXTRA_TEXT, post);
            intent.setType("text/plain");
            intent.setType("message/rfc822");
            view.setVisibility(View.GONE);
            startActivityForResult(Intent.createChooser(intent, "Send Email"), Constants.Content_email_Constants);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void getPost(int position) {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "ContentMaster", false);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    if(list_data.get(position).getText().equalsIgnoreCase(cursor.getString(3))){
                        post = cursor.getString(4);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Gesture inner Class
    class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener implements
            View.OnTouchListener {
        Context context;
        GestureDetector gDetector;
        static final int SWIPE_MIN_DISTANCE = 120;
        static final int SWIPE_MAX_OFF_PATH = 250;
        static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public SwipeGestureListener() {
            super();
        }

        public SwipeGestureListener(Context context) {
            this(context, null);
        }

        public SwipeGestureListener(Context context, GestureDetector gDetector) {

            if (gDetector == null)
                gDetector = new GestureDetector(context, this);

            this.context = context;
            this.gDetector = gDetector;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {
                final int position = lv.pointToPosition(
                        Math.round(e1.getX()), Math.round(e1.getY()));

                //String item_name = (String) lv.getItemAtPosition(position);

                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                    if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH
                            || Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                        // Toast.makeText(HomeActivity.this, "bottomToTop" + item_name,
                        //       Toast.LENGTH_SHORT).show();
                    } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                        //Toast.makeText(DemoSwipe.this,
                        //      "topToBottom  " + item_name, Toast.LENGTH_SHORT)
                        //    .show();
                    }
                } else {
                    if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                        // Toast.makeText(DemoSwipe.this,
                        //       "swipe RightToLeft " + item_name, 5000).show();

                        isvisible.set(pos.get(position), true);
                        isvisibleswipe = true;
                        adapter = new CustomListAdapter(HomeActivity.this, R.layout.activity_home, list_data);
                        lv.setAdapter(adapter);

                        return true;


                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                        //Toast.makeText(DemoSwipe.this,
                        //      "swipe LeftToright  " + item_name, 5000).show();
                        isvisible.set(pos.get(position), false);
                        isvisibleswipe = false;
                        adapter = new CustomListAdapter(HomeActivity.this, R.layout.activity_home, list_data);
                        lv.setAdapter(adapter);

                        return true;

                    }
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return gDetector.onTouchEvent(event);
        }

        public GestureDetector getDetector() {
            return gDetector;
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
