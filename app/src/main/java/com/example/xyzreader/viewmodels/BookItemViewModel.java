package com.example.xyzreader.viewmodels;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.xyzreader.MyApplication;
import com.example.xyzreader.data.BookItemRepository;

import javax.inject.Inject;

/**
 * Created by Md. Sifat-Ul Haque on 5/26/2017.
 */
public class BookItemViewModel extends AndroidViewModel {

    @Inject
    BookItemRepository bookItemRepository;
    private LiveData<String> mLiveBody;

    public BookItemViewModel(MyApplication application) {
        super(application);
        application.getApplicationComponent().inject(this);
    }

    public LiveData<String> getBodyById(int id) {
        if (mLiveBody == null) {
            mLiveBody = bookItemRepository.getBodyById(id);
        }
        return mLiveBody;
    }

}
