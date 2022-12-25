package com.Sadraafzar.Messenger.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Model.mesMessageChange_ResultDTO;
import com.Sadraafzar.Messenger.R;

import java.util.List;


public class submessageCompaneyApaptor extends RecyclerView.Adapter<submessageCompaneyApaptor.MyViewHolder> {
    public List<mesMessageChange_ResultDTO> listitems;
    private Context context;
    public submessageCompaneyApaptor(List<mesMessageChange_ResultDTO> listitems,Context mcontext) {
        this.listitems = listitems;
        context=mcontext;
    }
    @NonNull
    @Override
    public submessageCompaneyApaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_submessage, parent, false);
        return new MyViewHolder(view,listitems);
}

    @Override
    public void onBindViewHolder(@NonNull submessageCompaneyApaptor.MyViewHolder holder, int i) {
        holder.txtMesage.setText(listitems.get(i).message);
        holder.txttime.setText(listitems.get(i).sendTime);
        holder.rel_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              View vv= app.Dialog_.dialog_creat(context,R.layout.dialog_replay_answer);
              TextView txtmesagemain=vv.findViewById(R.id.txt_dialog_main);
                EditText edt_replay=vv.findViewById(R.id.edt_dialog_replay);
                RelativeLayout rel_acsept=vv.findViewById(R.id.rel_dialog_acsept);
                RelativeLayout rel_deacsept=vv.findViewById(R.id.rel_replay_dont_acsept);
                AlertDialog al=app.Dialog_.show_dialog(context,vv);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }
    public static class  MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        List<mesMessageChange_ResultDTO> listitems;
        TextView txtMesage;
TextView txttime;
RelativeLayout rel_replay;

        public MyViewHolder(View view,List<mesMessageChange_ResultDTO> listitems) {
            super(view);
            this.listitems=listitems;

            txtMesage = (TextView) view.findViewById(R.id.txt_recsubmessage_message);
            txttime=(TextView) view.findViewById(R.id.txt_time);
            rel_replay=(RelativeLayout)view.findViewById(R.id.rel_replay);
        }

        @Override
        public void onClick(View v) {

        }
    }
}

