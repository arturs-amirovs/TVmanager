package com.example.android.tvmanager;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetSeriesInformation extends AsyncTask<Void, Void, Void> {

    private String TAG = "State messsage: ";
    private String searchText = "";
    private String name = "";
    private String summary = "";
    private String status = "";
    private String genres = "";
    private String image = "";
    private String premiered = "";
    private String rating = "";

    private NetworkListenerInterface listener;


    public GetSeriesInformation(String searchText, NetworkListenerInterface listener){
        this.searchText = searchText;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response


        String url = "http://api.tvmaze.com/singlesearch/shows?q=" + searchText;
        String jsonStr = sh.makeServiceCall(url);
        System.out.println("Url = " + url);
        Log.e(TAG, "Response from url: " + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                name = jsonObj.optString("name");
                summary = jsonObj.optString("summary");
                status = jsonObj.optString("status");
                premiered = jsonObj.optString("premiered");
                JSONArray x = jsonObj.getJSONArray("genres");
                for(int i=0; i<x.length(); i++){
                    genres += x.optString(i) + " ";
                }

                JSONObject ratingObj = jsonObj.getJSONObject("rating");
                rating = ratingObj.optString("average");

                JSONObject imgObj = jsonObj.getJSONObject("image");
                image = imgObj.optString("medium");
//
//                // Getting JSON Array node
//
//                JSONArray address = currentPlace.optJSONArray("address_components");
//
//                for (int i = 1; i >= 0; i--) {
//                    JSONObject p = address.optJSONObject(i);
//                    placeAddress += p.getString("long_name");
//                    if (i == 1) placeAddress += " ";
//                }
//
//
//                phoneNumber = currentPlace.optString("formatted_phone_number");
//
//                try {
//                    JSONObject openingHours = currentPlace.getJSONObject("opening_hours");
//
//                    JSONArray workingHours = openingHours.getJSONArray("weekday_text");
//
//                    for (int i = 0; i < workingHours.length(); i++) {
//                        workHours += workingHours.optString(i) + "\n";
//                    }
//                } catch (final JSONException e){
//
//                }
//
//                rating = currentPlace.optString("rating");
//                website = currentPlace.optString("website");
//
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());

            }

        } else {
            Log.e(TAG, "Couldn't get json from server.");

        }



        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.e("JSON", "PARSED");
        ShowDetails.getInstance().setName(name);
        if(summary != null) {
            summary = summary.replaceAll("<p>", "");
            summary = summary.replaceAll("</p>", "");
            summary = summary.replaceAll("<b>", "");
            summary = summary.replaceAll("</b>", "");
            summary = summary.replaceAll("<i>", "");
            summary = summary.replaceAll("</i>", "");
            summary = summary.replaceAll("<br>", "");
        }
        ShowDetails.getInstance().setSummary(summary);
        ShowDetails.getInstance().setStatus(status);
        ShowDetails.getInstance().setPremiered(premiered);
        ShowDetails.getInstance().setGenres(genres);
        ShowDetails.getInstance().setRating(rating);
        ShowDetails.getInstance().setImage(image);
        listener.onTaskCompleted();

    }
}
