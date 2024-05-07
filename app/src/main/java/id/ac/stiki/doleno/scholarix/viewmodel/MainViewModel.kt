package id.ac.stiki.doleno.scholarix.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.ac.stiki.doleno.scholarix.model.Beasiswa
import id.ac.stiki.doleno.scholarix.model.DummyBeasiswa
import id.ac.stiki.doleno.scholarix.model.Lokasi
import id.ac.stiki.doleno.scholarix.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
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
    // MutableLiveData to store the list of scholarships
    private val _scholarships = MutableLiveData<List<Beasiswa>>(listOf())
    val scholarships: LiveData<List<Beasiswa>> = _scholarships

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    // Function to fetch scholarship details and update LiveData
    fun fetchScholarshipDetails() {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false
            val fetchedScholarships = mutableListOf<Beasiswa>()
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
                    val scholarship =
                        fetchDetailPage(detailUrl)  // Assume this function returns a Beasiswa object
                    if (scholarship != null) {
                        fetchedScholarships.add(scholarship)
                    }
                }
                _scholarships.postValue(fetchedScholarships)
                _isLoading.value = false
            } catch (e: Exception) {
                _isError.value = true
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }

    // Making fetchDetailPage a suspend function that returns Beasiswa or null
    private suspend fun fetchDetailPage(url: String): Beasiswa? {
        return try {
            // Use withContext to ensure we're on the IO dispatcher for network and IO operations
            withContext(Dispatchers.IO) {
                val detailDoc = Jsoup.connect(url).get()

                // Data extraction and parsing
                val titleElement = detailDoc.selectFirst("figcaption h2")
                val title = titleElement?.text()?.trim() ?: "Title not found"
                val fundingStatus =
                    detailDoc.select("figcaption span small a").first()?.text()?.trim()

                val applyBeforeInfo =
                    detailDoc.select("figcaption ul.jobsearch-jobdetail-options li:nth-child(3)")
                        .text().trim()
                val dateRegex = Regex("""Apply Before : (.+)""")
                val matchResult = dateRegex.find(applyBeforeInfo)
                val deadline = matchResult?.groupValues?.get(1)

                val degreesDiv = detailDoc.select("ul.jobsearch-row li").find {
                    it.select("div.jobsearch-services-text span").text().contains("Degree/Level")
                }
                val degrees = degreesDiv?.select("small")?.map { it.text().trim() }
                val convertedDegrees = degrees?.map { degree ->
                    when {
                        degree.contains("Bachelor/Undergraduate") -> "S1"
                        degree.contains("Master/Postgraduate") -> "S2"
                        degree.contains("Ph.D./Doctoral") -> "S3"
                        else -> degree
                    }
                }

                val location =
                    detailDoc.select("figcaption ul.jobsearch-jobdetail-options li").first()?.text()
                        ?.trim()?.replace(" View on Map", "")
                val parts = location?.split(",")?.map { it.trim() }
                var city: String? = null
                var country = ""

                if (parts != null) {
                    if (parts.size > 1) {
                        city = parts[0]
                        country = parts[1]
                    } else {
                        country = parts[0]
                    }
                }

                val hostInstitutionText = detailDoc.select("li:contains(Host Institution)").text()
                val regex = Regex("Host Institution:\\s*([^\\.]+)")
                val institutionName = regex.find(hostInstitutionText)?.groups?.get(1)?.value?.trim()
                    ?: "Institution not found"

                val opportunitiesText =
                    detailDoc.select("li:contains(No of Opportunities) small").text().trim()
                val totalOpportunities = parseOpportunitiesText(opportunitiesText)

                val durationText = detailDoc.select("li:contains(Duration) small").text().trim()
                val formattedDuration = formatDuration(durationText)

                val requiredLanguageCertificate =
                    detailDoc.select("ul li:contains(Required language certificate)").text()
                val certificateInfo = processLanguageCertificateInfo(requiredLanguageCertificate)

                val benefitsHtmlList = extractBenefitsHtml(detailDoc)
                val documentsHtmlList = extractDocumentsHtml(detailDoc)
                val (email, phone) = extractContactDetails(detailDoc)

                // Construct the Beasiswa object
                Beasiswa(
                    link = url,
                    nama = title,
                    pendanaan = fundingStatus,
                    deadline = deadline,
                    degrees = convertedDegrees ?: listOf(),
                    lokasi = Lokasi(city, country),
                    institusi = institutionName,
                    jumlah = totalOpportunities,
                    durasi = formattedDuration,
                    sertifikatBahasa = certificateInfo,
                    benefits = benefitsHtmlList,
                    documents = documentsHtmlList,
                    email = email,
                    phone = phone
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null // Return null in case of an error
        }
    }

//    private fun fetchDetailPage(url: String) {
//        viewModelScope.launch {
//            try {
//                // Menggunakan Dispatchers.IO untuk operasi I/O network
//                val detailDoc = withContext(Dispatchers.IO) {
//                    Jsoup.connect(url).get()
//                }
//
//                // Ekstraksi data seperti judul, deadline, dan deskripsi
//                val titleElement = detailDoc.selectFirst("figcaption h2")
//                val title = titleElement?.text()?.trim() ?: "Title not found"
//                val fundingStatus =
//                    detailDoc.select("figcaption span small a").first()?.text()?.trim()
//                val applyBeforeInfo =
//                    detailDoc.select("figcaption ul.jobsearch-jobdetail-options li:nth-child(3)")
//                        .text().trim()
//                // Menggunakan regex untuk mengekstrak tanggal
//                val dateRegex = Regex("""Apply Before : (.+)""")
//                val matchResult = dateRegex.find(applyBeforeInfo)
//                val deadline = matchResult?.groupValues?.get(1)
//
//                // Mengambil <div> yang mengandung teks 'Degree/Level'
//                val degreesDiv = detailDoc.select("ul.jobsearch-row li").find { li ->
//                    li.select("div.jobsearch-services-text span").text().contains("Degree/Level")
//                }
//                // Mengambil semua teks dari tag <small> di dalam div tersebut
//                val degrees = degreesDiv?.select("small")?.map { it.text().trim() }
//                // Convert degrees on a computation thread
//                val convertedDegrees = withContext(Dispatchers.Default) {
//                    degrees?.map { degree ->
//                        when {
//                            degree.contains("Bachelor/Undergraduate") -> "S1"
//                            degree.contains("Master/Postgraduate") -> "S2"
//                            degree.contains("Ph.D./Doctoral") -> "S3"
//                            else -> degree // Default case if none of the conditions are met
//                        }
//                    }
//                }
//
//                val location =
//                    detailDoc.select("figcaption ul.jobsearch-jobdetail-options li").first()?.text()
//                        ?.trim()?.replace(" View on Map", "")
//
//                val parts = location?.split(",")?.map { it.trim() }
//
//                // Tentukan nilai default untuk city dan country
//                var city: String? = null
//                var country: String = ""
//
//                // Kemudian, periksa dan atur nilai berdasarkan jumlah bagian
//                if (parts != null) {
//                    if (parts.size > 1) {
//                        city = parts[0] // Kota ada di elemen pertama
//                        country = parts[1] // Negara ada di elemen kedua
//                    } else {
//                        country = parts[0] // Seluruh string dianggap sebagai nama negara
//                    }
//                }
//
//                // Mengambil teks untuk "Host Institution" dari <li> yang spesifik
//                val hostInstitutionText = detailDoc.select("li:contains(Host Institution)").text()
//                // Menggunakan regex untuk mengambil hanya nama institusi sebelum tanda titik (jika ada)
//                val regex = Regex("Host Institution:\\s*([^\\.]+)")
//                val institutionName = regex.find(hostInstitutionText)?.groups?.get(1)?.value?.trim()
//                    ?: "Institution not found"
//
//                // Extracting the "No of Opportunities" information
//                val opportunitiesText =
//                    detailDoc.select("li:contains(No of Opportunities) small").text().trim()
//                // Determine how to handle the opportunitiesText
//                val totalOpportunities = parseOpportunitiesText(opportunitiesText)
//
//                // Extracting the duration from the "Duration" <li> tag
//                val durationText = detailDoc.select("li:contains(Duration) small").text().trim()
//                // Process the duration text to handle different cases
//                val formattedDuration = formatDuration(durationText)
//
//                // Extracting the required language certificate information
//                val requiredLanguageCertificate =
//                    detailDoc.select("ul li:contains(Required language certificate)").text()
//                // Process and extract the specific part of the text if necessary
//                val certificateInfo = processLanguageCertificateInfo(requiredLanguageCertificate)
//
//                // Extract benefits section HTML
//                val benefitsHtmlList = extractBenefitsHtml(detailDoc)
//
//                // Extract documents section HTML
//                val documentsHtmlList = extractDocumentsHtml(detailDoc)
//
//                val (email, phone) = withContext(Dispatchers.IO) { extractContactDetails(detailDoc) }
//
//                // TODO Umur susah,apakah ganti dengan kriteria lain?
//
//                val scholarship = Beasiswa(
//                    // TODO id nya otomatis di firebase bisa??
//                    link = url,
//                    nama = title,
//                    pendanaan = fundingStatus,
//                    deadline = deadline,
//                    degrees = convertedDegrees ?: listOf(),
//                    lokasi = Lokasi(city, country),
//                    institusi = institutionName,
//                    jumlah = totalOpportunities,
//                    durasi = formattedDuration,
//                    sertifikatBahasa = certificateInfo,
//                    benefits = benefitsHtmlList,
//                    documents = documentsHtmlList,
//                    email = email,
//                    phone = phone
//                )
//
//                // Serialize to JSON
//                val json = Gson().toJson(scholarship)
//                Log.d("Scholarship JSON", json)
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }

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
            // This selects all elements that are directly next siblings and all elements nested within these siblings
            val possibleContacts = contactHeader.nextElementSiblings().toList()
                .flatMap { it.select("*") } // Select all nested elements as well
                .take(10) // Assume contacts will be within the first 10 elements for safety

            possibleContacts.forEach { elem ->
                // Use regex to find email and phone numbers to be more accurate
                if ("email" in elem.text().lowercase() || elem.html().contains("mailto:")) {
                    // Extract email from 'mailto:' if exists
                    elem.select("a[href^=mailto:]").first()?.let {
                        email = it.attr("href").removePrefix("mailto:")
                    } ?: run {
                        email = elem.text().substringAfter("Email:").trim()
                    }
                }
                if ("phone" in elem.text().lowercase() || elem.html().contains("tel:")) {
                    // Extract phone from 'tel:' if exists
                    elem.select("a[href^=tel:]").first()?.let {
                        phone = it.attr("href").removePrefix("tel:")
                    } ?: run {
                        phone = elem.text().substringAfter("Phone No:").trim()
                    }
                }
            }
        }

        return Pair(email, phone)
    }

    private fun parseScholarshipJson(jsonString: String): List<Beasiswa> {
        return Json.decodeFromString(ListSerializer(Beasiswa.serializer()), jsonString)
    }

    fun loadScholarships(jsonString: String) {
        _scholarships.value = parseScholarshipJson(jsonString)
    }
}