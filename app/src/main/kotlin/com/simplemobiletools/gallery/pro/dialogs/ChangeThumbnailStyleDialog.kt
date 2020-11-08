package com.simplemobiletools.gallery.pro.dialogs

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.commons.extensions.beGone
import com.simplemobiletools.commons.extensions.beVisible
import com.simplemobiletools.commons.extensions.setupDialogStuff
import com.simplemobiletools.gallery.pro.R
import com.simplemobiletools.gallery.pro.extensions.config
import com.simplemobiletools.gallery.pro.helpers.*
import kotlinx.android.synthetic.main.dialog_change_thumbnail_style.view.*
import kotlinx.android.synthetic.main.directory_item_grid.view.*

class ChangeThumbnailStyleDialog(val activity: BaseSimpleActivity, val callback: () -> Unit) : DialogInterface.OnClickListener {
    private var config = activity.config
    private var view: View

    init {
        view = activity.layoutInflater.inflate(R.layout.dialog_change_thumbnail_style, null).apply {
            dialog_limit_title.isChecked = config.limitFolderTitle
        }

        AlertDialog.Builder(activity)
            .setPositiveButton(R.string.ok, this)
            .setNegativeButton(R.string.cancel, null)
            .create().apply {
                activity.setupDialogStuff(view, this)
            }

        setupStyle()
        setupMediaCount()
        updateSample()
    }

    private fun setupStyle() {
        val styleRadio = view.dialog_radio_style
        val styleBtn = when (config.folderStyle) {
            FOLDER_STYLE_SQUARE -> styleRadio.dialog_radio_square
            else -> styleRadio.dialog_radio_rounded_corners
        }

        styleBtn.isChecked = true
    }

    private fun setupMediaCount() {
        val countRadio = view.dialog_radio_count_holder
        countRadio.setOnCheckedChangeListener { group, checkedId ->
            updateSample()
        }

        val countBtn = when (config.showFolderMediaCount) {
            FOLDER_MEDIA_CNT_LINE -> countRadio.dialog_radio_count_line
            FOLDER_MEDIA_CNT_BRACKETS -> countRadio.dialog_radio_count_brackets
            else -> countRadio.dialog_radio_count_none
        }

        countBtn.isChecked = true
    }

    private fun updateSample() {
        val photoCount = 36
        val folderName = "Camera"
        view.apply {
            when (dialog_radio_count_holder.checkedRadioButtonId) {
                R.id.dialog_radio_count_line -> {
                    dir_name.text = folderName
                    photo_cnt.text = photoCount.toString()
                    photo_cnt.beVisible()
                }
                R.id.dialog_radio_count_brackets -> {
                    photo_cnt.beGone()
                    dir_name.text = "${dir_name.text} ($photoCount)"
                }
                else -> {
                    dir_name.text = folderName
                    photo_cnt.beGone()
                }
            }

            val options = RequestOptions().centerCrop()
            Glide.with(activity)
                .load(R.drawable.sample_logo)
                .apply(options)
                .into(dir_thumbnail)
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        val style = when (view.dialog_radio_style.checkedRadioButtonId) {
            R.id.dialog_radio_square -> FOLDER_STYLE_SQUARE
            else -> FOLDER_STYLE_ROUNDED_CORNERS
        }

        val count = when (view.dialog_radio_count_holder.checkedRadioButtonId) {
            R.id.dialog_radio_count_line -> FOLDER_MEDIA_CNT_LINE
            R.id.dialog_radio_count_brackets -> FOLDER_MEDIA_CNT_BRACKETS
            else -> FOLDER_MEDIA_CNT_NONE
        }

        config.folderStyle = style
        config.showFolderMediaCount = count
        config.limitFolderTitle = view.dialog_limit_title.isChecked
        callback()
    }
}