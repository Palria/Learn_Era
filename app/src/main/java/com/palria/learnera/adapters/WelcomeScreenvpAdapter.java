package com.palria.learnera.adapters;

import android.content.Context;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.palria.learnera.R;
import com.palria.learnera.models.WelcomeScreenItemModal;

import java.util.ArrayList;

public class WelcomeScreenvpAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<WelcomeScreenItemModal> items;


    public WelcomeScreenvpAdapter(Context mContext, ArrayList<WelcomeScreenItemModal> items) {
        this.mContext = mContext;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.welcome_activity_vpadapter_layout_item,container,false);

        WelcomeScreenItemModal itemModal = items.get(position);

        ImageView imageView = item_view.findViewById(R.id.image);
        TextView titleTextView = item_view.findViewById(R.id.title);
        TextView contentTextView = item_view.findViewById(R.id.body);

        titleTextView.setText(itemModal.getTitle());
        contentTextView.setText(itemModal.getSubTitle());
        imageView.setImageResource(itemModal.getImageId());

        Animation slideAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        Animation slideDownAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        Animation slideUpSlowAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_slow);
        titleTextView.startAnimation(slideAnimation);
        imageView.startAnimation(slideDownAnimation);
        contentTextView.startAnimation(slideUpSlowAnimation);


        container.addView(item_view);
        return item_view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((LinearLayout)object);
    }
}
