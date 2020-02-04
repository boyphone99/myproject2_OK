package com.example.myproject2.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myproject2.R;

import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<Product> productList;

    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(product.getProduct_img())
                .into(holder.imageViewProductImg);

        holder.textViewName.setText(product.getProduct_name());
        holder.textViewUnit.setText(product.getUnit()+"/บาท");
        holder.textViewPriceLow.setText(String.valueOf(product.getPrice_low()));
        holder.textViewPriceHigh.setText(String.valueOf(product.getPrice_high()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewUnit, textViewPriceLow, textViewPriceHigh;
        ImageView imageViewProductImg;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.tvNameP);
            textViewUnit = itemView.findViewById(R.id.tvUnit);
            textViewPriceLow = itemView.findViewById(R.id.tvPriceLow);
            textViewPriceHigh = itemView.findViewById(R.id.tvPriceHigh);
            imageViewProductImg = itemView.findViewById(R.id.productImg);
        }
    }
}
