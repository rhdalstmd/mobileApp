package com.example.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private int[] images;
    private String[] imageTexts = {"프론트엔드 제작한 음료구매 사이트입니다", "oracle 시간에 제작한 데이터베이스 자료입니다.", "사물인터넷 과제를 소개하는 html파일입니다"};

    public ViewPagerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        // 이미지 배열의 크기를 반환합니다.
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        // 현재 view와 object가 같은지 확인합니다.
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // LayoutInflater를 사용하여 뷰를 생성합니다.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_layout, container, false);

        // 레이아웃에서 ImageView와 TextView를 찾습니다.
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);

        // 이미지와 텍스트를 설정합니다.
        imageView.setImageResource(images[position]);
        textView.setText(imageTexts[position]);

        // 컨테이너에 뷰를 추가합니다.
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 컨테이너에서 뷰를 제거합니다.
        container.removeView((View) object);
    }
}
