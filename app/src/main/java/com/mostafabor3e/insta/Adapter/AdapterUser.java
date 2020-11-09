package com.mostafabor3e.insta.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Fragment.Profile;
import com.mostafabor3e.insta.MainActivity;
import com.mostafabor3e.insta.Model.User;
import com.mostafabor3e.insta.R;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserHolder> {
    List<User>userList;
    Context context;
    DatabaseReference reference;

    FirebaseUser firebaseUser;

    public AdapterUser(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_item_user,null,false);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        return(new UserHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, int position) {
final User user=userList.get(position);
holder.name.setText(user.getUsername());
holder.fullName.setText(user.getFullname());
        Glide.with(context).load(user.getImage()).into(holder.image_user);
        if (firebaseUser.getUid().equals(user.getId()))
        {
            holder.follow.setVisibility(View.GONE);
        }
        else {
            holder.follow.setVisibility(View.VISIBLE);
        }
        following(user.getId(),holder.follow);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor sharedPreferences=context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                sharedPreferences.putString("user_id",user.getId());
                sharedPreferences.apply();
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragmet_continer,new Profile()).commit();

            }
        });
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.follow.getText().equals("follow")){
                     FirebaseDatabase.getInstance().getReference().child("follow").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("follow").
                            child(user.getId()).child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                    addNotification(user.getId());
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("follow").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("follow").
                            child(user.getId()).child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        CircleImageView image_user;
        TextView name,fullName;
        Button follow;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            image_user=itemView.findViewById(R.id.iv_custome_user);
            name=itemView.findViewById(R.id.tv_custom_name);
            fullName=itemView.findViewById(R.id.tv_custom_fullname);
            follow=itemView.findViewById(R.id.bt_custom_foll);
        }
    }
    private void following(final String user_id, final Button button){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user_id).exists()){
                    button.setText("following");
                }
                else {
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addNotification(String userId){
        FirebaseUser  firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("notifications").child(userId);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userId",firebaseUser.getUid());
        hashMap.put("postId","");
        hashMap.put("text","start to follow you");
        hashMap.put("ispost",false);
        reference.push().setValue(hashMap);



    }
}
