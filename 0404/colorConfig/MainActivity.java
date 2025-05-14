package com.example.layoutconfig;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int[] colors = {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.rgb(255, 165, 0),
            Color.argb(255, 128, 0, 128)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeColor(View view){
        Random random = new Random();
        int randomIndex = random.nextInt(colors.length); // 색상 배열 길이 내에서 랜덤 인덱스 생성
        View rootView = getWindow().getDecorView(); // 화면의 루트 뷰 가져오기
        rootView.setBackgroundColor(colors[randomIndex]); // 랜덤 색상으로 배경색 설정
    }
}
