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

public class ContentsActivity extends AppCompatActivity {
    public static int h=0;
    static int j;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    final static public DatabaseReference BookRef = database.getReference();

    static  String currentIsbn="";
    int i = 0;
    public static Book[] books = new Book[j];
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    final static public StorageReference storageReference = storage.getReference();
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
                    createButton();

       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference BookRef = database.getReference("/ Books/");




       //Read data from database
       BookRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               j = (int) dataSnapshot.getChildrenCount();

           }

       @Override
       public void onCancelled(@NonNull DatabaseError databaseError) {

       }
   });
       BookRef.child("/ Books/").addListenerForSingleValueEvent(new ValueEventListener() {

           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               h = (int) dataSnapshot.getChildrenCount();
               System.out.println(h);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
               // ...
           }
       });

       BookRef.addValueEventListener(new ValueEventListener() {
           String Number;
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot BookSnapshot : dataSnapshot.getChildren()) {

                       String name         = (String) BookSnapshot.child ("BookName")    .getValue();
                       String imageAddress = (String) BookSnapshot.child ("ImageAddress").getValue();
                       String author       = (String) BookSnapshot.child ("Author")      .getValue();
                       String genre        = (String) BookSnapshot.child ("Genre")       .getValue();

                       books[i] = new Book(name, imageAddress, author, genre);
                       i++;
                   }
               }


           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
   }


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
                Intent intent = new Intent(ContentsActivity.this, BookList.class);
                startActivity(intent);
            }
        });


    }

}
