package com.test.goal_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class TaskDataBaseHelper extends SQLiteOpenHelper {


    ////// Table name, and column names as final strings
    public static final String TASK_TABLE = "TASK_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
    public static final String COLUMN_LONG_DESCRIPTION = "LONG_DESCRIPTION";
    public static final String COLUMN_DEAD_LINE_DATE = "DEAD_LINE_DATE";
    public static final String COLUMN_IS_COMPLETED = "IS_COMPLETED";
    public static final String COLUMN_IS_DELETED = "IS_DELETED";
    public static final String COLUMN_CREATED_DATE = "CREATED_DATE";
    public static final String COLUMN_COMPETED_DATE = "COMPLETED_DATE";
    public static final String COLUMN_PARENT_TASK_ID = "PARENT_TASK_ID";



    public TaskDataBaseHelper(@Nullable Context context) {
        super(context, "tasks.db", null, 1);
    }


    //Creates database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String creatTableString = "CREATE TABLE " + TASK_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SHORT_DESCRIPTION + " TEXT, " +
                COLUMN_LONG_DESCRIPTION + " TEXT, " +
                COLUMN_DEAD_LINE_DATE + " TEXT, " +
                COLUMN_IS_COMPLETED + " BOOL, " +
                COLUMN_IS_DELETED + " BOOL, " +
                COLUMN_CREATED_DATE + " TEXT, " +
                COLUMN_COMPETED_DATE + " TEXT, " +
                COLUMN_PARENT_TASK_ID + " INTEGER,  " +
                "FOREIGN KEY(" + COLUMN_PARENT_TASK_ID + ") REFERENCES " + TASK_TABLE + "(" + COLUMN_ID + "))";


        db.execSQL(creatTableString);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Adds a new Task to database
    public boolean addOne(TaskModel newTask){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, newTask.getName());
        cv.put(COLUMN_SHORT_DESCRIPTION, newTask.getShortDescription());
        cv.put(COLUMN_LONG_DESCRIPTION, newTask.getLongDescription());
        cv.put(COLUMN_DEAD_LINE_DATE, newTask.getDeadlineDate());
        cv.put(COLUMN_IS_COMPLETED, newTask.getCompleted());
        cv.put(COLUMN_IS_DELETED, newTask.getDeleted());
        cv.put(COLUMN_CREATED_DATE, newTask.getCreatedDate());
        cv.put(COLUMN_COMPETED_DATE, newTask.getCompletedDate());
        cv.put(COLUMN_PARENT_TASK_ID, newTask.getParentTaskID());




        long insert = db.insert(TASK_TABLE, null, cv);

        db.close();

        if(insert == -1){
            return false;
        }
        else{
            return true;
        }
    }

    // Updates a task with the same id a newTask
    public void updateTask(TaskModel newTask){
        SQLiteDatabase db = this.getWritableDatabase();

        String updateTableString = "UPDATE " + TASK_TABLE +
                " SET " +
                COLUMN_NAME + " = \'" + newTask.getName() + "\', " +
                COLUMN_SHORT_DESCRIPTION + " = \'" + newTask.getShortDescription() + "\', " +
                COLUMN_LONG_DESCRIPTION + " = \'" + newTask.getLongDescription() + "\', " +
                COLUMN_DEAD_LINE_DATE + " = \'" + newTask.getDeadlineDate() + "\', " +
                COLUMN_IS_COMPLETED + " = " + newTask.getCompleted() + ", " +
                COLUMN_IS_DELETED + " = " + newTask.getDeleted() + ", " +
                COLUMN_CREATED_DATE + " = \'" + newTask.getCreatedDate() + "\', " +
                COLUMN_COMPETED_DATE + " = \'" + newTask.getCompletedDate() + "\', " +
                COLUMN_PARENT_TASK_ID + " = " + newTask.getParentTaskID() +
                " WHERE " + COLUMN_ID + " = " + newTask.getId() + ";";

        Cursor cursor = db.rawQuery(updateTableString, null);

        if(cursor.moveToFirst()){}

        db.close();
        cursor.close();
    }


    // returns ArrayList of all tasks
    public ArrayList<TaskModel> getEveryone() {
        String queryString = "SELECT * FROM " + TASK_TABLE;

        return getAllFromQueryString(queryString);

    }


    public ArrayList<TaskModel> getAllMain() {
        String queryString = "SELECT * FROM " + TASK_TABLE
                + " WHERE " + COLUMN_IS_COMPLETED + " = 0 AND " + COLUMN_IS_DELETED + " = 0 AND " + COLUMN_PARENT_TASK_ID + " = -1";

        return getAllFromQueryString(queryString);
    }

    public ArrayList<TaskModel> getAllSubtask(int parentID) {
        String queryString = "SELECT * FROM " + TASK_TABLE
                + " WHERE " + COLUMN_IS_COMPLETED + " = 0 AND " + COLUMN_IS_DELETED + " = 0 AND " + COLUMN_PARENT_TASK_ID + " = " + parentID;

        return getAllFromQueryString(queryString);
    }

    public ArrayList<TaskModel> getAllMainOnDate(String selectedDate) {
        String queryString = "SELECT * FROM " + TASK_TABLE
                + " WHERE " + COLUMN_IS_COMPLETED + " = 0 AND "
                + COLUMN_IS_DELETED + " = 0 AND "
                + COLUMN_DEAD_LINE_DATE + " = \'" + selectedDate + "\' AND "
                + COLUMN_PARENT_TASK_ID + " = -1";

        return getAllFromQueryString(queryString);
    }


    public ArrayList<TaskModel> getMainByMonth(String selectedMonth) {
        String queryString = "SELECT * FROM " + TASK_TABLE
                + " WHERE " + COLUMN_IS_COMPLETED + " = 0 AND "
                + COLUMN_IS_DELETED + " = 0 AND "
                + COLUMN_DEAD_LINE_DATE + " LIKE \'" + selectedMonth + "\' AND "
                + COLUMN_PARENT_TASK_ID + " = -1";

        return getAllFromQueryString(queryString);
    }


    public ArrayList<TaskModel> getAllDeleted() {
        String queryString = "SELECT * FROM " + TASK_TABLE
                + " WHERE " + COLUMN_IS_DELETED + " = 1";

        return getAllFromQueryString(queryString);
    }

    public ArrayList<TaskModel> getAllCompleted() {
        String queryString = "SELECT * FROM " + TASK_TABLE
                + " WHERE " + COLUMN_IS_COMPLETED + " = 1";

        return getAllFromQueryString(queryString);
    }


    @NonNull
    private ArrayList<TaskModel> getAllFromQueryString(String queryString) {
        ArrayList<TaskModel> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        TaskModel task;

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){

            do{
                task = getTaskFromCursor(cursor);

                returnList.add(task);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }




    // Gets from data base with id number
    public TaskModel getByID(int id){
        TaskModel returnTask = null;

        String queryString = "SELECT * FROM " + TASK_TABLE
                + " WHERE " + COLUMN_ID + " = " + id + ";";


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            returnTask = getTaskFromCursor(cursor);
        }

        cursor.close();
        db.close();

        return returnTask;
    }



    @NonNull
    private static TaskModel getTaskFromCursor(Cursor cursor) {
        int taskID, taskParentID;
        String taskName, taskShortDescription, taskLongDescription, taskDeadLine, taskCreatedDate, taskCompletedDate;
        Boolean taskIsCompleted, taskIsDeleted;

        taskID = cursor.getInt(0);
        taskName = cursor.getString(1);
        taskShortDescription = cursor.getString(2);
        taskLongDescription = cursor.getString(3);
        taskDeadLine = cursor.getString(4);
        taskIsCompleted = cursor.getInt(5) == 1 ? true : false;
        taskIsDeleted = cursor.getInt(6) == 1 ? true : false;
        taskCreatedDate = cursor.getString(7);
        taskCompletedDate = cursor.getString(8);
        taskParentID = cursor.getInt(9);

        TaskModel task = new TaskModel(taskID, taskName, taskShortDescription, taskLongDescription,
                taskDeadLine, taskIsCompleted, taskIsDeleted, taskCreatedDate, taskCompletedDate, taskParentID);
        return task;
    }

}
