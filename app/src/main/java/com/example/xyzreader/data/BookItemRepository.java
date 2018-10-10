package com.example.xyzreader.data;

import java.util.List;

import android.arch.lifecycle.LiveData;

public interface BookItemRepository {

	LiveData<BookItem> getBookItemById(int id);

	LiveData<List<BookItem>> getAllBookItems();

}
