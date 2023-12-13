package com.example.budgetkuapp;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.pengeluaranIdTextView.setText(String.valueOf(pengeluaran.getPengeluaranId()));
        holder.jumlahTextView.setText("Rp. " + String.valueOf(pengeluaran.getJumlah()));
        holder.kategoriTextView.setText(pengeluaran.getKategori());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenu(holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pengeluaranList.size();
    }

    private void showContextMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks here
                int itemId = item.getItemId();

                if (itemId == R.id.menu_detail) {
                    // Implement your logic to view details
                } else if (itemId == R.id.menu_update) {
                    // Implement your logic to update pengeluaran
                } else if (itemId == R.id.menu_delete) {
                    // Implement your logic to delete pengeluaran
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views here
            pengeluaranIdTextView = itemView.findViewById(R.id.pengeluaranIdTextView);
            jumlahTextView = itemView.findViewById(R.id.jumlahTextView);
            kategoriTextView = itemView.findViewById(R.id.kategoriTextView);
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
