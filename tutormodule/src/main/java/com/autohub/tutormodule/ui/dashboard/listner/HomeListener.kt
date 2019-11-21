package com.autohub.tutormodule.ui.dashboard.listner

import com.autohub.skln.models.batches.BatchesModel

interface HomeListener {

    fun managerSelected()
    fun pendingRequestSelect()
    fun showAddBatchFragment(showAddBatch: Boolean)
    fun showBatchOptionsFragment(batch: BatchesModel)
    fun showStudentsListFragment()
    fun refreshSchedule()

}