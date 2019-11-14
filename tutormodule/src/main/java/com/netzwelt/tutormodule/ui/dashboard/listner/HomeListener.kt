package com.netzwelt.tutormodule.ui.dashboard.listner

interface HomeListener {

      fun managerSelected()
      fun pendingRequestSelect()
      fun showAddBatchFragment(showAddBatch : Boolean)
      fun showBatchOptionsFragment()
}