package com.mostafabor3e.insta.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Adapter.AdapterUser;
import com.mostafabor3e.insta.Model.User;
import com.mostafabor3e.insta.R;

import java.util.ArrayList;
import java.util.List;


public class Search extends Fragment {
    RecyclerView recyclerView;
    AdapterUser adapterUser;
    List<User>userList;
    EditText search;

    public Search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_search, container, false);
       recyclerView=view.findViewById(R.id.rec_search);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       recyclerView.setHasFixedSize(true);
       userList=new ArrayList<>();
       search=view.findViewById(R.id.ed_search_ser);

        adapterUser = new AdapterUser(userList, getContext());
        recyclerView.setAdapter(adapterUser);
        readuser();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchUser(s.toString().toLowerCase());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }
    public void readuser(){
        if (search.getText().toString().equals("")) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        User user = snap.getValue(User.class);
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != user.getId()) {
                            userList.add(user);
                        }
                        adapterUser.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void SearchUser(String s){
        Query  query=FirebaseDatabase.getInstance().getReference("users").orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snaop:snapshot.getChildren()){
                    User user=snaop.getValue(User.class);
                    userList.add(user);
                }
                adapterUser.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}