package com.example.android.tvmanager;

/**
 * Created by arturs.amirovs on 26/06/2017.
 */

class ShowDetails {
    private static ShowDetails mInstance = null;

    private String name = "";
    private String premiered = "";
    private String status = "";
    private String summary = "";
    private String genres = "";
    private String rating = "";
    private String image = "";

    private ShowDetails() {
    }

    public static ShowDetails getInstance() {
        if (mInstance == null) {
            mInstance = new ShowDetails();
        }
        return mInstance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPremiered() {
        return premiered;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
