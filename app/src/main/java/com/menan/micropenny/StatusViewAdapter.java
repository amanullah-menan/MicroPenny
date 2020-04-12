package com.menan.micropenny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StatusViewAdapter extends RecyclerView.Adapter<StatusViewAdapter.StatusViewHolder> {

private Context cntx;
private List<StatusSetFire> statusSetFireList;

    public StatusViewAdapter(Context cntx, List<StatusSetFire> statusSetFireList) {
        this.cntx = cntx;
        this.statusSetFireList = statusSetFireList;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.status_row,parent,false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int position) {
        StatusSetFire statusSetFire=statusSetFireList.get(position);
        statusViewHolder.date.setText(statusSetFire.getDate());
        statusViewHolder.impression.setText(String.valueOf(statusSetFire.getImpression()));
        statusViewHolder.click.setText(String.valueOf(statusSetFire.getClick()));
        statusViewHolder.status.setText(String.valueOf(statusSetFire.getStatus()));
        statusViewHolder.balance.setText(String.valueOf(statusSetFire.getBalance()));
        statusViewHolder.refBalance.setText(String.valueOf(statusSetFire.getRefBalance()));
    }

    @Override
    public int getItemCount() {
        return statusSetFireList.size();
    }


    class StatusViewHolder extends RecyclerView.ViewHolder{
        public TextView date,impression,click,status,balance,refBalance;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            impression=itemView.findViewById(R.id.impression);
            click=itemView.findViewById(R.id.click);
            status=itemView.findViewById(R.id.status);
            balance=itemView.findViewById(R.id.dailyBalance);
            refBalance=itemView.findViewById(R.id.refBalance);
        }
    }
}
