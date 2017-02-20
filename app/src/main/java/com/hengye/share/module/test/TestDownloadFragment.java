package com.hengye.share.module.test;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.fragment.BaseFragment;

/**
 * Created by yuhy on 2016/12/18.
 */

public class TestDownloadFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public String getTitle() {
        return getClass().getSimpleName();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test_download;
    }

    private ProgressBar mProgress, mProgressResult;
    private TextView mTextView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextView = (TextView) findViewById(R.id.tv_result);
        mProgress = (ProgressBar) findViewById(R.id.pb_request);
        mProgressResult = (ProgressBar) findViewById(R.id.pb_result);
        findViewById(R.id.btn_request_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_request_btn) {

            download();

//            Log.i("volleydemo", "path : " + getApplication().getExternalCacheDir().getPath().toString());
//            mProgress.setVisibility(View.VISIBLE);
//            RequestManager.addToRequestQueue(getRequest(), getRequestTag());
        }
    }

    private String downloadUrl = "https://publicobject.com/helloworld.txt";

    private void download() {
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);

        // uri 是你的下载地址，可以使用Uri.parse("http://")包装成Uri对象
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(downloadUrl));

// 通过setAllowedNetworkTypes方法可以设置允许在何种网络下下载，
// 也可以使用setAllowedOverRoaming方法，它更加灵活
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

// 此方法表示在下载过程中通知栏会一直显示该下载，在下载完成后仍然会显示，
// 直到用户点击该通知或者消除该通知。还有其他参数可供选择
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

// 设置下载文件存放的路径，同样你可以选择以下方法存放在你想要的位置。
// setDestinationUri
// setDestinationInExternalPublicDir
        req.setDestinationInExternalFilesDir(getContext(), Environment.DIRECTORY_DOWNLOADS, "test");

// 设置一些基本显示信息
        req.setTitle("test.txt");
        req.setDescription("下载完后请点击打开");
//        req.setMimeType("application/vnd.android.package-archive");

// Ok go!
        long downloadId = downloadManager.enqueue(req);

        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null && c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PENDING:
                    break;
                case DownloadManager.STATUS_PAUSED:
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    break;
                case DownloadManager.STATUS_FAILED:
                    break;
            }
            c.close();
        }
    }
}

















