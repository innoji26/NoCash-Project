package com.beranidigital.nocash.constants

import com.beranidigital.nocash.models.TextModel

class PrivacyPolicy {

    val data: List<TextModel> = arrayListOf(
        TextModel(
            "Informasi yang Kami Kumpulkan", arrayListOf(
                "Informasi Pribadi: Nama, alamat email, nomor telepon, dan informasi lain yang Anda berikan saat mendaftar.",
                "Informasi Transaksi: Data mengenai hutang piutang, transaksi, dan detail terkait yang Anda catat di aplikasi.",
                "Informasi Teknis: Informasi tentang perangkat yang Anda gunakan, seperti jenis perangkat, sistem operasi, alamat IP, dan log penggunaan aplikasi."
            )
        ),
        TextModel(
            "Penggunaan Informasi", arrayListOf(
                "Penyediaan Layanan: Mengelola dan mencatat transaksi hutang piutang Anda, mengirimkan pengingat, dan menyediakan laporan transaksi.",
                "Peningkatan Layanan: Menganalisis data penggunaan untuk meningkatkan fitur dan kinerja aplikasi.",
                "Komunikasi: Mengirimkan pemberitahuan, pembaruan, dan informasi penting terkait penggunaan aplikasi.",
                "Keamanan: Memastikan keamanan data Anda dan mencegah aktivitas yang tidak sah atau penipuan."
            )
        ),
        TextModel(
            "Berbagi Informasi", arrayListOf(
                "Pihak Ketiga Terpercaya: Kami dapat membagikan informasi Anda dengan penyedia layanan yang membantu kami dalam operasional aplikasi, seperti penyedia server dan layanan analitik.",
                "Kepatuhan Hukum: Kami dapat mengungkapkan informasi Anda jika diperlukan oleh hukum atau untuk memenuhi permintaan pemerintah yang sah."
            )
        ),
        TextModel(
            "Penyimpanan dan Keamanan", arrayListOf(
                "Keamanan Data: Kami menggunakan langkah-langkah keamanan teknis dan organisasi untuk melindungi informasi Anda dari akses, penggunaan, atau pengungkapan yang tidak sah.",
                "Penyimpanan Data: Informasi Anda disimpan di server yang aman dan hanya diakses oleh staf yang berwenang."
            )
        ),
        TextModel(
            "Hak Pengguna", arrayListOf(
                "Akses dan Pembaruan: Anda memiliki hak untuk mengakses, memperbarui, atau menghapus informasi pribadi Anda yang tersimpan di aplikasi.",
                "Penarikan Persetujuan: Anda dapat menarik persetujuan Anda atas penggunaan data pribadi Anda kapan saja dengan menutup akun Anda."
            )
        ),
        TextModel(
            "Perubahan Kebijakan Privasi", arrayListOf(
                "Kami dapat memperbarui Kebijakan Privasi ini dari waktu ke waktu. Kami akan memberitahukan Anda tentang perubahan signifikan melalui aplikasi atau email.",
            )
        ),
        TextModel(
            "Hubungi Kami", arrayListOf(
                "Jika Anda memiliki pertanyaan atau kekhawatiran mengenai Kebijakan Privasi ini, silakan hubungi kami di:",
                "Email: support@nocash.com ",
                "Alamat: Jl. Contoh No. 123, Jakarta, Indonesia"
            )
        ),
    )
}