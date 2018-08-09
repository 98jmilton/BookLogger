package com.example.ezmilja.booklogger;

public class Book {

    public String bookName;
    public String imageAddress;
    public String author;
    public String genre;

    Book(String bookName, String imageAddress, String author, String genre){

        this.bookName =bookName;
        this.imageAddress = imageAddress;
        this.author = author;
        this.author = genre;
    }

    public String getName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }
}