package com.example.budgetkuapp;

import android.app.DatePickerDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class PengeluaranFragment extends Fragment {

    private EditText editJumlahPengeluaran, editKategoriPengeluaran, editTanggalPengeluaran, editDeskripsiPengeluaran;
    private Button buttonSaveInput, buttonBackInput;
    private DataHelper dataHelper;
    private ImageView imagePengeluaran;
    private Button buttonChooseImage;
    private String imagePath;
    // Constants for image picker
    private static final int GALLERY_REQUEST_CODE = 1;
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
        imagePengeluaran = view.findViewById(R.id.imagePengeluaran);
        buttonChooseImage = view.findViewById(R.id.buttonChooseImage);

        buttonSaveInput = view.findViewById(R.id.buttonSaveInput);

        // Initialize DataHelper
        dataHelper = new DataHelper(getActivity());

        // Set up click listeners
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a dialog or launch an activity to choose an image
                // You can use Intent to launch image picker or camera app
                // For simplicity, let's assume you are using an image picker library
                launchImagePicker();
            }
        });
        buttonSaveInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToDatabase();
            }
        });

        // Set DatePicker dialog on editTanggalPengeluaran click
        editTanggalPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });

        return view;
    }

    private void saveDataToDatabase() {
        // Get the input values, including the image path
        String jumlah = editJumlahPengeluaran.getText().toString();
        String kategori = editKategoriPengeluaran.getText().toString();
        String tanggal = editTanggalPengeluaran.getText().toString();
        String deskripsi = editDeskripsiPengeluaran.getText().toString();

        // Validate input if needed

        // Check if an image is selected
        if (imagePath != null && !imagePath.isEmpty()) {
            // Get the user ID from your authentication system or session
            int userId = dataHelper.getCurrentUserId();
            boolean isInserted = dataHelper.insertPengeluaran(userId, Double.parseDouble(jumlah), kategori, tanggal, deskripsi, imagePath);

            // Check if data insertion was successful
            if (isInserted) {
                Toast.makeText(getActivity(), "Pengeluaran saved successfully", Toast.LENGTH_SHORT).show();
                // Clear input fields and reset image path
                clearInputFields();
                imagePath = null;
            } else {
                Toast.makeText(getActivity(), "Failed to save pengeluaran", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where no image is selected
            Toast.makeText(getActivity(), "Please choose an image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }

        return null;
    }

    private void clearInputFields() {
        editJumlahPengeluaran.getText().clear();
        editKategoriPengeluaran.getText().clear();
        editTanggalPengeluaran.getText().clear();
        editDeskripsiPengeluaran.getText().clear();
        imagePengeluaran.setImageResource(android.R.color.transparent);
    }

    private void launchImagePicker() {
        // Implement logic to launch image picker here
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    // Handle the result when an image is selected
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                // Handle the selected image from the gallery
                Uri selectedImageUri = data.getData();
                imagePengeluaran.setImageURI(selectedImageUri);
                imagePath = getPathFromUri(selectedImageUri);
                Log.d("Image Path", "Selected Image Path: " + imagePath);
            }
        }
    }
    public void showDatePicker(View view) {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // Format tanggal sesuai kebutuhan Anda
                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

                // Set tanggal yang dipilih ke dalam EditText
                editTanggalPengeluaran.setText(selectedDate);
            }
        }, year, month, day);

        // Tampilkan dialog tanggal
        datePickerDialog.show();
    }

    public static void hideKeyboard(Context context) {
        try {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if ((((Activity) context).getCurrentFocus() != null) && (((Activity) context).getCurrentFocus().getWindowToken() != null)) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context) {
        ((InputMethodManager) (context).getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
