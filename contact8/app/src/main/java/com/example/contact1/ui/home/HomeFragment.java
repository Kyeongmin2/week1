package com.example.contact1.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact1.R;
import com.example.contact1.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerViewAdapter adapter;
    private EditText editTextSearch;
    private List<RecyclerViewItem> currentList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // RecyclerView 초기화
        RecyclerView recyclerView = binding.contactRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // RecyclerViewAdapter 초기화
        List<RecyclerViewItem> itemList = new ArrayList<>();
        currentList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(requireContext(), itemList, currentList);
        recyclerView.setAdapter(adapter);

        // "새로운 연락처 추가" 버튼에 대한 클릭 리스너 설정
        FloatingActionButton addButton = root.findViewById(R.id.fabAddContact);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog();
            }
        });

        // 앱이 최초로 실행될 때 홍길동의 연락처를 추가
        addDefaultContact();

        editTextSearch = root.findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 필요한 경우 구현
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 검색어가 변경될 때마다 RecyclerView 아이템을 필터링하여 업데이트
                String query = charSequence.toString().toLowerCase(Locale.getDefault());
                adapter.getFilter().filter(query);
                if (TextUtils.isEmpty(query)) {
                    // 검색어가 비어 있는 경우, 전체 목록을 보여줌
                    adapter.getFilter().filter("");
                }// 빈 문자열 전달
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 필요한 경우 구현
            }
        });

        return root;
    }

    private void addDefaultContact() {
        // 홍길동의 연락처 아이템 생성
        Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_perm_identity_24);
        RecyclerViewItem defaultItem1 = new RecyclerViewItem(icon, "가", "010-1111-1111");
        RecyclerViewItem defaultItem2 = new RecyclerViewItem(icon, "나", "020-2222-2222");
        RecyclerViewItem defaultItem3 = new RecyclerViewItem(icon, "다", "030-3333-3333");

        // Adapter에 아이템 추가
        adapter.addItem(defaultItem1);
        adapter.addItem(defaultItem2);
        adapter.addItem(defaultItem3);
        currentList.add(defaultItem1);
        currentList.add(defaultItem2);
        currentList.add(defaultItem3);

        // RecyclerView 갱신
        adapter.notifyDataSetChanged();
    }

    private void showAddContactDialog() {
        // 다이얼로그에서 입력 받을 뷰 설정
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_contact, null);
        EditText nameEditText = dialogView.findViewById(R.id.editTextName);
        EditText numberEditText = dialogView.findViewById(R.id.editTextNumber);

        // 다이얼로그 생성
        new AlertDialog.Builder(requireContext())
                .setTitle("Add Contact")
                .setView(dialogView)
                .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 사용자로부터 입력 받은 데이터
                        String name = nameEditText.getText().toString();
                        String number = numberEditText.getText().toString();

                        // 새로운 연락처 아이템 생성
                        Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_perm_identity_24);
                        RecyclerViewItem newItem = new RecyclerViewItem(icon, name, number);

                        // Adapter에 새로운 아이템 추가
                        adapter.addItem(newItem);
                        currentList.add(newItem);

                        // RecyclerView 갱신
                        adapter.sortByName();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}