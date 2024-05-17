package com.example.newchat;

import static com.example.newchat.chatwindow.receiverIMG;
import static com.example.newchat.chatwindow.senderImg;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Msgmodelclass> messageAdapterArrayLIst;
    int ITEM_SEND=1,ITEM_RECEIVE=2;

    public messagesAdapter(Context context, ArrayList<Msgmodelclass> messageAdapterArrayLIst) {
        this.context = context;
        this.messageAdapterArrayLIst = messageAdapterArrayLIst;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND){
            View view=LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewholder(view);
        }
        else {
            View view=LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new receiverViewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Msgmodelclass messages=messageAdapterArrayLIst.get(position);
        if(holder.getClass()==senderViewholder.class){
            senderViewholder viewholder=(senderViewholder) holder;
            Log.e("search","found");
            viewholder.msgtext.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(viewholder.circleImageView);

        }
        else {
            receiverViewholder viewholder=(receiverViewholder) holder;
            viewholder.msgtext2.setText(messages.getMessage());
            Picasso.get().load(receiverIMG).into(viewholder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
         return 0;
    }
    public int getItemViewType(int position){
        Msgmodelclass messages=messageAdapterArrayLIst.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())){
            return ITEM_SEND;
        }
        else{
            return ITEM_RECEIVE;
        }

    }
    class senderViewholder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtext;
        public senderViewholder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.senderimg);
            msgtext=itemView.findViewById(R.id.sendername);
        }
    }
    class receiverViewholder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtext2;
        public receiverViewholder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.receiverimg);
            msgtext2=itemView.findViewById(R.id.receivername);
        }
    }
}
