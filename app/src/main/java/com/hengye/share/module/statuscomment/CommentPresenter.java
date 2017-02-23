package com.hengye.share.module.statuscomment;

import android.content.res.Resources;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.StatusComment;
import com.hengye.share.model.StatusComments;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBStatusComments;
import com.hengye.share.module.status.StatusRxUtil;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.WBUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class CommentPresenter extends ListDataPresenter<StatusComment, CommentContract.View>
        implements CommentContract.Presenter{

    private CommentGroup mCommentGroup;
    private CommentType mCommentType;

    public CommentPresenter(CommentContract.View mvpView, CommentGroup commentGroup) {
        super(mvpView);
        mCommentGroup = commentGroup;
        mCommentType = mCommentGroup.getCommentType();
    }

    @Override
    public void loadWBComment(String id, final boolean isRefresh) {
        if(id == null){
            id = "0";
        }
        getMvpView().onTaskStart();
        getComments(id, isRefresh)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribeWith(getStatusCommentsSubscriber(isRefresh));
    }

    private Single<ArrayList<StatusComment>> getComments(String id, final boolean isRefresh) {
        if (mCommentType == CommentType.COMMENT_TO_ME) {
            return RetrofitManager
                    .getWBService()
                    .listCommentToMeStatus(getWBCommentParameter(id, isRefresh))
                    .flatMap(flatWBStatusComments());
        } else {
            return RetrofitManager
                    .getWBService()
                    .listCommentByMeStatus(getWBCommentParameter(id, isRefresh))
                    .flatMap(flatWBStatusComments());
        }
    }

    private Map<String, String> getWBCommentParameter(String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getToken());
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }

        ub.addParameter("count", WBUtil.getWBStatusRequestCount());
        return ub.getParameters();
    }

    private SingleObserver<List<StatusComment>> getStatusCommentsSubscriber(final boolean isRefresh) {
        return new ListDataSingleObserver(isRefresh);
    }

    private Function<WBStatusComments, SingleSource<ArrayList<StatusComment>>> mFlatWBStatusComments;

    private Function<WBStatusComments, SingleSource<ArrayList<StatusComment>>> flatWBStatusComments() {
        if (mFlatWBStatusComments == null) {
            mFlatWBStatusComments = new Function<WBStatusComments, SingleSource<ArrayList<StatusComment>>>() {
                @Override
                public SingleSource<ArrayList<StatusComment>> apply(WBStatusComments wbStatusComments) throws Exception {
                    return Single
                            .just(StatusComment.getComments(wbStatusComments))
                            .flatMap(StatusRxUtil.flatStatusCommentsShortUrl())
                            .flatMap(new Function<StatusComments, SingleSource<ArrayList<StatusComment>>>() {
                                @Override
                                public SingleSource<ArrayList<StatusComment>> apply(StatusComments statusComments) throws Exception {
                                    saveData(statusComments.getComments());
                                    return SingleHelper.justArrayList(statusComments.getComments());
                                }
                            });
                }
            };
        }
        return mFlatWBStatusComments;
    }

    /**
     * 先检测有没有缓存，没有再请求服务器
     */
    @Override
    public void loadWBComment() {
        getMvpView().onTaskStart();
        getComments(null, true)
                .onErrorReturn(new Function<Throwable, ArrayList<StatusComment>>() {
                    @Override
                    public ArrayList<StatusComment> apply(Throwable throwable) throws Exception {
                        ArrayList<StatusComment> statuses = findData();
                        if(statuses != null){
                            return statuses;
                        }
                        throw new Exception(throwable);
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribeWith(getStatusCommentsSubscriber(true));
    }

    private ArrayList<StatusComment> findData() {
        return ShareJson.findData(getModelName(), new TypeToken<ArrayList<StatusComment>>() {
        }.getType());
    }

    public void saveData(List<StatusComment> data) {
        ShareJson.saveListData(getModelName(), data);
    }

//    public void clearCache() {
//        ShareJson.saveListData(getModelName(), null);
//    }

    private String mModuleName;
    private String mModuleUid;

    public String getModelName() {
        if (mModuleName == null || mModuleUid == null || !mModuleUid.equals(UserUtil.getUid())) {
            mModuleUid = UserUtil.getUid();
            mModuleName = Status.class.getSimpleName()
                    + mModuleUid
                    + mCommentType;
        }
        return mModuleName;
    }

    public enum CommentType {
        COMMENT_TO_ME, COMMENT_BY_ME;
    }

    public static class CommentGroup implements Serializable {

        private static final long serialVersionUID = -5809183018190271012L;

        private CommentType commentType;

        public CommentGroup(CommentType commentType) {
            this.commentType = commentType;
        }

        public CommentType getCommentType() {
            return commentType;
        }

        public void setCommentType(CommentType commentType) {
            this.commentType = commentType;
        }

        @Override
        public String toString() {
            return commentType.toString();
        }

        public static String getName(CommentGroup commentGroup, Resources resources) {
            switch (commentGroup.commentType) {
                case COMMENT_TO_ME:
                    return resources.getString(R.string.title_page_comment_to_me);
                case COMMENT_BY_ME:
                    return resources.getString(R.string.title_page_comment_by_me);
                default:
                    return null;
            }
        }
    }

}
