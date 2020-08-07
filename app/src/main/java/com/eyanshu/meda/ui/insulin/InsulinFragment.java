package com.eyanshu.meda.ui.insulin;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.eyanshu.meda.R;
import com.eyanshu.meda.insulin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InsulinFragment extends Fragment {

    private InsulinViewModel insulinViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FirebaseAuth a=FirebaseAuth.getInstance();
        final FirebaseUser user=a.getCurrentUser();
        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("insulin");
        insulinViewModel =
                ViewModelProviders.of(this).get(InsulinViewModel.class);
        View root = inflater.inflate(R.layout.fragment_insulin, container, false);
        final LinearLayout llMain = root.findViewById(R.id.rsl);
        final Context co=getContext();
        Toast.makeText(getContext(), "Here", Toast.LENGTH_SHORT).show();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView t=new TextView(getContext());
        t.setText("hello");
        llMain.addView(t,layoutParams);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long c=snapshot.getChildrenCount();
                Toast.makeText(getContext(),"hello"+( new Integer((int)c)).toString(),Toast.LENGTH_SHORT).show();
                final TextView[] tags= new TextView[(int) c];//create dynamic textviewsarray
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //Log.e("Count " ,""+snapshot.getChildrenCount());
                if(c==0)
                {
                    Toast.makeText(getContext(), "no Insulin values added", Toast.LENGTH_LONG).show();
                    return;
                }
                //return;

                int i=0;
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    insulin post = postSnapshot.getValue(insulin.class);
                    //StorageReference x=st.child(post);
                    Toast.makeText(getActivity(),post.toString(),Toast.LENGTH_SHORT).show();

                    //tags[i].setBackground(gD);
                    tags[i] = new TextView(getContext());
                    tags[i].setText(post.toString());

                    GradientDrawable gD = new GradientDrawable();
                    int strokeWidth = 5;
                    int strokeColor = getResources().getColor(R.color.colorAccent);
                    gD.setStroke(strokeWidth, strokeColor);
                    gD.setCornerRadius(15);
                    gD.setShape(GradientDrawable.RECTANGLE);
                    tags[i].setBackground(gD);
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