package com.vimalcvs.sample.internal.worker

import com.vimalcvs.sample.internal.app.DownloadConfig
import com.vimalcvs.sample.internal.app.NotificationConfig

internal data class WorkInputData(
    val url: String,
    val path: String,
    val fileName: String,
    val id: Int,
    val headers: HashMap<String, String>,
    var downloadConfig: DownloadConfig,
    val notificationConfig: NotificationConfig
)
