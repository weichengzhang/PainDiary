package com.example.mobilepaindiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Moon-Ete
 * @CreateDate: 2021/5/9 9:59
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private final AtomicBoolean mPending = new AtomicBoolean(false);
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<? super T> observer) {
        super.observe(owner, t -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

//    @MainThread
//    public void setValue(@Nullable T t) {
//        mPending.set(true);
//        super.setValue(t);
//    }
//
//
//
//    /**
//     * Used for cases where T is Void, to make calls cleaner.
//     */
//    @MainThread
//    public void call() {
//        setValue(null);
//    }


    @Override
    public void postValue(T value) {
        mPending.set(true);
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }
}
