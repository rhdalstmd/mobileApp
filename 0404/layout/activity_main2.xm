<?xml version="1.0" encoding="utf-8"?>
<TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TableRow
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight="0"
        android:gravity="center_horizontal">

        <ImageView
            android:layout_width="200dp"
            android:layout_height="200dp"
            android:layout_marginTop="100dp"
            android:src="@drawable/img" />
    </TableRow>

    <TableRow  android:layout_weight="0">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text=" 아이디"
            android:gravity="center_horizontal"/>

        <EditText

            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:hint="아이디 입력" />
    </TableRow>
    <TableRow android:layout_weight="0">
        <TextView
            android:layout_weight="1"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="비밀번호"
            android:gravity="center_horizontal"/>
        <EditText
            android:layout_weight="2"
            android:layout_width="200dp"
            android:layout_height="wrap_content"
            android:hint="비밀번호 입력"/>
    </TableRow>
    <TableRow  android:layout_weight="0">
        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="로그인"
            android:layout_marginHorizontal="50dp"/>

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="50dp"
            android:text="회원가입"/>
    </TableRow>
    <TableRow  android:layout_weight="1">
        <EditText
            android:gravity="top"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:inputType="textMultiLine"
            android:hint="여기에 텍스트를 입력하세요"/>
    </TableRow>
</TableLayout>
