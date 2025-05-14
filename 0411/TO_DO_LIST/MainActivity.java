package com.example.todolist;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private LinearLayout container;
    private EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);
        text = findViewById(R.id.todo);
    }

    public void insertCheckBox(View view){
        CheckBox checkBox = new CheckBox(MainActivity.this);
        String checkBoxText = text.getText().toString();
        checkBox.setText(checkBoxText);

        // 컨테이너에 체크박스 추가
        container.addView(checkBox);
        text.setText("");
    }
}
