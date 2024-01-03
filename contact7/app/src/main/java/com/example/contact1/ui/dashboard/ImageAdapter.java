package com.example.contact1.ui.dashboard;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.contact1.databinding.ItemImageBinding;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final List<Uri> imageList;
    private final OnItemClickListener onItemClick;
    private final OnItemLongClickListener onItemLongClick;

    public interface OnItemClickListener {
        void onItemClick(Uri imageUri);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public ImageAdapter(List<Uri> imageList, OnItemClickListener onItemClick, OnItemLongClickListener onItemLongClick) {
        this.imageList = imageList;
        this.onItemClick = onItemClick;
        this.onItemLongClick = onItemLongClick;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageBinding binding = ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = imageList.get(position);
        holder.bind(imageUri);
        holder.itemView.setOnClickListener(view -> onItemClick.onItemClick(imageUri));
        holder.itemView.setOnLongClickListener(view -> {
            onItemLongClick.onItemLongClick(position);
            return true;
        });

        //수정함->이미지둥글게처리
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(50)); // radius 값은 조절 가능
        Glide.with(holder.itemView.getContext())
                .load(imageUri)
                .apply(requestOptions)
                .into(holder.getImageView());
        //

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemImageBinding binding;

        public ImageViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Uri imageUri) {
            Glide.with(itemView.getContext())
                    .load(imageUri)
                    .into(binding.imageView);
        }

        public ImageView getImageView() {
            return binding.imageView;
        }

    }
}

