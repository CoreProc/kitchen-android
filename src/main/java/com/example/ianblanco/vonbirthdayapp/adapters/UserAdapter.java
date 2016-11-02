package com.example.ianblanco.vonbirthdayapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ianblanco.vonbirthdayapp.R;
import com.example.ianblanco.vonbirthdayapp.models.SampleModel;

import java.util.List;


/**
 * Created by IanBlanco on 9/9/2016.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

//    private List<SampleModel> mUsers;

    private List<SampleModel> mUsers;

    private int mRowLayout;
    private Context mContext;


    public UserAdapter(List<SampleModel> users, int rowLayout, Context context) {
        this.mUsers = users;
        this.mRowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mRowLayout, parent, false);

        return new UserViewHolder(view);

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayoutUser;
        TextView mTextViewFirstName;
        TextView mTextViewLastName;
        Button mButtonUpdate, mButtonDelete;


        public UserViewHolder(View v) {
            super(v);
            mLayoutUser = (LinearLayout) v.findViewById(R.id.user_layout);
            mTextViewFirstName = (TextView) v.findViewById(R.id.TextViewfirstName);
            mTextViewLastName = (TextView) v.findViewById(R.id.TextViewLastName);
            mButtonDelete = (Button) v.findViewById(R.id.buttonDelete);
            mButtonUpdate = (Button) v.findViewById(R.id.buttonUpdate);
        }
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        holder.mTextViewFirstName.setText(mUsers.get(position).getUser());
        holder.mTextViewLastName.setText(mUsers.get(position).getUrl());

//        holder.mTextViewFirstName.setText(mUsers.get(position).getName());
//        holder.mTextViewLastName.setText("" + mUsers.get(position).getAge());
//
//        holder.mLayoutUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("logs sample", "" + mUsers.get(position).getId());
//            }
//        });
//
//        holder.mButtonUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = GreenDaoSampleUpdate.newIntent(mContext, mUsers.get(position).getId(), mUsers.get(position).getName(), mUsers.get(position).getAge(), "Update");
//                mContext.startActivity(intent);
//            }
//        });
//
//        holder.mButtonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = GreenDaoSampleUpdate.newIntent(mContext,  mUsers.get(position).getId(), "Delete");
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


}
