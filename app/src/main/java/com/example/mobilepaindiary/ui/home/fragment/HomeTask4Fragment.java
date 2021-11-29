package com.example.mobilepaindiary.ui.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mobilepaindiary.MainActivity;
import com.example.mobilepaindiary.MyApplication;
import static com.example.mobilepaindiary.MyApplication.getContext;
import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.databinding.FragmentHomeTask4Binding;
import com.example.mobilepaindiary.databinding.ItemHomeBinding;
import com.example.mobilepaindiary.roomdb.CustomerRepository;
import com.example.mobilepaindiary.roomdb.PainRecord;
import com.example.mobilepaindiary.ui.home.viewmodel.TasksViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeTask4Fragment extends Fragment {

    private static final String TAG = "HomeTask4Fragment";

    private FragmentHomeTask4Binding binding;

    private TasksViewModel viewModel;

    private ItemAdapter itemAdapter;

    public HomeTask4Fragment (){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        createWorkManager();
    }

    private void createWorkManager() {
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

        OneTimeWorkRequest dailyWorkRequest = new OneTimeWorkRequest.Builder(DailyWorker.class).setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).
                build();

        WorkManager.getInstance(MyApplication.getContext()).enqueue(dailyWorkRequest);
    }

//    public class DailyWorker extends Worker {
//        public DailyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//            super(context, workerParams);
//        }
//
//        @NonNull
//        @Override
//        public Result doWork() {
//            // 这个就是WorkManager的工作Log,如果想快速输出,继续往下看
//            Log.d("DailyWorker", "doWork()");
//
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            CustomerRepository customerRepository = new CustomerRepository(MyApplication.getApplication());
//            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//            LiveData<List<PainRecord>> currentUserAllPainRecord = customerRepository.getCurrentUserAllPainRecord(currentUser.getEmail());
//            List<PainRecord> painRecordList = currentUserAllPainRecord.getValue();
//
//            for (int i = 0; i < painRecordList.size(); i++) {
//                PainRecord painRecord = painRecordList.get(i);
//                db.collection("pain_diary").document(String.valueOf(painRecord.id))
//                        .set(painRecord)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d("collection", "success");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("collection", "failure");
//                            }
//                        });
//            }
//
//            Calendar currentDate = Calendar.getInstance();
//            Calendar dueDate = Calendar.getInstance();
//
//            // 设置在大约 22:00:00 PM 执行
//            dueDate.set(Calendar.HOUR_OF_DAY, 22);
//            dueDate.set(Calendar.MINUTE, 0);
//            dueDate.set(Calendar.SECOND, 0);
//
//            if (dueDate.before(currentDate)) {
//                dueDate.add(Calendar.HOUR_OF_DAY, 24);
//            }
//
//            long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();
//
//            OneTimeWorkRequest dailyWorkRequest = new OneTimeWorkRequest.Builder(DailyWorker.class)
//                    // 把这里的timeDiff换成int值,就代表延迟多少毫秒执行
//                    // 例如.setInitialDelay(1000, TimeUnit.MILLISECONDS)
//                    // 代表每隔1秒执行(1秒=1000毫秒),然后就会类似无限递归一样,一直执行这个doWork()方法,就可以在控制台看到输出日志了
//                    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
//                    .build();
//
//            WorkManager.getInstance(getContext())
//                    .enqueue(dailyWorkRequest);
//
//            return Result.success();
//        }
//    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeTask4Binding.inflate(getLayoutInflater(), container, false);
        itemAdapter = new ItemAdapter(requireContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(itemAdapter);
        viewModel.getCurrentUserAllPainRecord();
        startObserve();
        return binding.getRoot();
    }

    private void startObserve() {
        viewModel.allPainRecord.observe(getViewLifecycleOwner(), painRecords -> {
            if (!painRecords.isEmpty()) {
                MyApplication.setPainRecordList(painRecords);
                itemAdapter.setData(painRecords);
            }
        });
    }


    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private List<PainRecord> data = new ArrayList<>();
        private String[] locations;
        private String[] moods;
        private Context context;

        public ItemAdapter(Context context) {
            this.context = context;
            locations = context.getResources().getStringArray(R.array.location);
            moods = context.getResources().getStringArray(R.array.mood);
        }

        public void setData(List<PainRecord> list) {
            data.clear();
            data.addAll(list);
            notifyItemRangeChanged(0, data.size());
        }

        @NonNull
        @NotNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            ItemHomeBinding inflate = ItemHomeBinding.inflate(getLayoutInflater(), parent, false);
            return new ItemViewHolder(inflate.getRoot(), inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ItemViewHolder holder, int position) {
            PainRecord painRecord = data.get(position);
            holder.mail.setText(painRecord.userMail);
            holder.date.setText(new SimpleDateFormat("yyyy-MM-dd").format(painRecord.currentTime));
            holder.currentSteps.setText(painRecord.currentSteps);
            holder.targetSteps.setText(painRecord.targetSteps);
            holder.painMood.setText(moods[painRecord.painMood]);
            holder.painLocation.setText(locations[painRecord.painLocation]);
            holder.painLevel.setText(String.valueOf(painRecord.painLevel));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            public TextView mail;
            public TextView date;
            public TextView painLocation;
            public TextView painMood;
            public TextView painLevel;
            public TextView targetSteps;
            public TextView currentSteps;

            public ItemViewHolder(@NonNull @NotNull View itemView, ItemHomeBinding binding) {
                super(itemView);
                mail = binding.tvUser;
                date = binding.tvPainDate;
                painLocation = binding.tvPainLocation;
                painLevel = binding.tvPainLevel;
                painMood = binding.tvPainMood;
                targetSteps = binding.tvTargetSteps;
                currentSteps = binding.tvCurrentSteps;
            }
        }

    }
}