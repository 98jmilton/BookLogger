package com.example.ezmilja.booklogger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import static com.example.ezmilja.booklogger.BookDetailsAdder.qrIsbn;

public class RetrieveRoomsJSONTask extends AsyncTask<String, String, JSONObject> {

    private final Context context;


    String AddtoURL = qrIsbn;

        public RetrieveRoomsJSONTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonArray = null;

            jsonArray = HttpRequestForCoolKids.getJSON("https://www.googleapis.com/books/v1/volumes?q=isbn:" + AddtoURL);
            return jsonArray;
        }

    @Override
    protected void onPostExecute(JSONObject rooms) {
        Log.e("response", String.valueOf(rooms));

    }



    }