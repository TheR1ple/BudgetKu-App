package com.example.budgetkuapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class BudgetFragment extends Fragment {
    private EditText editJumlahBudget;
    private Button buttonSaveInput;
    private DataHelper dataHelper;

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        editJumlahBudget = view.findViewById(R.id.editJumlahBudget);
        buttonSaveInput = view.findViewById(R.id.buttonSaveInput);
        dataHelper = new DataHelper(getActivity());

        // Set up click listeners
        buttonSaveInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToDatabase();
            }
        });
        return view;
    }

    private void saveDataToDatabase() {
        String budgetValue = editJumlahBudget.getText().toString();

        int userId = dataHelper.getCurrentUserId();

        boolean isInserted = dataHelper.insertBudget(userId, Double.parseDouble(budgetValue));

        // Check if data insertion was successful
        if (isInserted) {
            Toast.makeText(getActivity(), "Budget saved successfully", Toast.LENGTH_SHORT).show();
            // Clear input fields if needed
            clearInputFields();
        } else {
            Toast.makeText(getActivity(), "Failed to save budget", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        // Clear input field
        editJumlahBudget.getText().clear();
    }
}
