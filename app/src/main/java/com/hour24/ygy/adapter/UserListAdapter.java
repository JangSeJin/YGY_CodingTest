package com.hour24.ygy.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hour24.ygy.R;
import com.hour24.ygy.BR;
import com.hour24.ygy.model.UserModel;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;

import java.util.ArrayList;

/**
 * User List Adapter
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    // Variables
    private Context mContext;
    private ArrayList<UserModel.Item> mList;

    private boolean mIsUserList = true; // 화면에 어떤 종류를 보여줄 것인가 유무

    public UserListAdapter(Context context, ArrayList<UserModel.Item> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setIsUserList(boolean isUserList) {
        this.mIsUserList = isUserList;
    }

    // Like 된 아이템 리턴
    public ArrayList<UserModel.Item> getLikeList() {

        ArrayList<UserModel.Item> likeList = new ArrayList<>();

        for (UserModel.Item model : mList) {
            if (model.isLike()) {
                likeList.add(model);
            }
        }

        return likeList;
    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.main_user_item, parent, false).getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserListAdapter.ViewHolder holder, final int position) {

        try {

            UserModel.Item item = mList.get(position);
            holder.getBinding().setVariable(BR.model, item);
            holder.getBinding().setVariable(BR.handler, new BindingHandler());
            holder.getBinding().setVariable(BR.position, position);
            holder.getBinding().executePendingBindings();

//            if (item.isLike() && mIsUserList) {
//                holder.likeButton.setVisibility(View.VISIBLE);
//                holder.likeButton.performClick();
//                holder.likeButton.setOnAnimationEndListener(new OnAnimationEndListener() {
//                    @Override
//                    public void onAnimationEnd(LikeButton likeButton) {
//                        holder.likeButton.setVisibility(View.GONE);
//                    }
//                });
//            } else {
//                holder.likeButton.setVisibility(View.GONE);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;
        private LikeButton likeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            // Like Button
            likeButton = (LikeButton) itemView.findViewById(R.id.like_button);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    // Handlers
    public class BindingHandler {

        // xml 에 정의
        public void onClick(final View view, UserModel.Item item, int position) {
            try {
                switch (view.getId()) {
                    case R.id.like:
                        if (mIsUserList) {
                            // like 만 가능
                            if (!item.isLike()) {
                                item.setLike(true);
                                mList.set(position, item);
                                notifyItemChanged(position);
                            }
                        } else {
                            // unLike 만 가능
                            if (item.isLike()) {
                                item.setLike(false);
                                mList.set(position, item);
                                notifyItemChanged(position);
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
