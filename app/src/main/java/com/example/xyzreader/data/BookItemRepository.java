package com.example.xyzreader.data;

import java.util.List;

import android.arch.lifecycle.LiveData;

public interface BookItemRepository {

	LiveData<List<BookItem>> getAllBookItems();

	LiveData<String> getBodyById(int id);

	LiveData<BookItem> getBookItemById(int id);

}
