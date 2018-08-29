package com.example.ezmilja.booklogger;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import static com.example.ezmilja.booklogger.ContentsActivity.currentIsbn;

public class RetrieveDataJSON extends AsyncTask<String, String, JSONObject> {

    private final Context context;


    String AddtoURL = currentIsbn;

        public RetrieveDataJSON(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonArray = null;

            jsonArray = HttpRequest.getJSON("https://www.googleapis.com/books/v1/volumes?q=isbn:" + AddtoURL);
            return jsonArray;
        }

//    @Override
//    protected void onPostExecute(JSONObject book) {
//        Log.e("response", String.valueOf(book));
//
//    }



    }