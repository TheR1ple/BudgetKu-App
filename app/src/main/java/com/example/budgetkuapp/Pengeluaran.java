package com.example.budgetkuapp;

// Pengeluaran.java

public class Pengeluaran {
    private int pengeluaranId;   // Unique identifier for the pengeluaran entry
    private double jumlah;        // Amount of the pengeluaran
    private String kategori;      // Category of the pengeluaran

    // Constructors

    public Pengeluaran() {
        // Default constructor
    }

    public Pengeluaran(int pengeluaranId, int userId, double jumlah, String kategori) {
        this.pengeluaranId = pengeluaranId;
        this.jumlah = jumlah;
        this.kategori = kategori;
    }

    // Getters and Setters

    public int getPengeluaranId() {
        return pengeluaranId;
    }

    public void setPengeluaranId(int pengeluaranId) {
        this.pengeluaranId = pengeluaranId;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    // Additional methods if needed
}

