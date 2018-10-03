package com.example.xyzreader.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.xyzreader.MyApplication;
import com.example.xyzreader.data.BookItemRepository;

import javax.inject.Inject;

/**
 * Created by Md. Sifat-Ul Haque on 5/26/2017.
 */
public class BookItemViewModel extends AndroidViewModel {

    private final BookItemRepository bookItemRepository;
    private LiveData<String> mLiveBody;

    @Inject
    public BookItemViewModel(Application application, BookItemRepository bookItemRepository) {
        super(application);
        this.bookItemRepository = bookItemRepository;
    }

    public LiveData<String> getBodyById(int id) {
        if (mLiveBody == null) {
            mLiveBody = bookItemRepository.getBodyById(id);
        }
        return mLiveBody;
    }

}
