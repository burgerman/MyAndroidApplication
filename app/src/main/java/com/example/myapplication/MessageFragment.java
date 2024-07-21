package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {

    private Button delButton;
    private TextView msgView;
    private TextView idView;

    private String message;
    private long id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message_details_fragment, container, false);
        if (getArguments() != null) {
            message = getArguments().getString("message");
            id = getArguments().getLong("id");
        }
        delButton = view.findViewById(R.id.delete_button);
        msgView = view.findViewById(R.id.message_text_view);
        idView = view.findViewById(R.id.id_text_view);
        if(message!=null) {
            msgView.setText(message);
            idView.setText(String.valueOf(id));
        }
        delButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("id", id);
            getActivity().setResult(AppCompatActivity.RESULT_OK, resultIntent);
            getActivity().finish();
        });
        return view;
    }
}