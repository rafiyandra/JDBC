class Transaksi extends Barang {
    private int jumlahBeli;
    private int total;

    public Transaksi(String kodeBarang, String namaBarang, int hargaBarang, int jumlahBeli) {
        super(kodeBarang, namaBarang, hargaBarang); // Memanggil konstruktor Barang
        if (jumlahBeli <= 0) {
            throw new IllegalArgumentException("Jumlah beli harus lebih dari 0.");
        }
        this.jumlahBeli = jumlahBeli;
        this.total = hitungTotal();
    }

    private int hitungTotal() {
        return this.hargaBarang * this.jumlahBeli;
    }

    public int getJumlahBeli() {
        return jumlahBeli;
    }

    public int getTotal() {
        return total;
    }
}