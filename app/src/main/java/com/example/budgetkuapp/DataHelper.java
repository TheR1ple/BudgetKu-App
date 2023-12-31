package com.example.budgetkuapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BudgetKu.db";
    private static final int DATABASE_VERSION = 3;

    // Table names
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_PENGELUARAN = "pengeluaran";
    private static final String TABLE_BUDGETS = "budgets";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";

    // LOGIN table column names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_SIGNED_IN = "is_signed_in";

    // PENGELUARAN table column names
    private static final String KEY_PENGELUARAN_ID = "pengeluaran_id";
    private static final String KEY_PENGELUARAN_AMOUNT = "jumlah";
    private static final String KEY_PENGELUARAN_CATEGORY = "kategori";
    private static final String KEY_PENGELUARAN_DATE = "tanggal";
    private static final String KEY_PENGELUARAN_DESCRIPTION = "deskripsi";
    private static final String KEY_PENGELUARAN_IMAGE_PATH = "image_path";

    // BUDGETS table column names
    private static final String KEY_BUDGET_ID = "budget_id";
    private static final String KEY_BUDGET_AMOUNT = "totalbudget";

    // CREATE TABLE statements
    private static final String CREATE_TABLE_LOGIN =
            "CREATE TABLE " + TABLE_LOGIN +
                    "(" + KEY_USER_ID + " INTEGER PRIMARY KEY, " +
                    KEY_USERNAME + " TEXT, " +
                    KEY_PASSWORD + " TEXT, " +
                    KEY_IS_SIGNED_IN + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_PENGELUARAN =
            "CREATE TABLE " + TABLE_PENGELUARAN +
                    "(" + KEY_PENGELUARAN_ID + " INTEGER PRIMARY KEY, " +
                    KEY_USER_ID + " INTEGER, " +
                    KEY_PENGELUARAN_AMOUNT + " REAL, " +
                    KEY_PENGELUARAN_CATEGORY + " TEXT, " +
                    KEY_PENGELUARAN_DATE + " TEXT, " +
                    KEY_PENGELUARAN_DESCRIPTION + " TEXT, " +
                    KEY_PENGELUARAN_IMAGE_PATH + " TEXT, " +
                    "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_LOGIN + "(" + KEY_USER_ID + "))";

    private static final String CREATE_TABLE_BUDGETS =
            "CREATE TABLE " + TABLE_BUDGETS +
                    "(" + KEY_BUDGET_ID + " INTEGER PRIMARY KEY, " +
                    KEY_USER_ID + " INTEGER, " +
                    KEY_BUDGET_AMOUNT + " REAL)";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create LOGIN, PENGELUARAN, and BUDGETS tables
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_PENGELUARAN);
        db.execSQL(CREATE_TABLE_BUDGETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENGELUARAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);

        onCreate(db);
    }



    // Original methods for sign up and sign in
    public Boolean insertData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USERNAME, username);
        contentValues.put(KEY_PASSWORD, password);
        long result = db.insert(TABLE_LOGIN, null, contentValues);
        return result != -1;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOGIN + " WHERE " + KEY_USERNAME + " = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public Boolean checkuserpass(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOGIN + " WHERE " + KEY_USERNAME + " = ? AND " + KEY_PASSWORD + " = ?", new String[]{username, password});

        if (cursor.getCount() > 0) {
            // User found, update the KEY_IS_SIGNED_IN column to 1
            ContentValues values = new ContentValues();
            values.put(KEY_IS_SIGNED_IN, 1);
            db.update(TABLE_LOGIN, values, KEY_USERNAME + " = ? AND " + KEY_PASSWORD + " = ?", new String[]{username, password});

            // Set KEY_IS_SIGNED_IN = 0 for other users
            db.execSQL("UPDATE " + TABLE_LOGIN + " SET " + KEY_IS_SIGNED_IN + " = 0 WHERE " + KEY_USERNAME + " <> ?", new String[]{username});

            // Close the cursor and database
            cursor.close();
            db.close();

            return true;
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return false;
    }


    public String getCurrentUsername() {
        SQLiteDatabase db = this.getReadableDatabase();
        String username = null;

        Cursor cursor = db.rawQuery("SELECT username FROM login WHERE is_signed_in = 1", null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("username");

            // Check if the column index is valid
            if (columnIndex >= 0) {
                username = cursor.getString(columnIndex);
            } else {
                // Handle the case where the column index is -1 (column not found)
                Log.e("DataHelper", "getColumnIndex returned -1 for column 'username'");
            }
        }

        cursor.close();
        db.close();

        return username;
    }

    public int getCurrentUserId() {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Default value if no user is signed in

        Cursor cursor = db.rawQuery("SELECT " + KEY_USER_ID + " FROM " + TABLE_LOGIN + " WHERE " + KEY_IS_SIGNED_IN + " = 1", null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(KEY_USER_ID);

            // Check if the column index is valid
            if (columnIndex >= 0) {
                userId = cursor.getInt(columnIndex);
            } else {
                // Handle the case where the column index is -1 (column not found)
                Log.e("DataHelper", "getColumnIndex returned -1 for column 'user_id'");
            }
        }

        cursor.close();
        db.close();

        return userId;
    }

    // Example method to insert a pengeluaran with user ID
    public Boolean insertPengeluaran(int userId, double amount, String category, String date, String description, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, userId);
        contentValues.put(KEY_PENGELUARAN_AMOUNT, amount);
        contentValues.put(KEY_PENGELUARAN_CATEGORY, category);
        contentValues.put(KEY_PENGELUARAN_DATE, date);
        contentValues.put(KEY_PENGELUARAN_DESCRIPTION, description);
        contentValues.put(KEY_PENGELUARAN_IMAGE_PATH, imagePath);  // Menambahkan path gambar
        long result = db.insert(TABLE_PENGELUARAN, null, contentValues);
        return result != -1;
    }

    // Example method to get pengeluaran for a specific user
    public Cursor getUserPengeluaran(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PENGELUARAN + " WHERE " + KEY_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        // Logging untuk melihat jumlah baris yang dikembalikan oleh cursor
        Log.d("DataHelper", "Number of rows returned by getUserPengeluaran: " + cursor.getCount());

        return cursor;
    }

    public Cursor getPengeluaranDetail(int pengeluaranId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PENGELUARAN + " WHERE " + KEY_PENGELUARAN_ID + " = ?", new String[]{String.valueOf(pengeluaranId)});
    }

    public boolean updatePengeluaran(int pengeluaranId, double jumlah, String kategori, String tanggal, String deskripsi, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PENGELUARAN_AMOUNT, jumlah);
        contentValues.put(KEY_PENGELUARAN_CATEGORY, kategori);
        contentValues.put(KEY_PENGELUARAN_DATE, tanggal);
        contentValues.put(KEY_PENGELUARAN_DESCRIPTION, deskripsi);
        contentValues.put(KEY_PENGELUARAN_IMAGE_PATH, imagePath);

        try {
            // Update pengeluaran based on pengeluaranId
            int rowsAffected = db.update(TABLE_PENGELUARAN, contentValues, KEY_PENGELUARAN_ID + "=?", new String[]{String.valueOf(pengeluaranId)});
            // Check if the update is successful
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public boolean deletePengeluaran(int pengeluaranId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsAffected = db.delete(TABLE_PENGELUARAN, KEY_PENGELUARAN_ID + "=?", new String[]{String.valueOf(pengeluaranId)});
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
    // Example method to insert a budget for a specific user
    public Boolean insertBudget(int userId, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, userId);
        contentValues.put(KEY_BUDGET_AMOUNT, amount);

        // Check if the budget entry for the user already exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDGETS + " WHERE " + KEY_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.getCount() > 0) {
            // Budget entry for the user already exists, perform UPDATE
            int updateResult = db.update(TABLE_BUDGETS, contentValues, KEY_USER_ID + " = ?", new String[]{String.valueOf(userId)});
            cursor.close();
            db.close();
            return updateResult != -1;
        } else {
            // Budget entry for the user does not exist, perform INSERT
            long insertResult = db.insert(TABLE_BUDGETS, null, contentValues);
            cursor.close();
            db.close();
            return insertResult != -1;
        }
    }

    // Example method to get the budget for a specific user
    public Cursor getUserBudget(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BUDGETS + " WHERE " + KEY_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    public void logout() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_SIGNED_IN, 0);
        db.update(TABLE_LOGIN, values, KEY_IS_SIGNED_IN + " = 1", null);
        db.close();
    }
}