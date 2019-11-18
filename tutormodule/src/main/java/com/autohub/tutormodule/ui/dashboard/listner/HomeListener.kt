package com.autohub.tutormodule.ui.dashboard.listner

interface HomeListener {

    fun managerSelected()
    fun pendingRequestSelect()
    fun showAddBatchFragment(showAddBatch: Boolean)
    fun showBatchOptionsFragment()
    fun showStudentsListFragment()

}