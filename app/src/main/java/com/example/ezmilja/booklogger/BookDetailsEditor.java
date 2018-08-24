package com.example.ezmilja.booklogger;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.widget.Toast.*;
import static com.example.ezmilja.booklogger.ContentsActivity.currentIsbn;
import static com.example.ezmilja.booklogger.ContentsActivity.detailscurrentPage;
import static com.example.ezmilja.booklogger.ContentsActivity.editorcurrentPage;
import static com.example.ezmilja.booklogger.ContentsActivity.listcurrentPage;
import static com.example.ezmilja.booklogger.SplashScreen.BookRef;
import static com.example.ezmilja.booklogger.SplashScreen.storageReference;

public class BookDetailsEditor extends AppCompatActivity {
    private Button choose,btnSubmit,uploadImage,Delete;
    private ImageView imageView;
    private Uri filePath;
    private boolean bookSubmit = false;
    static boolean isDeleted;
    Typeface myTypeFace1;

    private int bookNumber = 0;

    private final int PICK_IMAGE_REQUEST = 71;

    String urlstring,bookSName,bookSAuthor,bookSISBN,bookSMaxCopys,bookSDescription,bookSNumCopys,bookSPage,bookSPublisher,bookSNumRating,bookSImg,bookSGenre,bookSRating,Genre,imageAddress,ISBN,Name,Author,Publisher,Description,Rating,Pages,MxCopys,NumRating,NumCopys;
    URL imageUrl;

    public static TextView bookISBN;
    EditText bookName,bookGenre,bookAuthor, bookMaxCopys, bookDescription, bookNumCopys, bookPage, bookPublisher, bookNumRating, bookRating;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetailseditor);
        myTypeFace1 = Typeface.createFromAsset(getAssets(),"yourfont.ttf");

        imageView = findViewById(R.id.imgView);
        uploadImage = findViewById(R.id.uploadImage);
        choose = findViewById(R.id.btnChoose);
        btnSubmit = findViewById(R.id.btn_submit);
        Delete = findViewById(R.id.btn_delete);
        bookISBN = findViewById(R.id.bookISBN);
        bookName = findViewById(R.id.bookName);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookMaxCopys = findViewById(R.id.bookMaxCpys);
        bookDescription = findViewById(R.id.bookDescription);
        bookNumCopys = findViewById(R.id.bookNumCpys);
        bookPage = findViewById(R.id.bookPages);
        bookPublisher = findViewById(R.id.bookPublisher);
        bookNumRating = findViewById(R.id.bookNumRating);
        bookRating = findViewById(R.id.bookRating);
        bookGenre = findViewById(R.id.bookGenre);


        BookRef.child("/Books/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               if (editorcurrentPage) {

                   ISBN = (String) dataSnapshot.child(currentIsbn).child("ISBN").getValue();
                   Name = (String) dataSnapshot.child(currentIsbn).child("BookName").getValue();
                   Author = (String) dataSnapshot.child(currentIsbn).child("Author").getValue();
                   Publisher = (String) dataSnapshot.child(currentIsbn).child("Publisher").getValue();
                   Description = (String) dataSnapshot.child(currentIsbn).child("Description").getValue();
                   Rating = (String) dataSnapshot.child(currentIsbn).child("Rating").getValue();
                   Pages = (String) dataSnapshot.child(currentIsbn).child("Pages").getValue();
                   MxCopys = (String) dataSnapshot.child(currentIsbn).child("MaxCopys").getValue();
                   NumRating = (String) dataSnapshot.child(currentIsbn).child("NumRating").getValue();
                   NumCopys = (String) dataSnapshot.child(currentIsbn).child("NumCopys").getValue();
                   imageAddress = (String) dataSnapshot.child(currentIsbn).child("ImageAddress").getValue();
                   Genre = (String) dataSnapshot.child(currentIsbn).child("Genre").getValue();


                   try {
                       if (ISBN != null && Name != null && Author != null && Publisher != null && Description != null && Rating != null && Pages != null && MxCopys != null && NumRating != null && NumCopys != null && imageAddress != null && Genre != null) {
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
                           bookGenre.setText(Genre);
                           imageUrl = new URL(imageAddress);
                       } else {
                           makeOopsDialog();
                       }
                   } catch (MalformedURLException e) {
                       //e.printStackTrace();
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
            public void onClick(View view) {

                 makeDeleteDialog();
            }

        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIsbn = bookISBN.getText().toString();

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
                bookSGenre = bookGenre.getText().toString();
                uploadData();
        }});

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIsbn = bookISBN.getText().toString();
                if (currentIsbn.length() == 13) {
                    uploadImage();
                }
                else{
                    Toast.makeText(BookDetailsEditor.this,"Please enter a 13 digit ISBN" ,LENGTH_LONG).show();
                }
            }
        });
    }

    private void makeDeleteDialog(){


        final Dialog deletedialog = new Dialog(BookDetailsEditor.this);
        deletedialog.setContentView(R.layout.deleterequest);

        deletedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

          TextView title = deletedialog.findViewById(R.id.title);
          title.setText("Are you sure you want to delete this book from the database?");
          title.setTypeface(myTypeFace1);

          Button yes= deletedialog.findViewById(R.id.yes);
          Button no = deletedialog.findViewById(R.id.no);

          yes.setTypeface(myTypeFace1);
          no.setTypeface(myTypeFace1);
            deletedialog.show();

          yes.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View view) {
                  String book = currentIsbn;

                  BookRef.child("/Books/").child(book).removeValue();

                  Intent intent = new Intent(BookDetailsEditor.this, ContentsActivity.class);
                  isDeleted = true;
                  editorcurrentPage=false;
                  finish();
                  startActivity(intent);
              }
          });

          no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                deletedialog.dismiss();
            }
        });
    }

    private void makeOopsDialog(){

        final Dialog deletedialog = new Dialog(BookDetailsEditor.this);
        deletedialog.setContentView(R.layout.oopsdialog);
        deletedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = deletedialog.findViewById(R.id.title);
        TextView oops = deletedialog.findViewById(R.id.oops);
        Button back = deletedialog.findViewById(R.id.back);

        back.setTypeface(myTypeFace1);
        title.setTypeface(myTypeFace1);
        oops.setTypeface(myTypeFace1);

        deletedialog.show();

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailsEditor.this, ContentsActivity.class);
                editorcurrentPage=false;
                finish();
                startActivity(intent);
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

        urlstring = String.valueOf(imageUrl);

        BookRef.child("/Books/").child(currentIsbn).child("BookName").setValue(bookSName);
        BookRef.child("/Books/").child(currentIsbn).child("Author").setValue(bookSAuthor);
        BookRef.child("/Books/").child(currentIsbn).child("ISBN").setValue(currentIsbn);
        BookRef.child("/Books/").child(currentIsbn).child("MaxCopys").setValue(bookSMaxCopys);
        BookRef.child("/Books/").child(currentIsbn).child("Description").setValue(bookSDescription);
        BookRef.child("/Books/").child(currentIsbn).child("NumCopys").setValue(bookSNumCopys);
        BookRef.child("/Books/").child(currentIsbn).child("Pages").setValue(bookSPage);
        BookRef.child("/Books/").child(currentIsbn).child("Publisher").setValue(bookSPublisher);
        BookRef.child("/Books/").child(currentIsbn).child("Rating").setValue(bookSRating);
        BookRef.child("/Books/").child(currentIsbn).child("NumRating").setValue(bookSNumRating);
        BookRef.child("/Books/").child(currentIsbn).child("ImageAddress").setValue(urlstring);
        BookRef.child("/Books/").child(currentIsbn).child("Genre").setValue(bookSGenre);

        if(bookSubmit) {
            storageReference.child(currentIsbn).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    bookSImg = uri.toString();
                    BookRef.child("/Books/").child(currentIsbn).child("ImageAddress").setValue(bookSImg);

                    bookName.setText("");
                    bookDescription.setText("");
                    bookPublisher.setText("");
                    bookISBN.setText("");
                    bookAuthor.setText("");
                    bookRating.setText("");
                    bookPage.setText("");
                    bookNumRating.setText("");
                    bookGenre.setText("");

                    Intent intent = new Intent(BookDetailsEditor.this, ContentsActivity.class);
                    editorcurrentPage=false;
                    finish();
                    startActivity(intent);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        else{bookName.setText("");
            bookDescription.setText("");
            bookPublisher.setText("");
            bookISBN.setText("");
            bookAuthor.setText("");
            bookRating.setText("");
            bookPage.setText("");
            bookNumRating.setText("");
            bookGenre.setText("");

            Intent intent = new Intent(BookDetailsEditor.this, ContentsActivity.class);
            editorcurrentPage=false;
            startActivity(intent);
            finish();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editorcurrentPage=false;
        listcurrentPage=true;
        this.finish();
        Intent intent = new Intent( this, BookList.class);
        startActivity(intent);
    }
}