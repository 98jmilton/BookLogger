package com.example.ezmilja.booklogger;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.widget.Toast.*;
import static com.example.ezmilja.booklogger.BooksArray.books;
import static com.example.ezmilja.booklogger.SplashScreen.j;

public class BookDetailsAdder extends AppCompatActivity
{
    int k = (int) j;
    private Button btnChoose;
    private Button btn_autofill;
    private Button Scan;
    private Button btnSubmit;
    private ImageView imageView;
    private Uri filePath;
    private boolean isBook=false;
    private boolean bookSubmit=false;

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



    public static String qrIsbn;
    private final static String ERROR_MESSAGE = "Unable to scan bar code";


    Button send;
    Button choose;

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

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference BookRef = database.getReference("/ Books/");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetailsadder);




                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();

                imageView = (ImageView) findViewById(R.id.imgView);
                send = (Button) findViewById(R.id.send);
                choose= (Button) findViewById(R.id.btnChoose);
                btnSubmit = (Button) findViewById(R.id.btn_submit);

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

                bookISBN.setText(qrIsbn);
                Toast.makeText(this,"XXXXXXXXXXXXXX      "+j+"      XXXXXXXXXXXXXX",Toast.LENGTH_LONG).show();


                choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseImage();
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
                Intent intent = new Intent(BookDetailsAdder.this, ScanActivity.class);
                startActivity(intent);

            }
        });


        btn_autofill = (Button) findViewById(R.id.btn_autofill);
        btn_autofill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                qrIsbn = bookISBN.getText().toString();
                String bookNum;
                String qrNum ;


                for (int i = 0; i < j; i++) {

                       bookNum = books[i].isbn;
                       qrNum = qrIsbn;
                       if(isBook == true){break;}
                       if (qrNum.equals(bookNum)) {
                           isBook = true;
                           System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                           break;
                       } else {
                           //isBook = false;
                            System.out.println("AAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHH");
                       }

                   }


                    if((qrIsbn.matches("[0-9]+")) && (qrIsbn.length() == 13) && (isBook)){
                        System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHH");
                        makeInfoDialog();
                   }


                 if((qrIsbn.matches("[0-9]+")) && (qrIsbn.length() == 13) && (!isBook)){
                    System.out.println("PPPPPPPPPPPPPPPPAAAAAAAAUUUUUUULLLLLL");


                    bookISBN.setText(qrIsbn);

                    try {
                        System.out.println("SSSSSSSSSSSSSSSSSSEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAANNNNNNNNN");

                        JSONObject BookInfoObject = new RetrieveRoomsJSONTask(BookDetailsAdder.this).execute().get();

                        JSONArray items = BookInfoObject.getJSONArray("items");
                        JSONObject customerIDD = items.getJSONObject(0).getJSONObject("volumeInfo");

                        // For the Title
                        String title = (String) customerIDD.get("title");

                        // For the description
                        String description = (String) customerIDD.get("description");

                        // For the publisher
                        String publisher = (String) customerIDD.get("publisher");

                        // For the pagCount
                        int pageCount = (int) customerIDD.get("pageCount");
                        String pageCountString = Integer.toString(pageCount);

                        // For the Image link
                        String imageLink = (String) customerIDD.getJSONObject("imageLinks").get("thumbnail");


                        // For the ISBN Number
                        JSONObject ISBN = customerIDD.getJSONArray("industryIdentifiers").getJSONObject(0);
                        String theISBNNo = (String) ISBN.get("identifier");


                        // For printing all the authors
                        String a = "";
                        String authors = (String) customerIDD.getJSONArray("authors").get(0);
                        for(int i = 0; i < customerIDD.getJSONArray("authors").length(); i++) {
                            a += (String) customerIDD.getJSONArray("authors").get(i) + " ";
                        }



                        //  System.out.println(title + "\n " + description  + "\n " + publisher  + "\n " + pageCount  + "\n " + imageLink  + "\n " + theISBNNo  + "\n " + a );


                        bookName.setText(title);
                        bookDescription.setText(description);
                        bookPublisher.setText(publisher);
                        bookISBN.setText(theISBNNo);
                        bookAuthor.setText(a);
                        bookRating.setText("0");
                        bookPage.setText(pageCountString);
                        bookNumRating.setText("0");



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {

                        // TODO: PUT THE STUFF HERE TO HANDLE THE DATA NOT FOUND

                        e.printStackTrace();
                    }

                }

                else{

                    Toast.makeText(BookDetailsAdder.this, "Not a valid ISBN please scan barcode or enter one manually", Toast.LENGTH_LONG).show();
                }





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
                            makeText(BookDetailsAdder.this, "Uploaded", LENGTH_SHORT).show();
                            bookSubmit=true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            makeText(BookDetailsAdder.this, "Failed "+e.getMessage(), LENGTH_SHORT).show();
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

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageReference.child("books/"+bookSISBN).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageAddress =uri.toString();
                BookRef.child(bookSISBN).child("ImageAddress").setValue(imageAddress);

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
        dialog.setContentView(R.layout.popup);
        dialog.show();

        Typeface myTypeFace1 = Typeface.createFromAsset(getAssets(),"yourfont.ttf");

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
