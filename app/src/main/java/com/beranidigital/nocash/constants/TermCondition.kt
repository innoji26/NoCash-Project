package com.beranidigital.nocash.constants

import com.beranidigital.nocash.models.TextModel

class TermCondition {

    val data: List<TextModel> = arrayListOf(
        TextModel(
            "Pendaftaran dan Akun", arrayListOf(
                "Akun Pengguna: Pengguna diharuskan untuk mendaftar dengan informasi yang akurat dan lengkap. Setiap pengguna bertanggung jawab atas keamanan akun mereka",
                "Satu Akun untuk Penjual dan Pembeli: Pengguna dapat menggunakan satu akun untuk berperan sebagai penjual dan pembeli, memungkinkan pencatatan dan pengelolaan transaksi hutang piutang dengan pihak lain."
            )
        ),
        TextModel(
            "Penggunaan Aplikasi", arrayListOf(
                "Fitur Pencatatan: Pengguna dapat mencatat hutang piutang, melihat riwayat transaksi, dan mendapatkan laporan terperinci.",
                "Pengingat Pembayaran: Aplikasi akan mengirimkan pengingat otomatis terkait hutang piutang yang harus dibayar atau diterima",
                "Akses Data: Pengguna dapat mengakses data mereka kapan saja melalui aplikasi Nocash.",
            )
        ),
        TextModel(
            "Kewajiban Pengguna",
            arrayListOf(
                "Kebenaran Informasi: Pengguna wajib memasukkan informasi yang benar dan akurat terkait transaksi hutang piutang.",
                "Keamanan Akun: Pengguna bertanggung jawab untuk menjaga kerahasiaan informasi login mereka dan segera melaporkan aktivitas mencurigakan kepada pihak Nocash.",
                "Kepatuhan Hukum: Pengguna setuju untuk menggunakan aplikasi sesuai dengan hukum dan peraturan yang berlaku",
            ),
        ),
        TextModel(
            "Privasi dan Keamanan", arrayListOf(
                "Perlindungan Data: Nocash berkomitmen untuk melindungi privasi dan data pengguna dengan langkah-langkah keamanan yang memadai.",
                "Penggunaan Data: Data pengguna akan digunakan hanya untuk keperluan pencatatan transaksi dan peningkatan layanan aplikasi.",
            )
        ),
        TextModel(
            "Pembatasan Tanggung Jawab", arrayListOf(
                "Kesalahan Data: Nocash tidak bertanggung jawab atas kesalahan atau ketidaktepatan informasi yang dimasukkan oleh pengguna.",
                "Kerugian Finansial: Nocash tidak bertanggung jawab atas kerugian finansial yang mungkin timbul akibat penggunaan aplikasi ini."
            )
        ),
        TextModel(
            "Pembaruan dan Perubahan", arrayListOf(
                "Pembaruan Aplikasi: Nocash berhak untuk melakukan pembaruan atau perubahan pada aplikasi, fitur, dan syarat dan ketentuan ini kapan saja.",
                "Pemberitahuan Perubahan: Pengguna akan diberitahu mengenai perubahan penting melalui notifikasi dalam aplikasi atau email."
            )
        ),
        TextModel(
            "Penutupan Akun", arrayListOf(
                "Penutupan oleh Pengguna: Pengguna dapat menutup akun mereka kapan saja melalui pengaturan aplikasi.",
                "Penutupan oleh Nocash: Nocash berhak menutup atau menangguhkan akun pengguna jika ditemukan pelanggaran terhadap syarat dan ketentuan ini."
            )
        ),
    )


}