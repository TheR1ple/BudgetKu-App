package com.example.budgetkuapp;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Pengeluaran> pengeluaranList;
    private DataAdapter pengeluaranAdapter;
    private DataHelper dataHelper;
    private TextView textBudgetDashboard, textPengeluaranDashboard, textSisaDashboard, textDashboardUsername;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Ensure that you are using the correct ID for findViewById
        recyclerView = view.findViewById(R.id.recyclerViewDashboard);
        textBudgetDashboard = view.findViewById(R.id.textBudgetDashboard);
        textPengeluaranDashboard = view.findViewById(R.id.textPengeluaranDashboard);
        textSisaDashboard = view.findViewById(R.id.textSisaDashboard);
        textDashboardUsername = view.findViewById(R.id.textDashboardUsername);

        // Initialize pengeluaranList
        pengeluaranList = new ArrayList<>();

        // Initialize the adapter
        pengeluaranAdapter = new DataAdapter(pengeluaranList);

        // Set up the RecyclerView with a LinearLayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pengeluaranAdapter);

        // Call the RefreshList method to populate the RecyclerView
        RefreshList();
        return view;
    }


    public void RefreshList() {
        // Get the pengeluaran data for the current user
        DataHelper dataHelper = new DataHelper(getActivity());
        // Assuming you have a method to get the current user ID
        int userId = dataHelper.getCurrentUserId();
        Cursor cursor = dataHelper.getUserPengeluaran(userId);
        Log.d("RefreshList", "Number of rows in cursor: " + cursor.getCount());

        // Extract data from the cursor and populate the pengeluaranList
        pengeluaranList.clear(); // Clear the existing data
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);

            // Check if the column index is valid
            int pengeluaranIdIndex = cursor.getColumnIndex("pengeluaran_id");
            int userIdIndex = cursor.getColumnIndex("user_id");
            int imagePathIndex = cursor.getColumnIndex("image_path");
            int jumlahIndex = cursor.getColumnIndex("jumlah");
            int kategoriIndex = cursor.getColumnIndex("kategori");

            if (pengeluaranIdIndex != -1 && userIdIndex != -1 && jumlahIndex != -1 && kategoriIndex != -1) {
                // Create a Pengeluaran object and add it to the list
                Pengeluaran pengeluaran = new Pengeluaran(
                        cursor.getInt(pengeluaranIdIndex),
                        cursor.getInt(userIdIndex),
                        cursor.getDouble(jumlahIndex),
                        cursor.getString(kategoriIndex),
                        cursor.getString(imagePathIndex)
                );

                pengeluaranList.add(pengeluaran);
                cursor.moveToNext();

                Log.d("RefreshList", "Item " + cc + ": " + pengeluaran.toString());
            } else {
                // Handle the case where one of the columns is not found
                Log.e("RefreshList", "One or more columns not found in cursor");
            }
        }

        Log.d("RefreshList", "Populated list: " + pengeluaranList);

        // Notify the adapter about the data change
        pengeluaranAdapter.notifyDataSetChanged();

        String username = dataHelper.getCurrentUsername();
        if (username != null) {
            // Set the username to textDashboardUsername
            textDashboardUsername.setText(username);
        }

        // Get the budget data for the current user
        Cursor budgetCursor = dataHelper.getUserBudget(userId);
        double budgetAmount = 0;

        if (budgetCursor.moveToFirst()) {
            int budgetAmountIndex = budgetCursor.getColumnIndex("totalbudget");

            // Check if the column index is valid
            if (budgetAmountIndex >= 0) {
                budgetAmount = budgetCursor.getDouble(budgetAmountIndex);
                textBudgetDashboard.setText("Rp." + String.valueOf(budgetAmount));
            } else {
                // Handle the case where the column index is -1 (column not found)
                Log.e("RefreshList", "getColumnIndex returned -1 for column 'totalbudget'");
            }
        }

        // Get the total pengeluaran for the current user
        Cursor pengeluaranCursor = dataHelper.getUserPengeluaran(userId);
        double totalPengeluaran = 0;

        if (pengeluaranCursor.moveToFirst()) {
            int jumlahIndex = pengeluaranCursor.getColumnIndex("jumlah");

            do {
                // Check if the column index is valid
                if (jumlahIndex >= 0) {
                    totalPengeluaran += pengeluaranCursor.getDouble(jumlahIndex);
                } else {
                    // Handle the case where the column index is -1 (column not found)
                    Log.e("RefreshList", "getColumnIndex returned -1 for column 'jumlah'");
                }
            } while (pengeluaranCursor.moveToNext());
        }

        textPengeluaranDashboard.setText("Rp." + String.valueOf(totalPengeluaran));

        // Calculate and display sisa
        double sisa = budgetAmount - totalPengeluaran;
        textSisaDashboard.setText("Rp." + String.valueOf(sisa));
    }

}
