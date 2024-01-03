package com.example.contact1.ui.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact1.R;
import com.example.contact1.databinding.FragmentDashboardBinding;
import com.example.contact1.ui.dashboard.ImageAdapter;
import com.example.contact1.ui.dashboard.ImageDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private List<Uri> imageList = new ArrayList<>();
    private RecyclerView recyclerView;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.recyclerView;

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        ImageAdapter imageAdapter = new ImageAdapter(imageList,
                imageResource -> showImageDialog(imageResource),
                position -> showDeleteConfirmationDialog(position)
        );
        recyclerView.setAdapter(imageAdapter);

        Button addButton = binding.addButton;
        addButton.setOnClickListener(v -> openGallery());
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setMessage("Delete this image?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, which) -> deleteImage(position))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteImage(int position) {
        imageList.remove(position);
        recyclerView.getAdapter().notifyItemRemoved(position);
    }

    private void showImageDialog(Uri imageResource) {
        ImageDialogFragment dialogFragment = ImageDialogFragment.newInstance(imageResource);
        dialogFragment.show(getParentFragmentManager(), "ImageDialogFragment");
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imageList.add(selectedImageUri);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
