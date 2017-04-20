package com.cutesys.bibleapp.AdapterClasses;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cutesys.bibleapp.Helperclasses.ListItem;
import com.cutesys.bibleapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ViewPagerAdapter extends PagerAdapter {

    final Random rand = new Random();
    private Context mContext;
    private int[] mResources;
    private ArrayList<ListItem> mQuotes;
    private int mImageCount;

    public ViewPagerAdapter(Context mContext, int[] mResources,int mImageCount,ArrayList<ListItem> mQuotes) {
        this.mContext = mContext;
        this.mResources = mResources;
        this.mImageCount=mImageCount;
        this.mQuotes=mQuotes;
    }

    @Override
    public int getCount() {
        return mQuotes.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        ListItem item = mQuotes.get(position);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        TextView mQuotesView=(TextView)itemView.findViewById(R.id.qoutes_view);

        // n = the number of images, that start at idx 1
        int rndInt = rand.nextInt(mResources.length);

        mQuotesView.setText(item.get_name());
        imageView.setImageResource(mResources[rndInt]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
