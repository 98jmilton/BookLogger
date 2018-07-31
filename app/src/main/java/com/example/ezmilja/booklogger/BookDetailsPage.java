package com.example.ezmilja.booklogger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.ezmilja.booklogger.BookDetailsAdder.qrIsbn;
import static com.example.ezmilja.booklogger.BooksArray.books;
import static com.example.ezmilja.booklogger.SplashScreen.j;

public class BookDetailsPage extends AppCompatActivity {
    TextView bookISBN;
    TextView bookName;
    TextView bookAuthor;
    TextView bookPublisher;
    TextView bookDescription;
    TextView bookRating;
    TextView bookPages;
    private ImageView bookImage;
    private Button btn_edit;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference BookRef = database.getReference("/ Books/");

    private String ISBN = "Not Found";
    private String Name = "Not Found";
    private String Author = "Not Found";
    private String Publisher = "Not Found";
    private String Description  = "Not Found";
    private String Rating = "Not Found";
    private String Pages = "Not Found";
    private int bookNumber=0;

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

        btn_edit= findViewById(R.id.btn_edit);
        getBook();
        ISBN = books[bookNumber].isbn;
        Name = books[bookNumber].bookName;
        Author = books[bookNumber].author;
        Publisher = books[bookNumber].publisher;
        Description = books[bookNumber].description;
        Rating = books[bookNumber].rating;
        Pages = books[bookNumber].page;

        bookISBN.setText("ISBN: "+ ISBN);
        bookName.setText("Title: "+ Name);
        bookAuthor.setText("Author: "+ Author);
        bookPublisher.setText("Publisher: "+ Publisher);
        bookDescription.setText("Description: "+ Description);
        bookRating.setText("User Rating: "+ Rating);
        bookPages.setText("Page Count:"+ Pages);



        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageReference.child("books/"+ISBN).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageAddress =uri.toString();
                Glide.with(BookDetailsPage.this).load(imageAddress).into(bookImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailsPage.this, BookDetailsEditor.class);
                startActivity(intent);
            }
        });



    }

    private void getBook(){
        int k =(int) j;
        for (int i = 0; i<k; i++){
            if (books[i].isbn == qrIsbn){
                i = bookNumber;
            }
        }

    }
}
