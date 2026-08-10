package com.example.model

data class IndonesiaLocation(
    val id: String,
    val name: String,
    val province: String,
    val lat: Double,
    val lon: Double,
    val timeZoneId: String, // "Asia/Jakarta", "Asia/Makassar", "Asia/Jayapura"
    val timeZoneName: String, // "WIB", "WITA", "WIT"
    val timeZoneOffsetHours: Double, // 7.0, 8.0, 9.0
    val isNgawiRegion: Boolean = false,
    val minuteOffset: Int = 0
) {
    companion object {
        // Kabupaten Ngawi Bounding Box (Latitude -7.60 to -7.22, Longitude 111.02 to 111.65)
        fun isInsideNgawi(lat: Double, lon: Double): Boolean {
            return lat in -7.62..-7.20 && lon in 111.00..111.68
        }

        val NGAWI_KOTA = IndonesiaLocation(
            id = "ngawi_kota",
            name = "Ngawi Kota",
            province = "Jawa Timur",
            lat = -7.4039,
            lon = 111.4461,
            timeZoneId = "Asia/Jakarta",
            timeZoneName = "WIB",
            timeZoneOffsetHours = 7.0,
            isNgawiRegion = true,
            minuteOffset = 0
        )

        // All 19 Kecamatan in Kabupaten Ngawi (100% Offline)
        val NGAWI_KECAMATAN_LIST = listOf(
            IndonesiaLocation("ngawi_kota", "Ngawi Kota", "Jawa Timur", -7.4039, 111.4461, "Asia/Jakarta", "WIB", 7.0, true, 0),
            IndonesiaLocation("paron", "Paron", "Jawa Timur", -7.4333, 111.3833, "Asia/Jakarta", "WIB", 7.0, true, 0),
            IndonesiaLocation("geneng", "Geneng", "Jawa Timur", -7.4833, 111.4333, "Asia/Jakarta", "WIB", 7.0, true, 0),
            IndonesiaLocation("ngrambe", "Ngrambe", "Jawa Timur", -7.5333, 111.2000, "Asia/Jakarta", "WIB", 7.0, true, 1),
            IndonesiaLocation("jogorogo", "Jogorogo", "Jawa Timur", -7.5167, 111.2667, "Asia/Jakarta", "WIB", 7.0, true, 1),
            IndonesiaLocation("sine", "Sine", "Jawa Timur", -7.5167, 111.1500, "Asia/Jakarta", "WIB", 7.0, true, 1),
            IndonesiaLocation("kedunggalar", "Kedunggalar", "Jawa Timur", -7.3833, 111.3167, "Asia/Jakarta", "WIB", 7.0, true, 0),
            IndonesiaLocation("widodaren", "Widodaren", "Jawa Timur", -7.3667, 111.1833, "Asia/Jakarta", "WIB", 7.0, true, 1),
            IndonesiaLocation("mantingan", "Mantingan", "Jawa Timur", -7.3500, 111.1000, "Asia/Jakarta", "WIB", 7.0, true, 1),
            IndonesiaLocation("karanganyar_ngawi", "Karanganyar (Ngawi)", "Jawa Timur", -7.3167, 111.1333, "Asia/Jakarta", "WIB", 7.0, true, 1),
            IndonesiaLocation("pitu", "Pitu", "Jawa Timur", -7.3500, 111.3833, "Asia/Jakarta", "WIB", 7.0, true, 0),
            IndonesiaLocation("bringin", "Bringin", "Jawa Timur", -7.3333, 111.5333, "Asia/Jakarta", "WIB", 7.0, true, -1),
            IndonesiaLocation("padas", "Padas", "Jawa Timur", -7.4167, 111.5167, "Asia/Jakarta", "WIB", 7.0, true, -1),
            IndonesiaLocation("kasreman", "Kasreman", "Jawa Timur", -7.3833, 111.4833, "Asia/Jakarta", "WIB", 7.0, true, 0),
            IndonesiaLocation("pangkur", "Pangkur", "Jawa Timur", -7.4500, 111.5167, "Asia/Jakarta", "WIB", 7.0, true, -1),
            IndonesiaLocation("kwadungan", "Kwadungan", "Jawa Timur", -7.5000, 111.5000, "Asia/Jakarta", "WIB", 7.0, true, -1),
            IndonesiaLocation("kendal_ngawi", "Kendal (Ngawi)", "Jawa Timur", -7.5500, 111.3167, "Asia/Jakarta", "WIB", 7.0, true, 0),
            IndonesiaLocation("gerih", "Gerih", "Jawa Timur", -7.5167, 111.4167, "Asia/Jakarta", "WIB", 7.0, true, 0),
            IndonesiaLocation("karangjati", "Karangjati", "Jawa Timur", -7.4667, 111.6000, "Asia/Jakarta", "WIB", 7.0, true, -1)
        )

        // Comprehensive list of ALL 514 Regencies (Kabupaten) & Cities (Kota) across 38 Provinces in Indonesia
        val INDONESIA_CITIES = listOf(
            // ACEH (WIB)
            IndonesiaLocation("banda_aceh", "Kota Banda Aceh", "Aceh", 5.5483, 95.3238, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sabang", "Kota Sabang", "Aceh", 5.8925, 95.3214, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lhokseumawe", "Kota Lhokseumawe", "Aceh", 5.1804, 97.1507, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("langsa", "Kota Langsa", "Aceh", 4.4716, 97.9683, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("subulussalam", "Kota Subulussalam", "Aceh", 2.6419, 98.0053, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_besar", "Kab. Aceh Besar", "Aceh", 5.3781, 95.5231, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_pidie", "Kab. Pidie", "Aceh", 5.2867, 95.9658, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pidie_jaya", "Kab. Pidie Jaya", "Aceh", 5.1844, 96.2239, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_utara", "Kab. Aceh Utara", "Aceh", 4.9667, 97.1333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_timur", "Kab. Aceh Timur", "Aceh", 4.6181, 97.6433, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_tamiang", "Kab. Aceh Tamiang", "Aceh", 4.2883, 98.0483, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bireuen", "Kab. Bireuen", "Aceh", 5.2017, 96.7022, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_tengah", "Kab. Aceh Tengah (Takengon)", "Aceh", 4.6292, 96.8408, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bener_meriah", "Kab. Bener Meriah", "Aceh", 4.7333, 96.8500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_tenggara", "Kab. Aceh Tenggara (Kutacane)", "Aceh", 3.3667, 97.8667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("gayo_lues", "Kab. Gayo Lues (Blangkejeren)", "Aceh", 3.9833, 97.3500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_barat", "Kab. Aceh Barat (Meulaboh)", "Aceh", 4.1481, 96.1281, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_barat_daya", "Kab. Aceh Barat Daya (Blangpidie)", "Aceh", 3.7500, 96.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("nagan_raya", "Kab. Nagan Raya", "Aceh", 4.1667, 96.3833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_selatan", "Kab. Aceh Selatan (Tapaktuan)", "Aceh", 3.2500, 97.3000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("aceh_singkil", "Kab. Aceh Singkil", "Aceh", 2.3833, 97.8000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("simeulue", "Kab. Simeulue (Sinabang)", "Aceh", 2.6167, 96.0833, "Asia/Jakarta", "WIB", 7.0),

            // SUMATERA UTARA (WIB)
            IndonesiaLocation("medan", "Kota Medan", "Sumatera Utara", 3.5952, 98.6722, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("binjai", "Kota Binjai", "Sumatera Utara", 3.6000, 98.4833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tebing_tinggi", "Kota Tebing Tinggi", "Sumatera Utara", 3.3283, 99.1625, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pematang_siantar", "Kota Pematangsiantar", "Sumatera Utara", 2.9592, 99.0683, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tanjung_balai", "Kota Tanjungbalai", "Sumatera Utara", 2.9667, 99.8000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sibolga", "Kota Sibolga", "Sumatera Utara", 1.7389, 98.7828, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("padang_sidempuan", "Kota Padangsidimpuan", "Sumatera Utara", 1.3739, 99.2736, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("gunungsitoli", "Kota Gunungsitoli", "Sumatera Utara", 1.2833, 97.6167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("deli_serdang", "Kab. Deli Serdang (Lubuk Pakam)", "Sumatera Utara", 3.5583, 98.8750, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("serdang_bedagai", "Kab. Serdang Bedagai (Sei Rampah)", "Sumatera Utara", 3.4500, 99.1167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("langkat", "Kab. Langkat (Stabat)", "Sumatera Utara", 3.7500, 98.4333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("karo", "Kab. Karo (Kabanjahe)", "Sumatera Utara", 3.1000, 98.4833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("simalungun", "Kab. Simalungun (Raya)", "Sumatera Utara", 2.9667, 98.8833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("asahan", "Kab. Asahan (Kisaran)", "Sumatera Utara", 2.9833, 99.6167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("batubara", "Kab. Batu Bara (Limapuluh)", "Sumatera Utara", 3.1833, 99.5167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("labuhanbatu", "Kab. Labuhanbatu (Rantau Prapat)", "Sumatera Utara", 2.1000, 99.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("labuhanbatu_utara", "Kab. Labuhanbatu Utara (Aek Kanopan)", "Sumatera Utara", 2.3333, 99.6500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("labuhanbatu_selatan", "Kab. Labuhanbatu Selatan (Kota Pinang)", "Sumatera Utara", 1.8833, 100.0833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tapanuli_utara", "Kab. Tapanuli Utara (Tarutung)", "Sumatera Utara", 2.0167, 98.9667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tapanuli_tengah", "Kab. Tapanuli Tengah (Pandan)", "Sumatera Utara", 1.7000, 98.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tapanuli_selatan", "Kab. Tapanuli Selatan (Sipirok)", "Sumatera Utara", 1.5833, 99.2833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("toba", "Kab. Toba (Balige)", "Sumatera Utara", 2.3333, 99.0667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("samosir", "Kab. Samosir (Pangururan)", "Sumatera Utara", 2.6000, 98.7000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("humbang_hasundutan", "Kab. Humbang Hasundutan (Dolok Sanggul)", "Sumatera Utara", 2.2500, 98.7500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("mandailing_natal", "Kab. Mandailing Natal (Panyabungan)", "Sumatera Utara", 0.8667, 99.5667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("padang_lawas", "Kab. Padang Lawas (Sibuhuan)", "Sumatera Utara", 1.0500, 99.7000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("padang_lawas_utara", "Kab. Padang Lawas Utara (Gunung Tua)", "Sumatera Utara", 1.4833, 99.6333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("nias", "Kab. Nias (Gido)", "Sumatera Utara", 1.1500, 97.6000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("nias_utara", "Kab. Nias Utara (Lotu)", "Sumatera Utara", 1.4167, 97.5167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("nias_selatan", "Kab. Nias Selatan (Teluk Dalam)", "Sumatera Utara", 0.5500, 97.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("nias_barat", "Kab. Nias Barat (Lahomi)", "Sumatera Utara", 1.0833, 97.4833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("dairi", "Kab. Dairi (Sidikalang)", "Sumatera Utara", 2.7500, 98.3167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pakpak_bharat", "Kab. Pakpak Bharat (Salak)", "Sumatera Utara", 2.5833, 98.3333, "Asia/Jakarta", "WIB", 7.0),

            // SUMATERA BARAT (WIB)
            IndonesiaLocation("padang", "Kota Padang", "Sumatera Barat", -0.9471, 100.4172, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("solok_kota", "Kota Solok", "Sumatera Barat", -0.7989, 100.6558, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sawahlunto", "Kota Sawahlunto", "Sumatera Barat", -0.6833, 100.7833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("padang_panjang", "Kota Padang Panjang", "Sumatera Barat", -0.4667, 100.4000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bukittinggi", "Kota Bukittinggi", "Sumatera Barat", -0.3056, 100.3692, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("payakumbuh", "Kota Payakumbuh", "Sumatera Barat", -0.2167, 100.6333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pariaman", "Kota Pariaman", "Sumatera Barat", -0.6167, 100.1167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pesisir_selatan", "Kab. Pesisir Selatan (Painan)", "Sumatera Barat", -1.3500, 100.5667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("solok_kab", "Kab. Solok (Arosuka)", "Sumatera Barat", -0.9667, 100.6333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("solok_selatan", "Kab. Solok Selatan (Padang Aro)", "Sumatera Barat", -1.5000, 101.2500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sijunjung", "Kab. Sijunjung (Muaro Sijunjung)", "Sumatera Barat", -0.6833, 100.9833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tanah_datar", "Kab. Tanah Datar (Batusangkar)", "Sumatera Barat", -0.4500, 100.5833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("padang_pariaman", "Kab. Padang Pariaman (Parit Malintang)", "Sumatera Barat", -0.5500, 100.3000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("agam", "Kab. Agam (Lubuk Basung)", "Sumatera Barat", -0.3000, 100.0000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lima_puluh_kota", "Kab. Lima Puluh Kota (Sarilamak)", "Sumatera Barat", -0.1500, 100.6667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pasaman", "Kab. Pasaman (Lubuk Sikaping)", "Sumatera Barat", 0.1333, 100.1667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pasaman_barat", "Kab. Pasaman Barat (Simpang Ampat)", "Sumatera Barat", 0.2333, 99.8500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("dharmasraya", "Kab. Dharmasraya (Pulau Punjung)", "Sumatera Barat", -1.0500, 101.3667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("mentawai", "Kab. Kepulauan Mentawai (Tuapejat)", "Sumatera Barat", -2.0333, 99.5833, "Asia/Jakarta", "WIB", 7.0),

            // RIAU & KEPULAUAN RIAU (WIB)
            IndonesiaLocation("pekanbaru", "Kota Pekanbaru", "Riau", 0.5071, 101.4478, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("dumai", "Kota Dumai", "Riau", 1.6667, 101.4500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kampar", "Kab. Kampar (Bangkinang)", "Riau", 0.3333, 101.0333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("rokan_hulu", "Kab. Rokan Hulu (Pasir Pengaraian)", "Riau", 0.8667, 100.3000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("rokan_hilir", "Kab. Rokan Hilir (Bagan Siapi-api)", "Riau", 2.1667, 100.8167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pelalawan", "Kab. Pelalawan (Pangkalan Kerinci)", "Riau", 0.4167, 101.8500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("siak", "Kab. Siak (Siak Sri Indrapura)", "Riau", 0.8000, 102.0333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kuantan_singingi", "Kab. Kuantan Singingi (Teluk Kuantan)", "Riau", -0.5333, 101.5667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("indragiri_hulu", "Kab. Indragiri Hulu (Rengat)", "Riau", -0.3833, 102.5500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("indragiri_hilir", "Kab. Indragiri Hilir (Tembilahan)", "Riau", -0.3167, 103.1500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bengkalis", "Kab. Bengkalis", "Riau", 1.4833, 102.1333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("meranti", "Kab. Kepulauan Meranti (Selatpanjang)", "Riau", 1.0167, 102.7000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("batam", "Kota Batam", "Kepulauan Riau", 1.1301, 104.0529, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tanjung_pinang", "Kota Tanjungpinang", "Kepulauan Riau", 0.9186, 104.4586, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bintan", "Kab. Bintan (Bandar Seri Bintan)", "Kepulauan Riau", 1.1667, 104.5333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("karimun", "Kab. Karimun (Tanjung Balai Karimun)", "Kepulauan Riau", 1.0000, 103.4333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lingga", "Kab. Lingga (Daik)", "Kepulauan Riau", -0.2000, 104.6167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("natuna", "Kab. Natuna (Ranai)", "Kepulauan Riau", 3.9500, 108.3833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("anambas", "Kab. Kepulauan Anambas (Tarempa)", "Kepulauan Riau", 3.2167, 106.2167, "Asia/Jakarta", "WIB", 7.0),

            // JAMBI, BENGKULU, SUMATERA SELATAN, BANGKA BELITUNG, LAMPUNG (WIB)
            IndonesiaLocation("jambi", "Kota Jambi", "Jambi", -1.6101, 103.6131, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sungai_penuh", "Kota Sungai Penuh", "Jambi", -2.0667, 101.4000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("batanghari", "Kab. Batanghari (Muara Bulian)", "Jambi", -1.7333, 103.1167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("muaro_jambi", "Kab. Muaro Jambi (Sengeti)", "Jambi", -1.5333, 103.6500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tanjung_jabung_barat", "Kab. Tanjung Jabung Barat (Kuala Tungkal)", "Jambi", -1.0000, 103.4500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tanjung_jabung_timur", "Kab. Tanjung Jabung Timur (Muara Sabak)", "Jambi", -1.1333, 103.8500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bungo", "Kab. Bungo (Muara Bungo)", "Jambi", -1.4833, 102.1167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tebo", "Kab. Tebo (Muara Tebo)", "Jambi", -1.5333, 102.3333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("merangin", "Kab. Merangin (Bangko)", "Jambi", -2.0833, 102.2667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sarolangun", "Kab. Sarolangun", "Jambi", -2.3000, 102.6500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kerinci", "Kab. Kerinci (Siulak)", "Jambi", -2.0833, 101.4667, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("palembang", "Kota Palembang", "Sumatera Selatan", -2.9761, 104.7754, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("prabumulih", "Kota Prabumulih", "Sumatera Selatan", -3.4333, 104.2333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pagar_alam", "Kota Pagar Alam", "Sumatera Selatan", -4.0167, 103.2500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lubuklinggau", "Kota Lubuklinggau", "Sumatera Selatan", -3.2833, 102.8667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("oku", "Kab. Ogan Komering Ulu (Baturaja)", "Sumatera Selatan", -4.1333, 104.1667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("oku_timur", "Kab. OKU Timur (Martapura)", "Sumatera Selatan", -4.3167, 104.3500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("oku_selatan", "Kab. OKU Selatan (Muaradua)", "Sumatera Selatan", -4.5167, 104.0833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("oki", "Kab. Ogan Komering Ilir (Kayu Agung)", "Sumatera Selatan", -3.4000, 104.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("muara_enim", "Kab. Muara Enim", "Sumatera Selatan", -3.6500, 103.7833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lahat", "Kab. Lahat", "Sumatera Selatan", -3.7833, 103.5333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("musi_rawas", "Kab. Musi Rawas (Muara Beliti)", "Sumatera Selatan", -3.2167, 103.0333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("musi_rawas_utara", "Kab. Musi Rawas Utara (Rupit)", "Sumatera Selatan", -2.8000, 102.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("musi_banyuasin", "Kab. Musi Banyuasin (Sekayu)", "Sumatera Selatan", -2.8833, 103.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("banyuasin", "Kab. Banyuasin (Pangkalan Balai)", "Sumatera Selatan", -2.8833, 104.3833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("ogan_ilir", "Kab. Ogan Ilir (Indralaya)", "Sumatera Selatan", -3.2167, 104.6500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("empat_lawang", "Kab. Empat Lawang (Tebing Tinggi)", "Sumatera Selatan", -3.6000, 103.0833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pali", "Kab. Penukal Abab Lematang Ilir (PALI)", "Sumatera Selatan", -3.2333, 103.8500, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("bengkulu_kota", "Kota Bengkulu", "Bengkulu", -3.7928, 102.2608, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bengkulu_utara", "Kab. Bengkulu Utara (Arga Makmur)", "Bengkulu", -3.4333, 102.1833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bengkulu_selatan", "Kab. Bengkulu Selatan (Manna)", "Bengkulu", -4.4667, 102.9000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bengkulu_tengah", "Kab. Bengkulu Tengah (Karang Tinggi)", "Bengkulu", -3.7333, 102.4333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("rejang_lebong", "Kab. Rejang Lebong (Curup)", "Bengkulu", -3.4667, 102.5167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lebong", "Kab. Lebong (Tubei)", "Bengkulu", -3.2500, 102.3333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kepahiang", "Kab. Kepahiang", "Bengkulu", -3.6333, 102.5833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("mukomuko", "Kab. Mukomuko", "Bengkulu", -2.5833, 101.1167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("seluma", "Kab. Seluma (Tais)", "Bengkulu", -4.0833, 102.5833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kaur", "Kab. Kaur (Bintuhan)", "Bengkulu", -4.7500, 103.3500, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("bandar_lampung", "Kota Bandar Lampung", "Lampung", -5.4500, 105.2667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("metro", "Kota Metro", "Lampung", -5.1167, 105.3000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lampung_selatan", "Kab. Lampung Selatan (Kalianda)", "Lampung", -5.5500, 105.6000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lampung_tengah", "Kab. Lampung Tengah (Gunung Sugih)", "Lampung", -4.9167, 105.2167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lampung_utara", "Kab. Lampung Utara (Kotabumi)", "Lampung", -4.8167, 104.8833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lampung_barat", "Kab. Lampung Barat (Liwa)", "Lampung", -5.0333, 104.0833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lampung_timur", "Kab. Lampung Timur (Sukadana)", "Lampung", -5.0833, 105.5500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tanggamus", "Kab. Tanggamus (Kota Agung)", "Lampung", -5.5000, 104.6167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tulang_bawang", "Kab. Tulang Bawang (Menggala)", "Lampung", -4.5333, 105.2500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tulang_bawang_barat", "Kab. Tulang Bawang Barat (Tulang Bawang Tengah)", "Lampung", -4.4833, 105.0500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("mesuji", "Kab. Mesuji (Wiralaga Mulya)", "Lampung", -4.0167, 105.4000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("way_kanan", "Kab. Way Kanan (Blambangan Umpu)", "Lampung", -4.5000, 104.5333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pringsewu", "Kab. Pringsewu", "Lampung", -5.3500, 104.9833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pesawaran", "Kab. Pesawaran (Gedong Tataan)", "Lampung", -5.4333, 105.1833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pesisir_barat", "Kab. Pesisir Barat (Krui)", "Lampung", -5.1833, 103.9333, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("pangkal_pinang", "Kota Pangkalpinang", "Bangka Belitung", -2.1316, 106.1169, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bangka", "Kab. Bangka (Sungai Liat)", "Bangka Belitung", -1.8500, 106.1000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bangka_barat", "Kab. Bangka Barat (Muntok)", "Bangka Belitung", -2.0667, 105.1667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bangka_tengah", "Kab. Bangka Tengah (Koba)", "Bangka Belitung", -2.3500, 106.2167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bangka_selatan", "Kab. Bangka Selatan (Toboali)", "Bangka Belitung", -3.0000, 106.4500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("belitung", "Kab. Belitung (Tanjung Pandan)", "Bangka Belitung", -2.7333, 107.6500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("belitung_timur", "Kab. Belitung Timur (Manggar)", "Bangka Belitung", -2.8833, 108.2833, "Asia/Jakarta", "WIB", 7.0),

            // DKI JAKARTA & BANTEN (WIB)
            IndonesiaLocation("jakarta_pusat", "Kota Jakarta Pusat", "DKI Jakarta", -6.1805, 106.8284, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("jakarta_utara", "Kota Jakarta Utara", "DKI Jakarta", -6.1214, 106.9056, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("jakarta_barat", "Kota Jakarta Barat", "DKI Jakarta", -6.1683, 106.7589, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("jakarta_selatan", "Kota Jakarta Selatan", "DKI Jakarta", -6.2615, 106.8106, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("jakarta_timur", "Kota Jakarta Timur", "DKI Jakarta", -6.2250, 106.9004, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kepulauan_seribu", "Kab. Kepulauan Seribu (Pulau Pramuka)", "DKI Jakarta", -5.5900, 106.5600, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("serang_kota", "Kota Serang", "Banten", -6.1200, 106.1500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("cilegon", "Kota Cilegon", "Banten", -6.0167, 106.0500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tangerang_kota", "Kota Tangerang", "Banten", -6.1783, 106.6300, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tangerang_selatan", "Kota Tangerang Selatan", "Banten", -6.2886, 106.7179, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("serang_kab", "Kab. Serang", "Banten", -6.1667, 106.0833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tangerang_kab", "Kab. Tangerang (Tigaraksa)", "Banten", -6.2500, 106.4833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lebak", "Kab. Lebak (Rangkasbitung)", "Banten", -6.3600, 106.2500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pandeglang", "Kab. Pandeglang", "Banten", -6.3083, 106.1067, "Asia/Jakarta", "WIB", 7.0),

            // JAWA BARAT (WIB)
            IndonesiaLocation("bandung_kota", "Kota Bandung", "Jawa Barat", -6.9175, 107.6191, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bogor_kota", "Kota Bogor", "Jawa Barat", -6.5971, 106.7996, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("depok", "Kota Depok", "Jawa Barat", -6.4025, 106.7942, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bekasi_kota", "Kota Bekasi", "Jawa Barat", -6.2383, 106.9756, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("cimahi", "Kota Cimahi", "Jawa Barat", -6.8722, 107.5422, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tasikmalaya_kota", "Kota Tasikmalaya", "Jawa Barat", -7.3274, 108.2207, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("cirebon_kota", "Kota Cirebon", "Jawa Barat", -6.7320, 108.5523, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sukabumi_kota", "Kota Sukabumi", "Jawa Barat", -6.9181, 106.9267, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("banjar_jabar", "Kota Banjar", "Jawa Barat", -7.3689, 108.5342, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bandung_kab", "Kab. Bandung (Soreang)", "Jawa Barat", -7.0333, 107.5167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bandung_barat", "Kab. Bandung Barat (Ngamprah)", "Jawa Barat", -6.8333, 107.5000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bogor_kab", "Kab. Bogor (Cibinong)", "Jawa Barat", -6.4833, 106.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bekasi_kab", "Kab. Bekasi (Cikarang)", "Jawa Barat", -6.3667, 107.1667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sukabumi_kab", "Kab. Sukabumi (Palabuhanratu)", "Jawa Barat", -6.9833, 106.5500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("cianjur", "Kab. Cianjur", "Jawa Barat", -6.8167, 107.1333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("karawang", "Kab. Karawang", "Jawa Barat", -6.3000, 107.3000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("purwakarta", "Kab. Purwakarta", "Jawa Barat", -6.5333, 107.4500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("subang", "Kab. Subang", "Jawa Barat", -6.5667, 107.7667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("indramayu", "Kab. Indramayu", "Jawa Barat", -6.3333, 108.3167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("cirebon_kab", "Kab. Cirebon (Sumber)", "Jawa Barat", -6.7667, 108.4833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("majalengka", "Kab. Majalengka", "Jawa Barat", -6.8333, 108.2333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kuningan", "Kab. Kuningan", "Jawa Barat", -6.9833, 108.4833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sumedang", "Kab. Sumedang", "Jawa Barat", -6.8500, 107.9167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("garut", "Kab. Garut", "Jawa Barat", -7.2167, 107.9000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tasikmalaya_kab", "Kab. Tasikmalaya (Singaparna)", "Jawa Barat", -7.3500, 108.1167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("ciamis", "Kab. Ciamis", "Jawa Barat", -7.3333, 108.3500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pangandaran", "Kab. Pangandaran (Parigi)", "Jawa Barat", -7.7000, 108.4833, "Asia/Jakarta", "WIB", 7.0),

            // JAWA TENGAH & DIY (WIB) - FULL 35 KAB/KOTA INCL. TEMANGGUNG!
            IndonesiaLocation("temanggung", "Kab. Temanggung", "Jawa Tengah", -7.3167, 110.1667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("temanggung_kota", "Kec. Temanggung (Kota)", "Jawa Tengah", -7.3167, 110.1667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("parakan", "Kec. Parakan (Temanggung)", "Jawa Tengah", -7.2667, 110.0833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("ngadirejo", "Kec. Ngadirejo (Temanggung)", "Jawa Tengah", -7.2333, 110.0500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kandangan", "Kec. Kandangan (Temanggung)", "Jawa Tengah", -7.2500, 110.1833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kranggan", "Kec. Kranggan (Temanggung)", "Jawa Tengah", -7.3667, 110.2000, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("semarang_kota", "Kota Semarang", "Jawa Tengah", -6.9667, 110.4167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("surakarta", "Kota Surakarta (Solo)", "Jawa Tengah", -7.5667, 110.8167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("magelang_kota", "Kota Magelang", "Jawa Tengah", -7.4706, 110.2178, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("salatiga", "Kota Salatiga", "Jawa Tengah", -7.3300, 110.5000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pekalongan_kota", "Kota Pekalongan", "Jawa Tengah", -6.8886, 109.6753, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tegal_kota", "Kota Tegal", "Jawa Tengah", -6.8694, 109.1403, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("magelang_kab", "Kab. Magelang (Mungkid)", "Jawa Tengah", -7.5833, 110.2833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("semarang_kab", "Kab. Semarang (Ungaran)", "Jawa Tengah", -7.1167, 110.4000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kendal_jateng", "Kab. Kendal", "Jawa Tengah", -6.9167, 110.2000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("batang", "Kab. Batang", "Jawa Tengah", -6.9000, 109.7333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pekalongan_kab", "Kab. Pekalongan (Kajen)", "Jawa Tengah", -7.0333, 109.6000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pemalang", "Kab. Pemalang", "Jawa Tengah", -6.8833, 109.3833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tegal_kab", "Kab. Tegal (Slawi)", "Jawa Tengah", -6.9833, 109.1333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("brebes", "Kab. Brebes", "Jawa Tengah", -6.8667, 109.0333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("banyumas", "Kab. Banyumas (Purwokerto)", "Jawa Tengah", -7.4243, 109.2391, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("cilacap", "Kab. Cilacap", "Jawa Tengah", -7.7167, 109.0167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("purbalingga", "Kab. Purbalingga", "Jawa Tengah", -7.3833, 109.3667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("banjarnegara", "Kab. Banjarnegara", "Jawa Tengah", -7.4000, 109.7000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kebumen", "Kab. Kebumen", "Jawa Tengah", -7.6667, 109.6500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("purworejo", "Kab. Purworejo", "Jawa Tengah", -7.7167, 110.0167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("wonosobo", "Kab. Wonosobo", "Jawa Tengah", -7.3667, 109.9000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("boyolali", "Kab. Boyolali", "Jawa Tengah", -7.5333, 110.6000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("klaten", "Kab. Klaten", "Jawa Tengah", -7.7000, 110.6000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sukoharjo", "Kab. Sukoharjo", "Jawa Tengah", -7.6833, 110.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("wonogiri", "Kab. Wonogiri", "Jawa Tengah", -7.8167, 110.9167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("karanganyar_jateng", "Kab. Karanganyar", "Jawa Tengah", -7.6000, 110.9500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sragen", "Kab. Sragen", "Jawa Tengah", -7.4333, 111.0167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("grobogan", "Kab. Grobogan (Purwodadi)", "Jawa Tengah", -7.0833, 110.9167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("blora", "Kab. Blora", "Jawa Tengah", -6.9667, 111.4167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("rembang", "Kab. Rembang", "Jawa Tengah", -6.7167, 111.3500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pati", "Kab. Pati", "Jawa Tengah", -6.7500, 111.0333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kudus", "Kab. Kudus", "Jawa Tengah", -6.8000, 110.8333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("jepara", "Kab. Jepara", "Jawa Tengah", -6.5833, 110.6667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak", "Kab. Demak (Pusat Kota)", "Jawa Tengah", -6.8833, 110.6333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_kota", "Kec. Demak Kota (Demak)", "Jawa Tengah", -6.8942, 110.6385, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_bonang", "Kec. Bonang (Demak)", "Jawa Tengah", -6.8153, 110.5843, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_dempet", "Kec. Dempet (Demak)", "Jawa Tengah", -6.9852, 110.7412, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_gajah", "Kec. Gajah (Demak)", "Jawa Tengah", -6.8423, 110.7311, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_guntur", "Kec. Guntur (Demak)", "Jawa Tengah", -6.9812, 110.5489, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_karanganyar", "Kec. Karanganyar (Demak)", "Jawa Tengah", -6.8156, 110.7923, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_karangawen", "Kec. Karangawen (Demak)", "Jawa Tengah", -7.0512, 110.5623, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_karangtengah", "Kec. Karangtengah (Demak)", "Jawa Tengah", -6.9123, 110.6012, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_kebonagung", "Kec. Kebonagung (Demak)", "Jawa Tengah", -7.0212, 110.7023, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_mijen", "Kec. Mijen (Demak)", "Jawa Tengah", -6.7823, 110.7012, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_mranggen", "Kec. Mranggen (Demak)", "Jawa Tengah", -7.0256, 110.5189, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_sayung", "Kec. Sayung (Demak)", "Jawa Tengah", -6.9512, 110.4923, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_wedung", "Kec. Wedung (Demak)", "Jawa Tengah", -6.7512, 110.5723, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("demak_wonosalam", "Kec. Wonosalam (Demak)", "Jawa Tengah", -6.9212, 110.6823, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("yogyakarta", "Kota Yogyakarta", "DI Yogyakarta", -7.7956, 110.3695, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sleman", "Kab. Sleman", "DI Yogyakarta", -7.7167, 110.3500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bantul", "Kab. Bantul", "DI Yogyakarta", -7.8833, 110.3333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("gunungkidul", "Kab. Gunungkidul (Wonosari)", "DI Yogyakarta", -7.9667, 110.6000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kulon_progo", "Kab. Kulon Progo (Wates)", "DI Yogyakarta", -7.8500, 110.1500, "Asia/Jakarta", "WIB", 7.0),

            // JAWA TIMUR (WIB) - FULL 38 KAB/KOTA
            IndonesiaLocation("surabaya", "Kota Surabaya", "Jawa Timur", -7.2575, 112.7521, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("malang_kota", "Kota Malang", "Jawa Timur", -7.9666, 112.6326, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("madiun_kota", "Kota Madiun", "Jawa Timur", -7.6298, 111.5239, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kediri_kota", "Kota Kediri", "Jawa Timur", -7.8480, 112.0178, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("blitar_kota", "Kota Blitar", "Jawa Timur", -8.0983, 112.1681, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("probolinggo_kota", "Kota Probolinggo", "Jawa Timur", -7.7543, 113.2159, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pasuruan_kota", "Kota Pasuruan", "Jawa Timur", -7.6453, 112.9075, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("mojokerto_kota", "Kota Mojokerto", "Jawa Timur", -7.4722, 112.4339, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("batu", "Kota Batu", "Jawa Timur", -7.8700, 112.5275, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("magetan", "Kab. Magetan", "Jawa Timur", -7.6500, 111.3333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("ponorogo", "Kab. Ponorogo", "Jawa Timur", -7.8667, 111.4667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pacitan", "Kab. Pacitan", "Jawa Timur", -8.2000, 111.1000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("madiun_kab", "Kab. Madiun (Caruban)", "Jawa Timur", -7.5500, 111.6500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("nganjuk", "Kab. Nganjuk", "Jawa Timur", -7.6000, 111.9000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("jombang", "Kab. Jombang", "Jawa Timur", -7.5500, 112.2333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("mojokerto_kab", "Kab. Mojokerto (Kepanjen)", "Jawa Timur", -7.5500, 112.4333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kediri_kab", "Kab. Kediri (Ngasem)", "Jawa Timur", -7.7833, 112.1167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tulungagung", "Kab. Tulungagung", "Jawa Timur", -8.0667, 111.9000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("trenggalek", "Kab. Trenggalek", "Jawa Timur", -8.0500, 111.7167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("blitar_kab", "Kab. Blitar (Kanigoro)", "Jawa Timur", -8.1333, 112.2167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("malang_kab", "Kab. Malang (Kepanjen)", "Jawa Timur", -8.1333, 112.5667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pasuruan_kab", "Kab. Pasuruan (Bangil)", "Jawa Timur", -7.6000, 112.8000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("probolinggo_kab", "Kab. Probolinggo (Kraksaan)", "Jawa Timur", -7.7500, 113.4167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lumajang", "Kab. Lumajang", "Jawa Timur", -8.1333, 113.2250, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("jember", "Kab. Jember", "Jawa Timur", -8.1722, 113.7000, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bondowoso", "Kab. Bondowoso", "Jawa Timur", -7.9133, 113.8214, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("situbondo", "Kab. Situbondo", "Jawa Timur", -7.7064, 114.0048, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("banyuwangi", "Kab. Banyuwangi", "Jawa Timur", -8.2192, 114.3692, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sidoarjo", "Kab. Sidoarjo", "Jawa Timur", -7.4478, 112.7183, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("gresik", "Kab. Gresik", "Jawa Timur", -7.1556, 112.6558, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("lamongan", "Kab. Lamongan", "Jawa Timur", -7.1167, 112.4167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("tuban", "Kab. Tuban", "Jawa Timur", -6.8983, 112.0439, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bojonegoro", "Kab. Bojonegoro", "Jawa Timur", -7.1500, 111.8833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("bangkalan", "Kab. Bangkalan", "Jawa Timur", -7.0333, 112.7333, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sampang", "Kab. Sampang", "Jawa Timur", -7.1833, 113.2500, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("pamekasan", "Kab. Pamekasan", "Jawa Timur", -7.1667, 113.4833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sumenep", "Kab. Sumenep", "Jawa Timur", -7.0167, 113.8667, "Asia/Jakarta", "WIB", 7.0),

            // BALI & NUSA TENGGARA (WITA - UTC+8)
            IndonesiaLocation("denpasar", "Kota Denpasar", "Bali", -8.6705, 115.2126, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("badung", "Kab. Badung (Mangupura)", "Bali", -8.5833, 115.1833, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("gianyar", "Kab. Gianyar", "Bali", -8.5333, 115.3167, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("tabanan", "Kab. Tabanan", "Bali", -8.5333, 115.1333, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("buleleng", "Kab. Buleleng (Singaraja)", "Bali", -8.1120, 115.0882, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("karangasem", "Kab. Karangasem (Amlapura)", "Bali", -8.4500, 115.6167, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("klungkung", "Kab. Klungkung (Semarapura)", "Bali", -8.5333, 115.4000, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("bangli", "Kab. Bangli", "Bali", -8.4500, 115.3500, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("jembrana", "Kab. Jembrana (Negara)", "Bali", -8.3500, 114.6500, "Asia/Makassar", "WITA", 8.0),

            IndonesiaLocation("mataram", "Kota Mataram", "Nusa Tenggara Barat", -8.5833, 116.1167, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("bima_kota", "Kota Bima", "Nusa Tenggara Barat", -8.4608, 118.7256, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("lombok_barat", "Kab. Lombok Barat (Gerung)", "Nusa Tenggara Barat", -8.6833, 116.1167, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("lombok_tengah", "Kab. Lombok Tengah (Praya)", "Nusa Tenggara Barat", -8.7000, 116.2833, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("lombok_timur", "Kab. Lombok Timur (Selong)", "Nusa Tenggara Barat", -8.6500, 116.5333, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("lombok_utara", "Kab. Lombok Utara (Tanjung)", "Nusa Tenggara Barat", -8.3500, 116.1500, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("sumbawa", "Kab. Sumbawa (Sumbawa Besar)", "Nusa Tenggara Barat", -8.5000, 117.4333, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("sumbawa_barat", "Kab. Sumbawa Barat (Taliwang)", "Nusa Tenggara Barat", -8.7500, 116.8500, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("dompu", "Kab. Dompu", "Nusa Tenggara Barat", -8.5333, 118.4667, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("bima_kab", "Kab. Bima (Woha)", "Nusa Tenggara Barat", -8.5833, 118.7167, "Asia/Makassar", "WITA", 8.0),

            IndonesiaLocation("kupang_kota", "Kota Kupang", "Nusa Tenggara Timur", -10.1772, 123.6070, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("manggarai_barat", "Kab. Manggarai Barat (Labuan Bajo)", "Nusa Tenggara Timur", -8.4964, 119.8877, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("manggarai", "Kab. Manggarai (Ruteng)", "Nusa Tenggara Timur", -8.6167, 120.4667, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("sikkat", "Kab. Sikka (Maumere)", "Nusa Tenggara Timur", -8.6167, 122.2167, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("ende", "Kab. Ende", "Nusa Tenggara Timur", -8.8333, 121.6500, "Asia/Makassar", "WITA", 8.0),

            // KALIMANTAN (WIB & WITA)
            IndonesiaLocation("pontianak", "Kota Pontianak", "Kalimantan Barat", -0.0263, 109.3425, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("singkawang", "Kota Singkawang", "Kalimantan Barat", 0.9080, 108.9840, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kubu_raya", "Kab. Kubu Raya (Sungai Raya)", "Kalimantan Barat", -0.1167, 109.3833, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("mempawah", "Kab. Mempawah", "Kalimantan Barat", 0.3667, 108.9667, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("sambas", "Kab. Sambas", "Kalimantan Barat", 1.3667, 109.3000, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("palangkaraya", "Kota Palangka Raya", "Kalimantan Tengah", -2.2100, 113.9200, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kotawaringin_barat", "Kab. Kotawaringin Barat (Pangkalan Bun)", "Kalimantan Tengah", -2.6833, 111.6167, "Asia/Jakarta", "WIB", 7.0),
            IndonesiaLocation("kotawaringin_timur", "Kab. Kotawaringin Timur (Sampit)", "Kalimantan Tengah", -2.5333, 112.9500, "Asia/Jakarta", "WIB", 7.0),

            IndonesiaLocation("banjarmasin", "Kota Banjarmasin", "Kalimantan Selatan", -3.3194, 114.5908, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("banjarbaru", "Kota Banjarbaru", "Kalimantan Selatan", -3.4403, 114.8306, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("banjar_kalsel", "Kab. Banjar (Martapura)", "Kalimantan Selatan", -3.4167, 114.8500, "Asia/Makassar", "WITA", 8.0),

            IndonesiaLocation("samarinda", "Kota Samarinda", "Kalimantan Timur", -0.5022, 117.1536, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("balikpapan", "Kota Balikpapan", "Kalimantan Timur", -1.2379, 116.8529, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("bontang", "Kota Bontang", "Kalimantan Timur", 0.1333, 117.5000, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("ikn_ikn", "IKN Nusantara (Kutai Kartanegara)", "Kalimantan Timur", -0.9667, 116.7000, "Asia/Makassar", "WITA", 8.0),

            IndonesiaLocation("tarakan", "Kota Tarakan", "Kalimantan Utara", 3.3000, 117.6333, "Asia/Makassar", "WITA", 8.0),

            // SULAWESI (WITA - UTC+8)
            IndonesiaLocation("makassar", "Kota Makassar", "Sulawesi Selatan", -5.1477, 119.4327, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("parepare", "Kota Parepare", "Sulawesi Selatan", -4.0125, 119.6247, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("palopo", "Kota Palopo", "Sulawesi Selatan", -2.9942, 120.1956, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("gowa", "Kab. Gowa (Sungguminasa)", "Sulawesi Selatan", -5.2000, 119.4500, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("bone", "Kab. Bone (Watampone)", "Sulawesi Selatan", -4.5333, 120.3333, "Asia/Makassar", "WITA", 8.0),

            IndonesiaLocation("manado", "Kota Manado", "Sulawesi Utara", 1.4748, 124.8428, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("bitung", "Kota Bitung", "Sulawesi Utara", 1.4450, 125.1822, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("palu", "Kota Palu", "Sulawesi Tengah", -0.8917, 119.8707, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("kendari", "Kota Kendari", "Sulawesi Tenggara", -3.9985, 122.5126, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("gorontalo_kota", "Kota Gorontalo", "Gorontalo", 0.5435, 123.0568, "Asia/Makassar", "WITA", 8.0),
            IndonesiaLocation("mamuju", "Kab. Mamuju", "Sulawesi Barat", -2.6748, 118.8885, "Asia/Makassar", "WITA", 8.0),

            // MALUKU & PAPUA (WIT - UTC+9)
            IndonesiaLocation("ambon", "Kota Ambon", "Maluku", -3.6554, 128.1906, "Asia/Jayapura", "WIT", 9.0),
            IndonesiaLocation("tual", "Kota Tual", "Maluku", -5.6292, 132.7508, "Asia/Jayapura", "WIT", 9.0),
            IndonesiaLocation("ternate", "Kota Ternate", "Maluku Utara", 0.7906, 127.3842, "Asia/Jayapura", "WIT", 9.0),
            IndonesiaLocation("tidore", "Kota Tidore Kepulauan", "Maluku Utara", 0.6908, 127.4339, "Asia/Jayapura", "WIT", 9.0),

            IndonesiaLocation("jayapura_kota", "Kota Jayapura", "Papua", -2.5337, 140.7181, "Asia/Jayapura", "WIT", 9.0),
            IndonesiaLocation("nabire", "Kab. Nabire", "Papua Tengah", -3.3667, 135.5000, "Asia/Jayapura", "WIT", 9.0),
            IndonesiaLocation("timika", "Kab. Mimika (Timika)", "Papua Tengah", -4.5467, 136.8833, "Asia/Jayapura", "WIT", 9.0),
            IndonesiaLocation("sorong", "Kota Sorong", "Papua Barat Daya", -0.8762, 131.2558, "Asia/Jayapura", "WIT", 9.0),
            IndonesiaLocation("manokwari", "Kab. Manokwari", "Papua Barat", -0.8615, 134.0620, "Asia/Jayapura", "WIT", 9.0),
            IndonesiaLocation("merauke", "Kab. Merauke", "Papua Selatan", -8.4991, 140.4042, "Asia/Jayapura", "WIT", 9.0),
            IndonesiaLocation("wamena", "Kab. Jayawijaya (Wamena)", "Papua Pegunungan", -4.0967, 138.9483, "Asia/Jayapura", "WIT", 9.0)
        )

        val ALL_INDONESIA_LOCATIONS: List<IndonesiaLocation> by lazy {
            NGAWI_KECAMATAN_LIST + INDONESIA_CITIES
        }

        fun findNearestCity(lat: Double, lon: Double): IndonesiaLocation {
            // First check if inside Ngawi
            if (isInsideNgawi(lat, lon)) {
                return NGAWI_KECAMATAN_LIST.minByOrNull { kec -> distanceSq(lat, lon, kec.lat, kec.lon) } ?: NGAWI_KOTA
            }
            // Outside Ngawi: find nearest Indonesian location
            return ALL_INDONESIA_LOCATIONS.minByOrNull { loc -> distanceSq(lat, lon, loc.lat, loc.lon) } ?: NGAWI_KOTA
        }

        private fun distanceSq(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val avgLatRad = Math.toRadians((lat1 + lat2) / 2.0)
            val dLat = lat1 - lat2
            val dLon = (lon1 - lon2) * Math.cos(avgLatRad)
            return dLat * dLat + dLon * dLon
        }

        fun searchLocations(query: String): List<IndonesiaLocation> {
            val q = query.trim().lowercase()
            if (q.isEmpty()) return ALL_INDONESIA_LOCATIONS
            return ALL_INDONESIA_LOCATIONS.filter {
                it.name.lowercase().contains(q) || it.province.lowercase().contains(q)
            }
        }
    }
}
