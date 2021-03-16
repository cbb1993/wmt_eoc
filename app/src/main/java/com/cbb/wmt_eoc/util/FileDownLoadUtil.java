package com.cbb.wmt_eoc.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.cbb.baselib.base.BaseApplication;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */
public class FileDownLoadUtil {
    private static DownLoadCallback downLoadCallback;
    private static BaseDownloadTask task;
    static Handler handler = new Handler(BaseApplication.instance.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null) {
                return;
            }

            if (msg.what == 1) {
                if (downLoadCallback != null) {
                    downLoadCallback.downLoadStart();
                }
            } else if (msg.what == 2) {
                if (downLoadCallback != null) {
                    downLoadCallback.downLoadComplete();
                }
            } else {
                Bundle bundle = msg.getData();
                int percent = bundle.getInt("percent");
                String str = "下载进度为" + percent + "%";
                if (downLoadCallback != null) {
                    downLoadCallback.downLoadIng(percent);
                }
            }
        }
    };

    public static void downLoad(final String url, final String path, final DownLoadCallback callback) {
        downLoadCallback = callback;
        task = FileDownloader
                .getImpl()
                .create(url)
                .setPath(path)
                .setAutoRetryTimes(5)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        if (callback != null) {
                            handler.sendEmptyMessage(1);//开始下载
                        }
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        if (callback != null) {
                            sendMsg((int) ((float) soFarBytes / totalBytes * 100));
                        }
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        if (callback != null) {
                            handler.sendEmptyMessage(2);
                        }
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        if (callback != null) {
                            if (task != null) {
                                callback.error("异常：\n" + task.getUrl() + "\n" + e.getMessage());
                            }
                        }
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });
        task.start();
    }

    public static void cancel(){
        if(task!=null){
            task.cancel();
        }
    }

    private static void sendMsg(int percent) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("percent", percent);
        message.setData(bundle);
        message.what = 3;
        handler.sendMessage(message);
    }

    public interface DownLoadCallback {
        void downLoadIng(int progress);

        void downLoadStart();

        void downLoadComplete();

        void error(String error);

    }

    //安装apk
    public static void installApk(File file, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
    }
}
