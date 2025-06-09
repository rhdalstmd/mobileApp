package com.example.moviechart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {
    private EditText etTitle, etYear, etGenre, etDirector, etRating;
    private Button btnUpdate, btnDelete, btnBack;
    private DBHelper dbHelper;
    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        dbHelper = new DBHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etYear = findViewById(R.id.etYear);
        etGenre = findViewById(R.id.etGenre);
        etDirector = findViewById(R.id.etDirector);
        etRating = findViewById(R.id.etRating);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        currentMovie = new Movie();
        currentMovie.setId(getIntent().getIntExtra("movie_id", 0));
        currentMovie.setTitle(getIntent().getStringExtra("movie_title"));
        currentMovie.setYear(getIntent().getIntExtra("movie_year", 0));
        currentMovie.setGenre(getIntent().getStringExtra("movie_genre"));
        currentMovie.setDirector(getIntent().getStringExtra("movie_director"));
        currentMovie.setRating(getIntent().getDoubleExtra("movie_rating", 0.0));

        displayMovieInfo();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMovie();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmDialog();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayMovieInfo() {
        etTitle.setText(currentMovie.getTitle());
        etYear.setText(String.valueOf(currentMovie.getYear()));
        etGenre.setText(currentMovie.getGenre());
        etDirector.setText(currentMovie.getDirector());
        etRating.setText(String.valueOf(currentMovie.getRating()));
    }

    private void updateMovie() {
        String title = etTitle.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String genre = etGenre.getText().toString().trim();
        String director = etDirector.getText().toString().trim();
        String ratingStr = etRating.getText().toString().trim();

        if (title.isEmpty() || yearStr.isEmpty() || genre.isEmpty() ||
                director.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            double rating = Double.parseDouble(ratingStr);

            if (rating < 0 || rating > 10) {
                Toast.makeText(this, "평점은 0~10 사이의 값을 입력해주세요!", Toast.LENGTH_SHORT).show();
                return;
            }

            currentMovie.setTitle(title);
            currentMovie.setYear(year);
            currentMovie.setGenre(genre);
            currentMovie.setDirector(director);
            currentMovie.setRating(rating);

            int result = dbHelper.updateMovie(currentMovie);
            if (result > 0) {
                Toast.makeText(this, "영화 정보가 성공적으로 수정되었습니다!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "영화 정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "연도와 평점은 숫자로 입력해주세요!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("영화 삭제");
        builder.setMessage("정말로 이 영화를 삭제하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteMovie(currentMovie);
                Toast.makeText(MovieDetailActivity.this, "영화가 성공적으로 삭제되었습니다!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }
}
