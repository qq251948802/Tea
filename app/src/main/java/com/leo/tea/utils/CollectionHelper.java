package com.leo.tea.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by my on 2016/11/14.
 */

public class CollectionHelper extends SQLiteOpenHelper {
    public CollectionHelper(Context context) {
        super(context, "work.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table collection(_id varchar primary key  not null" +
                ",title varchar" +
                ",source varchar" +
                ",description varchar" +
                ",wap_thumb varchar" +
                ",create_time varchar" +
                ",nickname varchar" +
                " )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
