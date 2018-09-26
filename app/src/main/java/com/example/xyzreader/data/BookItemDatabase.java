package com.example.xyzreader.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.xyzreader.dao.BookItemDao;

@Database(entities = {BookItem.class}, version = 1, exportSchema = false)
public abstract class BookItemDatabase extends RoomDatabase {
    public abstract BookItemDao bookItemDao() ;
}
