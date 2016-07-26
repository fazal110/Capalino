package com.example.fazal.capalino.JavaBeen;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;

import com.example.fazal.capalino.CustomViews.CustomEditText_Book;
import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.R;

/**
 * Created by Fazal on 5/30/2016.
 */
public class ViewHolder_Track {
    public RatingBar ratingbar;
    public CustomTextView_Book title;
    public CustomTextView_Book Agency;
    public CustomTextView_Book track_started_date;

    public ViewHolder_Track(View view) {

        title = (CustomTextView_Book) view.findViewById(R.id.title_content);
        Agency = (CustomTextView_Book) view.findViewById(R.id.agency_content);
        ratingbar = (RatingBar) view.findViewById(R.id.rating_bar);
        track_started_date = (CustomTextView_Book) view.findViewById(R.id.trackdate_content);
    }
}
