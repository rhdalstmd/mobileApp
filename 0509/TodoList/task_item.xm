<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
android:orientation="horizontal"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:padding="8dp"
android:gravity="center_vertical">

<CheckBox
    android:id="@+id/checkBoxTask"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:text="할 일" />

<TextView
    android:id="@+id/textDueDate"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="날짜 없음"
    android:textColor="#888888"
    android:paddingStart="8dp"
    android:paddingEnd="8dp" />

    <ImageButton
        android:id="@+id/btnEdit"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@android:drawable/ic_menu_edit"
        android:contentDescription="수정" />

<ImageButton
    android:id="@+id/btnDelete"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@android:drawable/ic_menu_delete"
    android:contentDescription="삭제" />
</LinearLayout>
