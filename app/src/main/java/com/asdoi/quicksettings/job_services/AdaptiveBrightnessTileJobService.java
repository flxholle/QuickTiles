package com.asdoi.quicksettings.job_services;

import android.app.job.JobInfo;
import android.app.job.JobInfo.TriggerContentUri;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.service.quicksettings.TileService;

import com.asdoi.quicksettings.tiles.AdaptiveBrightnessTileService;

public class AdaptiveBrightnessTileJobService extends JobService {

    private static final int ADAPTIVE_BRIGHTNESS_TILE_JOB_ID = 234299;

    public static void scheduleUpdateJob(Context context) {
        ComponentName componentName = new ComponentName(context, AdaptiveBrightnessTileJobService.class);
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);

        if (jobScheduler != null) {
            jobScheduler.schedule(new JobInfo.Builder(ADAPTIVE_BRIGHTNESS_TILE_JOB_ID, componentName)
                    .addTriggerContentUri(
                            new TriggerContentUri(Settings.System.getUriFor(AdaptiveBrightnessTileService.SETTING), TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
                    .build());
        }
    }

    public static void cancelJob(Context context) {
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        if (jobScheduler != null) {
            jobScheduler.cancel(ADAPTIVE_BRIGHTNESS_TILE_JOB_ID);
        }
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        TileService.requestListeningState(this, new ComponentName(getApplicationContext(), AdaptiveBrightnessTileService.class));
        scheduleUpdateJob(this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}