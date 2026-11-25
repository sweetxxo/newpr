package com.example.data

import com.example.model.ChecklistItem
import com.example.model.ContentPipelineItem
import com.example.model.RevisionLogEntry
import com.example.model.WorkflowStage

object SampleData {
    fun initialItems(): List<ContentPipelineItem> {
        val now = System.currentTimeMillis()
        return listOf(
            ContentPipelineItem(
                id = "pr_item_01",
                campaignName = "Kampanye Transparansi Layanan Publik 2026",
                contentTitle = "Reels: 60 Detik Mengenal Alur Pengaduan Digital",
                currentStage = WorkflowStage.OUTLINE_CONTENT,
                highestStepReached = 4,
                audienceSegments = listOf("Mahasiswa & Gen-Z", "Masyarakat Umum"),
                demographics = "Usia 18–34 tahun, urban & sub-urban, aktif di Instagram & TikTok pada jam pulang kerja.",
                painPoints = "Publik merasa prosedur pengaduan layanan lambat, berbelit, dan tidak transparan statusnya.",
                audienceInsights = "Video vertikal berdurasi <60 detik dengan teks besar dan contoh alur visual meningkatkan retensi hingga 2,4x.",
                campaignGoals = listOf("Brand Awareness", "Edukasi Publik", "Trust & Reputasi"),
                keyMessages = "Setiap laporan publik diproses transparan, terpantau real-time, dan mendapat respons maksimal 2x24 jam.",
                targetKpi = "Reach 25.000+ | Save rate >= 4% | Sentimen komentar positif >= 85%",
                contentAngle = "Behind-the-scenes proses respon tim layanan + simulasi langkah cepat 1-2-3 di aplikasi.",
                contentFormat = "Video Pendek / Reels",
                platform = "Instagram",
                trendCompetitorNotes = "Format POV staf layanan dan overlay checklist terbukti viral di akun instansi pelayanan publik kota lain.",
                outlineHook = "\"Masih mikir ngelapor layanan publik butuh berhari-hari antre? Tonton 3 langkah ini.\"",
                outlineKeyPoints = "1. Masalah klasik antre manual (0-5 dtk)\n2. Demo cepat kanal pengaduan QR & nomor tiket (6-30 dtk)\n3. Bukti notifikasi progres real-time (31-48 dtk)",
                outlineCta = "Simpan video ini dan cek link di bio untuk panduan lengkap layanan publik.",
                outlineRevisionCount = 1,
                outlineRevisionNotes = "Revisi #1: Hook dipertajam agar langsung menyebut estimasi waktu respon 2x24 jam.",
                lastOutlineDecision = "Ya",
                takeDate = "2026-11-28",
                takeTime = "09:30 WIB",
                takeLocation = "Ruang Pelayanan Terpadu Lantai 1",
                takePic = "Farisy (Sutradara), Abimanyu (Kamera), Nauval (Talent)",
                productionChecklist = listOf(
                    ChecklistItem("chk_1", "Skrip & storyboard final disetujui Kabag Humas", true),
                    ChecklistItem("chk_2", "Izin lokasi Ruang Pelayanan Terpadu", true),
                    ChecklistItem("chk_3", "Mic clip-on wireless & gimbal siap pakai", false),
                    ChecklistItem("chk_4", "Seragam resmi humas & ID card talent", false)
                ),
                revisionLogs = listOf(
                    RevisionLogEntry(
                        id = "log_1",
                        stageName = "Revisi Outline #1",
                        note = "Hook pembuka diubah agar lebih menonjolkan janji SLA respon 2x24 jam.",
                        timestampLabel = "25 Nov 2026, 09:15"
                    )
                ),
                updatedAtMillis = now
            ),
            ContentPipelineItem(
                id = "pr_item_02",
                campaignName = "Program Beasiswa & Inovasi Kampus",
                contentTitle = "Carousel IG: 5 Mitos vs Fakta Seleksi Beasiswa",
                currentStage = WorkflowStage.RESPON_PUBLIK,
                highestStepReached = 8,
                audienceSegments = listOf("Mahasiswa & Gen-Z", "Calon Pendaftar"),
                demographics = "Usia 17–23 tahun, siswa SMA akhir & mahasiswa semester awal seluruh Indonesia.",
                painPoints = "Banyak calon pelamar ragu mendaftar karena misinformasi soal syarat IPK dan surat rekomendasi.",
                audienceInsights = "Konten Carousel 'Mitos vs Fakta' sangat sering di-save dan dibagikan ke grup WhatsApp/LINE.",
                campaignGoals = listOf("Edukasi Publik", "Engagement"),
                keyMessages = "Seleksi beasiswa terbuka bagi mahasiswa berprestasi maupun pegiat sosial dengan alur transparan tanpa biaya.",
                targetKpi = "Engagement Rate >= 6% | Minimal 400 Shares",
                contentAngle = "Klarifikasi interaktif 7 slide dengan warna kontras merah (Mitos) vs hijau (Fakta).",
                contentFormat = "Carousel Instagram",
                platform = "Instagram",
                trendCompetitorNotes = "Slide pertama menggunakan tipografi besar 5 kata maksimal meningkatkan swipe-through rate.",
                outlineHook = "Slide 1: \"Katanya Beasiswa Ini Khusus IPK 3.9? Cek 5 Faktanya!\"",
                outlineKeyPoints = "Slide 2-6: 5 poin Mitos vs Fakta terverifikasi panitia\nSlide 7: Jadwal pendaftaran & tautan FAQ resmi.",
                outlineCta = "Bagikan ke teman yang butuh info ini & tulis pertanyaanmu di kolom komentar!",
                outlineRevisionCount = 0,
                lastOutlineDecision = "Tidak",
                takeDate = "2026-11-20",
                takeTime = "13:00 WIB",
                takeLocation = "Studio Desain Humas",
                takePic = "Abimanyu (Desain Grafis), Nauval (Copywriter)",
                productionChecklist = listOf(
                    ChecklistItem("chk_1", "Verifikasi data syarat beasiswa dari Biro Akademik", true),
                    ChecklistItem("chk_2", "Asset grafis 1080x1350px selesai", true),
                    ChecklistItem("chk_3", "Alt-text aksesibilitas tiap slide disiapkan", true)
                ),
                productionStatus = "Final Disetujui",
                draftLink = "https://drive.internal/humas/carousel-beasiswa-v2",
                productionNotes = "Warna kontras sudah lulus cek keterbacaan mobile.",
                productionRevisionCount = 0,
                lastProductionDecision = "Layak",
                publishDate = "2026-11-23",
                publishTime = "19:00 WIB",
                publishChannel = "Instagram @humasresmi",
                publishCaption = "Jangan sampai gagal daftar gara-gara mitos! Geser sampai akhir untuk cek fakta resmi Seleksi Beasiswa 2026. #InfoBeasiswa #HumasTransparan",
                isPublished = true,
                metricReach = 18450,
                metricLikesCommentsShares = 1420,
                metricSavesCtr = "612 Saves • CTR 4.8%",
                engagementNotes = "Banyak komentar menanyakan batas usia pendaftar dan format esai.",
                engagementRevisionCount = 0,
                updatedAtMillis = now - 60_000L
            ),
            ContentPipelineItem(
                id = "pr_item_03",
                campaignName = "Laporan Keberlanjutan & Eco-Campus",
                contentTitle = "Siaran Pers & Infografis: Pengurangan Plastik 40%",
                currentStage = WorkflowStage.SELESAI,
                highestStepReached = 10,
                audienceSegments = listOf("Media & Jurnalis", "Mitra Industri", "Masyarakat Umum"),
                demographics = "Jurnalis lokal/nasional, pemangku kepentingan, dan komunitas lingkungan usia 22–50 tahun.",
                painPoints = "Kurangnya data terukur mengenai dampak nyata program kampus hijau.",
                audienceInsights = "Infografis ringkas satu halaman mempermudah media mengutip angka capaian secara akurat.",
                campaignGoals = listOf("Brand Awareness", "Trust & Reputasi"),
                keyMessages = "Program Eco-Campus berhasil menekan sampah plastik sekali pakai hingga 40% dalam 6 bulan.",
                targetKpi = "Dimuat di minimal 5 media massa & 500+ unduhan laporan",
                contentAngle = "Data-driven storytelling berbasis perbandingan sebelum dan sesudah program berjalan.",
                contentFormat = "Infografis",
                platform = "LinkedIn",
                trendCompetitorNotes = "Visualisasi data sederhana dengan ikon metrik besar mendapat impresi tinggi dari mitra korporat.",
                outlineHook = "Headline: \"40% Lebih Sedikit Plastik Sekali Pakai: Dampak Nyata 6 Bulan Eco-Campus\"",
                outlineKeyPoints = "1. Metrik pengurangan 4,2 ton plastik\n2. Partisipasi 18 unit kerja\n3. Rencana target 2027",
                outlineCta = "Unduh ringkasan laporan keberlanjutan di portal berita resmi kami.",
                outlineRevisionCount = 0,
                lastOutlineDecision = "Tidak",
                takeDate = "2026-11-10",
                takeTime = "10:00 WIB",
                takeLocation = "Ruang Rapat Media Center",
                takePic = "Farisy (PR Officer), Abimanyu (Visual Data)",
                productionChecklist = listOf(
                    ChecklistItem("chk_1", "Audit angka capaian dari Tim Lingkungan", true),
                    ChecklistItem("chk_2", "Kutipan resmi pimpinan disetujui", true)
                ),
                productionStatus = "Selesai",
                draftLink = "https://drive.internal/humas/infografis-ecocampus-final",
                productionNotes = "Sudah melewati 1 kali revisi penyelarasan label grafik.",
                productionRevisionCount = 1,
                productionRevisionNotes = "Perbesar angka 40% pada panel utama agar terbaca di feed LinkedIn.",
                lastProductionDecision = "Layak",
                publishDate = "2026-11-14",
                publishTime = "10:00 WIB",
                publishChannel = "LinkedIn & Portal Siaran Pers",
                publishCaption = "Transparansi capaian 6 bulan Eco-Campus: pengurangan 40% plastik sekali pakai berkat kolaborasi seluruh sivitas.",
                isPublished = true,
                metricReach = 31200,
                metricLikesCommentsShares = 2180,
                metricSavesCtr = "485 Saves • CTR 6.2%",
                engagementNotes = "Dikutip oleh 7 media regional dan mendapat respons positif dari mitra CSR.",
                lastEngagementDecision = "Ya",
                evaluationLearnings = "Infografis berbasis angka konkret + kutipan singkat pimpinan sangat efektif menarik perhatian jurnalis.",
                performanceTakeaway = "Target impresi dan liputan media terlampaui 140% pada jam tayang pagi hari kerja (10:00 WIB).",
                reportNotes = "Dokumentasi disahkan untuk arsip Laporan Kehumasan Triwulan IV.",
                continueNextContentDecision = "Tidak",
                revisionLogs = listOf(
                    RevisionLogEntry(
                        id = "log_2",
                        stageName = "Revisi Produksi #1",
                        note = "Ukuran tipografi angka 40% diperbesar pada infografis utama.",
                        timestampLabel = "12 Nov 2026, 15:40"
                    )
                ),
                updatedAtMillis = now - 120_000L
            )
        )
    }
}
