package com.example.model

enum class WorkflowStage(
    val stepNumber: Int,
    val title: String,
    val shortTitle: String,
    val subtitle: String,
    val isRevisionNode: Boolean = false,
    val isTerminal: Boolean = false
) {
    RISET_AUDIENS(
        stepNumber = 1,
        title = "Riset & Analisis Audiens",
        shortTitle = "Riset Audiens",
        subtitle = "Identifikasi segmen audiens, demografi, kebutuhan, dan insight publik"
    ),
    TUJUAN_KAMPANYE(
        stepNumber = 2,
        title = "Tentukan Tujuan Kampanye",
        shortTitle = "Tujuan Kampanye",
        subtitle = "Tetapkan objektif kehumasan dan pesan kunci yang ingin disampaikan"
    ),
    ANALISIS_CONTENT(
        stepNumber = 3,
        title = "Analisis Content",
        shortTitle = "Analisis Konten",
        subtitle = "Tentukan angle, format konten, kanal distribusi, serta tren kompetitor"
    ),
    OUTLINE_CONTENT(
        stepNumber = 4,
        title = "Outline Content & Keputusan",
        shortTitle = "Outline Konten",
        subtitle = "Susun kerangka hook, poin utama, CTA, dan tinjau apakah perlu revisi"
    ),
    REVISI_OUTLINE(
        stepNumber = 4,
        title = "Revisi Outline",
        shortTitle = "Revisi Outline",
        subtitle = "Perbaiki struktur outline berdasarkan catatan tinjauan sebelum jadwal take",
        isRevisionNode = true
    ),
    JADWAL_TAKE(
        stepNumber = 5,
        title = "Penentuan Jadwal Take Konten",
        shortTitle = "Jadwal Take",
        subtitle = "Atur tanggal, jam, lokasi, PIC, dan daftar periksa praproduksi"
    ),
    PRODUKSI_KONTEN(
        stepNumber = 6,
        title = "Produksi Konten & Kurasi Layak",
        shortTitle = "Produksi Konten",
        subtitle = "Pantau proses produksi, tautan draft, dan uji kelayakan hasil konten"
    ),
    REVISI_PRODUKSI(
        stepNumber = 6,
        title = "Revisi Produksi",
        shortTitle = "Revisi Produksi",
        subtitle = "Perbaiki aset visual/audio atau penyuntingan karena hasil belum layak",
        isRevisionNode = true
    ),
    PUBLIKASI_KONTEN(
        stepNumber = 7,
        title = "Publikasi Konten",
        shortTitle = "Publikasi",
        subtitle = "Jadwalkan jam tayang optimal, finalisasi caption, dan terbitkan konten"
    ),
    RESPON_PUBLIK(
        stepNumber = 8,
        title = "Respon Publik (Engagement)",
        shortTitle = "Respon Publik",
        subtitle = "Pantau metrik jangkauan serta interaksi dan evaluasi performa respon publik"
    ),
    REVISI_ENGAGEMENT(
        stepNumber = 8,
        title = "Revisi Optimasi Konten",
        shortTitle = "Revisi Konten",
        subtitle = "Catat perbaikan konten lalu kembali ke tahap Produksi Konten",
        isRevisionNode = true
    ),
    EVALUASI_LAPORAN(
        stepNumber = 9,
        title = "Evaluasi & Laporan Hasil",
        shortTitle = "Evaluasi Hasil",
        subtitle = "Rangkum pembelajaran kampanye dan putuskan apakah lanjut bikin konten lain"
    ),
    SELESAI(
        stepNumber = 10,
        title = "Program Konten Selesai",
        shortTitle = "Selesai",
        subtitle = "Seluruh alur penjadwalan konten kehumasan telah selesai dan terdokumentasi",
        isTerminal = true
    );

    companion object {
        val primaryPipelineStages: List<WorkflowStage> = listOf(
            RISET_AUDIENS,
            TUJUAN_KAMPANYE,
            ANALISIS_CONTENT,
            OUTLINE_CONTENT,
            JADWAL_TAKE,
            PRODUKSI_KONTEN,
            PUBLIKASI_KONTEN,
            RESPON_PUBLIK,
            EVALUASI_LAPORAN,
            SELESAI
        )

        fun fromNameOrDefault(name: String): WorkflowStage {
            return entries.firstOrNull { it.name == name } ?: RISET_AUDIENS
        }
    }
}
