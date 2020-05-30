package io.pantheonsite.alphaoptimus369.mrwaste.home_module.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.pantheonsite.alphaoptimus369.mrwaste.commons.listeners.OnRecyclerViewItemClickListener;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.Utils;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.LayoutItemWasteRequestBinding;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.models.WasteItem;


public class AdapterRvWasteItem extends RecyclerView.Adapter < AdapterRvWasteItem.ViewHolder >
{

    private Context context;
    private ArrayList <WasteItem> wasteItems;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public AdapterRvWasteItem(@NonNull Context context, @NonNull ArrayList<WasteItem> wasteItems,
                              @Nullable OnRecyclerViewItemClickListener
                                        onRecyclerViewItemClickListener)
    {
        this.context = context;
        this.wasteItems = wasteItems;
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;

        setHasStableIds(true);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemWasteRequestBinding binding =
                LayoutItemWasteRequestBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WasteItem currentItem = wasteItems.get(position);

        holder.binding.setAreaName(currentItem.areaName);
        holder.binding.setRequestCount(
                Utils.getTotalRequestsString(context, currentItem.requestCount)
        );

        holder.itemView.setOnClickListener(v -> {
            if (onRecyclerViewItemClickListener != null) {
                onRecyclerViewItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return wasteItems.size();
    }

    @Override
    public long getItemId(int position) {
        return wasteItems.get(position).id;
    }



    static class ViewHolder extends RecyclerView.ViewHolder
    {

        LayoutItemWasteRequestBinding binding;

        public ViewHolder(@NonNull LayoutItemWasteRequestBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
