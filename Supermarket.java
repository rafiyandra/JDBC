import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Supermarket {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Login
            if (!login(scanner)) {
                System.out.println("Login gagal! Program dihentikan.");
                return;
            }
            System.out.println("Login berhasil!");
            System.out.println("+----------------------------------------------------+");

            // Menu pilihan operasi
            while (true) {
                System.out.println("Pilih operasi:");
                System.out.println("1. Tambah Barang");
                System.out.println("2. Lihat Barang");
                System.out.println("3. Update Barang");
                System.out.println("4. Hapus Barang");
                System.out.println("5. Keluar");
                System.out.print("Masukkan pilihan: ");
                int pilihan = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (pilihan) {
                    case 1:
                        tambahBarang(scanner);
                        break;
                    case 2:
                        lihatBarang();
                        break;
                    case 3:
                        updateBarang(scanner);
                        break;
                    case 4:
                        hapusBarang(scanner);
                        break;
                    case 5:
                        System.out.println("Keluar dari program...");
                        return;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // Tambah Barang ke Database
    public static void tambahBarang(Scanner scanner) throws SQLException {
        System.out.print("Masukkan Kode Barang: ");
        String kodeBarang = scanner.nextLine();

        System.out.print("Masukkan Nama Barang: ");
        String namaBarang = scanner.nextLine();

        System.out.print("Masukkan Harga Barang: ");
        double hargaBarang = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Masukkan Jumlah Barang: ");
        int jumlahBarang = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "INSERT INTO barang (kode_barang, nama_barang, harga_barang, jumlah_barang) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, kodeBarang);
                stmt.setString(2, namaBarang);
                stmt.setDouble(3, hargaBarang);
                stmt.setInt(4, jumlahBarang);
                stmt.executeUpdate();
                System.out.println("Barang berhasil ditambahkan.");
                System.out.println("+----------------------------------------------------+\n");
            }
        }
    }

    // Lihat Barang dari Database
    public static void lihatBarang() throws SQLException {
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM barang";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    System.out.println("Kode Barang: " + rs.getString("kode_barang"));
                    System.out.println("Nama Barang: " + rs.getString("nama_barang"));
                    System.out.println("Harga Barang: " + rs.getDouble("harga_barang"));
                    System.out.println("Jumlah Barang: " + rs.getInt("jumlah_barang"));
                    System.out.println("+----------------------------------------------------+\n");
                }
            }
        }
    }

    // Update Barang di Database
    public static void updateBarang(Scanner scanner) throws SQLException {
        System.out.print("Masukkan Kode Barang yang ingin diubah: ");
        String kodeBarang = scanner.nextLine();

        System.out.print("Masukkan Nama Barang Baru: ");
        String namaBarang = scanner.nextLine();

        System.out.print("Masukkan Harga Barang Baru: ");
        double hargaBarang = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Masukkan Jumlah Barang Baru: ");
        int jumlahBarang = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "UPDATE barang SET nama_barang = ?, harga_barang = ?, jumlah_barang = ? WHERE kode_barang = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, namaBarang);
                stmt.setDouble(2, hargaBarang);
                stmt.setInt(3, jumlahBarang);
                stmt.setString(4, kodeBarang);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Barang berhasil diperbarui.");
                    System.out.println("+----------------------------------------------------+\n");
                } else {
                    System.out.println("Barang dengan kode tersebut tidak ditemukan.");
                    System.out.println("+----------------------------------------------------+\n");
                }
            }
        }
    }

    // Hapus Barang dari Database
    public static void hapusBarang(Scanner scanner) throws SQLException {
        System.out.print("Masukkan Kode Barang yang ingin dihapus: ");
        String kodeBarang = scanner.nextLine();

        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "DELETE FROM barang WHERE kode_barang = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, kodeBarang);
                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Barang berhasil dihapus.");
                } else {
                    System.out.println("Barang dengan kode tersebut tidak ditemukan.");
                }
            }
        }
    }

    // Login method (tidak berubah)
    public static boolean login(Scanner scanner) {
        String username = "Ganteng";
        String password = "098";
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.println("+----------------------------------------------------+");
            System.out.print("Username: ");
            String inputUsername = scanner.nextLine();
            System.out.print("Password: ");
            String inputPassword = scanner.nextLine();

            // Generate captcha
            String captcha = generateCaptcha(6);
            System.out.println("Captcha: " + captcha);
            System.out.print("Masukkan Captcha: ");
            String inputCaptcha = scanner.nextLine();
            System.out.println("+----------------------------------------------------+");

            // Validasi login
            if (inputUsername.equals(username) && inputPassword.equals(password) && inputCaptcha.equals(captcha)) {
                return true;
            } else {
                System.out.println("Login gagal! Silakan coba lagi.");
            }
        }

        return false; // Gagal login setelah beberapa percobaan
    }

    // Generate Captcha (tidak berubah)
    public static String generateCaptcha(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }

        return captcha.toString();
    }
}

class DatabaseHelper {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/supermarket";
        String user = "root";
        String password = ""; // Ganti dengan password MySQL Anda
        return DriverManager.getConnection(url, user, password);
    }
}
