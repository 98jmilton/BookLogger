package com.example.ezmilja.booklogger;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.ezmilja.booklogger.BooksArray.books;
import static com.example.ezmilja.booklogger.SplashScreen.j;
import static com.example.ezmilja.booklogger.SplashScreen.p;

public class ContentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
                    createButton();

        FirebaseStorage storage;
        final StorageReference storageReference;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference BookRef = database.getReference("/ Books/");

        //Read data from database
        BookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                j= dataSnapshot.getChildrenCount();

                for (DataSnapshot BookSnapshot : dataSnapshot.getChildren()) {
                    String isbn = (String) BookSnapshot.child("ISBN").getValue();
                    String author = (String) BookSnapshot.child("Author").getValue();
                    String name = (String) BookSnapshot.child("BookName").getValue();
                    String description = (String) BookSnapshot.child("Description").getValue();
                    String imageAddress = (String) BookSnapshot.child("ImageAddress").getValue();
                    String maxCopys = (String) BookSnapshot.child("MaxCopys").getValue();
                    String numCopys = (String) BookSnapshot.child("NumCopys").getValue();
                    String numRating = (String) BookSnapshot.child("NumRating").getValue();
                    String page = (String) BookSnapshot.child("Pages").getValue();
                    String totRating = (String) BookSnapshot.child("Rating").getValue();
                    String publisher = (String) BookSnapshot.child("Publisher").getValue();

//                    System.out.println(isbn +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(author +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(name +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(description +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(imageAddress +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(maxCopys +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(numCopys +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(numRating +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(page +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(totRating +"XXXXXXXXXXXXXXXXXXX");
//                    System.out.println(publisher +"XXXXXXXXXXXXXXXXXXX");

                    books[p] = new Book(isbn, name, imageAddress, author, description, page, publisher, totRating, numCopys, maxCopys, numRating);
                    p++;
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }



    private void createButton(){

        Typeface myTypeFace1 = Typeface.createFromAsset(getAssets(),"yourfont.ttf");
        Button btn_booksEditor = (Button) findViewById(R.id.btn_bookAdder);
        btn_booksEditor.setTypeface(myTypeFace1);

        Button btn_bookList = (Button) findViewById(R.id.btn_bookList);
        btn_bookList.setTypeface(myTypeFace1);

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setTypeface(myTypeFace1);

        btn_booksEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsActivity.this, BookDetailsAdder.class);
                startActivity(intent);
            }
        });
        btn_bookList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentsActivity.this, BookDetailsAdder.class);
                startActivity(intent);
            }
        });


        Button btn1 = (Button) findViewById(R.id.btn_bookList);
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
