package com.example.model

data class DuaItem(
    val id: String,
    val titleId: String,
    val titleAr: String,
    val arabic: String,
    val latin: String,
    val translationId: String,
    val translationAr: String,
    val category: String, // "Zikir Bakda Salat Fardhu", "Doa Sunnah Harian", "Dzikir Pagi", "Dzikir Petang"
    val reference: String = "HR. Muslim / Bukhari",
    val referenceAr: String = "رواه مسلم / البخاري"
)

object DuaDataProvider {
    val ALL_DUAS = listOf(
        DuaItem(
            id = "dzikir_1_istighfar",
            titleId = "1. Istighfar 3x & Doa Keselamatan (HR. Muslim)",
            titleAr = "١. الاستغفار ٣ مرات والدعاء بالسلام",
            arabic = "أَسْتَغْفِرُ اللهَ (٣×)\n\nاللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ، تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ",
            latin = "Astaghfirullah (3x).\n\nAllahumma antas-salamu wa minkas-salamu, tabarakta ya dhal-jalali wal-ikram.",
            translationId = "Aku memohon ampunan kepada Allah (3 kali).\n\nYa Allah, Engkau Maha Sejahtera dan dari-Mu lah keselamatan, Maha Suci Engkau wahai Rabb Pemilik Keagungan dan Kemuliaan.",
            translationAr = "استغفار الله تعالى ثلاث مرات وإتباعه بطلب السلام والبركة من الله تعالى.",
            category = "Zikir Bakda Salat Fardhu",
            reference = "HR. Muslim No. 591 (Hisnul Muslim No. 66)",
            referenceAr = "رواه مسلم (رقم ٥٩١) عن ثوبان رضي الله عنه"
        ),
        DuaItem(
            id = "dzikir_2_tauhid_pujian",
            titleId = "2. Zikir Tauhid & Penyerahan Diri (HR. Bukhari & Muslim)",
            titleAr = "٢. التهليل وتوحيد الله بعد الصلاة",
            arabic = "لاَ إِلَهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيْرٌ، اللَّهُمَّ لاَ مَانِعَ لِمَا أَعْطَيْتَ، وَلاَ مُعْطِيَ لِمَا مَنَعْتَ، وَلاَ يَنْفَعُ ذَا الْجَدِّ مِنْكَ الْجَدُّ",
            latin = "Laa ilaha illallahu wahdahu laa syarika lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli syai-in qadiir. Allahumma laa maani'a limaa a'thaita wa laa mu'thiya limaa mana'ta wa laa yanfa'u dhal-jaddi minkal-jadd.",
            translationId = "Tiada sesembahan yang berhak disembah selain Allah Yang Maha Esa, tiada sekutu bagi-Nya. Bagi-Nya kerajaan dan segala pujian, dan Dia Maha Kuasa atas segala sesuatu. Ya Allah, tidak ada yang dapat mencegah apa yang Engkau berikan, dan tidak ada yang dapat memberi apa yang Engkau cegah, dan tidak bermanfaat kekayaan/kebesaran bagi pemiliknya dari azab-Mu.",
            translationAr = "إفراد الله بالعبادة والملك والحمد، وإظهار العجز والخضوع لقدرته سبحانه وتعالى.",
            category = "Zikir Bakda Salat Fardhu",
            reference = "HR. Bukhari No. 844 & Muslim No. 593 (Hisnul Muslim No. 67)",
            referenceAr = "رواه البخاري (رقم ٨٤٤) ومسلم (رقم ٥٩٣)"
        ),
        DuaItem(
            id = "dzikir_3_tauhid_keikhlasan",
            titleId = "3. Zikir Tauhid, Kejujuran & Keikhlasan (HR. Muslim)",
            titleAr = "٣. التهليل وإخلاص الدين لله",
            arabic = "لاَ إِلَهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيْرٌ، لاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللهِ، لاَ إِلَهَ إِلاَّ اللهُ وَلاَ نَعْبُدُ إِلاَّ إِيَّاهُ، لَهُ النِّعْمَةُ وَلَهُ الْفَضْلُ وَلَهُ الثَّنَاءُ الْحَسَنُ، لاَ إِلَهَ إِلاَّ اللهُ مُخْلِصِيْنَ لَهُ الدِّيْنَ وَلَوْ كَرِهَ الْكَافِرُوْنَ",
            latin = "Laa ilaha illallahu wahdahu laa syarika lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli syai-in qadiir. Laa haula wa laa quwwata illa billah. Laa ilaha illallahu wa laa na'budu illa iyyah. Lahun-ni'matu wa lahul-fadhlu wa lahuth-thana-ul-hasan. Laa ilaha illallahu mukhlishiina lahud-diina walau karihal-kaafirun.",
            translationId = "Tiada sesembahan yang berhak disembah selain Allah Yang Maha Esa, tiada sekutu bagi-Nya. Bagi-Nya kerajaan dan pujian, dan Dia Maha Kuasa atas segala sesuatu. Tiada daya dan upaya kecuali dengan pertolongan Allah. Tiada sesembahan selain Allah dan kami tidak menyembah kecuali hanya kepada-Nya. Bagi-Nya segala nikmat, karunia, dan pujian yang baik. Tiada sesembahan selain Allah dengan memurnikan ibadah hanya kepada-Nya, walaupun orang-orang kafir tidak menyukainya.",
            translationAr = "لا إله إلا الله وحده لا شريك له، لا حول ولا قوة إلا بالله، ولا نعبد إلا إياه مخلصين له الدين.",
            category = "Zikir Bakda Salat Fardhu",
            reference = "HR. Muslim No. 594 (Hisnul Muslim No. 68)",
            referenceAr = "رواه مسلم (رقم ٥٩٤) عن عبد الله بن الزبير رضي الله عنهما"
        ),
        DuaItem(
            id = "dzikir_4_tasbih_tahmid_takbir",
            titleId = "4. Tasbih (33x), Tahmid (33x), Takbir (33x) & Penutup (HR. Muslim)",
            titleAr = "٤. التسبيح (٣٣) والتحميد (٣٣) والتكبير (٣٣) وختم المائة",
            arabic = "سُبْحَانَ اللهِ (٣٣×)\nالْحَمْدُ لِلَّهِ (٣٣×)\nاللهُ أَكْبَرُ (٣٣×)\n\nخِتَامُ المِائَةِ (Penutup ke-100):\nلاَ إِلَهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيْرٌ",
            latin = "Subhanallah (33x), Alhamdulillah (33x), Allahu Akbar (33x).\n\nPenutup ke-100:\nLaa ilaha illallahu wahdahu laa syarika lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli syai-in qadiir.",
            translationId = "Maha Suci Allah (33x), Segala Puji Bagi Allah (33x), Allah Maha Besar (33x).\nPenutup ke-100: Tiada sesembahan selain Allah semata, tiada sekutu bagi-Nya. Bagi-Nya kerajaan dan pujian, dan Dia Maha Kuasa atas segala sesuatu.\n(Keutamaan: Diampuni dosa-dosanya meskipun sebanyak buih di lautan).",
            translationAr = "قول سبحان الله والحمد لله والله أكبر ثلاثاً وثلاثين، وختم المائة بالتوحيد. تُغفر به الخطايا وإن كانت مثل زبد البحر.",
            category = "Zikir Bakda Salat Fardhu",
            reference = "HR. Muslim No. 597 (Hisnul Muslim No. 69)",
            referenceAr = "رواه مسلم (رقم ٥٩٧) عن أبي هريرة رضي الله عنه"
        ),
        DuaItem(
            id = "dzikir_5_muawwidzat",
            titleId = "5. Membaca Mu'awwidzat (Al-Ikhlas, Al-Falaq, An-Naas) (HR. Abu Dawud)",
            titleAr = "٥. قراءة المعوذات (الإخلاص، الفلق، الناس)",
            arabic = "سُورَةُ الإِخْلاَصِ:\nبِسْمِ اللهِ الرَّحْمَنِ الرَّحِيْمِ\nقُلْ هُوَ اللَّهُ أَحَدٌ ۞ اللَّهُ الصَّمَدُ ۞ لَمْ يَلِدْ وَلَمْ يُولَدْ ۞ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ\n\nسُورَةُ الْفَلَقِ:\nبِسْمِ اللهِ الرَّحْمَنِ الرَّحِيْمِ\nقُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۞ مِن شَرِّ مَا خَلَقَ ۞ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ ۞ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ ۞ وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ\n\nسُورَةُ النَّاسِ:\nبِسْمِ اللهِ الرَّحْمَنِ الرَّحِيْمِ\nقُلْ أَعُوذُ بِرَبِّ النَّاسِ ۞ مَلِكِ النَّاسِ ۞ إِلَٰهِ النَّاسِ ۞ مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ ۞ الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ ۞ مِنَ الْجِنَّةِ وَالنَّاسِ",
            latin = "Membaca Surah Al-Ikhlas, Surah Al-Falaq, dan Surah An-Naas secara lengkap.",
            translationId = "Dibaca 1x setelah setiap salat fardhu (Dzuhur, Ashar, dan Isya).\n📌 KETENTUAN SUNNAH KHUSUS: Dibaca masing-masing 3x setelah Salat Subuh dan Maghrib.",
            translationAr = "قراءة السور الثلاث المعوذة مرة بعد كل صلاة مكتوبة، وتكرارها ثلاث مرات بعد الفجر والمغرب للتحصين بالحفظ الإلهي.",
            category = "Zikir Bakda Salat Fardhu",
            reference = "HR. Abu Dawud No. 1523 & An-Nasa'i 3/68 (Hisnul Muslim No. 70)",
            referenceAr = "رواه أبو داود (رقم ١٥٢٣) والنسائي (٣/٦٨)"
        ),
        DuaItem(
            id = "dzikir_6_ayat_kursi",
            titleId = "6. Membaca Ayat Kursi Bakda Salat (HR. An-Nasa'i)",
            titleAr = "٦. قراءة آية الكرسي دبر كل صلاة",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            latin = "Allahu laa ilaaha illaa huwal-hayyul-qayyuum, laa ta'khudzuhu sinatuw-wa laa naum, lahu maa fis-samaawaati wa maa fil-ardh, man dzal-ladzii yasyfa'u 'indahuu illaa bi idznih, ya'lamu maa baina aidiihim wa maa khalfahum, wa laa yuhiithuuna bi syai-im min 'ilmihii illaa bi maa syaa-a, wasi'a kursiyyuhus-samaawaati wal-ardh, wa laa ya-uuduhuu hifdhuhumaa wa huwal-'aliyyul-'adhiim.",
            translationId = "Allah, tidak ada tuhan selain Dia. Yang Maha Hidup, yang terus menerus mengurus (makhluk-Nya)... (Keutamaan: Barangsiapa membacanya setiap selesai salat fardhu, tidak ada yang menghalanginya masuk surga selain kematian).",
            translationAr = "قراءة آية الكرسي العظيمة بعد الصلاة المكتوبة. فضلها: لم يمنعه من دخول الجنة إلا الموت.",
            category = "Zikir Bakda Salat Fardhu",
            reference = "HR. An-Nasa'i dalam 'Amal Al-Yaum wal Lailah No. 100 (Hisnul Muslim No. 71)",
            referenceAr = "رواه النسائي في عمل اليوم والليلة (رقم ١٠٠) وصححه الألباني"
        ),
        DuaItem(
            id = "dzikir_7_tauhid_subuh_maghrib",
            titleId = "7. Zikir Khusus 10x Bakda Salat Subuh & Maghrib (HR. At-Tirmidzi)",
            titleAr = "٧. الذكر ١٠ مرات بعد صلاتي المغرب والفجر",
            arabic = "لاَ إِلَهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيْكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ يُحْيِي وَيُمِيْتُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيْرٌ (١٠×)",
            latin = "Laa ilaha illallahu wahdahu laa syarika lah, lahul-mulku wa lahul-hamdu yuhyii wa yumiitu wa huwa 'ala kulli syai-in qadiir. (10x)",
            translationId = "Tiada sesembahan yang berhak disembah selain Allah Yang Maha Esa, tiada sekutu bagi-Nya. Bagi-Nya kerajaan dan bagi-Nya segala pujian, Dia yang menghidupkan dan yang mematikan, dan Dia Maha Kuasa atas segala sesuatu.\n📌 Dibaca 10x khusus setelah selesai Salat Subuh dan Maghrib sebelum merubah posisi/kaki dari tempat salat.",
            translationAr = "لا إله إلا الله وحده لا شريك له، له الملك وله الحمد يحيي ويميت وهو على كل شيء قدير. تُقال ١٠ مرات بعد صلاتي الفجر والمغرب قبل أن يثني رجليه.",
            category = "Zikir Bakda Salat Fardhu",
            reference = "HR. At-Tirmidzi No. 3534 & Ahmad 4/227 (Hisnul Muslim No. 72)",
            referenceAr = "رواه الترمذي (رقم ٣٥٣٤) وأحمد (٤/٢٢٧)"
        ),
        DuaItem(
            id = "dzikir_8_doa_subuh_ilmu",
            titleId = "8. Doa Permohonan Ilmu, Rezeki & Amal Khusus Bakda Subuh (HR. Ibnu Majah)",
            titleAr = "٨. الدعاء بعد السلام من صلاة الفجر",
            arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا طَيِّبًا، وَعَمَلاً مُتَقَبَّلاً",
            latin = "Allahumma innii as-aluka 'ilman naafi'an, wa rizqan thayyiban, wa 'amalan mutaqabbalan.",
            translationId = "Ya Allah, sesungguhnya aku memohon kepada-Mu ilmu yang bermanfaat, rezeki yang baik (halal), dan amal yang diterima.\n📌 Dibaca khusus setelah zikir/salam pada Salat Subuh.",
            translationAr = "اللهم إني أسألك علماً نافعاً، ورزقاً طيباً، وعملاً متقبلاً. يُقال بعد السلام من صلاة الفجر.",
            category = "Zikir Bakda Salat Fardhu",
            reference = "HR. Ibnu Majah No. 925 & An-Nasa'i (Hisnul Muslim No. 73)",
            referenceAr = "رواه ابن ماجة (رقم ٩٢٥) والنسائي في عمل اليوم والليلة"
        ),
        DuaItem(
            id = "dzikir_9_doa_bimbingan_ibadah",
            titleId = "9. Doa Mohon Bimbingan Zikir & Syukur (HR. Abu Dawud)",
            titleAr = "٩. دعاء طلب الإعانة على الذكر والشكر",
            arabic = "اللَّهُمَّ أَعِنِّي عَلَى ذِكْرِكَ وَشُكْرِكَ وَحُسْنِ عِبَادَتِكَ",
            latin = "Allahumma a'innii 'alaa dzikrika wa syukrika wa husni 'ibadaatik.",
            translationId = "Ya Allah, bantulah aku untuk selalu berzikir mengingat-Mu, bersyukur kepada-Mu, dan memperbagus ibadahku kepada-Mu.\n📌 Wasiat Rasulullah shallallahu 'alaihi wa sallam kepada Mu'adz bin Jabal radhiyallahu 'anhu untuk tidak meninggalkannya di akhir setiap salat.",
            translationAr = "اللهم أعني على ذكرك وشكرك وحسن عبادتك. دعاء مبارك أوصى به النبي صلى الله عليه وسلم معاذ بن جبل رضي الله عنه.",
            category = "Zikir Bakda Salat Fardhu",
            reference = "HR. Abu Dawud No. 1522 & An-Nasa'i No. 1303",
            referenceAr = "رواه أبو داود (رقم ١٥٢٢) والنسائي (رقم ١٣٠٣) بإسناد صحيح"
        ),
        DuaItem(
            id = "doa_sunnah_sebelum_salam",
            titleId = "Doa Perlindungan Dari 4 Hal (Sebelum Salam)",
            titleAr = "الاستعاذة من أربع قبل السلام",
            arabic = "اللَّهُمَّ إِنِّي أَعُوْذُ بِكَ مِنْ عَذَابِ جَهَنَّمَ، وَمِنْ عَذَابِ الْقَبْرِ، وَمِنْ فِتْنَةِ الْمَحْيَا وَالْمَمَاتِ، وَمِنْ شَرِّ فِتْنَةِ الْمَسِيْحِ الدَّجَّالِ",
            latin = "Allahumma innii a'uudzu bika min 'adzaabi jahannama, wa min 'adzaabil-qabri, wa min fitnatil-mahyaa wal-mamaat, wa min syarri fitnatil-masiihid-dajjaal.",
            translationId = "Ya Allah, sesungguhnya aku berlindung kepada-Mu dari azab Jahannam, dari azab kubur, dari fitnah kehidupan dan kematian, dan dari keburukan fitnah Masihid-Dajjal.",
            translationAr = "التعوذ بالله من عذاب جهنم، وعذاب القبر، وفتنة المحيا والممات، ومن شر فتنة المسيح الدجال قبل التسليم من الصلاة.",
            category = "Doa Sunnah Harian",
            reference = "HR. Bukhari No. 1377 & Muslim No. 588",
            referenceAr = "رواه البخاري (رقم ١٣٧٧) ومسلم (رقم ٥٨٨) عن أبي هريرة رضي الله عنه"
        ),
        DuaItem(
            id = "doa_sunnah_kebaikan_dunia_akhirat",
            titleId = "Doa Sapu Jagad (Kebaikan Dunia Akhirat)",
            titleAr = "دعاء ربنا آتنا في الدنيا حسنة",
            arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
            latin = "Rabbanaa aatinaa fid-dunyaa hasanatan wa fil-aakhirati hasanatan wa qinaa 'adzaaban-naar.",
            translationId = "Ya Rabb kami, berikanlah kami kebaikan di dunia dan kebaikan di akhirat dan lindungilah kami dari azab neraka.",
            translationAr = "دعاء جامع لخيري الدنيا والآخرة والسلامة من عذاب النار، وكان أكثر دعاء النبي صلى الله عليه وسلم.",
            category = "Doa Sunnah Harian",
            reference = "QS. Al-Baqarah: 201 & HR. Bukhari No. 6389",
            referenceAr = "سورة البقرة: ٢٠١ ورواه البخاري (رقم ٦٣٨٩) ومسلم (رقم ٢٦٩٠)"
        ),
        DuaItem(
            id = "dzikir_pagi_ayat_kursi",
            titleId = "1. Ayat Kursi",
            titleAr = "١. آية الكرسي (أذكار الصباح)",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            latin = "Allahu laa ilaaha illaa huwal-hayyul-qayyuum, laa ta'khudzuhu sinatuw-wa laa naum, lahu maa fis-samaawaati wa maa fil-ardh, man dzal-ladzii yasyfa'u 'indahuu illaa bi idznih, ya'lamu maa baina aidiihim wa maa khalfahum, wa laa yuhiithuuna bi syai-im min 'ilmihii illaa bi maa syaa-a, wasi'a kursiyyuhus-samaawaati wal-ardh, wa laa ya-uuduhuu hifdhuhumaa wa huwal-'aliyyul-'adhiim.",
            translationId = "Allah, tidak ada ilah (yang berhak disembah) melainkan Dia, yang hidup kekal lagi terus menerus mengurus (makhluk-Nya). Dia tidak mengantuk dan tidak tidur...",
            translationAr = "قراءة آية الكرسي في الصباح تجير القارئ من الجن والشياطين حتى يمسي.",
            category = "Dzikir Pagi",
            reference = "HR. Al-Hakim (1/562)",
            referenceAr = "رواه الحاكم (١/٥٦٢) وصححه الألباني"
        ),
        DuaItem(
            id = "dzikir_pagi_muawwidzat",
            titleId = "2. Al-Ikhlas, Al-Falaq, An-Naas (3x)",
            titleAr = "٢. قراءة المعوذات ثلاث مرات (صباحاً)",
            arabic = "قُلْ هُوَ اللَّهُ أَحَدٌ ... (٣×)\nقُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ... (٣×)\nقُلْ أَعُوذُ بِرَبِّ النَّاسِ ... (٣×)",
            latin = "Membaca Surah Al-Ikhlas, Surah Al-Falaq, dan Surah An-Naas (masing-masing 3x).",
            translationId = "Membaca Surah Al-Ikhlas, Al-Falaq, dan An-Naas masing-masing 3 kali. (Barangsiapa membacanya di pagi dan petang hari, maka akan dicukupkan dari segala sesuatu).",
            translationAr = "قراءة سورة الإخلاص، والفلق، والناس ثلاث مرات صباحاً ومساءً تكفي القارئ من كل سوء وشر.",
            category = "Dzikir Pagi",
            reference = "HR. Abu Dawud No. 5082 & At-Tirmidzi No. 3575",
            referenceAr = "رواه أبو داود (رقم ٥٠٨٢) والترمذي (رقم ٣٥٧٥) وقال حديث حسن صحيح"
        ),
        DuaItem(
            id = "dzikir_pagi_asbahna",
            titleId = "3. Doa Pagi Hari (Asbahna)",
            titleAr = "٣. أذكار الصباح (أصبحنا وأصبح الملك لله)",
            arabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ. رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَذَا الْيَوْمِ وَشَرِّ مَا بَعْدَهُ. رَبِّ أَعُوذُ بِكَ مِنَ الْكَسَلِ وَسُوءِ الْكِبَرِ، رَبِّ أَعُوذُ بِكَ مِنْ عَذَابٍ فِي النَّارِ وَعَذَابٍ فِي الْقَبْرِ",
            latin = "Ashbahnaa wa ashbahal-mulku lillaah, walhamdu lillaah laa ilaaha illallaah wahdahu laa syariika lah... Rabbi as-aluka khaira maa fii haadzal-yaum wa khaira maa ba'dahu...",
            translationId = "Kami telah memasuki waktu pagi dan kerajaan hanya milik Allah, segala puji bagi Allah. Tidak ada Tuhan (yang berhak disembah) selain Allah Yang Maha Esa, tiada sekutu bagi-Nya... Wahai Tuhanku, aku mohon kepada-Mu kebaikan di hari ini dan kebaikan sesudahnya...",
            translationAr = "افتتاح اليوم بالحمد والتوحيد وسؤال الله خير هذا اليوم وخير ما بعده والتعوذ من الشر والكسل وسوء الكبر وعذاب النار والقبر.",
            category = "Dzikir Pagi",
            reference = "HR. Muslim No. 2723",
            referenceAr = "رواه مسلم (رقم ٢٧٢٣) عن ابن مسعود رضي الله عنه"
        ),
        DuaItem(
            id = "dzikir_pagi_allahumma_bika",
            titleId = "4. Allahumma Bika Asbahna",
            titleAr = "٤. اللهم بك أصبحنا وبك أمسينا",
            arabic = "اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ",
            latin = "Allahumma bika ashbahnaa, wa bika amsaynaa, wa bika nahyaa, wa bika namuutu, wa ilaykan-nusyuur.",
            translationId = "Ya Allah, dengan rahmat dan pertolongan-Mu kami memasuki waktu pagi, dan dengan-Mu kami memasuki waktu petang. Dengan-Mu kami hidup dan dengan-Mu kami mati. Dan kepada-Mu kebangkitan (bagi semua makhluk).",
            translationAr = "الإقرار بفضل الله ورحمته في استقبال الصباح والمساء، والحياة والموت وإليه النشور.",
            category = "Dzikir Pagi",
            reference = "HR. At-Tirmidzi No. 3391",
            referenceAr = "رواه الترمذي (رقم ٣٣٩١) وأبو داود (رقم ٥٠٦٨)"
        ),
        DuaItem(
            id = "dzikir_pagi_sayyidul_istighfar",
            titleId = "5. Sayyidul Istighfar",
            titleAr = "٥. سيد الاستغفار (دعاء التوبة الأعظم)",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي؛ فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            latin = "Allahumma anta rabbii laa ilaaha illaa anta, khalaqtanii wa anaa 'abduka, wa anaa 'alaa 'ahdika wa wa'dika masta-tha'tu, a'uudzu bika min syarri maa shana'tu, abuu-u laka bini'matika 'alayya, wa abuu-u laka bidzanbii faghfir lii...",
            translationId = "Ya Allah, Engkau adalah Tuhanku, tidak ada Tuhan yang berhak disembah selain Engkau. Engkau telah menciptakanku dan aku adalah hamba-Mu... Aku mengakui nikmat-Mu kepadaku dan aku mengakui dosaku kepada-Mu, maka ampunilah aku...",
            translationAr = "أفضل وأعظم صيغ الاستغفار والتوبة، من قالها موقناً بها صباحاً ومات قبل المساء دخل الجنة.",
            category = "Dzikir Pagi",
            reference = "HR. Bukhari No. 6306",
            referenceAr = "رواه البخاري (رقم ٦٣٠٦) عن شداد بن أوس رضي الله عنه"
        ),
        DuaItem(
            id = "dzikir_pagi_radhitu_billahi",
            titleId = "6. Ridha kepada Allah (3x)",
            titleAr = "٦. الرضا بالله رباً وبالإسلام ديناً",
            arabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا (٣×)",
            latin = "Radhiitu billaahi rabbaa, wa bil-islaami diinaa, wa bimuhammadin shallallaahu 'alaihi wa sallama nabiyyaa. (3x)",
            translationId = "Aku ridha Allah sebagai Rabb-ku, Islam sebagai agamaku, dan Muhammad shallallahu 'alaihi wa sallam sebagai Nabiku. (3 kali)",
            translationAr = "الرضا بالله رباً وبالإسلام ديناً وبمحمد صلى الله عليه وسلم نبياً ورسولاً ثلاث مرات. فضلها: كان حقاً على الله أن يرضيه يوم القيامة.",
            category = "Dzikir Pagi",
            reference = "HR. Abu Dawud No. 5072",
            referenceAr = "رواه أبو داود (رقم ٥٠٧٢) وأحمد (٤/٣٣٧) بإسناد حسن"
        ),
        
        // DZIKIR PETANG
        DuaItem(
            id = "dzikir_petang_ayat_kursi",
            titleId = "1. Ayat Kursi",
            titleAr = "١. آية الكرسي (أذكار المساء)",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            latin = "Allahu laa ilaaha illaa huwal-hayyul-qayyuum, laa ta'khudzuhu sinatuw-wa laa naum... wa huwal-'aliyyul-'adhiim.",
            translationId = "Allah, tidak ada ilah (yang berhak disembah) melainkan Dia, yang hidup kekal lagi terus menerus mengurus (makhluk-Nya)...",
            translationAr = "قراءة آية الكرسي في المساء تجير القارئ من الجن والشياطين حتى يصبح.",
            category = "Dzikir Petang",
            reference = "HR. Al-Hakim (1/562)",
            referenceAr = "رواه الحاكم (١/٥٦٢) وصححه الألباني"
        ),
        DuaItem(
            id = "dzikir_petang_muawwidzat",
            titleId = "2. Al-Ikhlas, Al-Falaq, An-Naas (3x)",
            titleAr = "٢. قراءة المعوذات ثلاث مرات (مساءً)",
            arabic = "قُلْ هُوَ اللَّهُ أَحَدٌ ... (٣×)\nقُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ... (٣×)\nقُلْ أَعُوذُ بِرَبِّ النَّاسِ ... (٣×)",
            latin = "Membaca Surah Al-Ikhlas, Surah Al-Falaq, dan Surah An-Naas (masing-masing 3x).",
            translationId = "Membaca Surah Al-Ikhlas, Al-Falaq, dan An-Naas masing-masing 3 kali.",
            translationAr = "قراءة سورة الإخلاص، والفلق، والناس ثلاث مرات في المساء تكفي القارئ من كل شيء.",
            category = "Dzikir Petang",
            reference = "HR. Abu Dawud No. 5082 & At-Tirmidzi No. 3575",
            referenceAr = "رواه أبو داود (رقم ٥٠٨٢) والترمذي (رقم ٣٥٧٥)"
        ),
        DuaItem(
            id = "dzikir_petang_amsayna",
            titleId = "3. Doa Petang Hari (Amsayna)",
            titleAr = "٣. أذكار المساء (أمسينا وأمسى الملك لله)",
            arabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ. رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَذِهِ اللَّيْلَةِ وَخَيْرَ مَا بَعْدَهَا، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَذِهِ اللَّيْلَةِ وَشَرِّ مَا بَعْدَهَا. رَبِّ أَعُوذُ بِكَ مِنَ الْكَسَلِ وَسُوءِ الْكِبَرِ، رَبِّ أَعُوذُ بِكَ مِنْ عَذَابٍ فِي النَّارِ وَعَذَابٍ فِي الْقَبْرِ",
            latin = "Amsainaa wa amsayal-mulku lillaah, walhamdu lillaah laa ilaaha illallaah wahdahu laa syariika lah... Rabbi as-aluka khaira maa fii haadzihil-lailah wa khaira maa ba'dahaa...",
            translationId = "Kami telah memasuki waktu petang dan kerajaan hanya milik Allah, segala puji bagi Allah... Wahai Tuhanku, aku mohon kepada-Mu kebaikan di malam ini dan kebaikan sesudahnya...",
            translationAr = "استقبال الليل بالحمد والتوحيد وسؤال الله خير هذه الليلة وخير ما بعدها والتعوذ من الشر والكسل وعذاب القبر والنار.",
            category = "Dzikir Petang",
            reference = "HR. Muslim No. 2723",
            referenceAr = "رواه مسلم (رقم ٢٧٢٣) عن ابن مسعود رضي الله عنه"
        ),
        DuaItem(
            id = "dzikir_petang_allahumma_bika",
            titleId = "4. Allahumma Bika Amsayna",
            titleAr = "٤. اللهم بك أمسينا وبك أصبحنا",
            arabic = "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ الْمَصِيرُ",
            latin = "Allahumma bika amsaynaa, wa bika ashbahnaa, wa bika nahyaa, wa bika namuutu, wa ilaykal-mashiir.",
            translationId = "Ya Allah, dengan rahmat dan pertolongan-Mu kami memasuki waktu petang, dan dengan-Mu kami memasuki waktu pagi. Dengan-Mu kami hidup dan dengan-Mu kami mati. Dan kepada-Mu tempat kembali (bagi semua makhluk).",
            translationAr = "الإقرار بفضل الله وحفظه في استقبال المساء والصباح والحياة والموت وإليه المصير.",
            category = "Dzikir Petang",
            reference = "HR. At-Tirmidzi No. 3391",
            referenceAr = "رواه الترمذي (رقم ٣٣٩١) وأبو داود (رقم ٥٠٦٩)"
        ),
        DuaItem(
            id = "dzikir_petang_sayyidul_istighfar",
            titleId = "5. Sayyidul Istighfar",
            titleAr = "٥. سيد الاستغفار (مساءً)",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي؛ فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            latin = "Allahumma anta rabbii laa ilaaha illaa anta, khalaqtanii wa anaa 'abduka, wa anaa 'alaa 'ahdika wa wa'dika masta-tha'tu, a'uudzu bika min syarri maa shana'tu, abuu-u laka bini'matika 'alayya, wa abuu-u laka bidzanbii faghfir lii...",
            translationId = "Ya Allah, Engkau adalah Tuhanku, tidak ada Tuhan yang berhak disembah selain Engkau. Engkau telah menciptakanku dan aku adalah hamba-Mu... Aku mengakui nikmat-Mu kepadaku dan aku mengakui dosaku kepada-Mu, maka ampunilah aku...",
            translationAr = "أعظم صيغ الاستغفار والتوبة، من قالها موقناً بها مساءً ومات قبل الصباح دخل الجنة.",
            category = "Dzikir Petang",
            reference = "HR. Bukhari No. 6306",
            referenceAr = "رواه البخاري (رقم ٦٣٠٦) عن شداد بن أوس رضي الله عنه"
        ),
        DuaItem(
            id = "dzikir_petang_radhitu_billahi",
            titleId = "6. Ridha kepada Allah (3x)",
            titleAr = "٦. الرضا بالله رباً (مساءً)",
            arabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا (٣×)",
            latin = "Radhiitu billaahi rabbaa, wa bil-islaami diinaa, wa bimuhammadin shallallaahu 'alaihi wa sallama nabiyyaa. (3x)",
            translationId = "Aku ridha Allah sebagai Rabb-ku, Islam sebagai agamaku, dan Muhammad shallallahu 'alaihi wa sallam sebagai Nabiku. (3 kali)",
            translationAr = "الرضا بالله رباً وبالإسلام ديناً وبمحمد صلى الله عليه وسلم نبياً ثلاث مرات. كان حقاً على الله أن يرضيه يوم القيامة.",
            category = "Dzikir Petang",
            reference = "HR. Abu Dawud No. 5072",
            referenceAr = "رواه أبو داود (رقم ٥٠٧٢) وأحمد (٤/٣٣٧)"
        )
    )
}
