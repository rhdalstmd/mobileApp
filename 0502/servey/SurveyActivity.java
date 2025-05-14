package com.example.survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SurveyActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup answerRadioGroup;
    private Button prevButton;
    private Button nextButton;
    private TextView progressTextView;

    // 질문 목록
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

    // 사용자 응답 저장 배열
    private int[] answers = new int[10];
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // UI 요소 초기화
        questionTextView = findViewById(R.id.questionTextView);
        answerRadioGroup = findViewById(R.id.answerRadioGroup);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        progressTextView = findViewById(R.id.progressTextView);

        // 첫 번째 질문 표시
        updateQuestion();

        // 이전 버튼 클릭 이벤트
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnswer();
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    updateQuestion();
                }
            }
        });

        // 다음/제출 버튼 클릭 이벤트
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SurveyActivity.this, "답변을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveAnswer();

                if (currentQuestionIndex < questions.length - 1) {
                    currentQuestionIndex++;
                    updateQuestion();
                } else {
                    // 모든 질문 완료 - 결과 페이지로 이동
                    Intent intent = new Intent(SurveyActivity.this, ResultActivity.class);
                    intent.putExtra("answers", answers);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // 현재 질문 표시 업데이트
    private void updateQuestion() {
        questionTextView.setText(questions[currentQuestionIndex]);
        progressTextView.setText((currentQuestionIndex + 1) + " / " + questions.length);

        // 이전에 저장된 답변이 있으면 표시
        answerRadioGroup.clearCheck();
        if (answers[currentQuestionIndex] > 0) {
            RadioButton radioButton = (RadioButton) answerRadioGroup.getChildAt(answers[currentQuestionIndex] - 1);
            if (radioButton != null) {
                radioButton.setChecked(true);
            }
        }

        // 첫 번째 질문이면 이전 버튼 비활성화
        prevButton.setEnabled(currentQuestionIndex > 0);

        // 마지막 질문이면 다음 버튼 텍스트 변경
        if (currentQuestionIndex == questions.length - 1) {
            nextButton.setText("제출");
        } else {
            nextButton.setText("다음");
        }
    }

    // 현재 답변 저장
    private void saveAnswer() {
        int selectedRadioButtonId = answerRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            int radioIndex = answerRadioGroup.indexOfChild(selectedRadioButton);
            answers[currentQuestionIndex] = radioIndex + 1; // 1~5 저장
        }
    }
}
