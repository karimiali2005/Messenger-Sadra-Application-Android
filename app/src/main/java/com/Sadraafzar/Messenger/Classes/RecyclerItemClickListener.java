package com.Sadraafzar.Messenger.Classes;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Sadraafzar.Messenger.R;


/**
 * Created by Administrator on 12/04/2018.
 */

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;
    private OnItemClickListener mListener;
    public boolean IsStatus;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener)
    {
        mListener = listener;
        IsStatus=false;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }

            /*@Override
            public void onLongPress(MotionEvent e){
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if(childView != null && mListener != null){
                    mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }*/
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e)
    {
        boolean find=false;
        if(IsStatus)
            find=clickStatusImageView(view,e.getX(), e.getY());
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (find==false&&childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
    public boolean clickStatusImageView(RecyclerView view, float x, float y)
    {
        View viewStatusImageView=(TextView)view.findViewById(R.id.txt_main_titel);
        final float translationX = ViewCompat.getTranslationX(viewStatusImageView);
        final float translationY = ViewCompat.getTranslationY(viewStatusImageView);
        if (x >= viewStatusImageView.getLeft() + translationX &&
                x <= viewStatusImageView.getRight() + translationX &&
                y >= viewStatusImageView.getTop() + translationY ) {
            return true;
        }
        else
            return false;
    }
}