package com.example.todolist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ArrayList<Task> tasks;
    private Context context;
    private Runnable onSaveCallback;

    public TaskAdapter(Context context, ArrayList<Task> tasks, Runnable onSaveCallback) {
        this.context = context;
        this.tasks = tasks;
        this.onSaveCallback = onSaveCallback;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textDueDate;
        ImageButton btnDelete;
        ImageButton btnEdit;

        public TaskViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxTask);
            textDueDate = itemView.findViewById(R.id.textDueDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(Task task) {
            checkBox.setText(task.getTitle());
            checkBox.setChecked(task.isDone());
            textDueDate.setText(task.getDueDate());
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        // 뷰홀더에 데이터 바인딩
        holder.bind(task);

        // 체크박스 리스너 설정
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isDone());
        holder.checkBox.setOnCheckedChangeListener((btn, isChecked) -> {
            task.setDone(isChecked);
            onSaveCallback.run();
        });

        // 날짜 클릭 리스너
        holder.textDueDate.setOnClickListener(v -> showDatePicker(task, holder.getAdapterPosition()));

        // 삭제 버튼 클릭 리스너
        holder.btnDelete.setOnClickListener(v -> confirmDelete(holder.getAdapterPosition()));

        // 연필 이미지 클릭 리스너 추가
        holder.btnEdit.setOnClickListener(v -> editTaskTitle(task, holder.getAdapterPosition())); // 제목 수정
    }



    // 날짜 선택 다이얼로그 표시
    private void showDatePicker(Task task, int position) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context,
                (view, year, month, day) -> {
                    String dateStr = year + "-" + (month + 1) + "-" + day;
                    task.setDueDate(dateStr);
                    notifyItemChanged(position);
                    onSaveCallback.run();
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    // 삭제 확인 다이얼로그
    private void confirmDelete(int position) {
        if (position != RecyclerView.NO_POSITION) {
            new AlertDialog.Builder(context)
                    .setTitle("삭제 확인")
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        deleteTask(position);
                    })
                    .setNegativeButton("아니오", null)
                    .show();
        }
    }

    // 삭제 처리 메서드
    private void deleteTask(int position) {
        if (position >= 0 && position < tasks.size()) {
            tasks.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, tasks.size());
            onSaveCallback.run();
        }
    }

    // 제목 수정 다이얼로그
    private void editTaskTitle(Task task, int position) {
        EditText input = new EditText(context);
        input.setText(task.getTitle());

        new AlertDialog.Builder(context)
                .setTitle("할 일 수정")
                .setView(input)
                .setPositiveButton("수정", (dialog, which) -> {
                    String newTitle = input.getText().toString().trim();
                    if (!newTitle.isEmpty()) {
                        task.setTitle(newTitle);
                        notifyItemChanged(position);
                        onSaveCallback.run();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // 전체 삭제 메서드
    public void clearAllTasks() {
        tasks.clear();
        notifyDataSetChanged();
        onSaveCallback.run();
    }

    // 효율적인 리스트 업데이트 (DiffUtil 사용)
    public void updateTasks(List<Task> newTasks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return tasks.size();
            }

            @Override
            public int getNewListSize() {
                return newTasks.size();
            }

            @Override
            public boolean areItemsTheSame(int oldPosition, int newPosition) {
                // 같은 아이템인지 확인 (여기서는 제목으로 비교)
                return tasks.get(oldPosition).getTitle().equals(newTasks.get(newPosition).getTitle());
            }

            @Override
            public boolean areContentsTheSame(int oldPosition, int newPosition) {
                Task oldTask = tasks.get(oldPosition);
                Task newTask = newTasks.get(newPosition);
                return oldTask.getTitle().equals(newTask.getTitle()) &&
                        oldTask.isDone() == newTask.isDone() &&
                        oldTask.getDueDate().equals(newTask.getDueDate());
            }
        });

        tasks.clear();
        tasks.addAll(newTasks);
        diffResult.dispatchUpdatesTo(this);
    }
}
