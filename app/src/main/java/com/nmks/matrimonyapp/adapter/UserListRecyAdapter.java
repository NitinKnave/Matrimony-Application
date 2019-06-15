package com.nmks.matrimonyapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.nmks.matrimonyapp.R;
import com.nmks.matrimonyapp.entities.Result;
import com.nmks.matrimonyapp.users.UserPresenterImpl;
import com.nmks.matrimonyapp.users.UsersContract;

import net.colindodd.gradientlayout.GradientLinearLayout;
import net.colindodd.gradientlayout.GradientRelativeLayout;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListRecyAdapter extends RecyclerView.Adapter<UserListRecyAdapter.UserListHolder> {

    private Context mContext;
    private ArrayList<Result> results;
    private UsersContract.UserPresenter userPresenter;
    private Result result;
    private int resPosition;


    public UserListRecyAdapter(Context mContext, ArrayList<Result> results, UsersContract.UserPresenter userPresenter) {
        this.mContext = mContext;
        this.results = results;
        this.userPresenter = userPresenter;
    }

    @NonNull
    @Override
    public UserListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new UserListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListHolder holder, int position) {
        Random random = new Random();
        holder.layout.setStartColor(Color.rgb(255,random.nextInt(255),random.nextInt(255)))
                .setEndColor(Color.rgb(255,random.nextInt(255),random.nextInt(255)))
                .setOrientation(GradientDrawable.Orientation.TR_BL);

        SpannableStringBuilder nameBuider = new SpannableStringBuilder();
        nameBuider.append(doBold(results.get(position).getName().getFirst().toUpperCase()));
        nameBuider.append(results.get(position).getName().getLast());

        holder.userNameTv.setText(nameBuider);
        StringBuilder sb = new StringBuilder();
        if (results.get(position).getDob()!=null){
            if (results.get(position).getDob().getAge()!=0){
                sb.append(results.get(position).getDob().getAge()+" Yrs, ");
            }
        }
        if (results.get(position).getLocation()!=null){
            if (results.get(position).getLocation().getCity()!=null){
                sb.append(results.get(position).getLocation().getCity()+",");
            }
            if (results.get(position).getLocation().getState()!=null){
                sb.append(results.get(position).getLocation().getState()+" ");
            }

        }
        holder.userDiscTv.setText(sb.toString());

        Glide.with(mContext)
                .load(results.get(position).getPicture().getMedium())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                .into(holder.userImageTv);

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void addItemToPosition() {
        try {
            results.add(resPosition,result);
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class UserListHolder extends RecyclerView.ViewHolder implements View.OnClickListener,Animation.AnimationListener {
        @BindView(R.id.userImageTv)
        ImageView userImageTv;
        @BindView(R.id.userNameTv)
        TextView userNameTv;
        @BindView(R.id.layout)
        GradientRelativeLayout layout;
        @BindView(R.id.userDiscription)
        TextView userDiscTv;
        @BindView(R.id.cancelBtn)
        ImageView cancelBtn;
        @BindView(R.id.checkedBtn)
        ImageView selectBtn;
        View itemView;
        boolean isCancelled = true;

        public UserListHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            cancelBtn.setOnClickListener(this);
            selectBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Animation anim;
            if (v==cancelBtn){
                anim = AnimationUtils.loadAnimation(mContext,
                        R.anim.left_out);
                isCancelled = true;

            }else {
                anim = AnimationUtils.loadAnimation(mContext,
                        android.R.anim.slide_out_right);
                isCancelled = false;

            }
            anim.setDuration(400);
            itemView.startAnimation(anim);
            anim.setAnimationListener(this);
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            try {
                result = results.get(getAdapterPosition());
                resPosition = getAdapterPosition();
                results.remove(getAdapterPosition()); //Remove the current content from the array
                notifyDataSetChanged(); //Refresh list
                if (isCancelled){
                    userPresenter.showCancelledSnack(result.getName().getFirst());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private SpannableString doBold(String s) {
        SpannableString spanString = new SpannableString(s + "\f");
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        RelativeSizeSpan mediumSizeText = new RelativeSizeSpan(1);
        spanString.setSpan(mediumSizeText, 0, spanString.length(), 0);
        return spanString;
    }


}
