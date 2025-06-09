package com.example.videolistapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ListView 초기화
        listView = findViewById(R.id.listView);
        videoList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoList);
        listView.setAdapter(adapter);

        // 권한 확인 및 요청
        checkPermission();
    }

    private void checkPermission() {
        String permission;

        // Android 13 이상에서는 READ_MEDIA_VIDEO 권한 사용
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_VIDEO;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "동영상 목록을 표시하기 위해 저장소 권한이 필요합니다.",
                    Toast.LENGTH_LONG).show();

            // 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    PERMISSION_REQUEST_CODE);
        } else {
            // 권한이 있는 경우 동영상 로드
            loadVideos();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 승인된 경우 동영상 로드
                loadVideos();
            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "저장소 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadVideos() {
        // 이미지에서 제공된 핵심 코드
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        // Cursor 생성 - 동영상 제목만 가져오기
        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // 제목 컬럼 인덱스 가져오기
            int titleIndex = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);

            do {
                // 동영상 제목 가져오기
                String title = cursor.getString(titleIndex);

                // 리스트에 추가
                videoList.add(title);

            } while (cursor.moveToNext());

            // Cursor 닫기
            cursor.close();

            // 어댑터에 변경사항 알림
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "총 " + videoList.size() + "개의 동영상을 찾았습니다.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "동영상을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
