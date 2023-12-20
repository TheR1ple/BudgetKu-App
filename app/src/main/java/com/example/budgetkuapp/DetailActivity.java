package com.example.budgetkuapp;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_PENGELUARAN_ID = "PENGELUARAN_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Inisialisasi komponen UI
        ImageView imageViewBukti = findViewById(R.id.imageDetailBukti);
        TextView textViewDeskripsi = findViewById(R.id.textDetailDeskripsi);
        TextView textViewJumlah = findViewById(R.id.textDetailJumlah);
        TextView textViewTanggal = findViewById(R.id.textDetailTanggal);
        TextView textViewKategori = findViewById(R.id.textDetailKategori);

        // Mendapatkan data dari Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int pengeluaranId = extras.getInt(EXTRA_PENGELUARAN_ID);
            DataHelper dataHelper = new DataHelper(this);
            Cursor cursor = dataHelper.getPengeluaranDetail(pengeluaranId);

            if (cursor.moveToFirst()) {
                int deskripsiIndex = cursor.getColumnIndex("deskripsi");
                int jumlahIndex = cursor.getColumnIndex("jumlah");
                int tanggalIndex = cursor.getColumnIndex("tanggal");
                int kategoriIndex = cursor.getColumnIndex("kategori");
                int imagePathIndex = cursor.getColumnIndex("image_path"); // Indeks path gambar

                // Check if the column indexes are valid
                if (deskripsiIndex >= 0 && jumlahIndex >= 0 && tanggalIndex >= 0 && kategoriIndex >= 0) {
                    String deskripsi = cursor.getString(deskripsiIndex);
                    double jumlah = cursor.getDouble(jumlahIndex);
                    String tanggal = cursor.getString(tanggalIndex);
                    String kategori = cursor.getString(kategoriIndex);
                    String imagePath = imagePathIndex >= 0 ? cursor.getString(imagePathIndex) : null;

                    cursor.close();

                    // Mengisi data ke dalam tampilan
                    if (imagePath != null) {
                        Glide.with(this).load(imagePath).into(imageViewBukti);
                    } else {
                        imageViewBukti.setImageResource(R.drawable.jos_gandos);
                    }

                    textViewDeskripsi.setText(deskripsi);
                    textViewJumlah.setText("Rp. " + String.format("%,.2f", jumlah));
                    textViewTanggal.setText(tanggal);
                    textViewKategori.setText(kategori);
                    Log.e("DetailActivity", "Brhasil Mnampilkan data");
                } else {
                    // Handle the case where one or more column indexes are -1 (column not found)
                    Log.e("DetailActivity", "Invalid column index");
                }
            } else {
                // Handle the case where the cursor is empty (no data found)
                Log.e("DetailActivity", "Cursor is empty");
            }
            dataHelper.close();
        }
    }
}

