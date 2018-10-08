package com.example.xyzreader.data;

import android.arch.lifecycle.LiveData;

public interface BookItemRepository {

	LiveData<String> getBodyById(int id);

	LiveData<BookItem> getBookItemById(int id);

	LiveData<BookHeaderTuple> getHeaderTupleById(int id);

}
