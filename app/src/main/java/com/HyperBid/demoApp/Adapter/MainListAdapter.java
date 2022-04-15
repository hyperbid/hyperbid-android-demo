package com.HyperBid.demoApp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.HyperBid.demoApp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainListAdapter extends BaseAdapter implements View.OnClickListener {
    public interface MainListListener {
         void clickedItem(Integer position);
    }
    private MainListListener mainListListener;

    public void setMainListListener(MainListListener mainListListener) {
        this.mainListListener = mainListListener;
    }

    ArrayList<HashMap<String,Object>> hashMapArrayList = new ArrayList<>();

    private LayoutInflater inflater;
    private int currentPosition = -1;
    private Context _context;

    public MainListAdapter(Context context){

        HashMap<String,Object> hashMap = new HashMap();
        hashMap.put("image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.iv_rewarded_video));
        hashMap.put("unselected_image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.reward_video));
        hashMap.put("title",context.getResources().getString(R.string.hyperbid_title_rewarded_video));
        hashMap.put("description",context.getResources().getString(R.string.hyperbid_main_desc_rewarded_video));
        hashMapArrayList.add(hashMap);

        HashMap<String,Object> hashMap2 = new HashMap();
        hashMap2.put("image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_test_interstitial));
        hashMap2.put("unselected_image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.interstitial));
        hashMap2.put("title",context.getResources().getString(R.string.hyperbid_title_interstitial));
        hashMap2.put("description",context.getResources().getString(R.string.hyperbid_main_desc_interstitial));
        hashMapArrayList.add(hashMap2);

        HashMap<String,Object> hashMap3 = new HashMap();
        hashMap3.put("image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_test_splash));
        hashMap3.put("unselected_image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.splash));
        hashMap3.put("title",context.getResources().getString(R.string.hyperbid_title_splash));
        hashMap3.put("description",context.getResources().getString(R.string.hyperbid_main_desc_splash));
        hashMapArrayList.add(hashMap3);

        HashMap<String,Object> hashMap4 = new HashMap();
        hashMap4.put("image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_test_banner));
        hashMap4.put("unselected_image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.banner));
        hashMap4.put("title",context.getResources().getString(R.string.hyperbid_title_banner));
        hashMap4.put("description",context.getResources().getString(R.string.hyperbid_main_desc_banner));
        hashMapArrayList.add(hashMap4);

        HashMap<String,Object> hashMap5 = new HashMap();
        hashMap5.put("image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_test_native));
        hashMap5.put("unselected_image", BitmapFactory.decodeResource(context.getResources(),R.mipmap.native_un));
        hashMap5.put("title",context.getResources().getString(R.string.hyperbid_title_native));
        hashMap5.put("description",context.getResources().getString(R.string.hyperbid_main_desc_native));
        hashMapArrayList.add(hashMap5);

        _context = context;

        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return hashMapArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView = inflater.inflate(R.layout.main_list_row,null);

        }
        convertView.setTag(String.valueOf(position));
        convertView.setOnClickListener(this);

       HashMap<String,Object> hashMap =  hashMapArrayList.get(position);

        RelativeLayout container = convertView.findViewById(R.id.container);

        LinearLayout iv_container = convertView.findViewById(R.id.iv_container);

        TextView tv_title = convertView.findViewById(R.id.tv_title);
        tv_title.setText(hashMap.get("title").toString());

        TextView tv_desc = convertView.findViewById(R.id.tv_desc);
        tv_desc.setText(hashMap.get("description").toString());


        ImageView iv = convertView.findViewById(R.id.iv);


        if (currentPosition == position){
            container.setBackground(ResourcesCompat.getDrawable(_context.getResources(),R.drawable.selected_rounded_background,null));
            iv_container.setBackground(ResourcesCompat.getDrawable(_context.getResources(),R.drawable.shape_purple_width1_corners2,null));
            iv.setImageBitmap((Bitmap) hashMap.get("image"));
        }else {
            container.setBackground(ResourcesCompat.getDrawable(_context.getResources(),R.drawable.white_rounded_background,null));
            iv_container.setBackground(ResourcesCompat.getDrawable(_context.getResources(),R.drawable.shape_gray_width1_corners2,null));
            iv.setImageBitmap((Bitmap) hashMap.get("unselected_image"));
        }


        return convertView;
    }

    @Override
    public void onClick(View v) {

       currentPosition = Integer.parseInt(v.getTag().toString());
        notifyDataSetChanged();

        if (mainListListener!=null)
            mainListListener.clickedItem(currentPosition);
    }
}
