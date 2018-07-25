package com.example.ezmilja.booklogger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

public class RetrieveRoomsJSONTask extends AsyncTask<String, String, JSONObject> {

    private final Context context;



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

            jsonArray = HttpRequestForCoolKids.getJSON("https://www.googleapis.com/books/v1/volumes?q=isbn:" + "9780201379273");
            return jsonArray;
        }

    @Override
    protected void onPostExecute(JSONObject rooms) {
        Log.e("response", String.valueOf(rooms));

    }



    }