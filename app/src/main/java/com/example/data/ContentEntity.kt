package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.ChecklistItem
import com.example.model.ContentPipelineItem
import com.example.model.RevisionLogEntry
import com.example.model.WorkflowStage
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "content_items")
data class ContentEntity(
    @PrimaryKey val id: String,
    val campaignName: String,
    val contentTitle: String,
    val currentStageName: String,
    val highestStepReached: Int,
    val audienceSegmentsJson: String,
    val demographics: String,
    val painPoints: String,
    val audienceInsights: String,
    val campaignGoalsJson: String,
    val keyMessages: String,
    val targetKpi: String,
    val contentAngle: String,
    val contentFormat: String,
    val platform: String,
    val trendCompetitorNotes: String,
    val outlineHook: String,
    val outlineKeyPoints: String,
    val outlineCta: String,
    val outlineRevisionCount: Int,
    val outlineRevisionNotes: String,
    val lastOutlineDecision: String,
    val takeDate: String,
    val takeTime: String,
    val takeLocation: String,
    val takePic: String,
    val checklistJson: String,
    val productionStatus: String,
    val draftLink: String,
    val productionNotes: String,
    val productionRevisionCount: Int,
    val productionRevisionNotes: String,
    val lastProductionDecision: String,
    val publishDate: String,
    val publishTime: String,
    val publishChannel: String,
    val publishCaption: String,
    val isPublished: Boolean,
    val metricReach: Int,
    val metricLikesCommentsShares: Int,
    val metricSavesCtr: String,
    val engagementNotes: String,
    val engagementRevisionCount: Int,
    val engagementRevisionNotes: String,
    val lastEngagementDecision: String,
    val evaluationLearnings: String,
    val performanceTakeaway: String,
    val reportNotes: String,
    val continueNextContentDecision: String,
    val revisionLogsJson: String,
    val updatedAtMillis: Long
) {
    fun toDomain(): ContentPipelineItem {
        return ContentPipelineItem(
            id = id,
            campaignName = campaignName,
            contentTitle = contentTitle,
            currentStage = WorkflowStage.fromNameOrDefault(currentStageName),
            highestStepReached = highestStepReached,
            audienceSegments = decodeStringList(audienceSegmentsJson),
            demographics = demographics,
            painPoints = painPoints,
            audienceInsights = audienceInsights,
            campaignGoals = decodeStringList(campaignGoalsJson),
            keyMessages = keyMessages,
            targetKpi = targetKpi,
            contentAngle = contentAngle,
            contentFormat = contentFormat,
            platform = platform,
            trendCompetitorNotes = trendCompetitorNotes,
            outlineHook = outlineHook,
            outlineKeyPoints = outlineKeyPoints,
            outlineCta = outlineCta,
            outlineRevisionCount = outlineRevisionCount,
            outlineRevisionNotes = outlineRevisionNotes,
            lastOutlineDecision = lastOutlineDecision,
            takeDate = takeDate,
            takeTime = takeTime,
            takeLocation = takeLocation,
            takePic = takePic,
            productionChecklist = decodeChecklist(checklistJson),
            productionStatus = productionStatus,
            draftLink = draftLink,
            productionNotes = productionNotes,
            productionRevisionCount = productionRevisionCount,
            productionRevisionNotes = productionRevisionNotes,
            lastProductionDecision = lastProductionDecision,
            publishDate = publishDate,
            publishTime = publishTime,
            publishChannel = publishChannel,
            publishCaption = publishCaption,
            isPublished = isPublished,
            metricReach = metricReach,
            metricLikesCommentsShares = metricLikesCommentsShares,
            metricSavesCtr = metricSavesCtr,
            engagementNotes = engagementNotes,
            engagementRevisionCount = engagementRevisionCount,
            engagementRevisionNotes = engagementRevisionNotes,
            lastEngagementDecision = lastEngagementDecision,
            evaluationLearnings = evaluationLearnings,
            performanceTakeaway = performanceTakeaway,
            reportNotes = reportNotes,
            continueNextContentDecision = continueNextContentDecision,
            revisionLogs = decodeRevisionLogs(revisionLogsJson),
            updatedAtMillis = updatedAtMillis
        )
    }

    companion object {
        fun fromDomain(item: ContentPipelineItem): ContentEntity {
            return ContentEntity(
                id = item.id,
                campaignName = item.campaignName,
                contentTitle = item.contentTitle,
                currentStageName = item.currentStage.name,
                highestStepReached = item.highestStepReached,
                audienceSegmentsJson = encodeStringList(item.audienceSegments),
                demographics = item.demographics,
                painPoints = item.painPoints,
                audienceInsights = item.audienceInsights,
                campaignGoalsJson = encodeStringList(item.campaignGoals),
                keyMessages = item.keyMessages,
                targetKpi = item.targetKpi,
                contentAngle = item.contentAngle,
                contentFormat = item.contentFormat,
                platform = item.platform,
                trendCompetitorNotes = item.trendCompetitorNotes,
                outlineHook = item.outlineHook,
                outlineKeyPoints = item.outlineKeyPoints,
                outlineCta = item.outlineCta,
                outlineRevisionCount = item.outlineRevisionCount,
                outlineRevisionNotes = item.outlineRevisionNotes,
                lastOutlineDecision = item.lastOutlineDecision,
                takeDate = item.takeDate,
                takeTime = item.takeTime,
                takeLocation = item.takeLocation,
                takePic = item.takePic,
                checklistJson = encodeChecklist(item.productionChecklist),
                productionStatus = item.productionStatus,
                draftLink = item.draftLink,
                productionNotes = item.productionNotes,
                productionRevisionCount = item.productionRevisionCount,
                productionRevisionNotes = item.productionRevisionNotes,
                lastProductionDecision = item.lastProductionDecision,
                publishDate = item.publishDate,
                publishTime = item.publishTime,
                publishChannel = item.publishChannel,
                publishCaption = item.publishCaption,
                isPublished = item.isPublished,
                metricReach = item.metricReach,
                metricLikesCommentsShares = item.metricLikesCommentsShares,
                metricSavesCtr = item.metricSavesCtr,
                engagementNotes = item.engagementNotes,
                engagementRevisionCount = item.engagementRevisionCount,
                engagementRevisionNotes = item.engagementRevisionNotes,
                lastEngagementDecision = item.lastEngagementDecision,
                evaluationLearnings = item.evaluationLearnings,
                performanceTakeaway = item.performanceTakeaway,
                reportNotes = item.reportNotes,
                continueNextContentDecision = item.continueNextContentDecision,
                revisionLogsJson = encodeRevisionLogs(item.revisionLogs),
                updatedAtMillis = item.updatedAtMillis
            )
        }

        private fun encodeStringList(list: List<String>): String {
            val array = JSONArray()
            list.forEach { array.put(it) }
            return array.toString()
        }

        private fun decodeStringList(json: String): List<String> {
            return try {
                val array = JSONArray(json)
                buildList {
                    for (i in 0 until array.length()) {
                        add(array.getString(i))
                    }
                }
            } catch (_: Exception) {
                emptyList()
            }
        }

        private fun encodeChecklist(items: List<ChecklistItem>): String {
            val array = JSONArray()
            items.forEach { item ->
                array.put(
                    JSONObject().apply {
                        put("id", item.id)
                        put("label", item.label)
                        put("isChecked", item.isChecked)
                    }
                )
            }
            return array.toString()
        }

        private fun decodeChecklist(json: String): List<ChecklistItem> {
            return try {
                val array = JSONArray(json)
                buildList {
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        add(
                            ChecklistItem(
                                id = obj.optString("id", "chk_$i"),
                                label = obj.optString("label", ""),
                                isChecked = obj.optBoolean("isChecked", false)
                            )
                        )
                    }
                }
            } catch (_: Exception) {
                ContentPipelineItem.defaultChecklist()
            }
        }

        private fun encodeRevisionLogs(logs: List<RevisionLogEntry>): String {
            val array = JSONArray()
            logs.forEach { entry ->
                array.put(
                    JSONObject().apply {
                        put("id", entry.id)
                        put("stageName", entry.stageName)
                        put("note", entry.note)
                        put("timestampLabel", entry.timestampLabel)
                    }
                )
            }
            return array.toString()
        }

        private fun decodeRevisionLogs(json: String): List<RevisionLogEntry> {
            return try {
                val array = JSONArray(json)
                buildList {
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        add(
                            RevisionLogEntry(
                                id = obj.optString("id", "rev_$i"),
                                stageName = obj.optString("stageName", ""),
                                note = obj.optString("note", ""),
                                timestampLabel = obj.optString("timestampLabel", "")
                            )
                        )
                    }
                }
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}
