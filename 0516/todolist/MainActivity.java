package com.example.todolist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnItemClickListener,
        TodoAdapter.OnStartDragListener, ItemMoveCallback.ItemTouchHelperContract {

    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<TodoItem> todoItems;
    private ItemTouchHelper touchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 데이터베이스 헬퍼 초기화
        dbHelper = new DatabaseHelper(this);

        // 리사이클러뷰 설정
        recyclerView = findViewById(R.id.todo_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 할 일 목록 로드
        loadTodoItems();

        // 어댑터 설정
        adapter = new TodoAdapter(this, todoItems, this, this);
        recyclerView.setAdapter(adapter);

        // 아이템 터치 헬퍼 설정 (드래그 앤 드롭)
        ItemMoveCallback callback = new ItemMoveCallback(this);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        // FAB 버튼 설정 (할 일 추가)
        FloatingActionButton fab = findViewById(R.id.fab_add_todo);
        fab.setOnClickListener(v -> showAddTodoDialog());
    }

    // 할 일 목록 로드
    private void loadTodoItems() {
        todoItems = dbHelper.getAllTodos();
    }

    // 할 일 추가 다이얼로그 표시
    private void showAddTodoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_todo, null);
        builder.setView(dialogView);

        EditText titleEditText = dialogView.findViewById(R.id.edit_todo_title);
        EditText descriptionEditText = dialogView.findViewById(R.id.edit_todo_description);
        TextView dueDateTextView = dialogView.findViewById(R.id.text_due_date);
        Button datePickerButton = dialogView.findViewById(R.id.btn_date_picker);

        // 날짜 선택 버튼 리스너
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        datePickerButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dueDateTextView.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // 다이얼로그 버튼 설정
        builder.setPositiveButton("추가", (dialog, which) -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String dueDate = dueDateTextView.getText().toString().trim();

            if (!title.isEmpty()) {
                TodoItem newItem = new TodoItem(title, description, dueDate);
                long id = dbHelper.addTodo(newItem);

                if (id != -1) {
                    // 목록 새로고침
                    loadTodoItems();
                    adapter.updateList(todoItems);
                    Toast.makeText(MainActivity.this, "할 일이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "할 일 추가에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 할 일 수정 다이얼로그 표시
    private void showEditTodoDialog(int position) {
        TodoItem item = todoItems.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_todo, null);
        builder.setView(dialogView);

        EditText titleEditText = dialogView.findViewById(R.id.edit_todo_title);
        EditText descriptionEditText = dialogView.findViewById(R.id.edit_todo_description);
        TextView dueDateTextView = dialogView.findViewById(R.id.text_due_date);
        Button datePickerButton = dialogView.findViewById(R.id.btn_date_picker);

        // 현재 값 설정
        titleEditText.setText(item.getTitle());
        descriptionEditText.setText(item.getDescription());
        dueDateTextView.setText(item.getDueDate());

        // 날짜 선택 버튼 리스너
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        datePickerButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dueDateTextView.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // 다이얼로그 버튼 설정
        builder.setPositiveButton("수정", (dialog, which) -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String dueDate = dueDateTextView.getText().toString().trim();

            if (!title.isEmpty()) {
                item.setTitle(title);
                item.setDescription(description);
                item.setDueDate(dueDate);

                int result = dbHelper.updateTodo(item);

                if (result > 0) {
                    recyclerView.post(() -> adapter.notifyItemChanged(position));  // ← 여기 수정!
                    Toast.makeText(MainActivity.this, "할 일이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "할 일 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // OnItemClickListener 구현
    @Override
    public void onItemClick(int position) {
        // 할 일 수정 다이얼로그 표시
        showEditTodoDialog(position);
    }

    @Override
    public void onDeleteClick(int position) {
        // 할 일 삭제 확인 다이얼로그
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("할 일 삭제");
        builder.setMessage("이 할 일을 삭제하시겠습니까?");
        builder.setPositiveButton("삭제", (dialog, which) -> {
            // 할 일 삭제
            TodoItem item = todoItems.get(position);
            dbHelper.deleteTodo(item.getId());
            todoItems.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(MainActivity.this, "할 일이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onCheckBoxClick(int position, boolean isChecked) {
        TodoItem item = todoItems.get(position);
        item.setCompleted(isChecked);
        dbHelper.updateTodo(item);

        // 안전하게 UI 업데이트
        recyclerView.post(() -> adapter.notifyItemChanged(position));
    }

    // OnStartDragListener 구현
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    // ItemTouchHelperContract 구현
    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        // 목록 순서 변경
        adapter.swapItems(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(TodoAdapter.TodoViewHolder myViewHolder) {
        // 드래그 시작 시 아이템 강조 효과
        myViewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.selected_background));
    }

    @Override
    public void onRowClear(TodoAdapter.TodoViewHolder myViewHolder) {
        // 드래그 종료 시 원래 효과로 복원
        myViewHolder.itemView.setBackgroundColor(getResources().getColor(android.R.color.white));

        // DB에 변경된 순서 저장
        dbHelper.updatePositions(adapter.getTodoItems());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 리소스 정리
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
