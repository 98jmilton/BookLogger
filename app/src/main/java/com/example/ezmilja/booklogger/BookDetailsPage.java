package com.example.ezmilja.booklogger;

import android.content.Intent;
import android.icu.util.IslamicCalendar;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;

import static com.example.ezmilja.booklogger.ContentsActivity.BookRef;
import static com.example.ezmilja.booklogger.ContentsActivity.currentIsbn;


public class BookDetailsPage extends AppCompatActivity {
    TextView bookISBN;
    TextView bookName;
    TextView bookAuthor;
    TextView bookPublisher;
    TextView bookDescription;
    TextView bookRating;
    TextView bookPages;
    TextView bookGenre;
    private ImageView bookImage;

    private String ISBN = "Not Found";
    private String Name = "Not Found";
    private String Author = "Not Found";
    private String Publisher = "Not Found";
    private String Description  = "Not Found";
    private String Rating = "Not Found";
    private String Pages = "Not Found";
    private String imageAddress ;
    private String Genre = "Not Found";
    private URL imageUrl;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetails);

        bookISBN=  findViewById(R.id.bookISBN);
        bookName= findViewById(R.id.bookName);
        bookAuthor= findViewById(R.id.bookAuthor);
        bookPublisher= findViewById(R.id.bookPublisher);
        bookDescription= findViewById(R.id.bookDescription);
        bookRating= findViewById(R.id.bookRating);
        bookPages= findViewById(R.id.bookPages);
        bookImage= findViewById(R.id.bookImage);
        bookGenre = findViewById(R.id.bookGenre);

        BookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot BookSnapshot: dataSnapshot.getChildren()) {
                    ISBN = (String) BookSnapshot.child(currentIsbn).child("ISBN").getValue();
                    Name = (String) BookSnapshot.child(currentIsbn).child("BookName").getValue();
                    Author = (String) BookSnapshot.child(currentIsbn).child("Author").getValue();
                    Publisher = (String) BookSnapshot.child(currentIsbn).child("Publisher").getValue();
                    Description = (String) BookSnapshot.child(currentIsbn).child("Description").getValue();
                    Rating = (String) BookSnapshot.child(currentIsbn).child("Rating").getValue();
                    Pages = (String) BookSnapshot.child(currentIsbn).child("Pages").getValue();
                    imageAddress = (String) BookSnapshot.child(currentIsbn).child("ImageAddress").getValue();
                    Genre = (String) BookSnapshot.child(currentIsbn).child("Genre").getValue();

                    bookISBN.setText("ISBN: "+ ISBN);
                    bookName.setText("Title: "+ Name);
                    bookAuthor.setText("Author: "+ Author);
                    bookPublisher.setText("Publisher: "+ Publisher);
                    bookDescription.setText("Description: "+ Description);
                    bookRating.setText("User Rating: "+ Rating);
                    bookPages.setText("Page Count:"+ Pages);
                    bookGenre.setText("Genre:"+ Genre);


                    try {
                        imageUrl =new URL(imageAddress);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Glide.with(BookDetailsPage.this).load(imageUrl).into(bookImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailsPage.this, BookDetailsEditor.class);
                startActivity(intent);
            }
        });
    }
}
