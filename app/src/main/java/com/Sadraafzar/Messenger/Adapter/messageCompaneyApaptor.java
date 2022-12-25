package com.Sadraafzar.Messenger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Sadraafzar.Messenger.Classes.app;
import com.Sadraafzar.Messenger.Model.mesCompanyChange_ResultDTO;
import com.Sadraafzar.Messenger.R;
import com.Sadraafzar.Messenger.subMesssage;

import java.util.List;

public class messageCompaneyApaptor extends RecyclerView.Adapter<messageCompaneyApaptor.MyViewHolder>  {
    public List<mesCompanyChange_ResultDTO> listitems;

    Context mcContext;
    public messageCompaneyApaptor(List<mesCompanyChange_ResultDTO> listitems,Context context) {
        this.listitems = listitems;
        mcContext=context;

    }
    @NonNull
    @Override
    public messageCompaneyApaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_recycler, parent, false);
        return new MyViewHolder(view,listitems);
    }

    @Override
    public void onBindViewHolder(@NonNull messageCompaneyApaptor.MyViewHolder holder, int i) {
       // String strProductRequestCreateDate= app.DateTime.CovertToPersianDate(listitems.get(i).ProductRequestCreateDate);

        holder.txttitel.setText(listitems.get(i).companyName);
        holder.txtsubtitel.setText(listitems.get(i).lastmessage);
        holder.txt_time.setText(listitems.get(i).timesen);
if(listitems.get(i).CountShow>0)
{
    holder.txt_cunt.setVisibility(View.VISIBLE);
    holder.txt_cunt.setText(String.valueOf(listitems.get(i).CountShow));

}else
{
    holder.txt_cunt.setVisibility(View.GONE);
}

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        List<mesCompanyChange_ResultDTO> listitems;

        TextView txttitel;
        TextView txtsubtitel;
        TextView txt_cunt;
        TextView txt_time;
     RelativeLayout rel_item;

        public MyViewHolder(View view,List<mesCompanyChange_ResultDTO> listitems) {
            super(view);
            this.listitems=listitems;

            txttitel = (TextView) view.findViewById(R.id.txt_main_titel);
            txtsubtitel = (TextView) view.findViewById(R.id.txt_main_subtitel);
            rel_item = (RelativeLayout) view.findViewById(R.id.rel_item);
            txt_cunt=(TextView) view.findViewById(R.id.txt_main_subtitel_count);
            txt_time=(TextView) view.findViewById(R.id.txt_main_time);

            rel_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == rel_item.getId())
            {
                int position = getAdapterPosition();

                Intent intent = new Intent(v.getContext().getApplicationContext(),subMesssage.class);
                intent.putExtra("idcompaney", listitems.get(position).pkfCompany);
                app.Info.FkfCompanyId=listitems.get(position).pkfCompany;
                v.getContext().startActivity(intent);
            }
        }
    }


}
