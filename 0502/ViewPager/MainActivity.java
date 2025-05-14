package com.example.viewpager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewPager 초기화
        viewPager = findViewById(R.id.viewPager);

        // 어댑터 초기화
        adapter = new ViewPagerAdapter(this, images);

        // ViewPager에 어댑터 설정
        viewPager.setAdapter(adapter);
    }
}
