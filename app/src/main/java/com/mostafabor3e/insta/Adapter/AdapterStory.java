package com.mostafabor3e.insta.Adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Model.Story;
import com.mostafabor3e.insta.Model.User;
import com.mostafabor3e.insta.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterStory extends RecyclerView.Adapter<AdapterStory.StoryHolder> {
    private List<Story>mstory;
    private Context mcontext;

    public AdapterStory(List<Story> mstory, Context mcontext) {
        this.mstory = mstory;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            View view= LayoutInflater.from(mcontext).inflate(R.layout.story_add_item,null,false);
            return new StoryHolder(view);
        }
        View view= LayoutInflater.from(mcontext).inflate(R.layout.story_item,null,false);
        return new StoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryHolder holder, int position) {
        Story story=mstory.get(position);
        if (holder.getAdapterPosition()!=0){
            seenStory(holder,story.getUserid());
        }
        if (holder.getAdapterPosition()==0){
            mystory(holder.addstorytext,holder.pulse,false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition()==0){
                    mystory(holder.addstorytext,holder.pulse,true);
                }
                else {
                    //go to story
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class StoryHolder  extends RecyclerView.ViewHolder {
        CircleImageView storyPhoto,seenPhoto,pulse;
        TextView username,addstorytext;
        public StoryHolder(@NonNull View itemView) {
            super(itemView);
            storyPhoto=itemView.findViewById(R.id.story_photo);
            pulse=itemView.findViewById(R.id.story_plus);
            seenPhoto=itemView.findViewById(R.id.item_story_image);
            username=itemView.findViewById(R.id.item_story_name);
            addstorytext=itemView.findViewById(R.id.story_text);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return 0;
        }
        return 1;
    }
    private void userInf(final StoryHolder storyHolder, final String userID, final int pos){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    Glide.with(mcontext).load(user.getImage()).into(storyHolder.storyPhoto);
                    if (pos!=0){
                        Glide.with(mcontext).load(user.getImage()).into(storyHolder.storyPhoto);
                        storyHolder.username.setText(user.getUsername());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void mystory(final TextView textView, final ImageView imageView, final boolean click){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("story").child
                (FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                long timcurrent=System.currentTimeMillis();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Story story=dataSnapshot.getValue(Story.class);
                    if (timcurrent>story.getStarttime()&&timcurrent<story.getEndtime()){
                        count++;
                    }
                }
                if (click){
                    //show alrt dilog
                }
                else {
                    if (count>0){
                        textView.setText("my story");
                        imageView.setVisibility(View.GONE);
                    }else {
                        textView.setText("Add story");
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void seenStory(final StoryHolder storyHolder, String userId){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("story").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (!dataSnapshot.child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()&&
                    System.currentTimeMillis()<dataSnapshot.getValue(Story.class).getEndtime()){
                        i++;
                    }
                }
                if (i>0){
                    storyHolder.seenPhoto.setVisibility(View.GONE);
                    storyHolder.storyPhoto.setVisibility(View.VISIBLE);
                }
                else {
                    storyHolder.seenPhoto.setVisibility(View.VISIBLE);
                    storyHolder.storyPhoto.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
