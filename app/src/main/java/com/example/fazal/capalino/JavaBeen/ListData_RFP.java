package com.example.fazal.capalino.JavaBeen;

import java.io.Serializable;

/**
 * Created by Fazal on 5/13/2016.
 */
public class ListData_RFP implements Serializable {

    String header;
    double rating;
    String title;
    String agency;
    String public_date;
    String due_date;

    public ListData_RFP(String header, double rating, String title, String agency, String public_date, String due_date) {
        this.header = header;
        this.rating = rating;
        this.title = title;
        this.agency = agency;
        this.public_date = public_date;
        this.due_date = due_date;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getPublic_date() {
        return public_date;
    }

    public void setPublic_date(String public_date) {
        this.public_date = public_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
}
