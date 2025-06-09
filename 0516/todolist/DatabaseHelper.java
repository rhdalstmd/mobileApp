package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // 데이터베이스 버전 및 이름
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TodoDatabase";

    // 테이블 이름
    private static final String TABLE_TODO = "todo_items";

    // 테이블 컬럼 이름
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DUE_DATE = "due_date";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_POSITION = "position";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성 쿼리
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DUE_DATE + " TEXT,"
                + KEY_COMPLETED + " INTEGER,"
                + KEY_POSITION + " INTEGER"
                + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블이 이미 존재할 경우 삭제하고 다시 생성
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    // 할 일 추가
    public long addTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (todoItem.getPosition() <= 0) {
            int count = getCount(db);  // 열어둔 DB를 넘김
            todoItem.setPosition(count + 1);
        }

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, todoItem.getTitle());
        values.put(KEY_DESCRIPTION, todoItem.getDescription());
        values.put(KEY_DUE_DATE, todoItem.getDueDate());
        values.put(KEY_COMPLETED, todoItem.isCompleted() ? 1 : 0);
        values.put(KEY_POSITION, todoItem.getPosition());

        long id = db.insert(TABLE_TODO, null, values);
        db.close();
        return id;
    }


    // 할 일 조회
    public TodoItem getTodo(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODO, new String[] {
                KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DUE_DATE, KEY_COMPLETED, KEY_POSITION
        }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        TodoItem todoItem = new TodoItem(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4) == 1,
                cursor.getInt(5)
        );

        cursor.close();
        db.close();
        return todoItem;
    }

    // 모든 할 일 조회
    public List<TodoItem> getAllTodos() {
        List<TodoItem> todoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TODO + " ORDER BY " + KEY_POSITION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TodoItem todoItem = new TodoItem(
                        cursor.getLong(0),  // id
                        cursor.getString(1), // title
                        cursor.getString(2), // description
                        cursor.getString(3), // due_date
                        cursor.getInt(4) == 1, // completed
                        cursor.getInt(5)    // position
                );
                todoList.add(todoItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return todoList;
    }

    // 할 일 업데이트
    public int updateTodo(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, todoItem.getTitle());
        values.put(KEY_DESCRIPTION, todoItem.getDescription());
        values.put(KEY_DUE_DATE, todoItem.getDueDate());
        values.put(KEY_COMPLETED, todoItem.isCompleted() ? 1 : 0);
        values.put(KEY_POSITION, todoItem.getPosition());

        // 해당 id의 할 일 업데이트
        int result = db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(todoItem.getId()) });
        db.close();
        return result;
    }

    // 할 일 삭제
    public void deleteTodo(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    // 할 일 갯수 조회
    public int getCount(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TODO, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    // 위치 업데이트 (드래그 앤 드롭)
    public void updatePositions(List<TodoItem> todoItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            for (int i = 0; i < todoItems.size(); i++) {
                TodoItem item = todoItems.get(i);
                item.setPosition(i + 1);

                ContentValues values = new ContentValues();
                values.put(KEY_POSITION, item.getPosition());

                db.update(TABLE_TODO, values, KEY_ID + " = ?",
                        new String[] { String.valueOf(item.getId()) });
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
