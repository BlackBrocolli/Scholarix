package id.ac.stiki.doleno.scholarix.model

import java.time.LocalDate

data class Beasiswa(
    val nama: String,
    val pendanaan: String,
    val deadline: String = "N/A",
    val degree: List<String>,
    val lokasi: Lokasi
)

data class Lokasi(
    val kota: String,
    val negara: String
)

object DummyBeasiswa {
    val beasiswaList = listOf(
        Beasiswa(
            nama = "Beasiswa ABC INIlah beasiswa dengan nama panjang",
            pendanaan = "Fully Funded",
            degree = listOf("S1", "S2", "S3"),
            lokasi = Lokasi("Jakarta", "Indonesia")
        ),
        Beasiswa(
            nama = "Beasiswa XYZ",
            pendanaan = "Partial Funded",
            deadline = "May 15, 2024",
            degree = listOf("S2", "S3"),
            lokasi = Lokasi("Bandung", "Indonesia")
        ),
        Beasiswa(
            nama = "Beasiswa 123",
            pendanaan = "Fully Funded",
            deadline = "June 1, 2024",
            degree = listOf("S1", "S2"),
            lokasi = Lokasi("Surabaya", "Indonesia")
        )
    )
}