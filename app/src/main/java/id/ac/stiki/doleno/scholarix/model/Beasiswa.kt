package id.ac.stiki.doleno.scholarix.model

import kotlinx.serialization.Serializable

@Serializable
data class Beasiswa(
    val id: Long = -1L,
    val link: String,
    val nama: String,
    val pendanaan: String?,
    val deadline: String? = "N/A",
    val degrees: List<String>,
    val lokasi: Lokasi,
    // apa bisa setelah dapat link, nama, pendanaan, deadline, degrees, dan lokasi
    // langsung tampilkan UI beasiswanya di homescreen
    // sisanya dibawah ini discraping setelahnya di latar belakang
    val institusi: String,
    val jumlah: String = "N/A",
    val umur: String = "Tidak ada batasan",
    val durasi: String = "N/A",
    val sertifikatBahasa: String = "N/A",
    val documents: List<String>,
    val benefits: List<String>,
    val email: String?,
    val phone: String?
)

@Serializable
data class Lokasi(
    val kota: String?,
    val negara: String
)

object DummyBeasiswa {
    val beasiswaList = listOf(
        Beasiswa(
            id = 0L,
            link = "",
            nama = "Beasiswa ABC INIlah beasiswa dengan nama panjang",
            pendanaan = "Fully Funded",
            degrees = listOf("S1", "S2", "S3"),
            lokasi = Lokasi("Jakarta", "Indonesia"),
            institusi = "Colombia Educational Institutes",
            umur = "Anda harus lebih tua dari 50 tahun",
            documents = listOf(
                "Recommendation letter (Academic)",
                "Curriculum Vitae (CV)",
                "Spanish Proficiency proof",
                "Copy of Passport"
            ),
            benefits = listOf(
                "Full tuition fee coverage (only for academic programs found in the catalog of this call)",
                "Grant of the sum equivalent to 3 Minimum Monthly Legal salaries. \$ 2,484,348 Colombian pesos.",
                "A one-time stipend for books and materials at the first of the programs: \$401.321 COP",
                "Coverage of Medical Insurance during studies in Colombia."
            ),
            durasi = "2 years or more",
            jumlah = "103 scholarships",
            sertifikatBahasa = "Varies",
            email = null,
            phone = ""
        ),
        Beasiswa(
            id = 1L,
            link = "",
            nama = "Beasiswa XYZ",
            pendanaan = "Partial Funded",
            deadline = "May 15, 2024",
            degrees = listOf("S2", "S3"),
            lokasi = Lokasi("Bandung", "Indonesia"),
            institusi = "Colombia Educational Institutes",
            jumlah = "120",
            documents = listOf(
                "Recommendation letter (Academic)",
                "Curriculum Vitae (CV)",
                "Spanish Proficiency proof",
                "Copy of Passport"
            ),
            benefits = listOf(
                "Full tuition fee coverage (only for academic programs found in the catalog of this call)",
                "Grant of the sum equivalent to 3 Minimum Monthly Legal salaries. \$ 2,484,348 Colombian pesos.",
                "A one-time stipend for books and materials at the first of the programs: \$401.321 COP",
                "Coverage of Medical Insurance during studies in Colombia."
            ),
            sertifikatBahasa = "TOEFL, IELTS",
            email = null,
            phone = ""
        ),
        Beasiswa(
            id = 2L,
            nama = "Beasiswa 123",
            pendanaan = "Fully Funded",
            deadline = "June 1, 2024",
            degrees = listOf("S1", "S2"),
            lokasi = Lokasi("Surabaya", "Indonesia"),
            institusi = "Colombia Educational Institutes",
            documents = listOf(
                "Recommendation letter (Academic)",
                "Curriculum Vitae (CV)",
                "Spanish Proficiency proof",
                "Copy of Passport"
            ),
            benefits = listOf(
                "Full tuition fee coverage (only for academic programs found in the catalog of this call)",
                "Grant of the sum equivalent to 3 Minimum Monthly Legal salaries. \$ 2,484,348 Colombian pesos.",
                "A one-time stipend for books and materials at the first of the programs: \$401.321 COP",
                "Coverage of Medical Insurance during studies in Colombia."
            ),
            email = null,
            phone = "",
            link = "",
        )
    )
}