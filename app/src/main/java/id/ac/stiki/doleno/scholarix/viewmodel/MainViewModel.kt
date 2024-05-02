package id.ac.stiki.doleno.scholarix.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.model.DummyBeasiswa
import id.ac.stiki.doleno.scholarix.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

class MainViewModel : ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreen.Home)

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    // Tambahkan properti inputUrutkanBerdasar
    private val _inputUrutkanBerdasar: MutableState<String> = mutableStateOf("Deadline Terdekat")
    val inputUrutkanBerdasar: MutableState<String>
        get() = _inputUrutkanBerdasar

    fun setInputUrutkanBerdasar(value: String) {
        _inputUrutkanBerdasar.value = value
    }

    fun getABeasiswaById(id: Long): Beasiswa? {
        return DummyBeasiswa.beasiswaList.find { it.id == id }
    }

    // === WEB SCRAPING USING JSOUP ===
    // Metode untuk mengambil detail beasiswa di background thread
    fun fetchScholarshipDetails() {
        viewModelScope.launch {
            val baseUrl = "https://scholarshipsdb.org/"
            try {
                // Mengambil dokumen HTML dari halaman utama di IO Dispatcher
                val doc = withContext(Dispatchers.IO) {
                    Jsoup.connect(baseUrl).get()
                }

                // Ekstrak semua link dari elemen yang berisi link ke detail beasiswa
                val scholarships = doc.select("h2 a[href]")

                // Iterasi melalui setiap link dan akses halaman detail
                scholarships.forEach { element ->
                    val detailUrl = element.attr("abs:href")
                    Log.d("Link dari web", detailUrl)
                    fetchDetailPage(detailUrl)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchDetailPage(url: String) {
        viewModelScope.launch {
            try {
                // Menggunakan Dispatchers.IO untuk operasi I/O network
                val detailDoc = withContext(Dispatchers.IO) {
                    Jsoup.connect(url).get()
                }

                // Ekstraksi data seperti judul, deadline, dan deskripsi
                val titleElement = detailDoc.selectFirst("figcaption h2")
                val title = titleElement?.text()?.trim() ?: "Title not found"
                val fundingStatus =
                    detailDoc.select("figcaption span small a").first()?.text()?.trim()
                val applyBeforeInfo =
                    detailDoc.select("figcaption ul.jobsearch-jobdetail-options li:nth-child(3)")
                        .text().trim()
                // Menggunakan regex untuk mengekstrak tanggal
                val dateRegex = Regex("""Apply Before : (.+)""")
                val matchResult = dateRegex.find(applyBeforeInfo)
                val deadline = matchResult?.groupValues?.get(1)
                // Mengambil <div> yang mengandung teks 'Degree/Level'
                val degreesDiv = detailDoc.select("ul.jobsearch-row li").find { li ->
                    li.select("div.jobsearch-services-text span").text().contains("Degree/Level")
                }
                // Mengambil semua teks dari tag <small> di dalam div tersebut
                val degrees = degreesDiv?.select("small")?.map { it.text().trim() }
                // TODO ubah degrees ini menjadi S1, S2, S3, D3, D4 dsb
                /*
                * Bachelor -> S1
                * Undergraduate -> D3, D4, S1
                * Master -> S2
                * Postgraduate -> S2
                * Ph.D -> S3
                * Doctoral -> S3
                * */

                // Logging atau update UI di sini jika diperlukan
                Log.d(
                    "Scraping Result",
                    "Link: $url" +
                            "\nTitle: $title, " +
                            "\nPendanaan: $fundingStatus" +
                            "\nDeadline: $deadline" +
                            "\nDegrees: ${degrees?.joinToString(", ")}"
                )

                // TODO UBAH HASIL SCRAPING KE DALAM BENTUK JSON
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}