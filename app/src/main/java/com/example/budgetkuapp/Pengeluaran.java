package com.example.budgetkuapp;

// Pengeluaran.java

public class Pengeluaran {
    private int pengeluaranId;
    private int userId;
    private double jumlah;
    private String kategori;
    private String imagePath; // Tambahkan atribut ini

    // Constructors

    public Pengeluaran() {
        // Default constructor
    }

    public Pengeluaran(int pengeluaranId, int userId, double jumlah, String kategori) {
        this.pengeluaranId = pengeluaranId;
        this.userId = userId;
        this.jumlah = jumlah;
        this.kategori = kategori;
        this.imagePath = imagePath;
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
    public String getImagePath() {
        return imagePath;
    }

    // Additional methods if needed
}

