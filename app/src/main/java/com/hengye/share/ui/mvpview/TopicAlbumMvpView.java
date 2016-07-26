package com.hengye.share.ui.mvpview;

import java.util.List;

public interface TopicAlbumMvpView extends MvpView {

    void stopLoading(boolean isRefresh);

    void handleAlbumData(List<String> urls, boolean isRefresh);

    void handleCache(List<String> data);
}
