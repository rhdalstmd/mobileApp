package com.example.survey;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private LinearLayout resultsContainer;
    private BarChart barChart;
    private Button homeButton;

    // 질문 목록 (SurveyActivity와 동일)
    private String[] questions = {
            "1. 계획했던 것보다 더 오랜 시간 동안 휴대폰을 사용하게 된다.",
            "2. 휴대폰 사용 시간을 줄이려고 노력했지만 실패한 적이 있다.",
            "3. 휴대폰을 사용할 수 없을 때 불안하거나 초조해진다.",
            "4. 휴대폰이 옆에 없으면 불안하다.",
            "5. 수면 시간이 줄어들 정도로 휴대폰을 사용한다.",
            "6. 공부나 업무에 집중해야 할 때도 휴대폰을 자주 확인한다.",
            "7. 실제 대면 관계보다 휴대폰을 통한 소통을 더 편하게 느낀다.",
            "8. 휴대폰 사용으로 인해 일상생활에 문제가 생긴 적이 있다.",
            "9. 휴대폰을 사용하지 않을 때도 알림음이 울린 것 같은 착각을 경험한 적이 있다.",
            "10. 가족이나 친구들이 내가 휴대폰을 너무 많이 사용한다고 지적한 적이 있다."
    };

    // 각 점수별 응답 텍스트
    private String[] answerTexts = {
            "전혀 그렇지 않다",
            "그렇지 않다",
            "보통이다",
            "그렇다",
            "매우 그렇다"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultsContainer = findViewById(R.id.resultsContainer);
        barChart = findViewById(R.id.barChart);
        homeButton = findViewById(R.id.homeButton);
        TextView totalScoreTextView = findViewById(R.id.totalScoreTextView);

        // 설문 응답 데이터 가져오기
        int[] answers = getIntent().getIntArrayExtra("answers");

        if (answers != null) {
            // 총점 계산
            int totalScore = 0;
            for (int answer : answers) {
                totalScore += answer;
            }

            // 총점 결과 표시
            totalScoreTextView.setText("총점: " + totalScore + "/50");

            // 중독 수준 분석 및 표시
            TextView analysisTextView = findViewById(R.id.analysisTextView);
            String analysis;

            if (totalScore <= 15) {
                analysis = "휴대폰 사용이 매우 건전한 수준입니다.";
            } else if (totalScore <= 25) {
                analysis = "휴대폰 사용이 적절한 수준입니다.";
            } else if (totalScore <= 35) {
                analysis = "휴대폰 사용에 주의가 필요합니다.";
            } else if (totalScore <= 45) {
                analysis = "휴대폰 중독 경향이 있습니다. 사용 조절이 필요합니다.";
            } else {
                analysis = "심각한 휴대폰 중독 상태입니다. 전문가의 도움을 받아보세요.";
            }

            analysisTextView.setText(analysis);

            // 각 질문에 대한 응답 표시
            for (int i = 0; i < questions.length; i++) {
                TextView questionAnswerTextView = new TextView(this);
                questionAnswerTextView.setText(questions[i] + "\n답변: " + answerTexts[answers[i] - 1] + " (" + answers[i] + "점)");
                questionAnswerTextView.setPadding(20, 30, 20, 30);
                resultsContainer.addView(questionAnswerTextView);
            }

            // 그래프 데이터 준비
            setupBarChart(answers);
        }

        // 홈으로 버튼 클릭 이벤트
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    // 막대 그래프 설정
    private void setupBarChart(int[] answers) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // 응답 횟수 계산 (1점, 2점, 3점, 4점, 5점 각각 몇 번 선택했는지)
        int[] responseCounts = new int[5];
        for (int answer : answers) {
            responseCounts[answer - 1]++;
        }

        // 데이터 추가
        for (int i = 0; i < 5; i++) {
            entries.add(new BarEntry(i, responseCounts[i]));
            labels.add(answerTexts[i]);
        }

        // 차트 세팅
        BarDataSet dataSet = new BarDataSet(entries, "응답 분포");
        dataSet.setColors(new int[] {
                Color.rgb(65, 168, 121),  // 1점 (초록)
                Color.rgb(155, 222, 103), // 2점 (연한 초록)
                Color.rgb(255, 217, 102), // 3점 (노랑)
                Color.rgb(248, 156, 116), // 4점 (주황)
                Color.rgb(220, 57, 18)    // 5점 (빨강)
        });
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.7f);

        // X축 설정
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // Y축 설정
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        barChart.getAxisRight().setEnabled(false);

        // 차트 설정
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
