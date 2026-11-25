package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CompactHorizontalStepper
import com.example.ui.components.ExportJsonDialog
import com.example.ui.components.FlowchartView
import com.example.ui.components.NewContentDialog
import com.example.ui.components.PipelineSidebar
import com.example.ui.components.ResetConfirmDialog
import com.example.ui.components.StageWorkPanel
import com.example.ui.components.TeamCreditsDialog
import com.example.ui.components.WorkspaceHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PRPlannerScreen(viewModel: PRPlannerViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var showPipelineSheet by remember { mutableStateOf(false) }
    var showNewItemDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showTeamDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.statusBannerMessage) {
        val msg = uiState.statusBannerMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearBanner()
        }
    }

    // Handle system back when viewing Flowchart -> return to Workspace
    BackHandler(enabled = uiState.viewMode == WorkspaceViewMode.FLOWCHART) {
        viewModel.setViewMode(WorkspaceViewMode.PIPELINE)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val isCompact = maxWidth < 840.dp

            Column(modifier = Modifier.fillMaxSize()) {
                WorkspaceHeader(
                    isCompact = isCompact,
                    viewMode = uiState.viewMode,
                    totalItemsCount = uiState.items.size,
                    onViewModeChange = viewModel::setViewMode,
                    onOpenPipelineSheet = { showPipelineSheet = true },
                    onOpenExport = { showExportDialog = true },
                    onOpenReset = { showResetDialog = true },
                    onOpenTeamDialog = { showTeamDialog = true }
                )

                val activeItem = uiState.activeItem
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (activeItem == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Belum ada item konten PR.",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    if (isCompact) {
                        if (uiState.viewMode == WorkspaceViewMode.PIPELINE) {
                            CompactHorizontalStepper(
                                item = activeItem,
                                onJumpToStage = viewModel::jumpToStage
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            WorkspaceContentBody(
                                viewMode = uiState.viewMode,
                                activeItem = activeItem,
                                viewModel = viewModel,
                                onOpenExport = { showExportDialog = true }
                            )
                        }
                    } else {
                        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                            PipelineSidebar(
                                items = uiState.items,
                                activeItem = activeItem,
                                onSelectItem = viewModel::selectItem,
                                onCreateNewItem = { showNewItemDialog = true },
                                onDeleteItem = viewModel::deleteItem,
                                onJumpToStage = { stage ->
                                    viewModel.jumpToStage(stage)
                                    viewModel.setViewMode(WorkspaceViewMode.PIPELINE)
                                },
                                modifier = Modifier
                                    .width(320.dp)
                                    .fillMaxHeight()
                            )
                            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                                WorkspaceContentBody(
                                    viewMode = uiState.viewMode,
                                    activeItem = activeItem,
                                    viewModel = viewModel,
                                    onOpenExport = { showExportDialog = true }
                                )
                            }
                        }
                    }
                }
            }

            if (showPipelineSheet && isCompact) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                ModalBottomSheet(
                    onDismissRequest = { showPipelineSheet = false },
                    sheetState = sheetState
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 580.dp)
                    ) {
                        PipelineSidebar(
                            items = uiState.items,
                            activeItem = uiState.activeItem,
                            onSelectItem = { id ->
                                viewModel.selectItem(id)
                                showPipelineSheet = false
                            },
                            onCreateNewItem = {
                                showPipelineSheet = false
                                showNewItemDialog = true
                            },
                            onDeleteItem = viewModel::deleteItem,
                            onJumpToStage = { stage ->
                                viewModel.jumpToStage(stage)
                                viewModel.setViewMode(WorkspaceViewMode.PIPELINE)
                                showPipelineSheet = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    if (showNewItemDialog) {
        NewContentDialog(
            initialCampaign = uiState.activeItem?.campaignName ?: "Program Kehumasan 2026",
            onDismiss = { showNewItemDialog = false },
            onConfirm = { campaign, title ->
                viewModel.createNewItem(campaign, title)
                showNewItemDialog = false
            }
        )
    }

    if (showExportDialog) {
        val jsonText = remember(uiState.items) { viewModel.buildExportJson() }
        ExportJsonDialog(
            jsonText = jsonText,
            onCopy = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("PR Content Pipeline JSON", jsonText))
                viewModel.showBanner("JSON disalin ke papan klip")
            },
            onShare = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Ekspor PR & Content Planner")
                    putExtra(Intent.EXTRA_TEXT, jsonText)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Bagikan Data Pipeline PR"))
            },
            onDismiss = { showExportDialog = false }
        )
    }

    if (showResetDialog) {
        ResetConfirmDialog(
            onConfirm = {
                viewModel.resetToSampleData()
                showResetDialog = false
            },
            onDismiss = { showResetDialog = false }
        )
    }

    if (showTeamDialog) {
        TeamCreditsDialog(onDismiss = { showTeamDialog = false })
    }
}

@Composable
private fun WorkspaceContentBody(
    viewMode: WorkspaceViewMode,
    activeItem: com.example.model.ContentPipelineItem,
    viewModel: PRPlannerViewModel,
    onOpenExport: () -> Unit
) {
    AnimatedContent(
        targetState = viewMode,
        label = "workspace_view_transition"
    ) { mode ->
        when (mode) {
            WorkspaceViewMode.PIPELINE -> StageWorkPanel(
                item = activeItem,
                viewModel = viewModel,
                onOpenFlowchart = { viewModel.setViewMode(WorkspaceViewMode.FLOWCHART) },
                onOpenExport = onOpenExport
            )
            WorkspaceViewMode.FLOWCHART -> FlowchartView(
                item = activeItem,
                onSelectStageAndOpenWorkspace = { stage ->
                    viewModel.jumpToStage(stage)
                    viewModel.setViewMode(WorkspaceViewMode.PIPELINE)
                }
            )
        }
    }
}
