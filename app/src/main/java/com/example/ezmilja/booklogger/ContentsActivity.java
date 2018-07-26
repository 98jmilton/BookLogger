package com.example.ezmilja.booklogger;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
                    createButton();
        }


    private void createButton(){

        Typeface myTypeFace1 = Typeface.createFromAsset(getAssets(),"yourfont.ttf");
        Button btn_books = (Button) findViewById(R.id.btn_bookEditor);
        btn_books.setTypeface(myTypeFace1);

        Button btn_img = (Button) findViewById(R.id.btn_bookList);
        btn_img.setTypeface(myTypeFace1);

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setTypeface(myTypeFace1);

        btn_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsActivity.this, BookDetailsEditor.class);
                startActivity(intent);
            }
        });
        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsActivity.this, BookDetailsEditor.class);
                startActivity(intent);
            }
        });


        Button btn1 = (Button) findViewById(R.id.btn_scanner);
        btn1.setTypeface(myTypeFace1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentsActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });


    }

}
