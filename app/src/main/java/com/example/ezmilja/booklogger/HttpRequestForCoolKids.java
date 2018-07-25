package com.example.ezmilja.booklogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequestForCoolKids {

        public static JSONObject getJSON(String url) {
            HttpURLConnection con = null;
            try {
                URL u = new URL(url);
                con = (HttpURLConnection) u.openConnection();

                con.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                // Parse raw String to JSON and return
                return new JSONObject(String.valueOf(sb));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    try {
                        con.disconnect();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return null;
        }

    }
