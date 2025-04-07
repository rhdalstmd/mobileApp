package com.example.randomnumbergame;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int randomNum = new Random().nextInt(100)+1;
    private TextView result;
    private EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Guess(View view){
        result = findViewById(R.id.result);
        input = findViewById(R.id.number);
        int n = Integer.parseInt(input.getText().toString());
        if (randomNum == n){
            result.setText("성공");
        }
        else{
            if(randomNum > n){
                result.setText("High");
            }
            else{
                result.setText("low");
            }
        }
    }
}
