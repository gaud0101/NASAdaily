package com.github.gaud0101.nasa;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URISyntaxException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private DownloadTask.Result mParam1;

    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ImageFragment.
     */
    public static ImageFragment newInstance(DownloadTask.Result param1) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (DownloadTask.Result) getArguments().getSerializable(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        if (mParam1 == null) {
            return view;
        }

        ImageView imageView = view.findViewById(R.id.image);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(mParam1.image, 0, mParam1.image.length));

        TextView date = view.findViewById(R.id.image_date);
        date.setText(mParam1.date);

        TextView hdurl = view.findViewById(R.id.image_hdurl);
        hdurl.setText(mParam1.hdurl.toString());
        hdurl.setOnClickListener((v) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mParam1.hdurl.toString()))));

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener((v) -> {
            Database db = new Database(getContext());
            db.favorite(mParam1);
            Toast.makeText(getContext(), R.string.toast_faved, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}