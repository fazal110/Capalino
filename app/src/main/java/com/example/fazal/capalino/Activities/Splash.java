package com.example.fazal.capalino.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.fazal.capalino.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final DonutProgress progress = (DonutProgress) findViewById(R.id.donut_progress);
        ImageView imageview = (ImageView) findViewById(R.id.splash_image);
        int[] images = new int[] {R.drawable.splash1, R.drawable.splash2, R.drawable.splash3};

        int imageId = (int)(Math.random() * images.length);

        imageview.setBackgroundResource(images[imageId]);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setProgress(progress.getProgress() + 1);
                        if(progress.getProgress()==progress.getMax()){
                            Intent i = new Intent(Splash.this,HomeActivity.class);
                            i.putExtra("islogin","yes");
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        }, 1000, 100);

    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}
