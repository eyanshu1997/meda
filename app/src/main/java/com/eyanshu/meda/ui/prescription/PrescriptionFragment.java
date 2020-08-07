package com.eyanshu.meda.ui.prescription;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.eyanshu.meda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PrescriptionFragment extends Fragment {
    static Uri[] uris;
    private PrescriptionViewModel prescriptionViewModel;
    static int cur=0;
    static int count;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseAuth a=FirebaseAuth.getInstance();
        final FirebaseUser user=a.getCurrentUser();
        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("images");
        cur=0;
        prescriptionViewModel =
                ViewModelProviders.of(this).get(PrescriptionViewModel.class);
        root = inflater.inflate(R.layout.fragment_prescription, container, false);
        root.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        root.findViewById(R.id.next).setVisibility(View.GONE);
        root.findViewById(R.id.prev).setVisibility(View.GONE);
        root.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev();
            }
        });
        prescriptionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        final Context co=getContext();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    Toast.makeText(getContext(), "no precriptions added", Toast.LENGTH_LONG).show();
                    return;
                }
                long c=snapshot.getChildrenCount();

                count= (int) c;
                if(c==0) {
                    Toast.makeText(getContext(), "no precriptions added", Toast.LENGTH_LONG).show();
                    return;
                }
                root.findViewById(R.id.next).setVisibility(View.VISIBLE);
                root.findViewById(R.id.prev).setVisibility(View.VISIBLE);

                StorageReference st = FirebaseStorage.getInstance().getReference().child("users").child(user.getUid()).child("images");
                //Log.e("Count " ,""+snapshot.getChildrenCount());
                int i=0;
                PrescriptionFragment.uris=new Uri[(int)c];
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String post = postSnapshot.getValue(String.class);
                    StorageReference x=st.child(post);
                   // Toast.makeText(getActivity(),post,Toast.LENGTH_SHORT).show();
                    //tags[i].setBackground(gD);
                    //tags[i].setText(post);

                    final int finalI = i;
                    x.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    PrescriptionFragment.uris[finalI]=uri;
                                    Glide.with(co).load(uri).into((ImageView)root.findViewById(R.id.pres));
                                    // Got the download URL for 'users/me/profile.png'
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                    i++;
                    //Log.e("Get Data", post.<YourMethod>());
                    //Glide.with(co).load(uris[cur]).into((ImageView)root.findViewById(R.id.pres));
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
    void next()
    {
        if(cur<count-1)
            cur++;
        else
            Toast.makeText(getContext(),"last picture",Toast.LENGTH_SHORT).show();
        Glide.with(getContext()).load(uris[cur]).into((ImageView)root.findViewById(R.id.pres));
    }
    void prev()
    {
        if(cur<0)
            cur--;
        else
            Toast.makeText(getContext(),"first picture",Toast.LENGTH_SHORT).show();
        Glide.with(getContext()).load(uris[cur]).into((ImageView)root.findViewById(R.id.pres));
    }
}