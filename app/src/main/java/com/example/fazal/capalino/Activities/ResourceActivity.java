package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fazal.capalino.AppConstants.Constants;
import com.example.fazal.capalino.JavaBeen.ListData;
import com.example.fazal.capalino.JavaBeen.ListData_Resource;
import com.example.fazal.capalino.JavaBeen.ViewHolder;
import com.example.fazal.capalino.JavaBeen.ViewHolder_Resource;
import com.example.fazal.capalino.R;
import com.facebook.internal.Utility;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ResourceActivity extends Activity {

    private ListView lv;
    private CustomListAdapter adapter;
    private File videofiles;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        init();
    }

    private void init() {
        lv = (ListView) findViewById(R.id.list_resource_lv);
        populatelist();
        try {
            saveResourceToFile();
            //copyAssets();
            //WriteFiles();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populatelist() {
        ArrayList<ListData_Resource> list = new ArrayList<>();
        list.add(new ListData_Resource("About Capalino",R.drawable.post));
        list.add(new ListData_Resource("Capalino+Company MWBE Team",R.drawable.post));
        list.add(new ListData_Resource("NYC Lobbying Firms",R.drawable.video));
        list.add(new ListData_Resource("Capalino+Company Practice Groups",R.drawable.pdf));
        list.add(new ListData_Resource("Community Boards Interactive Map",R.drawable.post));
        adapter = new CustomListAdapter(ResourceActivity.this,R.layout.activity_resource,list);
        lv.setAdapter(adapter);
        lvclick(lv,list);
        adapter.notifyDataSetChanged();
    }

    private void lvclick(ListView lv, final ArrayList<ListData_Resource> list) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent i = new Intent(ResourceActivity.this, ShowPDFActivity.class);
                    switch (position) {
                        case 0: {
                            i.putExtra("url", "http://www.capalino.com/about/");
                            i.putExtra("status", "url");
                            break;
                        }

                        case 1: {
                            i.putExtra("url", "http://www.capalino.com/services/mwbe-consulting/");
                            i.putExtra("status", "url");
                            break;
                        }

                        case 2: {
                            String url = Environment.getExternalStorageDirectory() + "/Downloadnyclobbyingfirmscapalinocompany.mp4";
                            File file = new File(url);
                            i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(Uri.fromFile(file),"video/*");
                            startActivity(i);
                            break;
                        }

                        case 3: {
                            i.putExtra("url", "http://freecs13.hostei.com/celeritas-solutions/Capalino/Practice-Group-Diagram-2016-March.pdf");
                            i.putExtra("status", "pdf");
                            break;
                        }

                        case 4: {
                            i.putExtra("url", "http://www.capalino.com/resources/community-boards/");
                            i.putExtra("status", "url");
                            break;
                        }
                    }

                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void saveResourceToFile() throws IOException {
        InputStream in = null;
        FileOutputStream fout = null;
        try {
            in = getResources().openRawResource(R.raw.nyclobbyingfirmscapalinocompany);
            String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            String filename = "nyclobbyingfirmscapalinocompany.mp4";
            fout = new FileOutputStream(new File(downloadsDirectoryPath + filename));

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }



    //Fotter Tab Click Event

    public void BrowseClick(View view){
        Intent i = new Intent(ResourceActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(ResourceActivity.this,SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(ResourceActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void HomeClick(View view){

        try{
            Intent i = new Intent(ResourceActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class CustomListAdapter extends ArrayAdapter<ListData_Resource> {

        public CustomListAdapter(Context context, int resource, List<ListData_Resource> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try{

                ViewHolder_Resource viewHolder;
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_resource_row, null);

                    viewHolder = new ViewHolder_Resource(convertView);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder_Resource) convertView.getTag();
                }

                ListData_Resource data = getItem(position);
                viewHolder.text.setText(data.getText());
                viewHolder.image.setImageResource(data.getImage());
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
