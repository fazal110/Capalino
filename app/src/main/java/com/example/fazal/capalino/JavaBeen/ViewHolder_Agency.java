package com.example.fazal.capalino.JavaBeen;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.R;

/**
 * Created by Fazal on 5/13/2016.
 */
public class ViewHolder_Agency {
   public CustomTextView_Book title;
    public ImageView checkbox;

    public ViewHolder_Agency(View view) {
        title = (CustomTextView_Book) view.findViewById(R.id.title);
        checkbox = (ImageView) view.findViewById(R.id.checkbox_);
    }

}
