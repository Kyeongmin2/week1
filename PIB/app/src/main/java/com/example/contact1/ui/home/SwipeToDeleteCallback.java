package com.example.contact1.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final RecyclerViewAdapter mAdapter;
    private final Context mContext;
    private final List<RecyclerViewItem> deletedItems = new ArrayList<>();
    private int deletedItemPosition = -1;

    public SwipeToDeleteCallback(RecyclerViewAdapter adapter, Context context) {
        super(0, ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // RecyclerViewAdapter 객체가 null인 경우 예외처리
        if (mAdapter == null) {
            return;
        }

        // 스와이프된 항목의 위치를 가져옴
        int position = viewHolder.getAdapterPosition();

        // itemList가 null이거나 position이 itemList의 범위를 벗어나면 예외처리
        List<RecyclerViewItem> itemList = mAdapter.getItemList();
        if (itemList == null || position < 0 || position >= itemList.size()) {
            return;
        }

        // 삭제된 아이템을 저장
        RecyclerViewItem deletedItem = itemList.get(position);
        // 해당 전화번호로 전화 거는 기능 추가
        String phoneNumber = deletedItem.getNumber();
        makePhoneCall(phoneNumber);

    }

    private void makePhoneCall(String phoneNumber) {
        // 여기에 전화 걸기 Intent 등의 코드 추가
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber));

        // 암시적 인텐트 실행
        if (dialIntent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(dialIntent);
        } else {
            // 처리할 수 있는 전화 앱이 없는 경우에 대한 예외 처리
            Toast.makeText(mContext, "전화를 걸 수 있는 앱이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}