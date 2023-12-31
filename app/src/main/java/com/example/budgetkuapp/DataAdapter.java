package com.example.budgetkuapp;

import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

// DataAdapter.java
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private DashboardFragment dashboardFragment;
    private List<Pengeluaran> pengeluaranList;
    private static ItemClickListener itemClickListener;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;


    // Constructor
    public DataAdapter(List<Pengeluaran> pengeluaranList) {
        this.pengeluaranList = pengeluaranList;
        this.dashboardFragment = dashboardFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to your views here
        Pengeluaran pengeluaran = pengeluaranList.get(position);
        if (pengeluaran.getImagePath() != null && !pengeluaran.getImagePath().isEmpty()) {
            // Gunakan Glide atau library lainnya untuk memuat gambar
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse("file://" + pengeluaran.getImagePath())) // Tambahkan "file://"
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Nonaktifkan cache sementara
                    .skipMemoryCache(true) // Lewati cache di memori
                    .into(holder.imageView);
        } else {
            // Jika tidak ada gambar, Anda mungkin ingin menampilkan placeholder
            holder.imageView.setImageResource(R.drawable.jos_gandos);
        }
        Log.d("DataAdapter", "ImagePath: " + pengeluaran.getImagePath());
        holder.pengeluaranIdTextView.setText(String.valueOf(pengeluaran.getPengeluaranId()));
        holder.jumlahTextView.setText("Rp. " + String.valueOf(pengeluaran.getJumlah()));
        holder.kategoriTextView.setText(pengeluaran.getKategori());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenu(holder.itemView, pengeluaran.getPengeluaranId());
            }
        });
    }
    @Override
    public int getItemCount() {
        return pengeluaranList.size();
    }

    private void showContextMenu(View view, final int pengeluaranId) {
        Log.d("showContextMenu", "Selected pengeluaranId: " + pengeluaranId);

        // Periksa izin READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, maka minta izin secara dinamis
            ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Izin sudah diberikan, lanjutkan dengan menampilkan konteks menu
            showPopupMenu(view, pengeluaranId);
        }
    }

    private void showPopupMenu(View view, final int pengeluaranId) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_detail) {
                    Intent detailIntent = new Intent(view.getContext(), DetailActivity.class);
                    detailIntent.putExtra("PENGELUARAN_ID", pengeluaranId);
                    view.getContext().startActivity(detailIntent);
                } else if (itemId == R.id.menu_update) {
                    Intent editIntent = new Intent(view.getContext(), EditDataActivity.class);
                    editIntent.putExtra("PENGELUARAN_ID", pengeluaranId);
                    view.getContext().startActivity(editIntent);
                } else if (itemId == R.id.menu_delete) {
                    DataHelper dataHelper = new DataHelper(view.getContext());
                    boolean isSuccess = dataHelper.deletePengeluaran(pengeluaranId);

                    if (isSuccess) {
                        Toast.makeText(view.getContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                        if (dashboardFragment != null) {
                            dashboardFragment.RefreshList();
                        } else {
                            Log.e("DataAdapter", "dashboardFragment is null");
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                    }
                    dataHelper.close();
                }
                return true;
            }
        });
        popupMenu.show();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pengeluaranIdTextView;
        public TextView jumlahTextView;
        public TextView kategoriTextView;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views here
            pengeluaranIdTextView = itemView.findViewById(R.id.pengeluaranIdTextView);
            jumlahTextView = itemView.findViewById(R.id.jumlahTextView);
            kategoriTextView = itemView.findViewById(R.id.kategoriTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    // Set ItemClickListener
    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
}

