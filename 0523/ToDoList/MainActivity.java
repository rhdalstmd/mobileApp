package com.example.todolist;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button btnAddTask, btnSetTime;
    private ListView listViewTasks;
    private TextView textViewSelectedTime;

    private ArrayList<String> taskList;
    private ArrayAdapter<String> adapter;
    private Calendar selectedDateTime;

    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    private static final int EXACT_ALARM_PERMISSION_CODE = 101;
    private static final String CHANNEL_ID = "TodoNotificationChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupTaskList();
        createNotificationChannel();
        requestNotificationPermission();
        checkExactAlarmPermission();

        btnSetTime.setOnClickListener(v -> showDateTimePicker());
        btnAddTask.setOnClickListener(v -> addTask());
    }

    private void initViews() {
        editTextTask = findViewById(R.id.editTextTask);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnSetTime = findViewById(R.id.btnSetTime);
        listViewTasks = findViewById(R.id.listViewTasks);
        textViewSelectedTime = findViewById(R.id.textViewSelectedTime);

        selectedDateTime = Calendar.getInstance();
    }

    private void setupTaskList() {
        taskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listViewTasks.setAdapter(adapter);

        // 항목 길게 누르면 삭제
        listViewTasks.setOnItemLongClickListener((parent, view, position, id) -> {
            taskList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "할일이 삭제되었습니다", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void showDateTimePicker() {
        Calendar currentDate = Calendar.getInstance();

        // 날짜 선택
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // 시간 선택
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (timeView, hourOfDay, minute) -> {
                                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDateTime.set(Calendar.MINUTE, minute);
                                selectedDateTime.set(Calendar.SECOND, 0);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                textViewSelectedTime.setText("알림 시간: " + sdf.format(selectedDateTime.getTime()));
                            },
                            currentDate.get(Calendar.HOUR_OF_DAY),
                            currentDate.get(Calendar.MINUTE),
                            true);
                    timePickerDialog.show();
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void addTask() {
        String task = editTextTask.getText().toString().trim();

        if (task.isEmpty()) {
            Toast.makeText(this, "할일을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if (textViewSelectedTime.getText().toString().equals("알림 시간을 설정하세요")) {
            Toast.makeText(this, "알림 시간을 설정하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 현재 시간보다 이전 시간인지 확인
        if (selectedDateTime.getTimeInMillis() <= System.currentTimeMillis()) {
            Toast.makeText(this, "미래 시간을 선택하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String taskWithTime = task + " (알림: " + sdf.format(selectedDateTime.getTime()) + ")";

        taskList.add(taskWithTime);
        adapter.notifyDataSetChanged();

        // 알림 설정
        setNotification(task, selectedDateTime.getTimeInMillis());

        // 입력 필드 초기화
        editTextTask.setText("");
        textViewSelectedTime.setText("알림 시간을 설정하세요");

        Toast.makeText(this, "할일이 추가되고 알림이 설정되었습니다", Toast.LENGTH_SHORT).show();
    }

    private void setNotification(String task, long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Android 12 이상에서 정확한 알람 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "정확한 알람 권한이 필요합니다. 설정에서 허용해주세요.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                return;
            }
        }

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("task", task);

        int requestCode = (int) System.currentTimeMillis(); // 고유한 요청 코드
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                }
            } catch (SecurityException e) {
                Toast.makeText(this, "알람 설정 권한이 없습니다. 설정에서 허용해주세요.", Toast.LENGTH_LONG).show();
                // 일반 알람으로 대체
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Todo 알림";
            String description = "Todo 알림 채널";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    private void checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "정확한 알람 권한을 허용해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "알림 권한이 허용되었습니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "알림 권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
