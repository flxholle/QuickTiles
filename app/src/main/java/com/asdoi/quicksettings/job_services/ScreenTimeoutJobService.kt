package com.asdoi.quicksettings.job_services

import android.app.job.JobInfo
import android.app.job.JobInfo.TriggerContentUri
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.service.quicksettings.TileService
import com.asdoi.quicksettings.tiles.ScreenTimeoutTileService


class ScreenTimeoutJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        TileService.requestListeningState(applicationContext, ComponentName(applicationContext, ScreenTimeoutTileService::class.java))
        scheduleJob(applicationContext)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    companion object {
        const val SCREEN_TIMEOUT_JOB_SERVICE_ID = 1235
        private const val TAG = "SyncService"

        fun scheduleJob(context: Context) {
            val componentName = ComponentName(context, this::class.java)
            val builder = JobInfo.Builder(SCREEN_TIMEOUT_JOB_SERVICE_ID, componentName)
            builder.addTriggerContentUri(
                    TriggerContentUri(Settings.System.getUriFor(ScreenTimeoutTileService.SETTING), TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))

            val jobScheduler = context.getSystemService(JobScheduler::class.java)
            jobScheduler.schedule(builder.build())
        }
    }
}