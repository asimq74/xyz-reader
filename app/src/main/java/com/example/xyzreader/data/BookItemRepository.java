package com.example.xyzreader.data;

import android.arch.lifecycle.LiveData;

public interface BookItemRepository {

	LiveData<String> getBodyById(int id);

}
