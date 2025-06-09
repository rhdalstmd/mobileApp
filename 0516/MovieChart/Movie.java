package com.example.moviechart;

public class Movie {
    private int id;
    private String title;
    private int year;
    private String genre;
    private String director;
    private double rating;

    public Movie() {}

    public Movie(String title, int year, String genre, String director, double rating) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.director = director;
        this.rating = rating;
    }

    // Getter와 Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
