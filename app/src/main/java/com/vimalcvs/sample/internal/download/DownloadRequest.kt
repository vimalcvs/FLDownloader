package com.vimalcvs.sample.internal.download

import com.vimalcvs.sample.internal.app.DownloadConfig
import com.vimalcvs.sample.internal.app.NotificationConfig
import com.vimalcvs.sample.internal.app.Status
import com.vimalcvs.sample.internal.utils.DownloadConst
import com.vimalcvs.sample.internal.utils.FileUtil.getUniqueId

internal data class DownloadRequest(
    val url: String,
    val path: String,
    val fileName: String,
    val tag: String?,
    val id: Int = getUniqueId(url, path, fileName),
    val headers: HashMap<String, String> = hashMapOf(),
    var status: Status = Status.DEFAULT,
    var listener: DownloadRequestListener? = null,
    var timeQueued: Long = DownloadConst.DEFAULT_VALUE_TIME_QUEUED,
    var totalLength: Long = DownloadConst.DEFAULT_VALUE_LENGTH,
    var progress: Int = DownloadConst.DEFAULT_VALUE_PROGRESS,
    var speedInBytePerMs: Float = DownloadConst.DEFAULT_VALUE_SPEED,
    var downloadConfig: DownloadConfig = DownloadConfig(),
    val notificationConfig: NotificationConfig
)