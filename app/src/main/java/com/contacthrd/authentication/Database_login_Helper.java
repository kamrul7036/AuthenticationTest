package com.contacthrd.authentication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Database_login_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userdetails.db";
    private static final String TABLE_NAME = "user_details";
    private static final String ID = "Id";
//    private static final String NAME = "Name";
//    private static final String EMAIL = "Email";
    private static final String USER_NAME = "Username";
    private static final String PASSWORD = "Password";
    private static final int VERSION_NUMBER = 2;
    private Context context;
    private static  final String CREATE_TABLE = "CREATE  TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+USER_NAME+" TEXT NOT NULL,"+PASSWORD+" TEXT NOT NULL);";
    private  static final String DROP_TABLE = " DROP TABLE IF EXISTS "+TABLE_NAME;

    public Database_login_Helper(Context context) {
        super(context,DATABASE_NAME,null, VERSION_NUMBER);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context,"onCreate is Called",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(context,"Exception: "+e,Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            Toast.makeText(context,"onUpgrade is Called",Toast.LENGTH_LONG).show();
            db.execSQL(DROP_TABLE);
            onCreate(db);


        }catch (Exception e){
            Toast.makeText(context,"Exception: "+e,Toast.LENGTH_LONG).show();
        }

    }


    public long insertData(User_Details_loginPage  userDetails_loginPage){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME,userDetails_loginPage.getUsername());
        contentValues.put(PASSWORD,userDetails_loginPage.getPassword());


        long rowId = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return rowId;

    }

    public  boolean findPassword(String uname,String pass){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        boolean result = false;

        if(cursor.getCount() == 0){
            Toast.makeText(context,"No data is found!",Toast.LENGTH_LONG).show();
        }
        else{
            while (cursor.moveToNext()){
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                if(username.equals(uname)&& password.equals(pass)){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}



