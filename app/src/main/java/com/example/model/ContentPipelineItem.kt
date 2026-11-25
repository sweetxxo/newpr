package com.example.model

data class ChecklistItem(
    val id: String,
    val label: String,
    val isChecked: Boolean = false
)

data class RevisionLogEntry(
    val id: String,
    val stageName: String,
    val note: String,
    val timestampLabel: String
)

data class ContentPipelineItem(
    val id: String,
    val campaignName: String,
    val contentTitle: String,
    val currentStage: WorkflowStage = WorkflowStage.RISET_AUDIENS,
    val highestStepReached: Int = 1,

    // Stage 1: Riset & Analisis Audiens
    val audienceSegments: List<String> = emptyList(),
    val demographics: String = "",
    val painPoints: String = "",
    val audienceInsights: String = "",

    // Stage 2: Tentukan Tujuan Kampanye
    val campaignGoals: List<String> = emptyList(),
    val keyMessages: String = "",
    val targetKpi: String = "",

    // Stage 3: Analisis Content
    val contentAngle: String = "",
    val contentFormat: String = "Video Pendek / Reels",
    val platform: String = "Instagram",
    val trendCompetitorNotes: String = "",

    // Stage 4: Outline Content & Decision ("Apakah Perlu Revisi?")
    val outlineHook: String = "",
    val outlineKeyPoints: String = "",
    val outlineCta: String = "",
    val outlineRevisionCount: Int = 0,
    val outlineRevisionNotes: String = "",
    val lastOutlineDecision: String = "", // "Ya" | "Tidak" | ""

    // Stage 5: Penentuan Jadwal Take Konten
    val takeDate: String = "",
    val takeTime: String = "",
    val takeLocation: String = "",
    val takePic: String = "",
    val productionChecklist: List<ChecklistItem> = defaultChecklist(),

    // Stage 6: Produksi Konten & Decision ("Hasil konten: Layak / Tidak Layak")
    val productionStatus: String = "Persiapan",
    val draftLink: String = "",
    val productionNotes: String = "",
    val productionRevisionCount: Int = 0,
    val productionRevisionNotes: String = "",
    val lastProductionDecision: String = "", // "Layak" | "Tidak Layak" | ""

    // Stage 7: Publikasi Konten
    val publishDate: String = "",
    val publishTime: String = "",
    val publishChannel: String = "",
    val publishCaption: String = "",
    val isPublished: Boolean = false,

    // Stage 8: Respon Publik ("Respon publik (engagement) bagus?: Ya / Tidak")
    val metricReach: Int = 0,
    val metricLikesCommentsShares: Int = 0,
    val metricSavesCtr: String = "",
    val engagementNotes: String = "",
    val engagementRevisionCount: Int = 0,
    val engagementRevisionNotes: String = "",
    val lastEngagementDecision: String = "", // "Ya" | "Tidak" | ""

    // Stage 9: Evaluasi & Laporan Hasil & Decision ("Lanjut bikin konten lain?: Ya / Tidak")
    val evaluationLearnings: String = "",
    val performanceTakeaway: String = "",
    val reportNotes: String = "",
    val continueNextContentDecision: String = "", // "Ya" | "Tidak" | ""

    // Revision history across workflow
    val revisionLogs: List<RevisionLogEntry> = emptyList(),
    val updatedAtMillis: Long = System.currentTimeMillis()
) {
    val totalRevisions: Int
        get() = outlineRevisionCount + productionRevisionCount + engagementRevisionCount

    companion object {
        fun defaultChecklist(): List<ChecklistItem> = listOf(
            ChecklistItem("chk_1", "Skrip & poin pesan kunci telah disetujui", false),
            ChecklistItem("chk_2", "Konfirmasi narasumber / talent & jadwal", false),
            ChecklistItem("chk_3", "Peralatan kamera, audio, dan pencahayaan siap", false),
            ChecklistItem("chk_4", "Atribut merek & lokasi take steril/kondusif", false)
        )
    }
}
