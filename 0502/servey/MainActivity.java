package com.example.survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleText = findViewById(R.id.titleText);
        TextView descriptionText = findViewById(R.id.descriptionText);
        Button startButton = findViewById(R.id.startButton);

        titleText.setText("휴대폰중독 설문조사");
        descriptionText.setText("휴대폰 사용 습관과 중독 정도를 알아보기 위한 설문조사입니다.\n" +
                "총 10개의 질문에 답변해 주세요.\n\n" +
                "각 질문에 1~5점 척도로 답변해 주세요.\n" +
                "1: 전혀 그렇지 않다\n" +
                "2: 그렇지 않다\n" +
                "3: 보통이다\n" +
                "4: 그렇다\n" +
                "5: 매우 그렇다");

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 설문조사 시작
                Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                startActivity(intent);
            }
        });
    }
}
