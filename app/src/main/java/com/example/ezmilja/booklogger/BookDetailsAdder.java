package com.example.ezmilja.booklogger;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.IslamicCalendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import static android.widget.Toast.*;
import static com.example.ezmilja.booklogger.ContentsActivity.BookRef;
import static com.example.ezmilja.booklogger.ContentsActivity.currentIsbn;
import static com.example.ezmilja.booklogger.ContentsActivity.database;
import static com.example.ezmilja.booklogger.ContentsActivity.storageReference;


public class BookDetailsAdder extends AppCompatActivity
{
    static final int REQUEST_IMAGE_CAPTURE = 1;

    Button btn_autofill,Scan,btnSubmit,send,choose,btnCamera;
    ImageView imageView;
    private Uri filePath;
    private boolean bookSubmit=false;
    private boolean isScanned=false;
    private final int PICK_IMAGE_REQUEST = 71;
    public static String qrIsbn;
    public static EditText bookISBN;
    String bookSName,bookSAuthor,bookSISBN,bookSMaxCopys,bookSDescription,bookSNumCopys,bookSPage,bookSPublisher,bookSNumRating,bookSImg,bookSRating="";
    String theISBNNo,a,authors,title,description,publisher,pageCountString,imageLink,Imageaddress;
    int pageCount;
    Uri file;
    EditText bookName,bookAuthor,bookMaxCopys,bookDescription,bookNumCopys,bookPage,bookPublisher,bookNumRating,bookRating;
    JSONObject ISBN;
    Typeface myTypeFace1;

    private static int CONTENT_REQUEST=1337;
    private File output;

    String realPath;

    boolean PhotoTaken = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetailsadder);
        myTypeFace1 = Typeface.createFromAsset(getAssets(),"yourfont.ttf");


                imageView = (ImageView) findViewById(R.id.imgView);
                send = (Button) findViewById(R.id.send);
                choose= (Button) findViewById(R.id.btnChoose);
                btnSubmit = (Button) findViewById(R.id.btn_submit);
                btnCamera= (Button) findViewById(R.id.btncamera);
                 bookName       =findViewById(R.id.bookName);
                 bookAuthor     =findViewById(R.id.bookAuthor);
                 bookISBN       =findViewById(R.id.bookISBN);
                 bookMaxCopys   =findViewById(R.id.bookMaxCpys);
                 bookDescription=findViewById(R.id.bookDescription);
                 bookNumCopys   =findViewById(R.id.bookNumCpys);
                 bookPage       =findViewById(R.id.bookPages);
                 bookPublisher  =findViewById(R.id.bookPublisher);
                 bookNumRating  =findViewById(R.id.bookNumRating);
                 bookRating     =findViewById(R.id.bookRating);

                bookISBN.setText("9780131479418");
                if(isScanned){bookISBN.setText(qrIsbn);}

                choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseImage();
                    }
                });

                btnCamera.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        takeImage();
                    }
                });
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!bookSubmit){ Toast.makeText(BookDetailsAdder.this,"Please Submit a book image",LENGTH_LONG).show();}
                        else {
                            bookSName = bookName.getText().toString();
                            bookSAuthor = bookAuthor.getText().toString();
                            bookSISBN = bookISBN.getText().toString();
                            bookSMaxCopys = bookMaxCopys.getText().toString();
                            bookSDescription = bookDescription.getText().toString();
                            bookSNumCopys = bookNumCopys.getText().toString();
                            bookSPage = bookPage.getText().toString();
                            bookSPublisher = bookPublisher.getText().toString();
                            bookSNumRating = bookNumRating.getText().toString();
                            bookSImg = "oops";
                            bookSRating = bookRating.getText().toString();
                            uploadData();

                        }
                    }
                });

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImage();
                    }
                });



        Scan = (Button) findViewById(R.id.btn_scan);
        Scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                isScanned = true;
                Intent intent = new Intent(BookDetailsAdder.this, ScanActivity.class);
                startActivity(intent);

            }
        });


        btn_autofill = (Button) findViewById(R.id.btn_autofill);
        btn_autofill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentIsbn = bookISBN.getText().toString();


                try {

                    JSONObject BookInfoObject = new RetrieveDataJSON(BookDetailsAdder.this).execute().get();

                    JSONArray items = BookInfoObject.getJSONArray("items");
                    JSONObject customerIDD = items.getJSONObject(0).getJSONObject("volumeInfo");

                    // For the Title
                    title = (String) customerIDD.get("title");

                    // For the description
                    description = (String) customerIDD.get("description");

                    // For the publisher
                    publisher = (String) customerIDD.get("publisher");

                    // For the pagCount
                    pageCount = (int) customerIDD.get("pageCount");
                    pageCountString = Integer.toString(pageCount);

                    // For the Image link
                    imageLink = (String) customerIDD.getJSONObject("imageLinks").get("thumbnail");


                    // For the ISBN Number
                    ISBN = customerIDD.getJSONArray("industryIdentifiers").getJSONObject(0);
                    theISBNNo = (String) ISBN.get("identifier");


                    // For printing all the authors
                    a = "";
                    authors = (String) customerIDD.getJSONArray("authors").get(0);
                    for(int i = 0; i < customerIDD.getJSONArray("authors").length(); i++) {
                        a += (String) customerIDD.getJSONArray("authors").get(i) + " ";
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {

                    e.printStackTrace();
                }


                final DatabaseReference BookRef2 = database.getReference("/Books/");
                System.out.println("SSSSSSSSSSSSSSSSSSEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAANNNNNNNNN");

                BookRef2.addValueEventListener(new ValueEventListener() {
                    String Number;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot BookSnapshot : dataSnapshot.getChildren()) {
                            Number = BookSnapshot.getKey();
                            System.out.println("qrIsbn:"+currentIsbn  + "\n" + "Number:"+Number + "\n");

                            if (Number.equals(currentIsbn)) {
                                isBook();
                                System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                                break;
                            } else {
                                isntBook();
                                System.out.println("AAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHH");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        });

    }

    public void isBook() {

        if ((currentIsbn.matches("[0-9]+")) && (currentIsbn.length() == 13)) {
            bookName.setText("");
            bookDescription.setText("");
            bookPublisher.setText("");
            bookAuthor.setText("");
            bookRating.setText("");
            bookPage.setText("");
            bookNumRating.setText("");
            System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHH");
            makeInfoDialog();
        }
    }

    public void isntBook(){
        if((currentIsbn.matches("[0-9]+")) && (currentIsbn.length() == 13)){
            System.out.println("PPPPPPPPPPPPPPPPAAAAAAAAUUUUUUULLLLLL");


            bookISBN.setText(currentIsbn);
            bookName.setText(title);
            bookDescription.setText(description);
            bookPublisher.setText(publisher);
            bookISBN.setText(theISBNNo);
            bookAuthor.setText(a);
            bookRating.setText("0");
            bookPage.setText(pageCountString);
            bookNumRating.setText("0");
        }
        else{

            Toast.makeText(BookDetailsAdder.this, "Not a valid ISBN please scan barcode or enter one manually", Toast.LENGTH_LONG).show();
        }
    }



    private void chooseImage() {
        PhotoTaken = false;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void takeImage(){
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir=
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        output=new File(dir, "CameraContentDemo.jpeg");
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivity(i);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

          filePath = data.getData();


            realPath = ImageFilePath.getPath(BookDetailsAdder.this, data.getData());
//                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

            Toast.makeText(BookDetailsAdder.this,"onActivityResult: file path : " + realPath,LENGTH_LONG).show();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Log.d(TAG, String.valueOf(bitmap));


                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }}


         else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
                                makeText(BookDetailsAdder.this, "Uploaded", LENGTH_SHORT).show();
                                bookSubmit = true;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                makeText(BookDetailsAdder.this, "Failed " + e.getMessage(), LENGTH_SHORT).show();
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
                makeText(BookDetailsAdder.this, "Upload Failed ", LENGTH_SHORT).show();
                progressDialog.dismiss();
                Toast.makeText(this, "Please insert book ISBN", Toast.LENGTH_LONG).show();

            }
        }

    }
    private void uploadData() {


        BookRef.child("/Books/").child(bookSISBN).child("BookName").setValue(bookSName);
        BookRef.child("/Books/").child(bookSISBN).child("Author").setValue(bookSAuthor);
        BookRef.child("/Books/").child(bookSISBN).child("ISBN").setValue(bookSISBN);
        BookRef.child("/Books/").child(bookSISBN).child("MaxCopys").setValue(bookSMaxCopys);
        BookRef.child("/Books/").child(bookSISBN).child("Description").setValue(bookSDescription);
        BookRef.child("/Books/").child(bookSISBN).child("NumCopys").setValue(bookSNumCopys);
        BookRef.child("/Books/").child(bookSISBN).child("Pages").setValue(bookSPage);
        BookRef.child("/Books/").child(bookSISBN).child("Publisher").setValue(bookSPublisher);
        BookRef.child("/Books/").child(bookSISBN).child("ImageAddress").setValue(bookSImg);
        BookRef.child("/Books/").child(bookSISBN).child("Rating").setValue(bookSRating);
        BookRef.child("/Books/").child(bookSISBN).child("NumRating").setValue(bookSNumRating);

        storageReference.child(bookSISBN).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                 bookSImg =uri.toString();
                System.out.println("qedpqwjdpqwpowj"+bookSImg);
                BookRef.child("/Books/").child(bookSISBN).child("ImageAddress").setValue(bookSImg);

                bookName.setText("");
                bookDescription.setText("");
                bookPublisher.setText("");
                bookISBN.setText("");
                bookAuthor.setText("");
                bookRating.setText("");
                bookPage.setText("");
                bookNumRating.setText("");

                Intent intent = new Intent(BookDetailsAdder.this, ContentsActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void makeInfoDialog(){
        final Dialog dialog = new Dialog(BookDetailsAdder.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));;
        dialog.show();

        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setTypeface(myTypeFace1);

        Button yes= (Button) dialog.findViewById(R.id.yes);
        Button no = (Button) dialog.findViewById(R.id.no);

        yes.setTypeface(myTypeFace1);
        no.setTypeface(myTypeFace1);

        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailsAdder.this, BookDetailsPage.class);
                startActivity(intent);
                //dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
