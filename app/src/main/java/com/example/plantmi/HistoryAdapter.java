package com.example.plantmi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder>{
    LayoutInflater mInflater;
    Context context;
    HistoryDataSource historyDataSource;

    //constructor to take in data
    HistoryAdapter(Context context, HistoryDataSource historyDataSource){
        this.context = context;
        this.historyDataSource = historyDataSource;
        mInflater = LayoutInflater.from(context);
    }

    //inflates layout
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("RecyclerView", "onCreateViewHolder()");
        View itemView = mInflater.inflate(R.layout.historycard, parent, false);
        return new HistoryHolder(itemView);
    }

    //put data on list item
    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        String data = historyDataSource.getHistory(position);
        holder.textViewHistory.setText(data);
        Log.d("RecyclerView", "onBindViewHolder():" + data);
    }

    @Override
    public int getItemCount() {
        return historyDataSource.getSize();
    }

    // inner class to hold references to widgets in each list item
    class HistoryHolder extends RecyclerView.ViewHolder{
        private TextView textViewHistory;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewHistory = itemView.findViewById(R.id.cardViewTextData);
        }

        public TextView getTextview() {
            return textViewHistory;
        }
    }
}
