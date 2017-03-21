package com.example.alanflores.practice.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import com.example.alanflores.practice.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan.flores on 1/13/17.
 */

public class UserDataSource {
    private SQLiteDatabase database;
    private HelperUser helperUser;

    private String[] columnsTableUser = {
            HelperUser.TableUser.COLUMN_ID,
            HelperUser.TableUser.COLUMN_USERNAME,
            HelperUser.TableUser.COLUMN_PASSWORD
    };

    public UserDataSource(Context context) {
        helperUser = HelperUser.getInstance(context);
    }

    public void open(){
        database = helperUser.getWritableDatabase();
    }

    public void close(){
        helperUser.close();
    }

    public void insertUser(String name, String password){
        ContentValues values = new ContentValues();
        values.put(HelperUser.TableUser.COLUMN_USERNAME,name);
        values.put(HelperUser.TableUser.COLUMN_PASSWORD,password);

        database.insert(HelperUser.TableUser.TABLE, null, values);
    }

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        Cursor cursor = database.query(HelperUser.TableUser.TABLE,columnsTableUser, null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User user = parseCursorUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    public void deleteUsers(User user){
        database.delete(HelperUser.TableUser.TABLE, "1 = 1",null);
    }

    private User parseCursorUser(Cursor cursor){
        try {
            User user = new User();
            user.setId(Integer.toString(cursor.getInt(0)));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            return user;
        }catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            return null;
        }
    }

}
