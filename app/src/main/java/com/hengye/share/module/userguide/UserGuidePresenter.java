package com.hengye.share.module.userguide;

import android.util.Xml;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.IOUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class UserGuidePresenter extends ListDataPresenter<UserGuide, UserGuideContract.View>
        implements UserGuideContract.Presenter {

    public UserGuidePresenter(UserGuideContract.View mvpView) {
        super(mvpView);
    }

    @Override
    public void listUserGuide() {

        getMvpView().onTaskStart();


        Single.just(R.raw.user_guide)
                .flatMap(new Function<Integer, SingleSource<ArrayList<UserGuide>>>() {
                    @Override
                    public SingleSource<ArrayList<UserGuide>> apply(Integer integer) throws Exception {
                        InputStream is = ResUtil.getResources().openRawResource(integer);
                        ArrayList<UserGuide> userGuides = null;
                        try {
                            userGuides = loadUserGuideXml(is);
                        } catch (XmlPullParserException | IOException e) {
                            e.printStackTrace();
                        } finally {
                            IOUtil.closeQuietly(is);
                        }
                        return SingleHelper.justArrayList(userGuides);
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new ListDataSingleObserver(true));
//        ResUtil.getResources().openRawResource(R.r)
    }

    private ArrayList<UserGuide> loadUserGuideXml(InputStream inputstream)
            throws XmlPullParserException, IOException {
        ArrayList<UserGuide> userGuides = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputstream, "UTF-8");
        int eventCode = parser.getEventType();
        UserGuide userGuide = null;
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            switch (eventCode) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if ("item".equals(parser.getName())) {
                        userGuide = new UserGuide();
                        parser.next();
                        userGuide.setContent(parser.getText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("item".equals(parser.getName()) && userGuide != null) {
                        userGuides.add(userGuide);
                        userGuide = null;
                    }
                    break;
                default:
                    break;
            }
            eventCode = parser.next();
        }

        return userGuides;
    }
}
