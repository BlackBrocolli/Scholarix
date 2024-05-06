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
import org.jsoup.nodes.Document
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
                /*
                    TODO ubah degrees ini menjadi S1, S2, S3, D3, D4 dsb
                * Bachelor -> S1
                * Undergraduate -> D3, D4, S1
                * Master -> S2
                * Postgraduate -> S2
                * Ph.D -> S3
                * Doctoral -> S3
                * */

                val location =
                    detailDoc.select("figcaption ul.jobsearch-jobdetail-options li").first()?.text()
                        ?.trim()?.replace(" View on Map", "")

                // Mengambil teks untuk "Host Institution" dari <li> yang spesifik
                val hostInstitutionText = detailDoc.select("li:contains(Host Institution)").text()
                // Menggunakan regex untuk mengambil hanya nama institusi sebelum tanda titik (jika ada)
                val regex = Regex("Host Institution:\\s*([^\\.]+)")
                val institutionName = regex.find(hostInstitutionText)?.groups?.get(1)?.value?.trim()
                    ?: "Institution not found"

                // Extracting the "No of Opportunities" information
                val opportunitiesText =
                    detailDoc.select("li:contains(No of Opportunities) small").text().trim()
                // Determine how to handle the opportunitiesText
                val totalOpportunities = parseOpportunitiesText(opportunitiesText)

                // Extracting the duration from the "Duration" <li> tag
                val durationText = detailDoc.select("li:contains(Duration) small").text().trim()
                // Process the duration text to handle different cases
                val formattedDuration = formatDuration(durationText)

                // Extracting the required language certificate information
                val requiredLanguageCertificate =
                    detailDoc.select("ul li:contains(Required language certificate)").text()
                // Process and extract the specific part of the text if necessary
                val certificateInfo = processLanguageCertificateInfo(requiredLanguageCertificate)

                // Extract benefits section HTML
                val benefitsHtmlList = extractBenefitsHtml(detailDoc)

                // Extract documents section HTML
                val documentsHtmlList = extractDocumentsHtml(detailDoc)

                val (email, phone) = withContext(Dispatchers.IO) { extractContactDetails(detailDoc) }

                Log.d("Scraping Result", "Email: $email, Phone: $phone")

                // Logging atau update UI di sini jika diperlukan
                Log.d(
                    "Scraping Result",
                    "Link: $url" +
                            "\nTitle: $title, " +
                            "\nPendanaan: $fundingStatus" +
                            "\nDeadline: $deadline" +
                            "\nDegrees: ${degrees?.joinToString(", ")}" +
                            "\nLocation: $location" +
                            "\nInstitution: $institutionName" +
                            "\nNo Of Opportunities: $totalOpportunities" +
                            "\nDuration: $formattedDuration" +
                            "\nLanguage Certificate: $certificateInfo" +
                            "\nBenefits: $benefitsHtmlList" +
                            "\nDocuments: $documentsHtmlList" +
                            "\nEmail: $email" +
                            "\nPhone: $phone"
                )
                benefitsHtmlList.forEach { html ->
                    Log.d("Scraping result: benefits", html)
                }
                documentsHtmlList.forEach { html ->
                    Log.d("Scraping result: documents", html)
                }

                // TODO Umur susah,apakah ganti dengan kriteria lain?


                // TODO UBAH HASIL SCRAPING KE DALAM BENTUK JSON
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun parseOpportunitiesText(text: String): String {
        return if (text.contains("N/A")) {
            "N/A"
        } else {
            // Regex to find all numbers, assume they're separated by non-digit characters
            val regex = "\\d+".toRegex()
            val matches = regex.findAll(text)
            val numbers = matches.map { it.value.toInt() }.toList()

            if (numbers.isEmpty()) {
                "N/A" // In case no numbers found and it's not marked as N/A
            } else {
                numbers.sum().toString() // Summing all extracted numbers
            }
        }
    }

    private fun formatDuration(durationText: String): String {
        return when {
            durationText.contains("N/A", ignoreCase = true) -> "N/A"
            // Checking for both "various" and "varies" variations
            durationText.contains("various", ignoreCase = true) ||
                    durationText.contains("varies", ignoreCase = true) -> "Bervariasi"

            "\\d+ years".toRegex(RegexOption.IGNORE_CASE).matches(durationText) ->
                durationText.replace("years", "tahun", ignoreCase = true)

            "(?i)\\d+ years or more".toRegex().matches(durationText) ->
                durationText.replace("years or more", "tahun atau lebih", ignoreCase = true)

            else -> durationText // Handle unexpected formats gracefully
        }
    }

    private fun processLanguageCertificateInfo(text: String): String {
        // Assuming the format is always followed by "Required language certificate:"
        // You might want to adjust this logic depending on the actual format in the HTML
        return text.substringAfter("Required language certificate:").trim()
    }

    private fun extractBenefitsHtml(detailDoc: Document): List<String> {
        val benefitsList = mutableListOf<String>()

        // Find the <h2> tag that includes "Benefits Of" within any nested tags
        val benefitsHeader = detailDoc.select("h2:contains(Benefits of)").first()

        // Check if the header is found
        if (benefitsHeader != null) {
            var nextElem = benefitsHeader.nextElementSibling()

            while (nextElem != null && !(nextElem.tagName() == "h2" || nextElem.tagName() == "h3")) {
                // Add the outer HTML of the element to the list
                benefitsList.add(nextElem.outerHtml())
                nextElem = nextElem.nextElementSibling()
            }
        }

        return benefitsList
    }

    private fun extractDocumentsHtml(detailDoc: Document): List<String> {
        val documentsList = mutableListOf<String>()

        // Using (?i) for case insensitivity
        val documentsHeader = detailDoc.select("h2:matches((?i)documents)").first()

        if (documentsHeader != null) {
            var nextElem = documentsHeader.nextElementSibling()

            while (nextElem != null && !(nextElem.tagName() == "h2" || nextElem.tagName() == "h3")) {
                documentsList.add(nextElem.outerHtml())
                nextElem = nextElem.nextElementSibling()
            }
        }

        return documentsList
    }

    private fun extractContactDetails(detailDoc: Document): Pair<String?, String?> {
        val contactHeader = detailDoc.select("h2:contains(Contacts)").firstOrNull()

        // Initialize variables to hold email and phone details
        var email: String? = null
        var phone: String? = null

        if (contactHeader != null) {
            val possibleContacts =
                contactHeader.nextElementSiblings().take(2)  // Taking only the next two siblings

            possibleContacts.forEach { elem ->
                if ("email" in elem.text().lowercase()) {
                    email = elem.text().trim()
                } else if ("phone" in elem.text().lowercase()) {
                    phone = elem.text().trim()
                }
            }
        }

        return Pair(email, phone)
    }
}