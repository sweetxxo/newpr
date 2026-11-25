package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.ContentPipelineItem
import com.example.model.WorkflowStage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Stage1RisetAudiensForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onNext: () -> Unit
) {
    val presetSegments = listOf(
        "Mahasiswa & Gen-Z",
        "Masyarakat Umum",
        "Media & Jurnalis",
        "Mitra Industri",
        "Karyawan Internal",
        "Calon Pendaftar"
    )
    var customSegment by remember(item.id) { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = "Segmen Audiens Target",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            (presetSegments + item.audienceSegments.filterNot { it in presetSegments }).forEach { seg ->
                val selected = seg in item.audienceSegments
                FilterChip(
                    selected = selected,
                    onClick = {
                        onUpdate { cur ->
                            val next = if (selected) cur.audienceSegments - seg else cur.audienceSegments + seg
                            cur.copy(audienceSegments = next)
                        }
                    },
                    label = { Text(seg) }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = customSegment,
                onValueChange = { customSegment = it },
                label = { Text("Tambah Segmen Lain") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedButton(
                onClick = {
                    val trimmed = customSegment.trim()
                    if (trimmed.isNotEmpty()) {
                        onUpdate { cur ->
                            if (trimmed in cur.audienceSegments) cur
                            else cur.copy(audienceSegments = cur.audienceSegments + trimmed)
                        }
                        customSegment = ""
                    }
                },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah", modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Tambah")
            }
        }

        OutlinedTextField(
            value = item.demographics,
            onValueChange = { v -> onUpdate { it.copy(demographics = v) } },
            label = { Text("Demografi & Perilaku Audiens") },
            placeholder = { Text("Rentang usia, domisili, profesi, dan jam aktif media sosial") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage1_demographics_input")
        )
        OutlinedTextField(
            value = item.painPoints,
            onValueChange = { v -> onUpdate { it.copy(painPoints = v) } },
            label = { Text("Pain Points / Kebutuhan Informasi Publik") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage1_painpoints_input")
        )
        OutlinedTextField(
            value = item.audienceInsights,
            onValueChange = { v -> onUpdate { it.copy(audienceInsights = v) } },
            label = { Text("Insight Utama Riset Audiens") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage1_insights_input")
        )

        StagePrimaryActionButton(
            text = "Simpan & Lanjut ke Tujuan Kampanye",
            testTag = "stage1_next_button",
            onClick = onNext
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Stage2TujuanKampanyeForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onNext: () -> Unit
) {
    val presetGoals = listOf(
        "Brand Awareness",
        "Edukasi Publik",
        "Engagement",
        "Trust & Reputasi",
        "Klarifikasi / Krisis",
        "Aktivasi Event"
    )

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = "Pilih Tujuan Kampanye Kehumasan",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            presetGoals.forEach { goal ->
                val selected = goal in item.campaignGoals
                FilterChip(
                    selected = selected,
                    onClick = {
                        onUpdate { cur ->
                            val next = if (selected) cur.campaignGoals - goal else cur.campaignGoals + goal
                            cur.copy(campaignGoals = next)
                        }
                    },
                    label = { Text(goal) },
                    modifier = Modifier.testTag("goal_chip_$goal")
                )
            }
        }

        OutlinedTextField(
            value = item.keyMessages,
            onValueChange = { v -> onUpdate { it.copy(keyMessages = v) } },
            label = { Text("Pesan Kunci (Key Messages)") },
            placeholder = { Text("Pernyataan inti yang wajib tertangkap oleh publik") },
            minLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage2_key_messages_input")
        )
        OutlinedTextField(
            value = item.targetKpi,
            onValueChange = { v -> onUpdate { it.copy(targetKpi = v) } },
            label = { Text("Target Indikator Keberhasilan (KPI)") },
            placeholder = { Text("Contoh: Reach 20.000+, ER >= 5%, Sentimen positif >= 85%") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage2_kpi_input")
        )

        StagePrimaryActionButton(
            text = "Simpan & Lanjut ke Analisis Content",
            testTag = "stage2_next_button",
            onClick = onNext
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Stage3AnalisisContentForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onNext: () -> Unit
) {
    val formats = listOf(
        "Video Pendek / Reels",
        "Carousel Instagram",
        "Artikel / Siaran Pers",
        "Infografis",
        "Thread / Microblog",
        "Podcast / Audio"
    )
    val platforms = listOf(
        "Instagram",
        "TikTok",
        "LinkedIn",
        "YouTube Shorts",
        "Portal Berita / Website",
        "X (Twitter)"
    )

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        OutlinedTextField(
            value = item.contentAngle,
            onValueChange = { v -> onUpdate { it.copy(contentAngle = v) } },
            label = { Text("Sudut Pandang / Content Angle") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage3_angle_input")
        )

        Text("Format Konten", style = MaterialTheme.typography.titleSmall)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            formats.forEach { fmt ->
                FilterChip(
                    selected = item.contentFormat == fmt,
                    onClick = { onUpdate { it.copy(contentFormat = fmt) } },
                    label = { Text(fmt) }
                )
            }
        }

        Text("Platform Distribusi Utama", style = MaterialTheme.typography.titleSmall)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            platforms.forEach { plat ->
                FilterChip(
                    selected = item.platform == plat,
                    onClick = { onUpdate { it.copy(platform = plat) } },
                    label = { Text(plat) }
                )
            }
        }

        OutlinedTextField(
            value = item.trendCompetitorNotes,
            onValueChange = { v -> onUpdate { it.copy(trendCompetitorNotes = v) } },
            label = { Text("Catatan Tren & Analisis Kompetitor") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage3_trend_input")
        )

        StagePrimaryActionButton(
            text = "Simpan & Lanjut ke Outline Content",
            testTag = "stage3_next_button",
            onClick = onNext
        )
    }
}

@Composable
fun Stage4OutlineContentForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onDecideRevision: (needsRevision: Boolean, note: String) -> Unit
) {
    var pendingNote by remember(item.id, item.outlineRevisionCount) {
        mutableStateOf("")
    }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        OutlinedTextField(
            value = item.outlineHook,
            onValueChange = { v -> onUpdate { it.copy(outlineHook = v) } },
            label = { Text("Hook Pembuka (3 Detik Pertama / Headline)") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage4_hook_input")
        )
        OutlinedTextField(
            value = item.outlineKeyPoints,
            onValueChange = { v -> onUpdate { it.copy(outlineKeyPoints = v) } },
            label = { Text("Poin-Poin Utama Isi Konten") },
            minLines = 4,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage4_points_input")
        )
        OutlinedTextField(
            value = item.outlineCta,
            onValueChange = { v -> onUpdate { it.copy(outlineCta = v) } },
            label = { Text("Call to Action (CTA)") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage4_cta_input")
        )

        DecisionBoxCard(
            title = "Keputusan Tahap 4: Apakah Perlu Revisi?",
            subtitle = "Jumlah revisi outline saat ini: ${item.outlineRevisionCount} kali. Jika Ya, sistem akan mengarahkan ke cabang Revisi Outline.",
            noteValue = pendingNote,
            onNoteChange = { pendingNote = it },
            noteLabel = "Catatan Revisi Outline (diisi bila memilih Ya)",
            negativeButtonText = "Ya — Revisi Outline",
            negativeButtonTag = "stage4_decision_yes_revision",
            onNegative = { onDecideRevision(true, pendingNote) },
            positiveButtonText = "Tidak — Jadwal Take",
            positiveButtonTag = "stage4_decision_no_proceed",
            onPositive = { onDecideRevision(false, pendingNote) },
            isErrorTone = false
        )
    }
}

@Composable
fun Stage4RevisiOutlineForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onCompleteRevision: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        RevisionActiveBanner(
            title = "Cabang Aktif: Revisi Outline (#${item.outlineRevisionCount})",
            description = "Sempurnakan catatan revisi serta perbaiki hook, poin utama, atau CTA sebelum kembali ke Outline Content."
        )
        OutlinedTextField(
            value = item.outlineRevisionNotes,
            onValueChange = { v -> onUpdate { it.copy(outlineRevisionNotes = v) } },
            label = { Text("Catatan Perbaikan Outline") },
            minLines = 2,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = item.outlineHook,
            onValueChange = { v -> onUpdate { it.copy(outlineHook = v) } },
            label = { Text("Perbarui Hook Pembuka") },
            minLines = 2,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = item.outlineKeyPoints,
            onValueChange = { v -> onUpdate { it.copy(outlineKeyPoints = v) } },
            label = { Text("Perbarui Poin-Poin Utama") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onCompleteRevision,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .testTag("complete_outline_revision_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Replay, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Selesai Revisi — Kembali ke Outline Content")
        }
    }
}

@Composable
fun Stage5JadwalTakeForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onToggleChecklist: (String) -> Unit,
    onAddChecklist: (String) -> Unit,
    onNext: () -> Unit
) {
    var newChecklistText by remember(item.id) { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = item.takeDate,
                onValueChange = { v -> onUpdate { it.copy(takeDate = v) } },
                label = { Text("Tanggal Take") },
                placeholder = { Text("2026-11-30") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("stage5_date_input")
            )
            OutlinedTextField(
                value = item.takeTime,
                onValueChange = { v -> onUpdate { it.copy(takeTime = v) } },
                label = { Text("Jam Take") },
                placeholder = { Text("09:30 WIB") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("stage5_time_input")
            )
        }
        OutlinedTextField(
            value = item.takeLocation,
            onValueChange = { v -> onUpdate { it.copy(takeLocation = v) } },
            label = { Text("Lokasi Pengambilan Konten") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage5_location_input")
        )
        OutlinedTextField(
            value = item.takePic,
            onValueChange = { v -> onUpdate { it.copy(takePic = v) } },
            label = { Text("PIC / Tim Produksi Bertanggung Jawab") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage5_pic_input")
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        val doneCount = item.productionChecklist.count { it.isChecked }
        Text(
            text = "Daftar Periksa Praproduksi ($doneCount/${item.productionChecklist.size} siap)",
            style = MaterialTheme.typography.titleSmall
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item.productionChecklist.forEach { chk ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onToggleChecklist(chk.id) }
                        .testTag("checklist_item_${chk.id}"),
                    shape = RoundedCornerShape(10.dp),
                    color = if (chk.isChecked) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = chk.isChecked,
                            onCheckedChange = { onToggleChecklist(chk.id) }
                        )
                        Text(
                            text = chk.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newChecklistText,
                onValueChange = { newChecklistText = it },
                label = { Text("Tambah Item Checklist") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedButton(
                onClick = {
                    onAddChecklist(newChecklistText)
                    newChecklistText = ""
                },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah checklist", modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Tambah")
            }
        }

        StagePrimaryActionButton(
            text = "Jadwal Siap — Lanjut ke Produksi Konten",
            testTag = "stage5_next_button",
            onClick = onNext
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Stage6ProduksiKontenForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onDecideQuality: (isLayak: Boolean, note: String) -> Unit
) {
    val statuses = listOf("Persiapan", "Shooting / Take", "Editing", "Siap Review", "Layak Tayang")
    var pendingNote by remember(item.id, item.productionRevisionCount) { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Status Produksi Saat Ini", style = MaterialTheme.typography.titleSmall)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            statuses.forEach { status ->
                FilterChip(
                    selected = item.productionStatus == status,
                    onClick = { onUpdate { it.copy(productionStatus = status) } },
                    label = { Text(status) }
                )
            }
        }

        OutlinedTextField(
            value = item.draftLink,
            onValueChange = { v -> onUpdate { it.copy(draftLink = v) } },
            label = { Text("Tautan Draft / Repositori Aset Media") },
            placeholder = { Text("https://drive.internal/...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage6_draft_link_input")
        )
        OutlinedTextField(
            value = item.productionNotes,
            onValueChange = { v -> onUpdate { it.copy(productionNotes = v) } },
            label = { Text("Catatan Produksi & Penyuntingan") },
            minLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage6_notes_input")
        )

        DecisionBoxCard(
            title = "Keputusan Tahap 6: Hasil Konten Layak / Tidak Layak?",
            subtitle = "Revisi produksi tercatat: ${item.productionRevisionCount} kali. Jika Tidak Layak, alur masuk ke Revisi Produksi.",
            noteValue = pendingNote,
            onNoteChange = { pendingNote = it },
            noteLabel = "Catatan Revisi Produksi (bila Tidak Layak)",
            negativeButtonText = "Tidak Layak — Revisi",
            negativeButtonTag = "stage6_decision_not_feasible",
            onNegative = { onDecideQuality(false, pendingNote) },
            positiveButtonText = "Layak — Publikasi",
            positiveButtonTag = "stage6_decision_feasible",
            onPositive = { onDecideQuality(true, pendingNote) },
            isErrorTone = true
        )
    }
}

@Composable
fun Stage6RevisiProduksiForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onCompleteRevision: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        RevisionActiveBanner(
            title = "Cabang Aktif: Revisi Produksi (#${item.productionRevisionCount})",
            description = "Lakukan perbaikan visual, audio, atau teks overlay lalu kembalikan ke tahap Produksi Konten untuk kurasi ulang."
        )
        OutlinedTextField(
            value = item.productionRevisionNotes,
            onValueChange = { v -> onUpdate { it.copy(productionRevisionNotes = v) } },
            label = { Text("Catatan Revisi Produksi") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = item.draftLink,
            onValueChange = { v -> onUpdate { it.copy(draftLink = v) } },
            label = { Text("Tautan Draft Hasil Perbaikan") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onCompleteRevision,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .testTag("complete_production_revision_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Replay, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Selesai Revisi — Kembali ke Produksi Konten")
        }
    }
}

@Composable
fun Stage7PublikasiForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onPublishAndNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (item.isPublished) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(
                1.dp,
                if (item.isPublished) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (item.isPublished) Icons.Filled.CheckCircle else Icons.Filled.Publish,
                        contentDescription = null,
                        tint = if (item.isPublished) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (item.isPublished) "Status: Sudah Diterbitkan (Published)"
                        else "Status: Menunggu Publikasi",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                OutlinedButton(
                    onClick = { onUpdate { it.copy(isPublished = !it.isPublished) } },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(if (item.isPublished) "Batalkan" else "Tandai Published")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = item.publishDate,
                onValueChange = { v -> onUpdate { it.copy(publishDate = v) } },
                label = { Text("Tanggal Tayang") },
                placeholder = { Text("2026-12-01") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = item.publishTime,
                onValueChange = { v -> onUpdate { it.copy(publishTime = v) } },
                label = { Text("Jam Optimal") },
                placeholder = { Text("19:00 WIB") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = item.publishChannel,
            onValueChange = { v -> onUpdate { it.copy(publishChannel = v) } },
            label = { Text("Kanal Publikasi Resmi") },
            placeholder = { Text("Instagram @humasresmi / Portal Berita") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = item.publishCaption,
            onValueChange = { v -> onUpdate { it.copy(publishCaption = v) } },
            label = { Text("Caption & Tagar Final") },
            minLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage7_caption_input")
        )

        Button(
            onClick = onPublishAndNext,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .testTag("stage7_publish_next_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Icon(Icons.Filled.Publish, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Terbitkan Konten & Pantau Respon Publik")
        }
    }
}

@Composable
fun Stage8ResponPublikForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onDecideEngagement: (isGood: Boolean, note: String) -> Unit
) {
    var pendingNote by remember(item.id, item.engagementRevisionCount) { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = if (item.metricReach == 0) "" else item.metricReach.toString(),
                onValueChange = { v ->
                    val parsed = v.filter { it.isDigit() }.toIntOrNull() ?: 0
                    onUpdate { it.copy(metricReach = parsed) }
                },
                label = { Text("Reach / Jangkauan") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("stage8_reach_input")
            )
            OutlinedTextField(
                value = if (item.metricLikesCommentsShares == 0) "" else item.metricLikesCommentsShares.toString(),
                onValueChange = { v ->
                    val parsed = v.filter { it.isDigit() }.toIntOrNull() ?: 0
                    onUpdate { it.copy(metricLikesCommentsShares = parsed) }
                },
                label = { Text("Likes + Komen + Share") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("stage8_interactions_input")
            )
        }
        OutlinedTextField(
            value = item.metricSavesCtr,
            onValueChange = { v -> onUpdate { it.copy(metricSavesCtr = v) } },
            label = { Text("Saves & CTR") },
            placeholder = { Text("Contoh: 540 Saves • CTR 4.5%") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = item.engagementNotes,
            onValueChange = { v -> onUpdate { it.copy(engagementNotes = v) } },
            label = { Text("Ringkasan Sentimen & Respon Komentar Publik") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        DecisionBoxCard(
            title = "Keputusan Tahap 8: Respon Publik (Engagement) Bagus?",
            subtitle = "Jika Tidak, alur masuk ke Revisi Optimasi lalu kembali ke tahap Produksi Konten.",
            noteValue = pendingNote,
            onNoteChange = { pendingNote = it },
            noteLabel = "Catatan Optimasi / Edit Ulang (bila memilih Tidak)",
            negativeButtonText = "Tidak — Revisi Konten",
            negativeButtonTag = "stage8_decision_no_revise",
            onNegative = { onDecideEngagement(false, pendingNote) },
            positiveButtonText = "Ya — Evaluasi Hasil",
            positiveButtonTag = "stage8_decision_yes_evaluate",
            onPositive = { onDecideEngagement(true, pendingNote) },
            isErrorTone = true
        )
    }
}

@Composable
fun Stage8RevisiEngagementForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onReturnToProduction: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        RevisionActiveBanner(
            title = "Cabang Aktif: Revisi Optimasi Konten (#${item.engagementRevisionCount})",
            description = "Engagement belum memenuhi target. Catat rencana perbaikan lalu alur akan kembali ke tahap Produksi Konten."
        )
        OutlinedTextField(
            value = item.engagementRevisionNotes,
            onValueChange = { v -> onUpdate { it.copy(engagementRevisionNotes = v) } },
            label = { Text("Rencana Perbaikan / Re-edit Konten") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onReturnToProduction,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .testTag("complete_engagement_revision_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Replay, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Terapkan Revisi & Kembali ke Produksi Konten")
        }
    }
}

@Composable
fun Stage9EvaluasiLaporanForm(
    item: ContentPipelineItem,
    onUpdate: ((ContentPipelineItem) -> ContentPipelineItem) -> Unit,
    onDecideContinue: (continueNext: Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        OutlinedTextField(
            value = item.evaluationLearnings,
            onValueChange = { v -> onUpdate { it.copy(evaluationLearnings = v) } },
            label = { Text("Pembelajaran Utama (Key Learnings)") },
            minLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage9_learnings_input")
        )
        OutlinedTextField(
            value = item.performanceTakeaway,
            onValueChange = { v -> onUpdate { it.copy(performanceTakeaway = v) } },
            label = { Text("Kesimpulan Performa Kampanye") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stage9_takeaway_input")
        )
        OutlinedTextField(
            value = item.reportNotes,
            onValueChange = { v -> onUpdate { it.copy(reportNotes = v) } },
            label = { Text("Catatan Laporan Resmi Kehumasan") },
            minLines = 2,
            modifier = Modifier.fillMaxWidth()
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Keputusan Tahap 9: Lanjut Bikin Konten Lain?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Pilih Ya untuk membuat item konten baru yang otomatis membawa konteks kampanye ini dan kembali ke Riset & Analisis Audiens, atau Tidak untuk menandai workflow Selesai.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDecideContinue(true) },
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("stage9_decision_yes_next_content"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Ya — Konten Baru")
                    }
                    Button(
                        onClick = { onDecideContinue(false) },
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("stage9_decision_no_finish"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Tidak — Selesai")
                    }
                }
            }
        }
    }
}

@Composable
private fun StagePrimaryActionButton(
    text: String,
    testTag: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text)
        Spacer(Modifier.width(8.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun RevisionActiveBanner(title: String, description: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Filled.Loop,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.size(20.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@Composable
private fun DecisionBoxCard(
    title: String,
    subtitle: String,
    noteValue: String,
    onNoteChange: (String) -> Unit,
    noteLabel: String,
    negativeButtonText: String,
    negativeButtonTag: String,
    onNegative: () -> Unit,
    positiveButtonText: String,
    positiveButtonTag: String,
    onPositive: () -> Unit,
    isErrorTone: Boolean
) {
    val negColor: Color = if (isErrorTone) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = noteValue,
                onValueChange = onNoteChange,
                label = { Text(noteLabel) },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onNegative,
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag(negativeButtonTag),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.2.dp, negColor),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = negColor)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(negativeButtonText)
                }
                Button(
                    onClick = onPositive,
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag(positiveButtonTag),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(positiveButtonText)
                }
            }
        }
    }
}
