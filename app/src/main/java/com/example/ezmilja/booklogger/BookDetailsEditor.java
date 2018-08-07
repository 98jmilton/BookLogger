package com.example.ezmilja.booklogger;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.widget.Toast.*;
import static com.example.ezmilja.booklogger.BookDetailsAdder.qrIsbn;
import static com.example.ezmilja.booklogger.ContentsActivity.BookRef;
import static com.example.ezmilja.booklogger.ContentsActivity.currentIsbn;
import static com.example.ezmilja.booklogger.ContentsActivity.storageReference;
import static com.example.ezmilja.booklogger.SplashScreen.j;

public class BookDetailsEditor extends AppCompatActivity {
    int k = (int) j;
    private Button choose;
    private Button btnSubmit;
    private Button uploadImage;
    private Button Delete;
    private ImageView imageView;
    private Uri filePath;
    private boolean isBook = false;
    private boolean bookSubmit = false;

    private int bookNumber = 0;

    private final int PICK_IMAGE_REQUEST = 71;

    String bookSName = "";
    String bookSAuthor = "";
    String bookSISBN = "";
    String bookSMaxCopys = "";
    String bookSDescription = "";
    String bookSNumCopys = "";
    String bookSPage = "";
    String bookSPublisher = "";
    String bookSNumRating = "";
    String bookSImg = "";
    String bookSRating = "";
    String imageAddress = "";
    URL imageUrl;

    String ISBN;
    String Name;
    String Author;
    String Publisher;
    String Description;
    String Rating;
    String Pages;
    String MxCopys;
    String NumRating;
    String NumCopys;

    EditText bookName;
    EditText bookAuthor;
    public static EditText bookISBN;
    EditText bookMaxCopys;
    EditText bookDescription;
    EditText bookNumCopys;
    EditText bookPage;
    EditText bookPublisher;
    EditText bookNumRating;
    EditText bookRating;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetailseditor);


        imageView = (ImageView) findViewById(R.id.imgView);
        uploadImage = findViewById(R.id.uploadImage);
        choose = findViewById(R.id.btnChoose);
        btnSubmit = findViewById(R.id.btn_submit);
        Delete = findViewById(R.id.btn_delete);

        bookName = findViewById(R.id.bookName);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookISBN = findViewById(R.id.bookISBN);
        bookMaxCopys = findViewById(R.id.bookMaxCpys);
        bookDescription = findViewById(R.id.bookDescription);
        bookNumCopys = findViewById(R.id.bookNumCpys);
        bookPage = findViewById(R.id.bookPages);
        bookPublisher = findViewById(R.id.bookPublisher);
        bookNumRating = findViewById(R.id.bookNumRating);
        bookRating = findViewById(R.id.bookRating);


        BookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot BookSnapshot : dataSnapshot.getChildren()) {

                    ISBN = (String)BookSnapshot.child(currentIsbn).child("ISBN").getValue();
                    Name = (String) BookSnapshot.child(currentIsbn).child("BookName").getValue();
                    Author = (String) BookSnapshot.child(currentIsbn).child("Author").getValue();
                    Publisher = (String) BookSnapshot.child(currentIsbn).child("Publisher").getValue();
                    Description = (String) BookSnapshot.child(currentIsbn).child("Description").getValue();
                    Rating = (String) BookSnapshot.child(currentIsbn).child("Rating").getValue();
                    Pages = (String) BookSnapshot.child(currentIsbn).child("Pages").getValue();
                    MxCopys = (String) BookSnapshot.child(currentIsbn).child("MaxCopys").getValue();
                    NumRating = (String) BookSnapshot.child(currentIsbn).child("NumRating").getValue();
                    NumCopys = (String) BookSnapshot.child(currentIsbn).child("NumCopys").getValue();
                    imageAddress = (String) BookSnapshot.child(currentIsbn).child("ImageAddress").getValue();

                    System.out.println("qwepoiqwepoiqwepoi"+ISBN);

                    bookName.setText(Name);
                    bookAuthor.setText(Author);
                    bookISBN.setText(ISBN);
                    bookMaxCopys.setText(MxCopys);
                    bookDescription.setText(Description);
                    bookNumCopys.setText(NumCopys);
                    bookPage.setText(Pages);
                    bookPublisher.setText(Publisher);
                    bookNumRating.setText(NumRating);
                    bookRating.setText(Rating);

                    try {
                        imageUrl =new URL(imageAddress);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Glide.with(BookDetailsEditor.this).load(imageUrl).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book = qrIsbn;
                BookRef.child(book).removeValue();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookSName = bookName.getText().toString();
                bookSAuthor = bookAuthor.getText().toString();
                bookSISBN = bookISBN.getText().toString();
                bookSMaxCopys = bookMaxCopys.getText().toString();
                bookSDescription = bookDescription.getText().toString();
                bookSNumCopys = bookNumCopys.getText().toString();
                bookSPage = bookPage.getText().toString();
                bookSPublisher = bookPublisher.getText().toString();
                bookSNumRating = bookNumRating.getText().toString();
                bookSRating = bookRating.getText().toString();
                uploadData();
            }

        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            bookSISBN = bookISBN.getText().toString();
            if ((bookSISBN.matches("[0-9]+")) || (bookSISBN.length() == 13)) {
                final StorageReference ref = storageReference.child(bookSISBN);
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                makeText(BookDetailsEditor.this, "Uploaded", LENGTH_SHORT).show();
                                bookSubmit = true;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                makeText(BookDetailsEditor.this, "Failed " + e.getMessage(), LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            } else {
                makeText(BookDetailsEditor.this, "Upload Failed ", LENGTH_SHORT).show();
                progressDialog.dismiss();
                Toast.makeText(this, "Please insert book ISBN", Toast.LENGTH_LONG).show();

            }
        }

    }

    private void uploadData() {


        BookRef.child(bookSISBN).child("BookName").setValue(bookSName);
        BookRef.child(bookSISBN).child("Author").setValue(bookSAuthor);
        BookRef.child(bookSISBN).child("ISBN").setValue(bookSISBN);
        BookRef.child(bookSISBN).child("MaxCopys").setValue(bookSMaxCopys);
        BookRef.child(bookSISBN).child("Description").setValue(bookSDescription);
        BookRef.child(bookSISBN).child("NumCopys").setValue(bookSNumCopys);
        BookRef.child(bookSISBN).child("Pages").setValue(bookSPage);
        BookRef.child(bookSISBN).child("Publisher").setValue(bookSPublisher);
        BookRef.child(bookSISBN).child("ImageAddress").setValue(bookSImg);
        BookRef.child(bookSISBN).child("Rating").setValue(bookSRating);
        BookRef.child(bookSISBN).child("NumRating").setValue(bookSNumRating);

        storageReference.child(bookSISBN).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                bookSImg =uri.toString();
                System.out.println(bookSImg);
                BookRef.child("/Books/").child(bookSISBN).child("ImageAddress").setValue(bookSImg);

                bookName.setText("");
                bookDescription.setText("");
                bookPublisher.setText("");
                bookISBN.setText("");
                bookAuthor.setText("");
                bookRating.setText("");
                bookPage.setText("");
                bookNumRating.setText("");

                Intent intent = new Intent(BookDetailsEditor.this, ContentsActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
}