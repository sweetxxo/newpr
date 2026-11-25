package com.example

import com.example.data.SampleData
import com.example.model.WorkflowStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `workflow stages include all 9 steps plus revision branches and selesai`() {
        assertEquals(10, WorkflowStage.primaryPipelineStages.size)
        assertTrue(WorkflowStage.REVISI_OUTLINE.isRevisionNode)
        assertTrue(WorkflowStage.REVISI_PRODUKSI.isRevisionNode)
        assertTrue(WorkflowStage.REVISI_ENGAGEMENT.isRevisionNode)
        assertTrue(WorkflowStage.SELESAI.isTerminal)
    }

    @Test
    fun `sample data contains 3 realistic PR items across different stages`() {
        val items = SampleData.initialItems()
        assertEquals(3, items.size)
        assertEquals(WorkflowStage.OUTLINE_CONTENT, items[0].currentStage)
        assertEquals(WorkflowStage.RESPON_PUBLIK, items[1].currentStage)
        assertEquals(WorkflowStage.SELESAI, items[2].currentStage)
        assertTrue(items[0].totalRevisions >= 1)
    }
}
