package com.example.ezmilja.booklogger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.ezmilja.booklogger.SplashScreen.BookRef;

public class RequestList extends AppCompatActivity {

    static String requestName = "";
    private ListView listView;
    public static RequestBook requestBook;
    public static ArrayList<RequestBook> originalList=new ArrayList<>();
    private RequestList.CustomAdapter customAdapter;
    FirebaseUser user;
    boolean isUpVoted;
    String[] emails;
    int k;
    Typeface myTypeFace1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        sortlist(originalList);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        listView = findViewById(R.id.list_view);
        myTypeFace1 = Typeface.createFromAsset(getAssets(),"yourfont.ttf");

        BookRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                k = 0;
                originalList.clear();

                for (DataSnapshot BookSnapshotB : dataSnapshot.child("/Requests/").getChildren()) {
                    k= (int) dataSnapshot.child("/Requests/").getChildrenCount();

                    String reqbook      = (String) BookSnapshotB.child("bookName").getValue();
                    String reqauthor    = (String) BookSnapshotB.child("bookAuthor").getValue();
                    String reqvotes     = (String) BookSnapshotB.child("votes").getValue();
                    String email        = (String) BookSnapshotB.child("email").getValue();
                    String votedby      = (String) BookSnapshotB.child("votedBy").getValue();

                    if(votedby!=null) {emails = votedby.split(",");}
                    else{votedby="";}

                    int votes = Integer.valueOf(reqvotes);

                    try{
                        if(reqbook!=null && reqauthor!=null && reqvotes!=null && email!=null)originalList.add(requestBook= new RequestBook(reqbook,reqauthor, email, votes,votedby,isUpVoted));
                        makeListView();
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SearchView searchView = findViewById(R.id.request_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void sortlist(List list) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o, Object t1) {
                RequestBook b1 = (RequestBook) o;
                RequestBook b2 = (RequestBook) t1;
                return b1.compareTo(b2);
            }
        });
    }
    private void makeListView(){

        listView = findViewById(R.id.leaderbd_list);

        customAdapter = new RequestList.CustomAdapter(RequestList.this, originalList);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                Toast.makeText(RequestList.this, "Goodbye Dave! Hello Steve!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class CustomAdapter extends BaseAdapter implements Filterable {

        BookFilter bookFilter;
        Context context;
        List <RequestBook> showList;

        CustomAdapter(Context context,List <RequestBook> items){
            this.context = context;
            this.showList = items;
            sortlist(showList);
        }

        private class ViewHolder{
            TextView bookName;
            TextView bookVote;
            ImageView image;
            Button btn_more;
            Button btn_email;
        }
        @Override
        public int getCount() {
            return showList.size();
        }

        @Override
        public Object getItem(int position) {
            return showList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return showList.get(position).hashCode();
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {


            View vi = view;
            final ViewHolder holder ;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final RequestBook myBook = showList.get(position);

            if(view==null){

                vi = inflater.inflate(R.layout.request_layout,null);
                holder = new ViewHolder();
                holder.bookName=  vi.findViewById(R.id.tbx_bookName);
                holder.bookVote = vi.findViewById(R.id.tbx_voteCount);
                holder.image =  vi.findViewById(R.id.ibnt_vote);
                holder.btn_email =vi.findViewById(R.id.btn_email);
                holder.image.setTag(position);
                holder.btn_more =  vi.findViewById(R.id.btn_more);
                vi.setTag(holder);
            }

            else{
                holder = (ViewHolder) vi.getTag();
            }

            holder.bookName.setText(myBook.getBookName());
            holder.bookVote.setText(myBook.getVote()+ "");

            holder.image.setImageResource(R.drawable.delete);

            holder.btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(RequestList.this)
                            .setTitle(myBook.getBookName())
                            .setMessage("Author: " + "\n" + myBook.getAuthor() + "\n" + "\nRequested by "+myBook.getEmail()).setNeutralButton("Close", null).show();
                }
            });

            holder.btn_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String[] TO =  myBook.getVotedby().split(",");
                    String[] CC = {""};

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);

                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");

                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Book request added");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "The book " + myBook.getBookName() + " by " + myBook.getAuthor() + " has been added to the library.");

                    try
                    {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    }
                    catch (android.content.ActivityNotFoundException ex)
                    {
                        Toast.makeText(RequestList.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }

                    requestName = myBook.getBookName();

                    String request = requestName;
                    BookRef.child("/Requests/").child(request).removeValue();

                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    requestName = myBook.getBookName();

                    final Dialog deletedialog = new Dialog(RequestList.this);
                    deletedialog.setContentView(R.layout.deleterequest);
                    deletedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView title = deletedialog.findViewById(R.id.title);
                    title.setText("Are you sure you want to delete this request?");
                    title.setTypeface(myTypeFace1);

                    Button yes= deletedialog.findViewById(R.id.yes);
                    Button no = deletedialog.findViewById(R.id.no);

                    yes.setTypeface(myTypeFace1);
                    no.setTypeface(myTypeFace1);
                    deletedialog.show();

                    yes.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            String request = requestName;
                            BookRef.child("/Requests/").child(request).removeValue();
                            deletedialog.dismiss();
                            Toast.makeText(RequestList.this,"Request Deleted",Toast.LENGTH_LONG).show();
                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            deletedialog.dismiss();
                        }
                    });
                }
            });

            return vi;
        }

        @Override
        public Filter getFilter() {
            if (bookFilter == null)
                bookFilter = new BookFilter();
            return bookFilter;
        }

        class BookFilter extends Filter{

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // We implement here the filter logic
                if (constraint == null || constraint.length() < 1) {
                    // No filter implemented we return all the list
                    showList = originalList;
                    results.values = showList;
                    results.count = showList.size();
                }
                else {
                    // We perform filtering operation
                    List<RequestBook> nBookList = new ArrayList<>();

                    for (RequestBook b : originalList) {
                        if (b.getBookName().toUpperCase()
                                .contains(constraint.toString().toUpperCase())) {
                            nBookList.add(b);
                        }
                        else if (b.getAuthor().toUpperCase()
                                .contains(constraint.toString().toUpperCase())) {
                            nBookList.add(b);
                        }
                    }
                    showList = nBookList;
                    results.values = nBookList;
                    results.count = nBookList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                // Now we have to inform the adapter about the new list filtered
                if(results.count ==0) {
                    notifyDataSetInvalidated();
                }
                else
                {
                    showList = (List<RequestBook>) results.values;
                    notifyDataSetChanged();
                }
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent intent = new Intent( this, ContentsActivity.class);
        startActivity(intent);
    }
}