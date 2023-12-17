package com.example.budgetkuapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

// DataAdapter.java
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<Pengeluaran> pengeluaranList;
    private static ItemClickListener itemClickListener;

    // Constructor
    public DataAdapter(List<Pengeluaran> pengeluaranList) {
        this.pengeluaranList = pengeluaranList;
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
                    .load(Uri.parse(pengeluaran.getImagePath()))
                    .into(holder.imageView);
        } else {
            // Jika tidak ada gambar, Anda mungkin ingin menampilkan placeholder
            holder.imageView.setImageResource(R.drawable.jos_gandos);
        }
        holder.pengeluaranIdTextView.setText(String.valueOf(pengeluaran.getPengeluaranId()));
        holder.jumlahTextView.setText("Rp. " + String.valueOf(pengeluaran.getJumlah()));
        holder.kategoriTextView.setText(pengeluaran.getKategori());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Pengeluaran pengeluaran = pengeluaranList.get(position);
                    showContextMenu(holder.itemView, pengeluaran.getPengeluaranId());
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return pengeluaranList.size();
    }
    private static void showContextMenu(View view, final int pengeluaranId) {
        if (itemClickListener != null) {
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
                        // Implement your logic to delete pengeluaran
                    }

                    return true;
                }
            });

            popupMenu.show();
        }
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
    // Set ItemClickListener
    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
