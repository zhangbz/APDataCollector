package com.example.janiszhang.apdatacollector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by janiszhang on 2016/3/8.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{

    public static final String CREATE_TABLE = "create table apdata(" +
            "id integer primary key autoincrement, " +
            "ap_x integer, " +
            "ap_y integer, " +
            "ap_macaddr text, " +
            "ap_level1 integer," +
            "ap_level2 integer," +
            "ap_level3 integer," +
            "ap_name text)";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Toast.makeText(mContext, "Create table succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
