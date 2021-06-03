package com.sabharish.securenotes.card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.sabharish.securenotes.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    List<String> titles;
    List<String> cnos;
    List<String> expdates;
    List<Integer> cvvs;

    public CardAdapter(List<String> Name, List<String> cno,List<String> expdate,List<Integer> cvv) {
        this.titles = Name;
        this.cnos = cno;
        this.cvvs=cvv;
        this.expdates=expdate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Title.setText(titles.get(position));
        holder.CardNO.setText(cnos.get(position));
        holder.CVV.setText(cvvs.get(position));
        holder.Expdate.setText(expdates.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Title,CardNO,CVV,Expdate;
        View view;
        CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.cardname);
            CardNO = itemView.findViewById(R.id.no);
            CVV=itemView.findViewById(R.id.cvvno);
            Expdate=itemView.findViewById(R.id.date);
            mCardView = itemView.findViewById(R.id.carView);
            view = itemView;
        }
    }
}