package com.example.todolist;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<TodoItem> todoItems;
    private Context context;
    private OnItemClickListener listener;
    private OnStartDragListener dragListener;

    // 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onCheckBoxClick(int position, boolean isChecked);
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    // 생성자
    public TodoAdapter(Context context, List<TodoItem> todoItems, OnItemClickListener listener, OnStartDragListener dragListener) {
        this.context = context;
        this.todoItems = todoItems;
        this.listener = listener;
        this.dragListener = dragListener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem currentItem = todoItems.get(position);

        // 데이터 바인딩
        holder.titleTextView.setText(currentItem.getTitle());
        holder.dueDateTextView.setText(currentItem.getDueDate());
        holder.completeCheckBox.setChecked(currentItem.isCompleted());

        // 완료 상태에 따른 타이틀 스타일 변경
        if (currentItem.isCompleted()) {
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // 드래그 핸들 설정
        holder.dragHandle.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dragListener.onStartDrag(holder);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    // 아이템 순서 변경 메소드
    public void swapItems(int fromPosition, int toPosition) {
        TodoItem fromItem = todoItems.get(fromPosition);
        todoItems.remove(fromPosition);
        todoItems.add(toPosition, fromItem);
        notifyItemMoved(fromPosition, toPosition);
    }

    // 아이템 목록 업데이트
    public void updateList(List<TodoItem> newList) {
        this.todoItems = newList;
        notifyDataSetChanged();
    }

    // 현재 리스트 반환
    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    // 뷰홀더 클래스
    class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dueDateTextView;
        CheckBox completeCheckBox;
        ImageButton deleteButton;
        ImageButton dragHandle;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 연결
            titleTextView = itemView.findViewById(R.id.todo_title);
            dueDateTextView = itemView.findViewById(R.id.todo_due_date);
            completeCheckBox = itemView.findViewById(R.id.todo_checkbox);
            deleteButton = itemView.findViewById(R.id.todo_delete);
            dragHandle = itemView.findViewById(R.id.todo_drag_handle);

            // 클릭 리스너 설정
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(position);
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(position);
                }
            });

            completeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCheckBoxClick(position, isChecked);
                }
            });
        }
    }
}
