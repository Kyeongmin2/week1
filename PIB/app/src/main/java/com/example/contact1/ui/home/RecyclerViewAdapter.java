package com.example.contact1.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<RecyclerViewItem> itemList;
    private List<RecyclerViewItem> filteredList;
    private LayoutInflater inflater;
    private List<RecyclerViewItem> currentList;
    private Context context;
    // 생성자
    public RecyclerViewAdapter(Context context, List<RecyclerViewItem> itemList,List<RecyclerViewItem> currentList) {
        this.context = context;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewItem item = itemList.get(position);
        holder.iconImageView.setImageDrawable(item.getIcon());
        holder.titleTextView.setText(item.getTitle());
        holder.descTextView.setText(item.getDesc());

        // 수정을 위한 클릭 리스너 설정
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditContactDialog(item, position);
            }
        });
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
                    itemList.addAll(currentList);
                } else {
                    // 검색 결과를 UI에 반영
                    itemList.clear();
                    itemList.addAll(filteredList);
                }

                notifyDataSetChanged();
            }
        };
    }
    public void sortByName() {
        Collections.sort(currentList, new Comparator<RecyclerViewItem>() {
            @Override
            public int compare(RecyclerViewItem item1, RecyclerViewItem item2) {
                return item1.getTitle().compareToIgnoreCase(item2.getTitle());
            }
        });

        // UI 갱신
        itemList.clear();
        itemList.addAll(currentList);
        notifyDataSetChanged();
    }
    public void setCurrentList(List<RecyclerViewItem> newList) {
        this.currentList = newList;
    }
    private void showEditContactDialog(RecyclerViewItem item, int position) {
        // 다이얼로그에서 입력 받을 뷰 설정
        View dialogView = LayoutInflater.from(context).inflate(R.layout.add_contact, null);
        EditText nameEditText = dialogView.findViewById(R.id.editTextName);
        EditText numberEditText = dialogView.findViewById(R.id.editTextNumber);
        EditText memoEditText = dialogView.findViewById(R.id.Memo);

        // 기존 데이터로 대화 상자 미리 채우기
        nameEditText.setText(item.getName());
        numberEditText.setText(item.getNumber());
        memoEditText.setText(item.getMemo()); // 메모 설정

        // 대화 상자 생성 및 확인 버튼 클릭 처리
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 새 데이터로 항목 업데이트
                        String newName = nameEditText.getText().toString();
                        String newNumber = numberEditText.getText().toString();
                        String newMemo = memoEditText.getText().toString(); // 메모 가져오기

                        // 항목 데이터 업데이트
                        item.setName(newName);
                        item.setNumber(newNumber);
                        item.setMemo(newMemo); // 메모 설정

                        // 어댑터에 데이터 변경 알림
                        notifyItemChanged(position);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner);
        }

    }
    public List<RecyclerViewItem> getItemList() {
        return itemList;
    }

    // New method to remove an item at a specific position
    public void removeItem(int position) {
        itemList.remove(position);
        currentList.remove(position);
        // Adapter에 변경 사항 알림
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
}