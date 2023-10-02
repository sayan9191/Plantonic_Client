package com.example.plantonic.ui.dialogbox

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import com.example.plantonic.R


class LoadingScreen {
    companion object{
        var mAlertDialog: AlertDialog? = null

        fun showLoadingDialog(context: Context){
            val mDialogView: View = LayoutInflater.from(context).inflate(R.layout.loading_dialog_box, null)

            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setCancelable(false)

            if (mAlertDialog != null){
                if (!mAlertDialog?.isShowing!!){
                    mAlertDialog = mBuilder.show()
                }
            }else{
                mAlertDialog = mBuilder.show()
            }



            mAlertDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

            mAlertDialog?.setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    dialog.dismiss()
                    (context as Activity).finish()
                    return@setOnKeyListener true
                }
                false
            }
        }



        fun hideLoadingDialog(){
            if (mAlertDialog?.isShowing == true)
                mAlertDialog?.dismiss()
        }
    }
}