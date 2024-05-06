package com.vimalcvs.sample.internal.utils

import com.google.gson.Gson
import com.vimalcvs.sample.internal.worker.WorkInputData

internal object WorkUtil {

    fun WorkInputData.toJson(): String {
        return Gson().toJson(this)
    }

    fun fromJson(jsonStr: String): WorkInputData {
        return Gson().fromJson(jsonStr, WorkInputData::class.java)
    }
}