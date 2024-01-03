package com.example.contact1.ui.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private List<Uri> imageList = new ArrayList<>();
    private RecyclerView recyclerView;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;


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

        imageList.add(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.cat1));
        imageList.add(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.cat2));
        imageList.add(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.cat3));
        imageList.add(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.cat4));
        imageList.add(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.cat5));
        imageList.add(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.cat6));
        imageList.add(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.dog1));
        imageList.add(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.drawable.dog2));


        ImageAdapter imageAdapter = new ImageAdapter(imageList,
                imageResource -> showImageDialog(imageResource),
                position -> showDeleteConfirmationDialog(position)
        );
        recyclerView.setAdapter(imageAdapter);

        FloatingActionButton addButton = binding.addButton;
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());

        // 글자 색상을 설정합니다.
        int textColor = ContextCompat.getColor(requireContext(), R.color.edit_icon_color);

        alertDialogBuilder
                .setTitle(Html.fromHtml("<font color='" + textColor + "'>Select</font>"))
                .setCancelable(true)
                .setPositiveButton("Gallery", (dialog, which) -> openGalleryFromSource())
                .setNegativeButton("Camera", (dialog, which) -> openCameraWithPermissionCheck());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openGalleryFromSource() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    imageList.add(selectedImageUri);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = (Bitmap) extras.get("data");
                    Uri tempUri = getImageUri(requireContext(), photo);
                    imageList.add(tempUri);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }
    }


    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "제목", null);
        return Uri.parse(path);
    }

    private void openCameraWithPermissionCheck() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            // 권한이 부여되지 않았을 경우 권한 요청
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // 권한이 이미 부여되어 있을 경우 카메라 열기
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되면 카메라 열기
                openCamera();
            } else {
                // 권한이 거부되면 사용자에게 설명이나 설정으로 안내
                // 예를 들어, AlertDialog를 통해 권한이 필요한 이유를 알려줄 수 있습니다.
            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}