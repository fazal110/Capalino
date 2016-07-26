package com.example.fazal.capalino.JavaBeen;

import android.view.View;
import android.widget.ImageView;

import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.R;

/**
 * Created by Fazal on 5/27/2016.
 */
public class ViewHolder_Resource {

    public CustomTextView_Book text;
    public ImageView image;

    public ViewHolder_Resource(View view) {
        text = (CustomTextView_Book) view.findViewById(R.id.title);
        image = (ImageView) view.findViewById(R.id.image);
    }


}
