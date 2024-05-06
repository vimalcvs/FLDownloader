package com.vimalcvs.sample.internal.download

internal interface DownloadRequestListener {
    fun onQueue()
    fun onStart(length: Long)
    fun onProgress(progress: Int, speedInBytePerMs: Float)
    fun onSuccess()
    fun onFailure(error: String)
    fun onCancel()
}