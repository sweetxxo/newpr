package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.ContentPipelineItem
import com.example.model.WorkflowStage

@Composable
fun PipelineSidebar(
    items: List<ContentPipelineItem>,
    activeItem: ContentPipelineItem?,
    onSelectItem: (String) -> Unit,
    onCreateNewItem: () -> Unit,
    onDeleteItem: (String) -> Unit,
    onJumpToStage: (WorkflowStage) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Content Pipeline Items
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "DAFTAR KONTEN (${items.size})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = onCreateNewItem,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("add_content_sidebar_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tambah Konten",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Konten Baru")
                    }
                }

                items.forEach { item ->
                    val isSelected = item.id == activeItem?.id
                    ContentItemSelectorCard(
                        item = item,
                        isSelected = isSelected,
                        canDelete = items.size > 1,
                        onSelect = { onSelectItem(item.id) },
                        onDelete = { onDeleteItem(item.id) }
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            // Section 2: Stepper Alur Kerja
            if (activeItem != null) {
                StageStepperList(
                    item = activeItem,
                    onJumpToStage = onJumpToStage
                )
            }
        }
    }
}

@Composable
fun ContentItemSelectorCard(
    item: ContentPipelineItem,
    isSelected: Boolean,
    canDelete: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stageColor = stageStatusColor(item.currentStage)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onSelect)
            .testTag("pipeline_item_${item.id}"),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.campaignName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.contentTitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (canDelete) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("delete_item_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DeleteOutline,
                            contentDescription = "Hapus item konten",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = stageColor.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = "${item.currentStage.stepNumber}. ${item.currentStage.shortTitle}",
                        style = MaterialTheme.typography.labelSmall,
                        color = stageColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (item.totalRevisions > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Loop,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "Rev ${item.totalRevisions}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StageStepperList(
    item: ContentPipelineItem,
    onJumpToStage: (WorkflowStage) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TAHAPAN ALUR KERJA",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Total Revisi: ${item.totalRevisions}",
                style = MaterialTheme.typography.labelSmall,
                color = if (item.totalRevisions > 0) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        WorkflowStage.primaryPipelineStages.forEach { stage ->
            val isCurrentPrimary = item.currentStage == stage ||
                (item.currentStage == WorkflowStage.REVISI_OUTLINE && stage == WorkflowStage.OUTLINE_CONTENT) ||
                (item.currentStage == WorkflowStage.REVISI_PRODUKSI && stage == WorkflowStage.PRODUKSI_KONTEN) ||
                (item.currentStage == WorkflowStage.REVISI_ENGAGEMENT && stage == WorkflowStage.RESPON_PUBLIK)

            val isCompleted = item.currentStage == WorkflowStage.SELESAI ||
                stage.stepNumber < item.currentStage.stepNumber

            val revisionCountForStage = when (stage) {
                WorkflowStage.OUTLINE_CONTENT -> item.outlineRevisionCount
                WorkflowStage.PRODUKSI_KONTEN -> item.productionRevisionCount
                WorkflowStage.RESPON_PUBLIK -> item.engagementRevisionCount
                else -> 0
            }

            val branchNote = when (stage) {
                WorkflowStage.OUTLINE_CONTENT -> if (item.lastOutlineDecision.isNotEmpty()) "Revisi? ${item.lastOutlineDecision}" else null
                WorkflowStage.PRODUKSI_KONTEN -> if (item.lastProductionDecision.isNotEmpty()) "Hasil: ${item.lastProductionDecision}" else null
                WorkflowStage.RESPON_PUBLIK -> if (item.lastEngagementDecision.isNotEmpty()) "Bagus? ${item.lastEngagementDecision}" else null
                WorkflowStage.EVALUASI_LAPORAN -> if (item.continueNextContentDecision.isNotEmpty()) "Lanjut? ${item.continueNextContentDecision}" else null
                else -> null
            }

            val isInRevisionSubNode =
                (item.currentStage == WorkflowStage.REVISI_OUTLINE && stage == WorkflowStage.OUTLINE_CONTENT) ||
                    (item.currentStage == WorkflowStage.REVISI_PRODUKSI && stage == WorkflowStage.PRODUKSI_KONTEN) ||
                    (item.currentStage == WorkflowStage.REVISI_ENGAGEMENT && stage == WorkflowStage.RESPON_PUBLIK)

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onJumpToStage(stage) }
                    .testTag("stepper_stage_${stage.name}"),
                shape = RoundedCornerShape(10.dp),
                color = when {
                    isInRevisionSubNode -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)
                    isCurrentPrimary -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    else -> Color.Transparent
                },
                border = when {
                    isInRevisionSubNode -> BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
                    isCurrentPrimary -> BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    else -> null
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isInRevisionSubNode -> MaterialTheme.colorScheme.tertiary
                                    isCurrentPrimary -> MaterialTheme.colorScheme.primary
                                    isCompleted -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted && !isCurrentPrimary) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Text(
                                text = if (stage == WorkflowStage.SELESAI) "✓" else "${stage.stepNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                color = when {
                                    isInRevisionSubNode -> MaterialTheme.colorScheme.onTertiary
                                    isCurrentPrimary -> MaterialTheme.colorScheme.onPrimary
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isInRevisionSubNode) "${stage.shortTitle} • ${item.currentStage.shortTitle}"
                            else stage.shortTitle,
                            style = MaterialTheme.typography.labelLarge,
                            color = when {
                                isInRevisionSubNode -> MaterialTheme.colorScheme.onTertiaryContainer
                                isCurrentPrimary -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (branchNote != null || revisionCountForStage > 0) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (branchNote != null) {
                                    Text(
                                        text = branchNote,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (revisionCountForStage > 0) {
                                    Text(
                                        text = "Rev×$revisionCountForStage",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompactHorizontalStepper(
    item: ContentPipelineItem,
    onJumpToStage: (WorkflowStage) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.currentStage.stepNumber}/10 • ${item.currentStage.title}",
                    style = MaterialTheme.typography.labelLarge,
                    color = stageStatusColor(item.currentStage),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (item.totalRevisions > 0) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Revisi: ${item.totalRevisions}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (item.totalRevisions > 0) MaterialTheme.colorScheme.onTertiaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WorkflowStage.primaryPipelineStages.forEach { stage ->
                    val isActive = item.currentStage == stage ||
                        (item.currentStage == WorkflowStage.REVISI_OUTLINE && stage == WorkflowStage.OUTLINE_CONTENT) ||
                        (item.currentStage == WorkflowStage.REVISI_PRODUKSI && stage == WorkflowStage.PRODUKSI_KONTEN) ||
                        (item.currentStage == WorkflowStage.REVISI_ENGAGEMENT && stage == WorkflowStage.RESPON_PUBLIK)

                    val isCompleted = item.currentStage == WorkflowStage.SELESAI ||
                        stage.stepNumber < item.currentStage.stepNumber

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onJumpToStage(stage) }
                            .testTag("compact_step_${stage.name}"),
                        shape = RoundedCornerShape(8.dp),
                        color = when {
                            isActive && item.currentStage.isRevisionNode -> MaterialTheme.colorScheme.tertiaryContainer
                            isActive -> MaterialTheme.colorScheme.primary
                            isCompleted -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        border = if (isActive) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Text(
                            text = if (stage == WorkflowStage.SELESAI) "✓ Selesai" else "${stage.stepNumber}. ${stage.shortTitle}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            color = when {
                                isActive && item.currentStage.isRevisionNode -> MaterialTheme.colorScheme.onTertiaryContainer
                                isActive -> MaterialTheme.colorScheme.onPrimary
                                isCompleted -> MaterialTheme.colorScheme.onSecondaryContainer
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun stageStatusColor(stage: WorkflowStage): Color {
    return when {
        stage == WorkflowStage.SELESAI -> MaterialTheme.colorScheme.secondary
        stage.isRevisionNode -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
}
