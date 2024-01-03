package com.example.contact1.ui.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment {

    private static final String ARG_IMAGE_URI = "arg_image_uri";

    private Uri imageUri;
    private Runnable onDeleteListener;

    public static DeleteDialogFragment newInstance(Uri imageUri) {
        DeleteDialogFragment fragment = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_IMAGE_URI, imageUri);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnDeleteListener(Runnable listener) {
        onDeleteListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        imageUri = requireArguments().getParcelable(ARG_IMAGE_URI);

        return new AlertDialog.Builder(requireContext())
                .setTitle("Delete Image")
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (onDeleteListener != null) {
                        onDeleteListener.run();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create();
    }
}
