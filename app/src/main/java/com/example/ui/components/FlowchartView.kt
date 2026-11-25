package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ContentPipelineItem
import com.example.model.WorkflowStage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowchartView(
    item: ContentPipelineItem,
    onSelectStageAndOpenWorkspace: (WorkflowStage) -> Unit,
    modifier: Modifier = Modifier
) {
    val active = item.currentStage
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
                .widthIn(max = 820.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Legend & Context Header
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Peta Alur Kerja (Flowchart) — ${item.contentTitle}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Ketuk simpul mana saja untuk membuka tahap tersebut di Ruang Kerja.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "Aktif: ${active.shortTitle}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LegendPill("Simpul Aktif", MaterialTheme.colorScheme.primary)
                        LegendPill("Tahap Selesai", MaterialTheme.colorScheme.secondary)
                        LegendPill("Cabang Revisi", MaterialTheme.colorScheme.tertiary)
                        LegendPill("Cabang Tidak Layak", MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Start badge
            FlowTerminalPill(
                label = "MULAI PROGRAM KEHUMASAN",
                icon = Icons.Filled.PlayArrow,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            DownArrowConnector()

            // Stage 1
            FlowProcessNode(
                stage = WorkflowStage.RISET_AUDIENS,
                activeStage = active,
                summaryText = if (item.audienceSegments.isNotEmpty())
                    item.audienceSegments.joinToString(", ") else "Segmen audiens & demografi",
                onClick = { onSelectStageAndOpenWorkspace(WorkflowStage.RISET_AUDIENS) }
            )
            DownArrowConnector()

            // Stage 2
            FlowProcessNode(
                stage = WorkflowStage.TUJUAN_KAMPANYE,
                activeStage = active,
                summaryText = if (item.campaignGoals.isNotEmpty())
                    item.campaignGoals.joinToString(", ") else "Tujuan kampanye & pesan kunci",
                onClick = { onSelectStageAndOpenWorkspace(WorkflowStage.TUJUAN_KAMPANYE) }
            )
            DownArrowConnector()

            // Stage 3
            FlowProcessNode(
                stage = WorkflowStage.ANALISIS_CONTENT,
                activeStage = active,
                summaryText = "${item.contentFormat} • ${item.platform}",
                onClick = { onSelectStageAndOpenWorkspace(WorkflowStage.ANALISIS_CONTENT) }
            )
            DownArrowConnector()

            // Stage 4 + Decision + Revisi Outline Branch
            FlowProcessNode(
                stage = WorkflowStage.OUTLINE_CONTENT,
                activeStage = active,
                summaryText = item.outlineHook.ifBlank { "Hook, poin utama & CTA" },
                revisionBadgeCount = item.outlineRevisionCount,
                onClick = { onSelectStageAndOpenWorkspace(WorkflowStage.OUTLINE_CONTENT) }
            )
            DownArrowConnector()

            FlowDecisionNodeWithBranch(
                question = "Apakah Perlu Revisi?",
                chosenDecision = item.lastOutlineDecision,
                positiveLabel = "Tidak → Jadwal Take",
                branchLabel = "Ya → Revisi Outline",
                branchStage = WorkflowStage.REVISI_OUTLINE,
                activeStage = active,
                branchRevisionCount = item.outlineRevisionCount,
                branchLoopHint = "↺ Kembali ke 4. Outline Content",
                isBranchError = false,
                onOpenBranch = { onSelectStageAndOpenWorkspace(WorkflowStage.REVISI_OUTLINE) }
            )
            DownArrowConnector(label = "Tidak")

            // Stage 5
            FlowProcessNode(
                stage = WorkflowStage.JADWAL_TAKE,
                activeStage = active,
                summaryText = listOf(item.takeDate, item.takeTime, item.takeLocation)
                    .filter { it.isNotBlank() }
                    .joinToString(" • ")
                    .ifBlank { "Jadwal pengambilan & checklist" },
                onClick = { onSelectStageAndOpenWorkspace(WorkflowStage.JADWAL_TAKE) }
            )
            DownArrowConnector()

            // Stage 6 + Decision + Revisi Produksi Branch
            FlowProcessNode(
                stage = WorkflowStage.PRODUKSI_KONTEN,
                activeStage = active,
                summaryText = "Status: ${item.productionStatus}",
                revisionBadgeCount = item.productionRevisionCount,
                onClick = { onSelectStageAndOpenWorkspace(WorkflowStage.PRODUKSI_KONTEN) }
            )
            DownArrowConnector()

            FlowDecisionNodeWithBranch(
                question = "Hasil konten: Layak / Tidak Layak?",
                chosenDecision = item.lastProductionDecision,
                positiveLabel = "Layak → Publikasi",
                branchLabel = "Tidak Layak → Revisi Produksi",
                branchStage = WorkflowStage.REVISI_PRODUKSI,
                activeStage = active,
                branchRevisionCount = item.productionRevisionCount,
                branchLoopHint = "↺ Kembali ke 6. Produksi Konten",
                isBranchError = true,
                onOpenBranch = { onSelectStageAndOpenWorkspace(WorkflowStage.REVISI_PRODUKSI) }
            )
            DownArrowConnector(label = "Layak")

            // Stage 7
            FlowProcessNode(
                stage = WorkflowStage.PUBLIKASI_KONTEN,
                activeStage = active,
                summaryText = if (item.isPublished) "Sudah Diterbitkan (${item.publishChannel})"
                else "Jadwal tayang & caption final",
                onClick = { onSelectStageAndOpenWorkspace(WorkflowStage.PUBLIKASI_KONTEN) }
            )
            DownArrowConnector()

            // Stage 8 Decision: Respon publik bagus?
            FlowProcessNode(
                stage = WorkflowStage.RESPON_PUBLIK,
                activeStage = active,
                summaryText = "Reach ${item.metricReach} • Interaksi ${item.metricLikesCommentsShares}",
                revisionBadgeCount = item.engagementRevisionCount,
                onClick = { onSelectStageAndOpenWorkspace(WorkflowStage.RESPON_PUBLIK) }
            )
            DownArrowConnector()

            FlowDecisionNodeWithBranch(
                question = "Respon publik (engagement) bagus?",
                chosenDecision = item.lastEngagementDecision,
                positiveLabel = "Ya → Evaluasi & Laporan",
                branchLabel = "Tidak → Revisi Optimasi",
                branchStage = WorkflowStage.REVISI_ENGAGEMENT,
                activeStage = active,
                branchRevisionCount = item.engagementRevisionCount,
                branchLoopHint = "↺ Loop kembali ke 6. Produksi Konten",
                isBranchError = true,
                onOpenBranch = { onSelectStageAndOpenWorkspace(WorkflowStage.REVISI_ENGAGEMENT) }
            )
            DownArrowConnector(label = "Ya")

            // Stage 9
            FlowProcessNode(
                stage = WorkflowStage.EVALUASI_LAPORAN,
                activeStage = active,
                summaryText = item.performanceTakeaway.ifBlank { "Pembelajaran & laporan hasil PR" },
                onClick = { onSelectStageAndOpenWorkspace(WorkflowStage.EVALUASI_LAPORAN) }
            )
            DownArrowConnector()

            // Decision: Lanjut bikin konten lain?
            FlowDecisionNodeWithBranch(
                question = "Lanjut bikin konten lain?",
                chosenDecision = item.continueNextContentDecision,
                positiveLabel = "Tidak → Selesai",
                branchLabel = "Ya → Konten Baru (Riset Audiens)",
                branchStage = WorkflowStage.RISET_AUDIENS,
                activeStage = WorkflowStage.EVALUASI_LAPORAN, // only highlight when chosen or clicked
                branchRevisionCount = 0,
                branchLoopHint = "↺ Mulai konten baru di Tahap 1",
                isBranchError = false,
                forceInactiveBranch = true,
                onOpenBranch = { onSelectStageAndOpenWorkspace(WorkflowStage.EVALUASI_LAPORAN) }
            )
            DownArrowConnector(label = "Tidak")

            // Terminal Selesai
            val isFinished = active == WorkflowStage.SELESAI
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onSelectStageAndOpenWorkspace(WorkflowStage.SELESAI) }
                    .testTag("flowchart_node_SELESAI"),
                shape = RoundedCornerShape(24.dp),
                color = if (isFinished) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                border = BorderStroke(
                    width = if (isFinished) 2.dp else 1.dp,
                    color = MaterialTheme.colorScheme.secondary
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Flag,
                        contentDescription = null,
                        tint = if (isFinished) MaterialTheme.colorScheme.onSecondary
                        else MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isFinished) "SELESAI (AKTIF)" else "SELESAI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isFinished) MaterialTheme.colorScheme.onSecondary
                        else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            TeamCreditFooter()
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LegendPill(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FlowTerminalPill(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor
            )
        }
    }
}

@Composable
private fun FlowProcessNode(
    stage: WorkflowStage,
    activeStage: WorkflowStage,
    summaryText: String,
    revisionBadgeCount: Int = 0,
    onClick: () -> Unit
) {
    val isActive = activeStage == stage
    val isDone = activeStage == WorkflowStage.SELESAI ||
        (stage.stepNumber < activeStage.stepNumber && !activeStage.isRevisionNode) ||
        (activeStage.isRevisionNode && stage.stepNumber < activeStage.stepNumber)

    val borderColor = when {
        isActive -> MaterialTheme.colorScheme.primary
        isDone -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.outline
    }
    val containerColor = when {
        isActive -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        isDone -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.surface
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("flowchart_node_${stage.name}"),
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        border = BorderStroke(if (isActive) 2.dp else 1.dp, borderColor),
        tonalElevation = if (isActive) 3.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isActive -> MaterialTheme.colorScheme.primary
                            isDone -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isDone && !isActive) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = "${stage.stepNumber}",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isActive) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stage.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = summaryText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isActive) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "AKTIF",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }
                if (revisionBadgeCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Text(
                            text = "Revisi ×$revisionBadgeCount",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FlowDecisionNodeWithBranch(
    question: String,
    chosenDecision: String,
    positiveLabel: String,
    branchLabel: String,
    branchStage: WorkflowStage,
    activeStage: WorkflowStage,
    branchRevisionCount: Int,
    branchLoopHint: String,
    isBranchError: Boolean,
    forceInactiveBranch: Boolean = false,
    onOpenBranch: () -> Unit
) {
    val isBranchActive = !forceInactiveBranch && activeStage == branchStage
    val branchAccent = if (isBranchError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
    val branchBg = if (isBranchError) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f)
    else MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.55f)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Decision Diamond Card
        Surface(
            modifier = Modifier.weight(1.1f),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.55f))
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Diamond,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "KEPUTUSAN",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = question,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (chosenDecision.isNotBlank()) "Pilihan: $chosenDecision" else positiveLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // Horizontal connector arrow
        val arrowColor = branchAccent
        Canvas(
            modifier = Modifier
                .width(26.dp)
                .height(24.dp)
        ) {
            val y = size.height / 2f
            drawLine(
                color = arrowColor,
                start = Offset(0f, y),
                end = Offset(size.width - 6f, y),
                strokeWidth = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f),
                cap = StrokeCap.Round
            )
            val head = Path().apply {
                moveTo(size.width, y)
                lineTo(size.width - 10f, y - 7f)
                lineTo(size.width - 10f, y + 7f)
                close()
            }
            drawPath(head, color = arrowColor)
        }

        // Branch target card (Revision or Loop-back)
        Surface(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .clickable(onClick = onOpenBranch)
                .testTag("flowchart_branch_${branchStage.name}"),
            shape = RoundedCornerShape(14.dp),
            color = if (isBranchActive) branchBg else MaterialTheme.colorScheme.surface,
            border = BorderStroke(if (isBranchActive) 2.dp else 1.dp, branchAccent.copy(alpha = if (isBranchActive) 1f else 0.6f))
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = if (isBranchActive) Icons.Filled.Loop else Icons.AutoMirrored.Filled.Reply,
                            contentDescription = null,
                            tint = branchAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = branchLabel,
                            style = MaterialTheme.typography.labelLarge,
                            color = branchAccent
                        )
                    }
                    if (branchRevisionCount > 0) {
                        Text(
                            text = "×$branchRevisionCount",
                            style = MaterialTheme.typography.labelSmall,
                            color = branchAccent
                        )
                    }
                }
                Text(
                    text = branchLoopHint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DownArrowConnector(label: String? = null) {
    val lineColor = MaterialTheme.colorScheme.outlineVariant
    val labelColor = MaterialTheme.colorScheme.secondary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(width = 24.dp, height = 28.dp)) {
            val cx = size.width / 2f
            drawLine(
                color = lineColor,
                start = Offset(cx, 0f),
                end = Offset(cx, size.height - 6f),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
            val path = Path().apply {
                moveTo(cx, size.height)
                lineTo(cx - 7f, size.height - 10f)
                lineTo(cx + 7f, size.height - 10f)
                close()
            }
            drawPath(path, color = lineColor)
        }
        if (label != null) {
            Surface(
                modifier = Modifier.padding(start = 74.dp),
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = labelColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                )
            }
        }
    }
}
