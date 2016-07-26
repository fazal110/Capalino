package com.example.fazal.capalino.JavaBeen;

import android.view.View;
import android.widget.ImageView;

import com.example.fazal.capalino.CustomViews.CustomTextView_Bold;
import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.R;

/**
 * Created by Fazal on 5/10/2016.
 */
public class ViewHolder {
    public CustomTextView_Bold text;
    public CustomTextView_Book time;
    public ImageView image_icon;

    public ViewHolder(View view) {
        text = (CustomTextView_Bold) view.findViewById(R.id.text);
        time = (CustomTextView_Book) view.findViewById(R.id.time_duration);
        image_icon = (ImageView) view.findViewById(R.id.icon_img);
    }
}
