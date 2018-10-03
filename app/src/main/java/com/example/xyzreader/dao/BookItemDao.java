package com.example.xyzreader.dao;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.example.xyzreader.data.BookHeaderTuple;
import com.example.xyzreader.data.BookItem;

@Dao
public interface BookItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleBookItems (@NonNull List<BookItem> bookItemList);
    @Query("SELECT * from bookItems where id = :id")
    @NonNull BookItem fetchOneBookItembyId (int id);
    @Query("SELECT body from bookItems where id = :id")
    @NonNull
    LiveData<String> fetchBodyById (int id);
    @Query("SELECT author, photo, published_date, thumb, title from bookItems where id = :id")
    @NonNull
    LiveData<BookHeaderTuple> fetchBookHeaderTupleById (int id);
    @Query("SELECT * from bookItems ORDER BY id ASC")
    @NonNull List<BookItem> fetchAllBookItems();
}
