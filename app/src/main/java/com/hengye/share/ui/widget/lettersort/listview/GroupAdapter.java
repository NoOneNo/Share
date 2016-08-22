package com.hengye.share.ui.widget.lettersort.listview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hengye.share.adapter.listview.CommonAdapter;
import com.hengye.share.adapter.listview.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuhy on 16/4/25.
 */
public abstract class GroupAdapter<K extends GroupAdapter.SortKey, V, VH_G extends GroupAdapter.GroupHolder, VH_C extends GroupAdapter.GroupHolder> extends CommonAdapter<Object, GroupAdapter.GroupHolder> {

    public final static int VIEW_TYPE_GROUP = 0;
    public final static int VIEW_TYPE_CHILD = 1;

    private HashMap<SortKey, Integer> mKeyIndex = new HashMap<>();

    public GroupAdapter(Context context, Map<K, List<V>> map) {
        init(context, convertMapToList(map));
    }

    public GroupAdapter(Context context, List<Object> list) {
        init(context, list);
    }

    /**
     * 转换分组数据为List,并且更新键值的索引
     * @param map
     * @return
     */
    public List<Object> convertMapToList(Map<K, List<V>> map) {
        List<Object> mData = new ArrayList<>();
        mKeyIndex.clear();
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            mData.add(entry.getKey());
            mKeyIndex.put(entry.getKey(), mData.size() - 1);
            for (V v : entry.getValue()) {
                mData.add(v);
            }
        }
        return mData;
    }

    public void refresh(Map<K, List<V>> map) {
        super.refresh(convertMapToList(map));
    }

    @Override
    public void refresh(List<Object> data) {
        super.refresh(data);
        mKeyIndex.clear();
    }

    /**
     * 得到键值的索引值
     * @param k
     * @return
     */
    public int getKeyIndex(K k){
        Integer integer =  mKeyIndex.get(k);
        if(integer == null){
            return getKeyIndexFromList(k);
        }
        return integer;
    }

    public int getKeyIndexFromList(K k){
        for(int i = 0; i < getCount(); i++){
            Object obj = getItem(i);
            if(obj != null && obj instanceof SortKey){
                if(obj.equals(k)){
                    mKeyIndex.put(k, i);
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = getItem(position);

        if (obj instanceof SortKey) {
            return VIEW_TYPE_GROUP;
        }
        return VIEW_TYPE_CHILD;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);

        View view = null;
        Object obj = getItem(position);
        switch (viewType) {
            case VIEW_TYPE_GROUP:
                view = getGroupView((K)obj, position, convertView, parent);
                break;
            case VIEW_TYPE_CHILD:
                view = getChildView((V)obj, position, convertView, parent);
                break;
        }
        return view;
    }

    public View getGroupView(K key, int position, View convertView, ViewGroup parent){
        VH_G vh;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(getGroupLayoutId(), null);
            vh = onCreateGroupViewHolder(convertView, parent);
            convertView.setTag(vh);
        }else{
            vh = (VH_G)convertView.getTag();
        }

        onBindGroupViewHolder(vh, key, position);
        return convertView;
    }

    public View getChildView(V value, int position, View convertView, ViewGroup parent){
        VH_C vh;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(getChildLayoutId(), null);
            vh = onCreateChildViewHolder(convertView, parent);
            convertView.setTag(vh);
        }else{
            vh = (VH_C)convertView.getTag();
        }

        onBindChildViewHolder(vh, value, position);
        return convertView;
    }

    public abstract @LayoutRes int getGroupLayoutId();

    public abstract VH_G onCreateGroupViewHolder(View convertView, ViewGroup parent);

    public abstract void onBindGroupViewHolder(VH_G vh, K key, int position);

    public abstract @LayoutRes int getChildLayoutId();

    public abstract VH_C onCreateChildViewHolder(View convertView, ViewGroup parent);

    public abstract void onBindChildViewHolder(VH_C vh, V value, int position);

    public interface SortKey {
    }

    public abstract static class GroupHolder extends ViewHolder<Object>{

        public GroupHolder(View v){
            super(v);
        }
    }
}
