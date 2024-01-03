package com.example.contact1.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<RecyclerViewItem> itemList;
    private List<RecyclerViewItem> filteredList;
    private LayoutInflater inflater;
    private List<RecyclerViewItem> currentList;
    // 생성자
    public RecyclerViewAdapter(Context context, List<RecyclerViewItem> itemList,List<RecyclerViewItem> currentList) {
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
        this.filteredList = new ArrayList<>(itemList);
        this.currentList = currentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecyclerViewItem item = itemList.get(position);
        holder.iconImageView.setImageDrawable(item.getIcon());
        holder.titleTextView.setText(item.getTitle());
        holder.descTextView.setText(item.getDesc());

        // 삭제 버튼에 대한 클릭 리스너 설정
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 위치의 아이템 삭제
                itemList.remove(position);
                currentList.remove(position);
                // Adapter에 변경 사항 알림
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // addItem 메서드 추가
    public void addItem(RecyclerViewItem newItem) {
        itemList.add(newItem);
        notifyItemInserted(itemList.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView titleTextView;
        TextView descTextView;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().toLowerCase(Locale.getDefault());
                List<RecyclerViewItem> filteredList = new ArrayList<>();

                if (TextUtils.isEmpty(query)) {
                    // 검색어가 비어 있는 경우, 전체 목록을 보여줌
                    filteredList.addAll(itemList);
                } else {
                    // 검색어에 따라 필터링 수행
                    for (RecyclerViewItem item : currentList) {
                        if (item.getTitle().toLowerCase(Locale.getDefault()).contains(query) ||
                                item.getDesc().toLowerCase(Locale.getDefault()).contains(query)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<RecyclerViewItem> filteredList = (List<RecyclerViewItem>) filterResults.values;

                // 검색어가 없는 경우, 모든 연락처 보이도록 함
                if (TextUtils.isEmpty(charSequence)) {
                    itemList.clear();
                    itemList.addAll(currentList); // 수정된 부분: originalItemList로 변경
                } else {
                    // 검색 결과를 UI에 반영
                    itemList.clear();
                    itemList.addAll(filteredList);
                }

                notifyDataSetChanged();
            }
        };
    }
}