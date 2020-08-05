package com.eyanshu.meda.ui.gallery;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.eyanshu.meda.R;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseAuth a=FirebaseAuth.getInstance();
        FirebaseUser user=a.getCurrentUser();
        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("images");

        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        final LinearLayout llMain = root.findViewById(R.id.rlMain);
        final Context co=getContext();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long c=snapshot.getChildrenCount();
                TextView[] tags= new TextView[(int) c];//create dynamic textviewsarray
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //Log.e("Count " ,""+snapshot.getChildrenCount());
                int i=0;
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String post = postSnapshot.getValue(String.class);
                    Toast.makeText(getActivity(),post,Toast.LENGTH_SHORT).show();
                    tags[i] = new TextView(getContext());
                    GradientDrawable gD = new GradientDrawable();
                    int strokeWidth = 5;
                    int strokeColor = getResources().getColor(R.color.colorAccent);
                    gD.setStroke(strokeWidth, strokeColor);
                    gD.setCornerRadius(15);
                    gD.setShape(GradientDrawable.RECTANGLE);

                    tags[i].setBackground(gD);
                    tags[i].setText(post);
                    layoutParams.setMargins(10, 5, 10, 5);
                    tags[i].setPadding(17, 15, 17, 15);
                    llMain.addView(tags[i],layoutParams);
                    i++;
                    //Log.e("Get Data", post.<YourMethod>());
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                String x=firebaseError.toString();
                Toast.makeText(getActivity(),x,Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}