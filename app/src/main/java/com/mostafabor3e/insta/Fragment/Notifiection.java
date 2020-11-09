package com.mostafabor3e.insta.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Adapter.AdpterNotifaction;
import com.mostafabor3e.insta.Model.Notifaction;
import com.mostafabor3e.insta.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Notifiection extends Fragment {
RecyclerView  recyclerView;
List<Notifaction>notifactions;
AdpterNotifaction adpterNotifaction;
FirebaseUser firebaseUser;

    public Notifiection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view= inflater.inflate(R.layout.fragment_favroite, container, false);
     recyclerView=view.findViewById(R.id.rec_not);
     notifactions=new ArrayList<>();
     recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
     recyclerView.setHasFixedSize(true);
     adpterNotifaction=new AdpterNotifaction(notifactions,getContext());
     recyclerView.setAdapter(adpterNotifaction);
     firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
     ReadNot();
     return  view;
    }
    private void ReadNot(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("notifications").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifactions.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Notifaction notifaction=dataSnapshot.getValue(Notifaction.class);
                   // Toast.makeText(getContext(), "vv"+notifaction.getText(), Toast.LENGTH_SHORT).show();
                    notifactions.add(notifaction);
                }
                Collections.reverse(notifactions);
                adpterNotifaction.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}