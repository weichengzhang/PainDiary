package com.example.mobilepaindiary.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobilepaindiary.databinding.FragmentViewTask5Binding;
import com.example.mobilepaindiary.ui.chart.LocationPieActivity;
import com.example.mobilepaindiary.ui.chart.PainLineActivity;
import com.example.mobilepaindiary.ui.chart.StepsPieActivity;
import com.example.mobilepaindiary.ui.home.viewmodel.TasksViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ViewTask5Fragment extends Fragment {

    private FragmentViewTask5Binding binding;
    private TasksViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewTask5Binding.inflate(getLayoutInflater(), container, false);
        binding.btnLocationPie.setOnClickListener(v -> startActivity(new Intent(requireContext(), LocationPieActivity.class)));
        binding.btnStepsPie.setOnClickListener(v -> startActivity(new Intent(requireContext(), StepsPieActivity.class)));
        binding.btnPainLine.setOnClickListener(v -> startActivity(new Intent(requireContext(), PainLineActivity.class)));
        return binding.getRoot();
    }

}