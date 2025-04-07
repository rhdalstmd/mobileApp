package com.example.countor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private int count = 0;
    private TextView countNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countNumber = findViewById(R.id.textCount);
    }

    public void countUp(View view){
        count += 1;
        countNumber.setText("카운터:"+count);
    }

    public void countDown(View view){
        count -= 1;
        countNumber.setText("카운터:"+count);
    }

    public void reset(View view){
        count = 0;
        countNumber.setText("카운터:"+count);
    }
}
