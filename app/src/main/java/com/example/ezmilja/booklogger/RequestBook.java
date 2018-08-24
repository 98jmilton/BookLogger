package com.example.ezmilja.booklogger;

import android.support.annotation.NonNull;

import static com.example.ezmilja.booklogger.SplashScreen.BookRef;

public class RequestBook implements Comparable{

        private final String bookName;
        private final String author;
        private int votes;
        private boolean isUpVoted;
        private final String email;
        private final  String votedby;

        RequestBook(final String bookName, final String author, final String email,
                    final int votes,final String votedby, final boolean isUpVoted){

            this.bookName =bookName;
            this.author = author;
            this.email = email;
            this.votes = votes;
            this.isUpVoted = isUpVoted;
            this.votedby = votedby;
        }


        public String getBookName(){return bookName;}
        public String getAuthor(){return author;}
        public String getEmail(){return email;}
        public int getVote(){return votes;}
        public String getVotedby() { return votedby ;}

        public boolean getisUpVoted(){return isUpVoted;}
        public void addVote(int add){votes += add; BookRef.child("/Requests/").child(bookName).child("votes").setValue(String.valueOf(votes));}
        public void setisUpVoted(boolean isUpVoted){this.isUpVoted = isUpVoted;}

        @Override
        public int compareTo(@NonNull Object o) {
            RequestBook b = (RequestBook) o;
            if (b.getVote() > votes) {
                return 1;
            }
            else if (b.getVote() < votes){
                return -1;
            }
            return 0;
        }
    }
