package com.scatl.uestcbbs.module.update.view;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.scatl.uestcbbs.R;
import com.scatl.uestcbbs.base.BaseDialogFragment;
import com.scatl.uestcbbs.base.BasePresenter;
import com.scatl.uestcbbs.entity.UpdateBean;
import com.scatl.uestcbbs.module.update.presenter.UpdatePresenter;
import com.scatl.uestcbbs.util.Constant;
import com.scatl.uestcbbs.util.FileUtil;
import com.scatl.uestcbbs.util.SharePrefUtil;

import java.io.File;
import java.text.DecimalFormat;

/**
 * author: sca_tl
 * description:
 * date: 2019/12/18 19:33
 */
public class UpdateFragment extends BaseDialogFragment implements UpdateView{

    private TextView title, content, progressText;
    private CheckBox ignoreUpdate;
    private ProgressBar progressBar;
    private Button downloadBtn;

    private UpdatePresenter updatePresenter;
    private UpdateBean updateBean;

    private File apkFile;

    public static UpdateFragment getInstance(Bundle bundle) {
        UpdateFragment updateFragment = new UpdateFragment();
        updateFragment.setArguments(bundle);
        return updateFragment;
    }

    @Override
    protected void getBundle(Bundle bundle) {
        super.getBundle(bundle);
        if (bundle != null) {
            updateBean = (UpdateBean) bundle.getSerializable(Constant.IntentKey.DATA);
        }
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_update;
    }

    @Override
    protected void findView() {
        title = view.findViewById(R.id.dialog_update_title);
        content = view.findViewById(R.id.dialog_update_content);
        progressBar = view.findViewById(R.id.dialog_update_progressbar);
        downloadBtn = view.findViewById(R.id.dialog_update_download_btn);
        progressText = view.findViewById(R.id.dialog_update_progress_text);
        ignoreUpdate = view.findViewById(R.id.dialog_update_ignore_update);
    }

    @Override
    protected void initView() {
        updatePresenter = (UpdatePresenter) presenter;

        if (updateBean.isForceUpdate) { setCancelable(false); }

        downloadBtn.setOnClickListener(this);
        ignoreUpdate.setOnClickListener(this);

        downloadBtn.setTag(DownloadStatus.DOWNLOAD_PREPARE);
        title.setText(updateBean.title);
        content.setText(Html.fromHtml(updateBean.updateContent));
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onClickListener(View view) {
        if (view.getId() == R.id.dialog_update_download_btn) {
            if (downloadBtn.getTag() == DownloadStatus.DOWNLOAD_PREPARE) {
                downloadBtn.setTag(DownloadStatus.DOWNLOADING);
                downloadBtn.setText("下载中");
                downloadBtn.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);
                updatePresenter.downloadApk(mActivity.getExternalFilesDir(Constant.AppPath.TEMP_PATH),
                        updateBean.apkUrl);
            }
            if (downloadBtn.getTag() == DownloadStatus.DOWNLOADED) {
                if (apkFile != null) FileUtil.installApk(mActivity, apkFile);
            }
        }

        if (view.getId() == R.id.dialog_update_ignore_update && !updateBean.isForceUpdate && downloadBtn.getTag() != DownloadStatus.DOWNLOADING) {
            SharePrefUtil.setIgnoreVersionCode(mActivity, updateBean.versionCode);
            dismiss();
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        return new UpdatePresenter();
    }


    @Override
    public void onDownloadProgress(int progress, long total) {
        setCancelable(false);
        mActivity.runOnUiThread(() -> {
            progressBar.setProgress(progress);
            DecimalFormat df = new DecimalFormat("#0.00");
            progressText.setText(String.valueOf(progress + "%  " +
                    df.format((double) progress * total/1024/1024/100) + "MB/" +
                    df.format((double) total/1024/1024) + "MB"));
        });
    }

    @Override
    public void onDownloadSuccess(File file) {
        this.apkFile = file;
        if (!updateBean.isForceUpdate) { setCancelable(true); }
        mActivity.runOnUiThread(() -> {
            downloadBtn.setText("安装");
            downloadBtn.setClickable(true);
            downloadBtn.setTag(DownloadStatus.DOWNLOADED);
        });

        FileUtil.installApk(mActivity, file);
    }

    @Override
    public void onDownloadFail(String msg) {
        mActivity.runOnUiThread(() -> {
            downloadBtn.setText("立即更新");
            downloadBtn.setClickable(true);
            downloadBtn.setTag(DownloadStatus.DOWNLOAD_PREPARE);
            progressBar.setVisibility(View.INVISIBLE);
            showToast(msg);
        });
    }

    public enum DownloadStatus {
        DOWNLOAD_PREPARE,
        DOWNLOADING,
        DOWNLOADED
    }

}
