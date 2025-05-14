package com.example.todolist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {

    EditText editTextTask;
    Button buttonAdd;
    RecyclerView recyclerView;
    ArrayList<Task> taskList;
    TaskAdapter adapter;

    SharedPreferences sharedPreferences;
    Gson gson;

    // 백그라운드 작업을 위한 ExecutorService 추가
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        recyclerView = findViewById(R.id.recyclerView);

        // 한글 입력을 위한 IME 설정
        editTextTask.setOnEditorActionListener((v, actionId, event) -> {
            addTask();
            return true;
        });

        sharedPreferences = getSharedPreferences("ToDoListPrefs", MODE_PRIVATE);
        gson = new Gson();

        // 임시 빈 리스트로 초기화
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList, this::saveTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 백그라운드에서 데이터 로드
        loadTasksAsync();

        buttonAdd.setOnClickListener(v -> {
            addTask();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_clear_all) {
            showClearAllConfirmDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showClearAllConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("모든 할 일 삭제")
                .setMessage("정말 모든 할 일을 삭제하시겠습니까?")
                .setPositiveButton("예", (dialog, which) -> {
                    adapter.clearAllTasks();
                })
                .setNegativeButton("아니오", null)
                .show();
    }

    private void addTask() {
        String taskText = editTextTask.getText().toString().trim();
        if (!taskText.isEmpty()) {
            Task newTask = new Task(taskText, false, "날짜 없음");
            taskList.add(newTask);
            adapter.notifyItemInserted(taskList.size() - 1);
            editTextTask.setText("");

            // 백그라운드에서 저장
            saveTasksAsync();

            // 키보드 숨기기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextTask.getWindowToken(), 0);
        }
    }

    // 비동기로 저장 작업 수행
    private void saveTasksAsync() {
        executor.execute(() -> {
            try {
                String json = gson.toJson(taskList);
                sharedPreferences.edit().putString("taskList", json).apply();
            } catch (Exception e) {
                mainHandler.post(() ->
                        Toast.makeText(MainActivity.this, "저장 중 오류 발생: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    // saveTasks는 TaskAdapter에서 호출되는 메서드
    public void saveTasks() {
        saveTasksAsync();
    }

    // 비동기로 로드 작업 수행
    private void loadTasksAsync() {
        executor.execute(() -> {
            try {
                String json = sharedPreferences.getString("taskList", null);
                Type type = new TypeToken<ArrayList<Task>>() {}.getType();

                final ArrayList<Task> loadedTasks;
                if (json == null) {
                    loadedTasks = new ArrayList<>();
                } else {
                    ArrayList<Task> temp = gson.fromJson(json, type);
                    loadedTasks = (temp != null) ? temp : new ArrayList<>();
                }

                // UI 업데이트는 메인 스레드에서 수행
                mainHandler.post(() -> {
                    taskList.clear();
                    taskList.addAll(loadedTasks);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                mainHandler.post(() ->
                        Toast.makeText(MainActivity.this, "로드 중 오류 발생: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    @Override
    protected void onDestroy() {
        // 앱 종료시 스레드 풀 정리
        executor.shutdown();
        super.onDestroy();
    }
}
