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
import java.net.URI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class BookDetailsEditor extends AppCompatActivity
{
    private Button btnChoose;
    private ImageView imageView;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;

    String bookSName        ="";
    String bookSAuthor      ="";
    String bookSISBN        ="";
    String bookSMaxCopys    ="";
    String bookSDescription ="";
    String bookSNumCopys    ="";
    String bookSPage        ="";
    String bookSPublisher   ="";
    String bookSNumRating   ="";
    String bookSImg         ="";
    String bookSRating      ="";



    public String qrInfo;
    private final static String ERROR_MESSAGE = "Unable to scan bar code";


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference BookRef = database.getReference("/ Books/");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetails);

        qrInfo = getValue(getIntent());


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imageView = (ImageView) findViewById(R.id.imgView);
        Button send = (Button) findViewById(R.id.send);
        Button choose= (Button) findViewById(R.id.btnChoose);

        final EditText bookName       =findViewById(R.id.bookName);
        final EditText bookAuthor     =findViewById(R.id.bookAuthor);
        final EditText bookISBN       =findViewById(R.id.bookISBN);
        final EditText bookMaxCopys   =findViewById(R.id.bookMaxCpys);
        final EditText bookDescription=findViewById(R.id.bookDescription);
        final EditText bookNumCopys   =findViewById(R.id.bookNumCpys);
        final EditText bookPage       =findViewById(R.id.bookPages);
        final EditText bookPublisher  =findViewById(R.id.bookPublisher);
        final EditText bookNumRating  =findViewById(R.id.bookNumRating);
        final EditText bookRating     =findViewById(R.id.bookRating);

        bookISBN.setText(qrInfo);

            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();
                }
            });

            send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookSName        = bookName.getText().toString();
                bookSAuthor      = bookAuthor.getText().toString();
                bookSISBN        = bookISBN.getText().toString();
                bookSMaxCopys    = bookMaxCopys.getText().toString();
                bookSDescription = bookDescription.getText().toString();
                bookSNumCopys    = bookNumCopys.getText().toString();
                bookSPage        = bookPage.getText().toString();
                bookSPublisher   = bookPublisher.getText().toString();
                bookSNumRating   = bookNumRating.getText().toString();
                bookSImg         = "oops";
                bookSRating      = bookRating.getText().toString();
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
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("books/"+ bookSISBN);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(BookDetailsEditor.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            uploadData();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(BookDetailsEditor.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }

    }
    private void uploadData() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



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

        /*StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        storageRef.child("books/"+bookSISBN).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                bookSISBN=uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        BookRef.child(bookSISBN).child("ImageAddress").setValue(bookSImg);
        */

    }



// For QR scanner passing value of scanned item through the Saved instances
    private String getValue(final Intent intent) {
        try {
            final String barCodeString = intent.getExtras().getString(Constants.SCAN_BAR_TEST_KEY);

            return barCodeString != null ? barCodeString : ERROR_MESSAGE;

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return ERROR_MESSAGE;
    }
}
