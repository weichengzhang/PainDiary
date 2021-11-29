package com.example.mobilepaindiary.ui.home.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.AlarmBroadcastReceiver;
import com.example.mobilepaindiary.R;
import com.example.mobilepaindiary.databinding.FragmentAddTask3Binding;
import com.example.mobilepaindiary.roomdb.PainRecord;
import com.example.mobilepaindiary.ui.home.viewmodel.TasksViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddTask3Fragment extends Fragment {

    private static final String TAG = "AddTask3Fragment";

    private FragmentAddTask3Binding binding;

    private int currentPainLocationPosition;
    private int currentPainMoodPosition;
    private int currentPainLevel;
    private String[] locations;
    private String[] moods;

    private TasksViewModel viewModel;

    private PainRecord todayPainRecord;

    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(requireContext());
        viewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
        locations = getResources().getStringArray(R.array.location);
        moods = getResources().getStringArray(R.array.mood);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTask3Binding.inflate(getLayoutInflater(), container, false);
        initView();
        viewModel.getTodayPainRecord();
        startObserve();
        return binding.getRoot();
    }

    private void initView() {
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = binding.inputTime.getHour();
                int min = binding.inputTime.getMinute();
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                sendAlarmEveryday(calendar);
                Toast.makeText(requireContext(), "Alarm ok", Toast.LENGTH_SHORT).show();
                binding.infoLayout.setVisibility(View.VISIBLE);
                binding.timeLayout.setVisibility(View.GONE);
            }
        });
        binding.spPainLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPainLocationPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spPainMood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPainMoodPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.sbPainLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentPainLevel = progress;
                binding.tvPainLevel.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.btnSave.setOnClickListener(v -> {
            String targetSteps = binding.etTargetSteps.getText().toString().trim();
            if (targetSteps.isEmpty()) {
                Toast.makeText(requireContext(), "Steps goal can`t be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            String currentSteps = binding.etCurrentSteps.getText().toString().trim();
            if (currentSteps.isEmpty()) {
                Toast.makeText(requireContext(), "Steps today can`t be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            PainRecord record = new PainRecord();
            record.userMail = currentUser.getEmail();
            record.targetSteps = targetSteps;
            record.currentSteps = currentSteps;
            record.painLevel = currentPainLevel;
            record.painLocation = currentPainLocationPosition;
            record.painMood = currentPainMoodPosition;
            record.currentTime = new Date();

            dialog.setMessage("Saving");
            if (!dialog.isShowing()) {
                dialog.show();
            }
            if (todayPainRecord != null) {
                record.id = todayPainRecord.id;
                record.currentTemperature = todayPainRecord.currentTemperature;
                record.currentHumidity = todayPainRecord.currentHumidity;
                record.currentPressure = todayPainRecord.currentPressure;
                viewModel.updatePainRecord(record);
            } else {
                viewModel.insertPainRecord(record);
            }

        });
        binding.btnEdit.setOnClickListener(v -> {
            openInput();
            binding.btnSave.setEnabled(true);
            binding.btnEdit.setEnabled(false);
        });
    }

    private void sendAlarmEveryday(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), AlarmBroadcastReceiver.class);
        intent.setAction("alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 120000, pendingIntent);
    }

    private void startObserve() {
        viewModel.insertPainRecordLiveDate.observe(getViewLifecycleOwner(), ids -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (ids.length != 0) {
                closeInput();
                binding.btnSave.setEnabled(false);
                binding.btnEdit.setEnabled(true);
                Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Saved failed", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.updatePainRecordLiveDate.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (integer != null && integer != 0) {
                    closeInput();
                    binding.btnSave.setEnabled(false);
                    binding.btnEdit.setEnabled(true);
                    Toast.makeText(requireContext(), "Modified successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Modified failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.todayPainRecord.observe(getViewLifecycleOwner(), painRecord -> {
            if (painRecord != null) {
                todayPainRecord = painRecord;
                currentPainLevel = todayPainRecord.painLevel;
                currentPainLocationPosition = todayPainRecord.painLocation;
                currentPainMoodPosition = todayPainRecord.painMood;
                closeInput();
                binding.sbPainLevel.setProgress(todayPainRecord.painLevel);
                binding.spPainLocation.setSelection(todayPainRecord.painLocation);
                binding.spPainMood.setSelection(todayPainRecord.painMood);
                binding.etTargetSteps.setText(todayPainRecord.targetSteps);
                binding.etCurrentSteps.setText(todayPainRecord.currentSteps);
                binding.btnSave.setEnabled(false);
                binding.btnEdit.setEnabled(true);
            }
        });
    }

    private void closeInput() {
        binding.spPainMood.setEnabled(false);
        binding.spPainLocation.setEnabled(false);
        binding.sbPainLevel.setEnabled(false);
        binding.etTargetSteps.setEnabled(false);
        binding.etCurrentSteps.setEnabled(false);
    }

    private void openInput() {
        binding.spPainMood.setEnabled(true);
        binding.spPainLocation.setEnabled(true);
        binding.sbPainLevel.setEnabled(true);
        binding.etTargetSteps.setEnabled(true);
        binding.etCurrentSteps.setEnabled(true);
    }
}