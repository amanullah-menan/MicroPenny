package com.menan.micropenny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReferralViewAdapter extends RecyclerView.Adapter<ReferralViewAdapter.ReferralViewHolder> {

    private Context cntx;
    private List<ReferralSetFire> referralSetFireList;


    public ReferralViewAdapter(Context cntx, List<ReferralSetFire> referralSetFireList) {
        this.cntx = cntx;
        this.referralSetFireList = referralSetFireList;

    }

    @NonNull
    @Override
    public ReferralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.referral_row,parent,false);
        return new ReferralViewAdapter.ReferralViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferralViewHolder holder, int position) {
        ReferralSetFire referralSetFire=referralSetFireList.get(position);
        holder.RefName.setText(referralSetFire.getName());
    }

    @Override
    public int getItemCount() {
        return referralSetFireList.size();
    }

    class ReferralViewHolder extends RecyclerView.ViewHolder{
        public TextView RefName;

        public ReferralViewHolder(@NonNull View itemView) {
            super(itemView);
            RefName=itemView.findViewById(R.id.referralName);
        }
    }

}
