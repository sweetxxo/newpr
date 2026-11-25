package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ContentRepository
import com.example.model.ChecklistItem
import com.example.model.ContentPipelineItem
import com.example.model.RevisionLogEntry
import com.example.model.WorkflowStage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class WorkspaceViewMode {
    PIPELINE,
    FLOWCHART
}

data class PRPlannerUiState(
    val items: List<ContentPipelineItem> = emptyList(),
    val selectedItemId: String? = null,
    val viewMode: WorkspaceViewMode = WorkspaceViewMode.PIPELINE,
    val isLoading: Boolean = true,
    val statusBannerMessage: String? = null
) {
    val activeItem: ContentPipelineItem?
        get() = items.firstOrNull { it.id == selectedItemId } ?: items.firstOrNull()
}

class PRPlannerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ContentRepository by lazy {
        val db = AppDatabase.getInstance(application)
        ContentRepository(db.contentDao())
    }

    private val selectedIdFlow = MutableStateFlow<String?>(null)
    private val viewModeFlow = MutableStateFlow(WorkspaceViewMode.PIPELINE)
    private val isLoadingFlow = MutableStateFlow(true)
    private val bannerFlow = MutableStateFlow<String?>(null)

    val uiState: StateFlow<PRPlannerUiState> = combine(
        repository.allItems,
        selectedIdFlow,
        viewModeFlow,
        isLoadingFlow,
        bannerFlow
    ) { items, selectedId, viewMode, loading, banner ->
        val resolvedId = if (items.any { it.id == selectedId }) selectedId else items.firstOrNull()?.id
        PRPlannerUiState(
            items = items,
            selectedItemId = resolvedId,
            viewMode = viewMode,
            isLoading = loading,
            statusBannerMessage = banner
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PRPlannerUiState()
    )

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            isLoadingFlow.value = false
        }
    }

    fun selectItem(id: String) {
        selectedIdFlow.value = id
    }

    fun setViewMode(mode: WorkspaceViewMode) {
        viewModeFlow.value = mode
    }

    fun clearBanner() {
        bannerFlow.value = null
    }

    fun showBanner(message: String) {
        bannerFlow.value = message
    }

    fun updateActiveItem(transform: (ContentPipelineItem) -> ContentPipelineItem) {
        val current = uiState.value.activeItem ?: return
        val updated = transform(current).copy(updatedAtMillis = System.currentTimeMillis())
        viewModelScope.launch {
            repository.upsert(updated)
        }
    }

    fun jumpToStage(stage: WorkflowStage) {
        updateActiveItem { item ->
            item.copy(
                currentStage = stage,
                highestStepReached = maxOf(item.highestStepReached, stage.stepNumber)
            )
        }
    }

    fun advanceTo(stage: WorkflowStage) {
        updateActiveItem { item ->
            item.copy(
                currentStage = stage,
                highestStepReached = maxOf(item.highestStepReached, stage.stepNumber)
            )
        }
        bannerFlow.value = "Berlanjut ke tahap: ${stage.shortTitle}"
    }

    // Stage 4 Decision: "Apakah Perlu Revisi?"
    fun decideOutlineRevision(needsRevision: Boolean, note: String) {
        val current = uiState.value.activeItem ?: return
        if (needsRevision) {
            val nextCount = current.outlineRevisionCount + 1
            val finalNote = note.ifBlank { "Perlu penyempurnaan hook & struktur poin utama." }
            val logEntry = RevisionLogEntry(
                id = "rev_${UUID.randomUUID().toString().take(6)}",
                stageName = "Revisi Outline #$nextCount",
                note = finalNote,
                timestampLabel = currentTimestampLabel()
            )
            updateActiveItem { item ->
                item.copy(
                    currentStage = WorkflowStage.REVISI_OUTLINE,
                    outlineRevisionCount = nextCount,
                    outlineRevisionNotes = finalNote,
                    lastOutlineDecision = "Ya",
                    revisionLogs = listOf(logEntry) + item.revisionLogs
                )
            }
            bannerFlow.value = "Masuk Revisi Outline (#$nextCount)"
        } else {
            updateActiveItem { item ->
                item.copy(
                    currentStage = WorkflowStage.JADWAL_TAKE,
                    highestStepReached = maxOf(item.highestStepReached, WorkflowStage.JADWAL_TAKE.stepNumber),
                    lastOutlineDecision = "Tidak"
                )
            }
            bannerFlow.value = "Outline disetujui — lanjut ke Jadwal Take Konten"
        }
    }

    fun completeOutlineRevision() {
        updateActiveItem { item ->
            item.copy(currentStage = WorkflowStage.OUTLINE_CONTENT)
        }
        bannerFlow.value = "Revisi outline diterapkan — kembali ke Outline Content"
    }

    // Stage 5 Checklist helpers
    fun toggleChecklistItem(checklistId: String) {
        updateActiveItem { item ->
            item.copy(
                productionChecklist = item.productionChecklist.map { chk ->
                    if (chk.id == checklistId) chk.copy(isChecked = !chk.isChecked) else chk
                }
            )
        }
    }

    fun addChecklistItem(label: String) {
        val trimmed = label.trim()
        if (trimmed.isEmpty()) return
        updateActiveItem { item ->
            val newChk = ChecklistItem(
                id = "chk_${UUID.randomUUID().toString().take(6)}",
                label = trimmed,
                isChecked = false
            )
            item.copy(productionChecklist = item.productionChecklist + newChk)
        }
    }

    // Stage 6 Decision: "Hasil konten: Layak / Tidak Layak"
    fun decideProductionQuality(isLayak: Boolean, note: String) {
        val current = uiState.value.activeItem ?: return
        if (!isLayak) {
            val nextCount = current.productionRevisionCount + 1
            val finalNote = note.ifBlank { "Kualitas visual/audio atau kesesuaian skrip perlu diperbaiki." }
            val logEntry = RevisionLogEntry(
                id = "rev_${UUID.randomUUID().toString().take(6)}",
                stageName = "Revisi Produksi #$nextCount",
                note = finalNote,
                timestampLabel = currentTimestampLabel()
            )
            updateActiveItem { item ->
                item.copy(
                    currentStage = WorkflowStage.REVISI_PRODUKSI,
                    productionStatus = "Perlu Revisi",
                    productionRevisionCount = nextCount,
                    productionRevisionNotes = finalNote,
                    lastProductionDecision = "Tidak Layak",
                    revisionLogs = listOf(logEntry) + item.revisionLogs
                )
            }
            bannerFlow.value = "Hasil Tidak Layak — masuk Revisi Produksi (#$nextCount)"
        } else {
            updateActiveItem { item ->
                item.copy(
                    currentStage = WorkflowStage.PUBLIKASI_KONTEN,
                    highestStepReached = maxOf(item.highestStepReached, WorkflowStage.PUBLIKASI_KONTEN.stepNumber),
                    productionStatus = "Layak Tayang",
                    lastProductionDecision = "Layak"
                )
            }
            bannerFlow.value = "Konten dinyatakan Layak — lanjut ke Publikasi Konten"
        }
    }

    fun completeProductionRevision() {
        updateActiveItem { item ->
            item.copy(
                currentStage = WorkflowStage.PRODUKSI_KONTEN,
                productionStatus = "Editing Ulang"
            )
        }
        bannerFlow.value = "Kembali ke tahap Produksi Konten untuk kurasi ulang"
    }

    // Stage 7 -> Stage 8
    fun publishAndContinue() {
        updateActiveItem { item ->
            item.copy(
                isPublished = true,
                publishChannel = item.publishChannel.ifBlank { item.platform },
                currentStage = WorkflowStage.RESPON_PUBLIK,
                highestStepReached = maxOf(item.highestStepReached, WorkflowStage.RESPON_PUBLIK.stepNumber)
            )
        }
        bannerFlow.value = "Konten diterbitkan — pantau Respon Publik (Engagement)"
    }

    // Stage 8 Decision: "Respon publik (engagement) bagus?: Ya / Tidak"
    fun decideEngagementQuality(isGood: Boolean, note: String) {
        val current = uiState.value.activeItem ?: return
        if (!isGood) {
            val nextCount = current.engagementRevisionCount + 1
            val finalNote = note.ifBlank { "Engagement di bawah target — optimasi ulang hook, format, atau editing produksi." }
            val logEntry = RevisionLogEntry(
                id = "rev_${UUID.randomUUID().toString().take(6)}",
                stageName = "Revisi Engagement #$nextCount",
                note = finalNote,
                timestampLabel = currentTimestampLabel()
            )
            updateActiveItem { item ->
                item.copy(
                    currentStage = WorkflowStage.REVISI_ENGAGEMENT,
                    engagementRevisionCount = nextCount,
                    engagementRevisionNotes = finalNote,
                    lastEngagementDecision = "Tidak",
                    revisionLogs = listOf(logEntry) + item.revisionLogs
                )
            }
            bannerFlow.value = "Respon belum optimal — masuk Revisi (#$nextCount)"
        } else {
            updateActiveItem { item ->
                item.copy(
                    currentStage = WorkflowStage.EVALUASI_LAPORAN,
                    highestStepReached = maxOf(item.highestStepReached, WorkflowStage.EVALUASI_LAPORAN.stepNumber),
                    lastEngagementDecision = "Ya"
                )
            }
            bannerFlow.value = "Engagement bagus — lanjut ke Evaluasi & Laporan Hasil"
        }
    }

    fun completeEngagementRevisionToProduction() {
        updateActiveItem { item ->
            item.copy(
                currentStage = WorkflowStage.PRODUKSI_KONTEN,
                productionStatus = "Produksi Ulang (Optimasi)"
            )
        }
        bannerFlow.value = "Kembali ke Produksi Konten dengan catatan optimasi"
    }

    // Stage 9 Decision: "Lanjut bikin konten lain?: Ya / Tidak"
    fun decideContinueNextContent(continueNext: Boolean) {
        val current = uiState.value.activeItem ?: return
        viewModelScope.launch {
            if (continueNext) {
                val updatedSource = current.copy(
                    continueNextContentDecision = "Ya",
                    updatedAtMillis = System.currentTimeMillis()
                )
                repository.upsert(updatedSource)
                val nextItem = repository.createNextContentFromCampaign(updatedSource)
                selectedIdFlow.value = nextItem.id
                bannerFlow.value = "Konten baru dibuat dari konteks kampanye — mulai di Riset & Analisis Audiens"
            } else {
                val finished = current.copy(
                    continueNextContentDecision = "Tidak",
                    currentStage = WorkflowStage.SELESAI,
                    highestStepReached = WorkflowStage.SELESAI.stepNumber,
                    updatedAtMillis = System.currentTimeMillis()
                )
                repository.upsert(finished)
                bannerFlow.value = "Alur kerja konten ditandai Selesai!"
            }
        }
    }

    fun createNewItem(campaignName: String, contentTitle: String) {
        viewModelScope.launch {
            val created = repository.createBlankItem(
                campaignName = campaignName.ifBlank { "Program Kehumasan Baru" },
                contentTitle = contentTitle.ifBlank { "Ide Konten PR Baru" }
            )
            selectedIdFlow.value = created.id
            bannerFlow.value = "Konten baru ditambahkan ke pipeline"
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            repository.deleteById(id)
            bannerFlow.value = "Item konten dihapus"
        }
    }

    fun resetToSampleData() {
        viewModelScope.launch {
            repository.resetToSampleData()
            selectedIdFlow.value = null
            bannerFlow.value = "Data direset ke contoh PR awal"
        }
    }

    fun buildExportJson(): String {
        return repository.exportItemsToJson(uiState.value.items)
    }

    private fun currentTimestampLabel(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
        return formatter.format(Date())
    }
}
