package com.example.opinionpoll;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioSelected;
    private RadioButton a233, a41, a44;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        radioSelected = findViewById(R.id.radioSelected);
    }

    public void setClick(View view) {
        int selectedId = radioSelected.getCheckedRadioButtonId();

        if (selectedId == R.id.a233)
            img.setImageResource(R.drawable.image0);
        else if (selectedId == R.id.a41)
            img.setImageResource(R.drawable.image1);
         else if (selectedId == R.id.a44)
             img.setImageResource(R.drawable.image2);
        else
            Toast.makeText(this, "라디오 버튼을 선택해주세요", Toast.LENGTH_SHORT).show();

    }
}
