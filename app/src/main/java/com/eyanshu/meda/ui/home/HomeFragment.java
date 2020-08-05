package com.eyanshu.meda.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.eyanshu.meda.R;
import com.eyanshu.meda.insulin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root= inflater.inflate(R.layout.fragment_home, container, false);
        root.findViewById(R.id.ibutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inval=root.findViewById(R.id.insulinval);
                String val=inval.getText().toString();
                insulin ne=new insulin(val);
                ne.upload();
                closeKeyBoard();
                Snackbar.make(view, "uploaded", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        root.findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });
        return root;
    }
    private void test()
    {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();

        FirebaseUser user=mAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(user.getUid()).child("test").push();
        ref.setValue("aasadasdsa").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                // ..
                View view=getView();
                Snackbar.make(view, "uploaded", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        View view=getView();
                        Snackbar.make(view, "uploaded", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    }
                });
    }
    private void closeKeyBoard(){
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}