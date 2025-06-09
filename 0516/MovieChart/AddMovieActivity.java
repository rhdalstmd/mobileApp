package com.example.moviechart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddMovieActivity extends AppCompatActivity {
    private EditText etTitle, etYear, etGenre, etDirector, etRating;
    private Button btnSave, btnCancel;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        dbHelper = new DBHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etYear = findViewById(R.id.etYear);
        etGenre = findViewById(R.id.etGenre);
        etDirector = findViewById(R.id.etDirector);
        etRating = findViewById(R.id.etRating);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMovie();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveMovie() {
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

            Movie movie = new Movie(title, year, genre, director, rating);
            dbHelper.addMovie(movie);

            Toast.makeText(this, "영화가 성공적으로 추가되었습니다!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "연도와 평점은 숫자로 입력해주세요!", Toast.LENGTH_SHORT).show();
        }
    }
}
