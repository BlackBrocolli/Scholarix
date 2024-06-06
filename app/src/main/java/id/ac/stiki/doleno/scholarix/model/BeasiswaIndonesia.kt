package id.ac.stiki.doleno.scholarix.model

data class BeasiswaIndonesia (
    var id: String = "",
    val link: String = "",
    val name: String = "",
    val caramendaftar: List<String> = listOf(""),
    val kontak: List<String> = listOf(""),
    val persyaratan: List<String> = listOf(""),
    val pilihan: List<String> = listOf(""),
    val tahapan: List<String> = listOf(""),
)