
package com.example.todolist;

public class Task {
    private String title;
    private boolean isDone;
    private String dueDate; // yyyy-MM-dd

    public Task(String title, boolean isDone, String dueDate) {
        this.title = title;
        this.isDone = isDone;
        this.dueDate = dueDate;
    }

    public String getTitle() { return title; }
    public boolean isDone() { return isDone; }
    public String getDueDate() { return dueDate; }

    public void setTitle(String title) { this.title = title; }
    public void setDone(boolean done) { isDone = done; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
}
