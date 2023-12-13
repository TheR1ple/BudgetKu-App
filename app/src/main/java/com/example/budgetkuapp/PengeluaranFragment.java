package com.example.budgetkuapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class PengeluaranFragment extends Fragment {

    private EditText editJumlahPengeluaran, editKategoriPengeluaran, editTanggalPengeluaran, editDeskripsiPengeluaran;
    private Button buttonSaveInput, buttonBackInput;
    private DataHelper dataHelper;

    public PengeluaranFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengeluaran, container, false);

        // Initialize views
        editJumlahPengeluaran = view.findViewById(R.id.editJumlahPengeluaran);
        editKategoriPengeluaran = view.findViewById(R.id.editKategoriPengeluaran);
        editTanggalPengeluaran = view.findViewById(R.id.editTanggalPengeluaran);
        editDeskripsiPengeluaran = view.findViewById(R.id.editDeskripsiPengeluaran);

        buttonSaveInput = view.findViewById(R.id.buttonSaveInput);

        // Initialize DataHelper
        dataHelper = new DataHelper(getActivity());

        // Set up click listeners
        buttonSaveInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToDatabase();
            }
        });

        // You can add a click listener for the back button if needed

        return view;
    }

    private void saveDataToDatabase() {
        // Get the input values
        String jumlah = editJumlahPengeluaran.getText().toString();
        String kategori = editKategoriPengeluaran.getText().toString();
        String tanggal = editTanggalPengeluaran.getText().toString();
        String deskripsi = editDeskripsiPengeluaran.getText().toString();

        // Validate input if needed

        // Get the user ID from your authentication system or session
        int userId = dataHelper.getCurrentUserId();
        boolean isInserted = dataHelper.insertPengeluaran(userId, Double.parseDouble(jumlah), kategori, tanggal, deskripsi);

        // Check if data insertion was successful
        if (isInserted) {
            Toast.makeText(getActivity(), "Pengeluaran saved successfully", Toast.LENGTH_SHORT).show();
            // Clear input fields if needed
            clearInputFields();
        } else {
            Toast.makeText(getActivity(), "Failed to save pengeluaran", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        editJumlahPengeluaran.getText().clear();
        editKategoriPengeluaran.getText().clear();
        editTanggalPengeluaran.getText().clear();
        editDeskripsiPengeluaran.getText().clear();
    }
}
