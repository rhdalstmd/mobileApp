package com.example.moviechart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MovieDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MOVIES = "movies";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_YEAR = "year";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_DIRECTOR = "director";
    private static final String KEY_RATING = "rating";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_YEAR + " INTEGER,"
                + KEY_GENRE + " TEXT,"
                + KEY_DIRECTOR + " TEXT,"
                + KEY_RATING + " REAL" + ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_YEAR, movie.getYear());
        values.put(KEY_GENRE, movie.getGenre());
        values.put(KEY_DIRECTOR, movie.getDirector());
        values.put(KEY_RATING, movie.getRating());
        db.insert(TABLE_MOVIES, null, values);
        db.close();
    }

    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MOVIES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(0)));
                movie.setTitle(cursor.getString(1));
                movie.setYear(Integer.parseInt(cursor.getString(2)));
                movie.setGenre(cursor.getString(3));
                movie.setDirector(cursor.getString(4));
                movie.setRating(Double.parseDouble(cursor.getString(5)));
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movieList;
    }

    public int updateMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_YEAR, movie.getYear());
        values.put(KEY_GENRE, movie.getGenre());
        values.put(KEY_DIRECTOR, movie.getDirector());
        values.put(KEY_RATING, movie.getRating());
        return db.update(TABLE_MOVIES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(movie.getId())});
    }

    public void deleteMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, KEY_ID + " = ?",
                new String[]{String.valueOf(movie.getId())});
        db.close();
    }
}
