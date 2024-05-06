package com.vimalcvs.sample

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.vimalcvs.sample.databinding.ActivityMainBinding
import com.vimalcvs.sample.internal.app.Downloader
import com.vimalcvs.sample.internal.app.NotificationConfig
import com.vimalcvs.sample.internal.app.Request
import com.vimalcvs.sample.internal.app.Status
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var snackbar: Snackbar

    private lateinit var downloader: Downloader

    private var length1: Long = 0L
    private var length1Text: String = "0.00 b"
    private var request1: Request? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) return

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openTestFragment()
            return
        }

        var permissions = arrayOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !getSystemService(NotificationManager::class.java).areNotificationsEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions = permissions.plus(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            snackbar =
                Snackbar.make(binding.root, "Allow storage permission", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Settings") {
                        val permission = Intent()
                        permission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                        startActivity(permission)
                        snackbar.dismiss()
                    }
            snackbar.show()
        } else if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                permissions = permissions.plus(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        if (permissions.isNotEmpty()) {
            requestPermissions(permissions, 101)
            Toast.makeText(this, "Notification and Storage Permission Required", Toast.LENGTH_SHORT)
                .show()
            return
        }

        openTestFragment()
    }

    private fun openTestFragment() {
        downloader = Downloader.init(
            this,
            notificationConfig = NotificationConfig(
                enabled = true,
                smallIcon = R.drawable.ic_launcher_foreground
            )
        )


        binding.cancelButton1.setOnClickListener {
            if (binding.cancelButton1.text == "Cancel") {
                if (request1 != null) {
                    downloader.cancel(request1!!.id)
                }
            } else if (binding.cancelButton1.text == "Open") {
                if (request1 != null) {
                    openFile(
                        this,
                        request1!!.path,
                        request1!!.fileName
                    )
                }
            } else if (binding.cancelButton1.text == "Download") {
                download1()
                binding.cancelButton1.text = "Cancel"
            }
        }

    }


    @SuppressLint("SetTextI18n")
    private fun download1() {
        request1 = downloader.download(
            url = "https://file-examples.com/storage/fe4996602366316ffa06467/2017/04/file_example_MP4_640_3MG.mp4",
            fileName = "Sample_Video.mp4",
            onQueue = {
                binding.fileName1.text = request1?.fileName
                binding.status1.text = Status.QUEUED.toString()
            },
            onStart = { length ->
                length1 = length
                length1Text = Util.getTotalLengthText(length)
                binding.status1.text = Status.STARTED.toString()
            },
            onProgress = { progress, speedInBytePerMs ->
                binding.status1.text = Status.PROGRESS.toString()
                binding.progressBar1.progress = progress
                binding.progressText1.text = "$progress%/$length1Text, "
                binding.size1.text = Util.getTimeLeftText(
                    speedInBytePerMs,
                    progress,
                    length1
                ) + ", " + Util.getSpeedText(speedInBytePerMs)
            },
            onSuccess = {
                binding.status1.text = Status.SUCCESS.toString()
                binding.progressBar1.progress = 100
                binding.progressText1.text = "100%/$length1Text"
                binding.size1.text = ""
                binding.cancelButton1.text = "Open"
            },
            onFailure = {
                binding.status1.text = Status.FAILED.toString()
                binding.progressText1.text = it
                binding.size1.text = ""
            },
            onCancel = {
                binding.status1.text = Status.CANCELLED.toString()
                binding.progressBar1.progress = 0
                binding.progressText1.text = ""
                binding.size1.text = ""
            }
        )
    }

    private fun openFile(context: Context?, path: String, fileName: String) {
        val file = File(path, fileName)
        if (file.exists()) {
            val uri = context?.applicationContext?.let {
                FileProvider.getUriForFile(
                    it,
                    it.packageName + ".provider",
                    file
                )
            }
            if (uri != null) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, contentResolver.getType(uri))
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                try {
                    startActivity(intent)
                } catch (ignore: Exception) {
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openTestFragment()
            }
        }
    }
}