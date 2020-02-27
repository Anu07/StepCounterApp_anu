package com.sd.src.stepcounterapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.sd.src.stepcounterapp.R;

public class LoadingDialog {

    static LoadingDialog mInstance;
    @Nullable
    Dialog mProgress;

    public static void initLoader() {
        if (mInstance == null)
            mInstance = new LoadingDialog();
    }

    public static LoadingDialog getLoader() {
        if (mInstance != null)
            return mInstance;
        else {
            mInstance = new LoadingDialog();
            return mInstance;
        }
    }

    public void dismissLoader() {
        try {
            if (mProgress != null) {
                mProgress.cancel();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoader(@NonNull Context con) {
        if (mProgress != null) {
            mProgress.cancel();
        }
        mProgress = new Dialog(con);
        mProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = mProgress.getWindow().getAttributes();

        wmlp.gravity = Gravity.CENTER;
        mProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mProgress.setCancelable(false);
        mProgress.setContentView(R.layout.loading_dialog);
        mProgress.show();

    }



}
