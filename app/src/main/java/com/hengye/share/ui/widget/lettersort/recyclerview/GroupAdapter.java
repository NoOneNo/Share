package com.hengye.share.ui.widget.lettersort.recyclerview;


import android.content.Context;
import android.view.ViewGroup;

import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.adapter.recyclerview.ItemViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by yuhy on 16/8/22.
 */
public abstract class GroupAdapter<K extends GroupAdapter.SortKey, V> extends CommonAdapter<Object> {


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

        if(map != null && !map.isEmpty()) {
            for (Map.Entry<K, List<V>> entry : map.entrySet()) {
                mData.add(entry.getKey());
                mKeyIndex.put(entry.getKey(), mData.size() - 1);
                for (V v : entry.getValue()) {
                    mData.add(v);
                }
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
        for(int i = 0; i < getBasicItemCount(); i++){
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
    public int getBasicItemType(int position) {
        Object obj = getItem(position);

        if (obj instanceof SortKey) {
            return VIEW_TYPE_GROUP;
        }
        return VIEW_TYPE_CHILD;
    }

    @Override
    public ItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_GROUP){
            return onCreateGroupViewHolder(parent);
        }else{
            return onCreateChildViewHolder(parent);
        }
    }


    public abstract ItemViewHolder onCreateGroupViewHolder(ViewGroup parent);

    public abstract ItemViewHolder onCreateChildViewHolder(ViewGroup parent);

    public interface SortKey {
    }

}













