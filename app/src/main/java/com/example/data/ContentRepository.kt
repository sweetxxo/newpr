package com.example.data

import com.example.model.ContentPipelineItem
import com.example.model.WorkflowStage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class ContentRepository(private val dao: ContentDao) {
    val allItems: Flow<List<ContentPipelineItem>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    suspend fun ensureSeeded() {
        if (dao.countItems() == 0) {
            dao.upsertAll(SampleData.initialItems().map { ContentEntity.fromDomain(it) })
        }
    }

    suspend fun upsert(item: ContentPipelineItem) {
        dao.upsert(ContentEntity.fromDomain(item.copy(updatedAtMillis = System.currentTimeMillis())))
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    suspend fun resetToSampleData() {
        val samples = SampleData.initialItems().map { ContentEntity.fromDomain(it) }
        dao.replaceAll(samples)
    }

    suspend fun createBlankItem(
        campaignName: String = "Program Kehumasan Baru",
        contentTitle: String = "Ide Konten PR Baru"
    ): ContentPipelineItem {
        val newItem = ContentPipelineItem(
            id = "pr_${UUID.randomUUID().toString().take(8)}",
            campaignName = campaignName,
            contentTitle = contentTitle,
            currentStage = WorkflowStage.RISET_AUDIENS,
            highestStepReached = 1,
            updatedAtMillis = System.currentTimeMillis()
        )
        dao.upsert(ContentEntity.fromDomain(newItem))
        return newItem
    }

    suspend fun createNextContentFromCampaign(source: ContentPipelineItem): ContentPipelineItem {
        val nextItem = ContentPipelineItem(
            id = "pr_${UUID.randomUUID().toString().take(8)}",
            campaignName = source.campaignName,
            contentTitle = "Lanjutan: ${source.campaignName}",
            currentStage = WorkflowStage.RISET_AUDIENS,
            highestStepReached = 1,
            audienceSegments = source.audienceSegments,
            demographics = source.demographics,
            campaignGoals = source.campaignGoals,
            keyMessages = source.keyMessages,
            targetKpi = source.targetKpi,
            platform = source.platform,
            updatedAtMillis = System.currentTimeMillis()
        )
        dao.upsert(ContentEntity.fromDomain(nextItem))
        return nextItem
    }

    fun exportItemsToJson(items: List<ContentPipelineItem>): String {
        val root = JSONObject()
        root.put("program", "Program Kehumasan: Penjadwalan Konten Optimal")
        root.put("exportedAtMillis", System.currentTimeMillis())
        val array = JSONArray()
        items.forEach { item ->
            val entity = ContentEntity.fromDomain(item)
            array.put(
                JSONObject().apply {
                    put("id", item.id)
                    put("campaignName", item.campaignName)
                    put("contentTitle", item.contentTitle)
                    put("currentStage", item.currentStage.title)
                    put("totalRevisions", item.totalRevisions)
                    put("audienceSegments", JSONArray(entity.audienceSegmentsJson))
                    put("demographics", item.demographics)
                    put("painPoints", item.painPoints)
                    put("audienceInsights", item.audienceInsights)
                    put("campaignGoals", JSONArray(entity.campaignGoalsJson))
                    put("keyMessages", item.keyMessages)
                    put("contentAngle", item.contentAngle)
                    put("contentFormat", item.contentFormat)
                    put("platform", item.platform)
                    put("outlineHook", item.outlineHook)
                    put("outlineKeyPoints", item.outlineKeyPoints)
                    put("outlineCta", item.outlineCta)
                    put("takeSchedule", "${item.takeDate} ${item.takeTime}".trim())
                    put("takeLocation", item.takeLocation)
                    put("takePic", item.takePic)
                    put("productionStatus", item.productionStatus)
                    put("publishSchedule", "${item.publishDate} ${item.publishTime}".trim())
                    put("publishChannel", item.publishChannel)
                    put("isPublished", item.isPublished)
                    put("metrics", JSONObject().apply {
                        put("reach", item.metricReach)
                        put("interactions", item.metricLikesCommentsShares)
                        put("savesCtr", item.metricSavesCtr)
                    })
                    put("evaluationLearnings", item.evaluationLearnings)
                    put("performanceTakeaway", item.performanceTakeaway)
                    put("revisionLogs", JSONArray(entity.revisionLogsJson))
                }
            )
        }
        root.put("items", array)
        return root.toString(2)
    }
}
