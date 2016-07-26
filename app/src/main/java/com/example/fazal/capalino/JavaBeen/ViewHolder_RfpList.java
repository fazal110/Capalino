package com.example.fazal.capalino.JavaBeen;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;

import com.example.fazal.capalino.CustomViews.CustomButton;
import com.example.fazal.capalino.CustomViews.CustomTextView;
import com.example.fazal.capalino.CustomViews.CustomTextView_Book;
import com.example.fazal.capalino.R;

/**
 * Created by Fazal on 5/13/2016.
 */
public class ViewHolder_RfpList {
    public CustomTextView_Book title;
    public CustomTextView_Book Agency;
    public CustomTextView_Book public_date;
    public CustomTextView_Book due_date;
    public RatingBar ratingbar;
    public CustomButton track;
    public CustomButton advice;

    public ViewHolder_RfpList(View view) {
        title = (CustomTextView_Book) view.findViewById(R.id.title_content);
        Agency = (CustomTextView_Book) view.findViewById(R.id.agency_content);
        public_date = (CustomTextView_Book) view.findViewById(R.id.publicationdate_content);
        due_date = (CustomTextView_Book) view.findViewById(R.id.duedate_content);
        ratingbar = (RatingBar) view.findViewById(R.id.rating_bar);
        track = (CustomButton) view.findViewById(R.id.trackbtn);
        advice = (CustomButton) view.findViewById(R.id.advicebtn);
    }

    public CustomTextView_Book getTitle() {
        return title;
    }

    public void setTitle(CustomTextView_Book title) {
        this.title = title;
    }

    public CustomTextView_Book getAgency() {
        return Agency;
    }

    public void setAgency(CustomTextView_Book agency) {
        Agency = agency;
    }

    public CustomTextView_Book getPublic_date() {
        return public_date;
    }

    public void setPublic_date(CustomTextView_Book public_date) {
        this.public_date = public_date;
    }

    public CustomTextView_Book getDue_date() {
        return due_date;
    }

    public void setDue_date(CustomTextView_Book due_date) {
        this.due_date = due_date;
    }

    public RatingBar getRatingbar() {
        return ratingbar;
    }

    public void setRatingbar(RatingBar ratingbar) {
        this.ratingbar = ratingbar;
    }
}
