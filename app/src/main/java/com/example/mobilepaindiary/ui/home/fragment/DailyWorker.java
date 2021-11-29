package com.example.mobilepaindiary.ui.home.fragment;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobilepaindiary.MyApplication;
import com.example.mobilepaindiary.roomdb.CustomerRepository;
import com.example.mobilepaindiary.roomdb.PainRecord;
import com.example.mobilepaindiary.ui.chart.LocationPieViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DailyWorker extends Worker {

    public DailyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // 这个就是WorkManager的工作Log,如果想快速输出,继续往下看
        Log.d("DailyWorker", "doWork()");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CustomerRepository customerRepository = new CustomerRepository(MyApplication.getApplication());
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        LiveData<List<PainRecord>> currentUserAllPainRecord = customerRepository.getCurrentUserAllPainRecord(currentUser.getEmail());
        List<PainRecord> painRecordList = MyApplication.getPainRecordList();
        //       LocationPieViewModel viewModel = new ViewModelProvider(this).get(LocationPieViewModel.class);

        for (int i = 0; i < painRecordList.size(); i++) {
            PainRecord painRecord = painRecordList.get(i);
            db.collection("pain_diary").document(String.valueOf(painRecord.id))
                    .set(painRecord)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("collection", "success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("collection", "failure");
                        }
                    });
        }

        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();

        // 设置在大约 22:00:00 PM 执行
        dueDate.set(Calendar.HOUR_OF_DAY, 22);
        dueDate.set(Calendar.MINUTE, 0);
        dueDate.set(Calendar.SECOND, 0);

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }

        long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();

        OneTimeWorkRequest dailyWorkRequest = new OneTimeWorkRequest.Builder(DailyWorker.class)
                // 把这里的timeDiff换成int值,就代表延迟多少毫秒执行
                // 例如.setInitialDelay(1000, TimeUnit.MILLISECONDS)
                // 代表每隔1秒执行(1秒=1000毫秒),然后就会类似无限递归一样,一直执行这个doWork()方法,就可以在控制台看到输出日志了
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(MyApplication.getContext())
                .enqueue(dailyWorkRequest);

        return Result.success();
    }
}
