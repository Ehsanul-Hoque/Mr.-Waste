package io.pantheonsite.alphaoptimus369.mrwaste.home_module.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import io.pantheonsite.alphaoptimus369.mrwaste.R;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.listeners.OnRecyclerViewItemClickListener;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.utils.Utils;
import io.pantheonsite.alphaoptimus369.mrwaste.databinding.LayoutItemProductAndPriceBinding;
import io.pantheonsite.alphaoptimus369.mrwaste.home_module.models.ProductItem;


public class AdapterRvProductItem extends RecyclerView.Adapter < AdapterRvProductItem.ViewHolder >
{

    private Context context;
    private ArrayList < ProductItem > productItems;
    private boolean multiplePieceAllowed;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public AdapterRvProductItem(@NonNull Context context, @NonNull ArrayList<ProductItem> productItems,
                                boolean multiplePieceAllowed,
                                @Nullable OnRecyclerViewItemClickListener
                                        onRecyclerViewItemClickListener)
    {
        this.context = context;
        this.productItems = productItems;
        this.multiplePieceAllowed = multiplePieceAllowed;
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;

        setHasStableIds(true);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemProductAndPriceBinding binding =
                LayoutItemProductAndPriceBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductItem currentItem = productItems.get(position);

        String title;
        String price;

        if (multiplePieceAllowed) {
            title = String.format(
                    Locale.ENGLISH,
                    "%s (%d %s)",
                    currentItem.name,
                    currentItem.itemCount,
                    context.getResources().getQuantityString(R.plurals.pieces, currentItem.itemCount)
            );

            price = Utils.getTotalPriceString(
                    currentItem.singleItemPriceInDollar,
                    currentItem.itemCount
            );

        } else {
            title = String.format(
                    Locale.ENGLISH,
                    "%s (%s)",
                    currentItem.name,
                    context.getString(R.string.per_piece)
            );

            price = Utils.getTotalPriceString(currentItem.singleItemPriceInDollar, 1);
        }

        holder.binding.setTitle(title);
        holder.binding.setPrice(price);

        holder.itemView.setOnClickListener(v -> {
            if (onRecyclerViewItemClickListener != null) {
                onRecyclerViewItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productItems.size();
    }

    @Override
    public long getItemId(int position) {
        return productItems.get(position).id;
    }



    static class ViewHolder extends RecyclerView.ViewHolder
    {

        LayoutItemProductAndPriceBinding binding;

        public ViewHolder(@NonNull LayoutItemProductAndPriceBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
