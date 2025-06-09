package com.example.moviechart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private ListView listView;
    private Button btnAddMovie;
    private List<Movie> movieList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> movieTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        listView = findViewById(R.id.listView);
        btnAddMovie = findViewById(R.id.btnAddMovie);

        btnAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie selectedMovie = movieList.get(position);
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra("movie_id", selectedMovie.getId());
                intent.putExtra("movie_title", selectedMovie.getTitle());
                intent.putExtra("movie_year", selectedMovie.getYear());
                intent.putExtra("movie_genre", selectedMovie.getGenre());
                intent.putExtra("movie_director", selectedMovie.getDirector());
                intent.putExtra("movie_rating", selectedMovie.getRating());
                startActivity(intent);
            }
        });

        loadMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();
    }

    private void loadMovies() {
        movieList = dbHelper.getAllMovies();
        movieTitles = new ArrayList<>();

        for (Movie movie : movieList) {
            movieTitles.add(movie.getTitle() + " (" + movie.getYear() + ")");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieTitles);
        listView.setAdapter(adapter);

        Toast.makeText(this, "총 " + movieList.size() + "개의 영화가 있습니다.", Toast.LENGTH_SHORT).show();
    }
}
