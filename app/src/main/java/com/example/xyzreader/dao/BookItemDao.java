package com.example.xyzreader.dao;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.example.xyzreader.data.BookItem;

@Dao
public interface BookItemDao {

	@Query("SELECT * from bookItems ORDER BY id ASC")
	@NonNull
	List<BookItem> fetchAllBookItems();

	@Query("SELECT * from bookItems ORDER BY id ASC")
	@NonNull
	LiveData<List<BookItem>> fetchAllLiveBookItems();

	@Query("SELECT * from bookItems where id = :id")
	@NonNull
	LiveData<BookItem> fetchOneBookItembyId(int id);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertMultipleBookItems(@NonNull List<BookItem> bookItemList);
}
