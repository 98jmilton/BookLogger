    package com.example.ezmilja.booklogger;

    import android.Manifest;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.graphics.Typeface;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.v4.app.ActivityCompat;
    import android.support.v4.content.ContextCompat;
    import android.support.v7.app.AppCompatActivity;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.concurrent.ExecutionException;

    import static com.example.ezmilja.booklogger.BooksArray.books;


    public class SplashScreen extends AppCompatActivity {

        public static long j=0;


        final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);
            Typeface myTypeFace1 = Typeface.createFromAsset(getAssets(), "yourfont.ttf");
            TextView TextView1 = (TextView) findViewById(R.id.TextView1);
            TextView1.setTypeface(myTypeFace1);

            FirebaseStorage storage;
            final StorageReference storageReference;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference BookRef = database.getReference("/ Books/");
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            //Read data from database
            BookRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    j= dataSnapshot.getChildrenCount();
                    int i = 0;

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
                        String publisher = (String) BookSnapshot.child("Rating").getValue();


                        books[i] = new Book(isbn, name, imageAddress, author, description, page, publisher, totRating, numCopys, maxCopys, numRating);
                        i++;
                    }
                }



                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            requestPermission();


            try {


                JSONObject BookInfoObject = new RetrieveRoomsJSONTask(SplashScreen.this).execute().get();

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

                // For the Image link
                String imageLink = (String) customerIDD.getJSONObject("imageLinks").get("thumbnail");


                // For the ISBN Number
                JSONObject ISBN = customerIDD.getJSONArray("industryIdentifiers").getJSONObject(1);
                String theISBNNo = (String) ISBN.get("identifier");


                // For printing all the authors
                String a = "";
                String authors = (String) customerIDD.getJSONArray("authors").get(0);
                for(int i = 0; i < customerIDD.getJSONArray("authors").length(); i++) {
                    a += (String) customerIDD.getJSONArray("authors").get(i) + " ";
                }

    //

                System.out.println(title + "\n " + description  + "\n " + publisher  + "\n " + pageCount  + "\n " + imageLink  + "\n " + theISBNNo  + "\n " + a );



            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                // TODO: PUT THE STUFF HERE TO HANDLE THE DATA NOT FOUND

                e.printStackTrace();
            }

        }

        private void requestPermission() {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat
                        .requestPermissions(SplashScreen.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
            }
            else {

                Thread myThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                            Intent intent = new Intent(getApplicationContext(), ContentsActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                myThread.start();
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            switch (requestCode) {
                case REQUEST_CODE_ASK_PERMISSIONS:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission Granted
                        Toast.makeText(SplashScreen.this, "Permission Granted", Toast.LENGTH_SHORT)
                                .show();

                        Thread myThread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(2000);
                                    Intent intent = new Intent(getApplicationContext(),
                                            ContentsActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        myThread.start();
                    }

                    else {
                        // Permission Denied
                        Toast.makeText(SplashScreen.this, "Permission Denied", Toast.LENGTH_SHORT)
                                .show();

                        Thread myThread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(2000);
                                    Intent intent = new Intent(getApplicationContext(), ContentsActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        myThread.start();

                    }
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }




