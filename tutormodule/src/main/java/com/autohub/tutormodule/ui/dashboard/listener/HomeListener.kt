package com.autohub.tutormodule.ui.dashboard.listener

import com.autohub.skln.models.batches.BatchesModel

interface HomeListener {

    fun managerSelected()
    fun pendingRequestSelect()
    fun showAddBatchFragment(showAddBatch: Boolean, batch: BatchesModel)
    fun showBatchOptionsFragment(batch: BatchesModel)
    fun showStudentsListFragment()
    fun refreshSchedule()

}