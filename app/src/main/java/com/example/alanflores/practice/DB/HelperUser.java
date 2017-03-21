package com.example.alanflores.practice.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alan.flores on 1/13/17.
 */

public class HelperUser extends SQLiteOpenHelper {
    private static HelperUser helperUser = null;

    private static final String NOMBRE_BD = "BD";
    private static final int VERSION_DB = 1;

    public static class TableUser{
        public static String TABLE = "User";
        public static String COLUMN_ID = "id";
        public static String COLUMN_USERNAME = "nombre";
        public static String COLUMN_PASSWORD = "apellido";
    }

    private static final String CREATE_TABLE = "create table "
            + TableUser.TABLE + "("
            + TableUser.COLUMN_ID + " integer primary key autoincrement, "
            + TableUser.COLUMN_USERNAME + " VARCHAR, "
            + TableUser.COLUMN_PASSWORD + " VARCHAR); ";

    private static final String DELETE_TABLE = "delete table if exists" + TableUser.TABLE;

    public static HelperUser getInstance(Context context){
        if(helperUser == null){
            helperUser = new HelperUser(context.getApplicationContext(),NOMBRE_BD,null,VERSION_DB);
        }
        return helperUser;
    }


    HelperUser(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
