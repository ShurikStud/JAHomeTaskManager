package com.example.shurik.jahometaskmanager.System;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shurik.jahometaskmanager.Users.CurrentUser;

public class MyDataBase extends SQLiteOpenHelper{

    private SQLiteDatabase sqLiteDatabase = null;

    private static final String DATABASE_NAME   = "MyDB";
    private static final int DATABASE_VERSION   = 5;

    private static final String TABLE_USER = "user";
    private static final String TABLE_CURRENT_USER = "current_user";

    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DESCRIPTION = "description";

    private String CREATE_USER_TABLE    = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT, "
            + KEY_DESCRIPTION + " TEXT"
            + ")";

    private static final String[] COLUMNS_USER = {KEY_ID, KEY_EMAIL, KEY_PASSWORD, KEY_DESCRIPTION};
    private static final String[] COLUMNS_CURRENT_USER = {KEY_EMAIL, KEY_PASSWORD, KEY_DESCRIPTION};

    private String CREATE_CURRENTUSER_TABLE    = "CREATE TABLE " + TABLE_CURRENT_USER + "("
            + KEY_EMAIL + " TEXT PRIMARY KEY, "
            + KEY_PASSWORD + " TEXT, "
            + KEY_DESCRIPTION + " TEXT"
            + ")";

    private String CREATE_MASTER_ACCOUNT = "INSERT INTO " + TABLE_USER
            + " (" + KEY_EMAIL + ", " + KEY_PASSWORD + ", " + KEY_DESCRIPTION + ") "
            + "VALUES ('admin@admin', 'master', 'служебный пользователь с админскими правами')";

    private String CREATE_DEMO_ACCOUNT1 = "INSERT INTO " + TABLE_USER
            + " (" + KEY_EMAIL + ", " + KEY_PASSWORD + ", " + KEY_DESCRIPTION + ") "
            + "VALUES ('user1@com', 'user1', 'демо-пользователь 1')";
    private String CREATE_DEMO_ACCOUNT2 = "INSERT INTO " + TABLE_USER
            + " (" + KEY_EMAIL + ", " + KEY_PASSWORD + ", " + KEY_DESCRIPTION + ") "
            + "VALUES ('user2@com', 'password', 'демо-пользователь 2')";

    public MyDataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CURRENTUSER_TABLE);
        db.execSQL(CREATE_MASTER_ACCOUNT);
        db.execSQL(CREATE_DEMO_ACCOUNT1);
        db.execSQL(CREATE_DEMO_ACCOUNT2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_USER);
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    public boolean isUserExist(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_USER, // имя таблицы
                        COLUMNS_USER, // массив имен колонок
                        KEY_EMAIL + " = ?", // условия
                        new String[]{name}, // массив аргументов
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null) {
            return true;
        }

        return false;
    }

    public boolean login(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_USER, // имя таблицы
                        COLUMNS_USER, // массив имен колонок
                        KEY_EMAIL + " = ? AND " + KEY_PASSWORD + " = ?", // условия
                        new String[]{name, password}, // массив аргументов
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null && cursor.getCount() == 1) {
            cursor.moveToFirst();
            CurrentUser currentUser = CurrentUser.getInstance();
            currentUser.init(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)), cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
            return true;
        }

        return false;
    }

    public boolean isExistCurrentUser(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_CURRENT_USER,
                        COLUMNS_CURRENT_USER,
                        null,
                        null,
                        null,
                        null,
                        null);

        if (cursor != null && cursor.getCount() == 1){
            cursor.moveToFirst();
            CurrentUser currentUser = CurrentUser.getInstance();
            currentUser.init(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)), cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
            return true;
        }

        return false;
    }

    public boolean saveCurrentUser(){
        ContentValues contentValues = new ContentValues();
        CurrentUser currentUser = CurrentUser.getInstance();

        contentValues.clear();
        contentValues.put(KEY_EMAIL, currentUser.getEmail());
        contentValues.put(KEY_PASSWORD, currentUser.getPassword());
        contentValues.put(KEY_DESCRIPTION, currentUser.getDescription());

        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("DELETE FROM " + TABLE_CURRENT_USER);
        db.insertOrThrow(TABLE_CURRENT_USER, null, contentValues);

        return false;
    }

}
