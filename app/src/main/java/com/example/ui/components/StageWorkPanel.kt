package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ContentPipelineItem
import com.example.model.WorkflowStage
import com.example.ui.PRPlannerViewModel

@Composable
fun StageWorkPanel(
    item: ContentPipelineItem,
    viewModel: PRPlannerViewModel,
    onOpenFlowchart: () -> Unit,
    onOpenExport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 760.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campaign & Content Context Card
            CampaignContextCard(
                item = item,
                onUpdateCampaignName = { name -> viewModel.updateActiveItem { it.copy(campaignName = name) } },
                onUpdateContentTitle = { title -> viewModel.updateActiveItem { it.copy(contentTitle = title) } },
                onOpenFlowchart = onOpenFlowchart
            )

            // Active Stage Working Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    StageHeaderRow(stage = item.currentStage)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                    when (item.currentStage) {
                        WorkflowStage.RISET_AUDIENS -> Stage1RisetAudiensForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onNext = { viewModel.advanceTo(WorkflowStage.TUJUAN_KAMPANYE) }
                        )
                        WorkflowStage.TUJUAN_KAMPANYE -> Stage2TujuanKampanyeForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onNext = { viewModel.advanceTo(WorkflowStage.ANALISIS_CONTENT) }
                        )
                        WorkflowStage.ANALISIS_CONTENT -> Stage3AnalisisContentForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onNext = { viewModel.advanceTo(WorkflowStage.OUTLINE_CONTENT) }
                        )
                        WorkflowStage.OUTLINE_CONTENT -> Stage4OutlineContentForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onDecideRevision = viewModel::decideOutlineRevision
                        )
                        WorkflowStage.REVISI_OUTLINE -> Stage4RevisiOutlineForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onCompleteRevision = viewModel::completeOutlineRevision
                        )
                        WorkflowStage.JADWAL_TAKE -> Stage5JadwalTakeForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onToggleChecklist = viewModel::toggleChecklistItem,
                            onAddChecklist = viewModel::addChecklistItem,
                            onNext = { viewModel.advanceTo(WorkflowStage.PRODUKSI_KONTEN) }
                        )
                        WorkflowStage.PRODUKSI_KONTEN -> Stage6ProduksiKontenForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onDecideQuality = viewModel::decideProductionQuality
                        )
                        WorkflowStage.REVISI_PRODUKSI -> Stage6RevisiProduksiForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onCompleteRevision = viewModel::completeProductionRevision
                        )
                        WorkflowStage.PUBLIKASI_KONTEN -> Stage7PublikasiForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onPublishAndNext = viewModel::publishAndContinue
                        )
                        WorkflowStage.RESPON_PUBLIK -> Stage8ResponPublikForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onDecideEngagement = viewModel::decideEngagementQuality
                        )
                        WorkflowStage.REVISI_ENGAGEMENT -> Stage8RevisiEngagementForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onReturnToProduction = viewModel::completeEngagementRevisionToProduction
                        )
                        WorkflowStage.EVALUASI_LAPORAN -> Stage9EvaluasiLaporanForm(
                            item = item,
                            onUpdate = viewModel::updateActiveItem,
                            onDecideContinue = viewModel::decideContinueNextContent
                        )
                        WorkflowStage.SELESAI -> CompletionSummaryCard(
                            item = item,
                            onStartNextContent = { viewModel.decideContinueNextContent(true) },
                            onReopenEvaluation = { viewModel.jumpToStage(WorkflowStage.EVALUASI_LAPORAN) },
                            onExport = onOpenExport
                        )
                    }
                }
            }

            // Revision Log Timeline Card
            if (item.revisionLogs.isNotEmpty()) {
                RevisionHistoryCard(item = item)
            }

            TeamCreditFooter()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CampaignContextCard(
    item: ContentPipelineItem,
    onUpdateCampaignName: (String) -> Unit,
    onUpdateContentTitle: (String) -> Unit,
    onOpenFlowchart: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KONTEKS KAMPANYE & KONTEN",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                TextButton(
                    onClick = onOpenFlowchart,
                    modifier = Modifier.testTag("quick_open_flowchart_button")
                ) {
                    Icon(Icons.Filled.AccountTree, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Lihat Peta Alur")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = item.campaignName,
                    onValueChange = onUpdateCampaignName,
                    label = { Text("Nama Kampanye PR") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("campaign_name_input")
                )
                OutlinedTextField(
                    value = item.contentTitle,
                    onValueChange = onUpdateContentTitle,
                    label = { Text("Judul Konten") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("content_title_input")
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                InfoPill(
                    label = "Tahap: ${item.currentStage.shortTitle}",
                    bg = stageStatusColor(item.currentStage).copy(alpha = 0.14f),
                    fg = stageStatusColor(item.currentStage)
                )
                InfoPill(
                    label = "Total Revisi: ${item.totalRevisions}",
                    bg = if (item.totalRevisions > 0) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant,
                    fg = if (item.totalRevisions > 0) MaterialTheme.colorScheme.onTertiaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                InfoPill(
                    label = "${item.contentFormat} • ${item.platform}",
                    bg = MaterialTheme.colorScheme.surfaceVariant,
                    fg = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StageHeaderRow(stage: WorkflowStage) {
    val badgeColor = stageStatusColor(stage)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(badgeColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (stage == WorkflowStage.SELESAI) "✓" else "${stage.stepNumber}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stage.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stage.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CompletionSummaryCard(
    item: ContentPipelineItem,
    onStartNextContent: () -> Unit,
    onReopenEvaluation: () -> Unit,
    onExport: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(34.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = "Workflow Selesai & Terdokumentasi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Konten \"${item.contentTitle}\" telah menyelesaikan seluruh siklus Penjadwalan Konten Optimal.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SummaryMetricTile("Kanal Publikasi", item.publishChannel.ifBlank { item.platform })
            SummaryMetricTile("Reach Publik", "${item.metricReach}")
            SummaryMetricTile("Total Interaksi", "${item.metricLikesCommentsShares}")
            SummaryMetricTile("Total Revisi", "${item.totalRevisions} kali")
        }

        if (item.performanceTakeaway.isNotBlank() || item.evaluationLearnings.isNotBlank()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Ringkasan Evaluasi", style = MaterialTheme.typography.titleSmall)
                    if (item.performanceTakeaway.isNotBlank()) {
                        Text(item.performanceTakeaway, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (item.evaluationLearnings.isNotBlank()) {
                        Text(
                            "Pembelajaran: ${item.evaluationLearnings}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onReopenEvaluation,
                modifier = Modifier
                    .weight(1f)
                    .minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Filled.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Edit Evaluasi")
            }
            OutlinedButton(
                onClick = onExport,
                modifier = Modifier
                    .weight(1f)
                    .minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Filled.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Ekspor JSON")
            }
        }
        Button(
            onClick = onStartNextContent,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .testTag("finished_create_next_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Buat Konten Baru untuk Kampanye Ini")
        }
    }
}

@Composable
private fun SummaryMetricTile(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun RevisionHistoryCard(item: ContentPipelineItem) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Riwayat Revisi (${item.revisionLogs.size})",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            item.revisionLogs.forEach { log ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = log.stageName,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = log.timestampLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = log.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoPill(
    label: String,
    bg: androidx.compose.ui.graphics.Color,
    fg: androidx.compose.ui.graphics.Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = fg,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
        )
    }
}
