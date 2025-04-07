package com.example.imagebutton;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    ImageButton leftButton, rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        leftButton = (ImageButton) findViewById(R.id.leftButton);
        rightButton = (ImageButton) findViewById(R.id.rightButton);
    }
    public void setImage1(View v){
        imageView.setImageResource(R.drawable.pic);
    }
    public void setImage2(View v){
        imageView.setImageResource(R.drawable.pic2);
    }
}
