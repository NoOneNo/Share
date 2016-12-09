package com.hengye.share.module.groupmanage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Address;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;

import java.util.List;

/**
 * Created by yuhy on 2016/12/6.
 */

public class AroundAddressAdapter extends CommonAdapter<Address>{

    public AroundAddressAdapter(Context context, List<Address> data) {
        super(context, data);
    }

    @Override
    public AroundAddressHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new AroundAddressHolder(inflate(R.layout.item_around_address, parent));
    }

    public static class AroundAddressHolder extends ItemViewHolder<Address> {

        TextView title, desc, distance;

        public AroundAddressHolder(View v) {
            super(v);

            title = (TextView) findViewById(R.id.tv_title);
            desc = (TextView) findViewById(R.id.tv_description);
            distance = (TextView) findViewById(R.id.tv_distance);
        }

        @Override
        public void bindData(Context context, Address address, int position) {
            title.setText(address.getName());
            if(CommonUtil.isEmpty(address.getAddress())){
                desc.setText(R.string.tip_null);
            }else{
                desc.setText(address.getAddress());
            }
            distance.setText(ResUtil.getString(R.string.label_space_between, DataUtil.getCounter(address.getDistance())));
        }
    }
}
