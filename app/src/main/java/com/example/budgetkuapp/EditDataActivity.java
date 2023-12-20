package com.example.budgetkuapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import java.util.Calendar;

public class EditDataActivity extends AppCompatActivity {

    private EditText editJumlah, editKategori, editTanggal, editDeskripsi;
    private ImageView imageEditPengeluaran;
    private Button buttonChooseImage, buttonBackEditData, buttonSaveEditData;
    private int pengeluaranId;
    private String imagePath;
    private static final int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        // Inisialisasi komponen UI
        editJumlah = findViewById(R.id.editDetailJumlahPengeluaran);
        editKategori = findViewById(R.id.editDetailKategoriPengeluaran);
        editTanggal = findViewById(R.id.editDetailTanggalPengeluaran);
        editDeskripsi = findViewById(R.id.editDetailDeskripsiPengeluaran);
        imageEditPengeluaran = findViewById(R.id.imageEditPengeluaran);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        buttonBackEditData = findViewById(R.id.buttonBackEditData);
        buttonSaveEditData = findViewById(R.id.buttonSaveEditData);

        // Mendapatkan data dari Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pengeluaranId = extras.getInt("PENGELUARAN_ID");
            loadDataFromDatabase(pengeluaranId);
        }

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

        // Mengatur onClickListener untuk tombol Back
        buttonBackEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Mengatur onClickListener untuk tombol Simpan
        buttonSaveEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataInDatabase();
            }
        });

        editTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                // Format tanggal sesuai kebutuhan Anda
                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

                // Set tanggal yang dipilih ke dalam EditText
                editTanggal.setText(selectedDate);
            }
        }, year, month, day);

        // Tampilkan dialog tanggal
        datePickerDialog.show();
    }

    private void launchImagePicker() {
        // Implement logic to launch image picker here
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    // Handle the result when an image is selected
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                // Handle the selected image from the gallery
                Uri selectedImageUri = data.getData();
                imageEditPengeluaran.setImageURI(selectedImageUri);
                imagePath = getPathFromUri(selectedImageUri);
                Log.d("Image Path", "Selected Image Path: " + imagePath);
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(columnIndex);
            } finally {
                cursor.close();
            }
        }

        return null;
    }


    private void loadDataFromDatabase(int pengeluaranId) {
        DataHelper dataHelper = new DataHelper(this);
        Cursor cursor = dataHelper.getPengeluaranDetail(pengeluaranId);

        if (cursor.moveToFirst()) {
            int jumlahIndex = cursor.getColumnIndex("jumlah");
            int kategoriIndex = cursor.getColumnIndex("kategori");
            int tanggalIndex = cursor.getColumnIndex("tanggal");
            int deskripsiIndex = cursor.getColumnIndex("deskripsi");
            int imagePathIndex = cursor.getColumnIndex("image_path");

            if (jumlahIndex != -1 && kategoriIndex != -1 && tanggalIndex != -1 && deskripsiIndex != -1) {
                // Mengisi data ke dalam tampilan
                editJumlah.setText(String.valueOf(cursor.getDouble(jumlahIndex)));
                editKategori.setText(cursor.getString(kategoriIndex));
                editTanggal.setText(cursor.getString(tanggalIndex));
                editDeskripsi.setText(cursor.getString(deskripsiIndex));

                // Memuat gambar menggunakan Glide atau metode lainnya
                if (imagePathIndex != -1) {
                    imagePath = cursor.getString(imagePathIndex);
                    Glide.with(this).load(imagePath).into(imageEditPengeluaran);
                }
            }
        }
        cursor.close();
        dataHelper.close();
    }

    private void updateDataInDatabase() {
        DataHelper dataHelper = new DataHelper(this);
        boolean isSuccess = dataHelper.updatePengeluaran(pengeluaranId, Double.parseDouble(editJumlah.getText().toString()), editKategori.getText().toString(), editTanggal.getText().toString(), editDeskripsi.getText().toString(), imagePath);

        if (isSuccess) {
            Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
        }

        dataHelper.close();
    }
}
