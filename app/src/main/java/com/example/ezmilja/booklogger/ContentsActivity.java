package com.example.ezmilja.booklogger;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.ezmilja.booklogger.BookDetailsEditor.isDeleted;

public class ContentsActivity extends AppCompatActivity {

       static String currentIsbn="";
    public static Book[] books;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_contents);

       //Notify when book is deleted from database
      if  (isDeleted){   Toast.makeText(this,"Book deleted from database",LENGTH_LONG).show();}

       createButton();
   }

   //Buttons to navigate to other activities
    private void createButton(){

        Typeface myTypeFace1 = Typeface.createFromAsset(getAssets(),"yourfont.ttf");
        Button btn_booksEditor = findViewById(R.id.btn_bookAdder);
        btn_booksEditor.setTypeface(myTypeFace1);

        Button btn_bookList = findViewById(R.id.btn_bookList);
        btn_bookList.setTypeface(myTypeFace1);

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setTypeface(myTypeFace1);

        btn_booksEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ContentsActivity.this, BookDetailsAdder.class);
                startActivity(intent);
                ContentsActivity.this.finish();
            }
        });

        btn_bookList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ContentsActivity.this, BookList.class);
                startActivity(intent);
                ContentsActivity.this.finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}