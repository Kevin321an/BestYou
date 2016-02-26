package com.bestofyou.fm.bestofyou;

/**
 * Created by FM on 2/14/2016.
 */
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.helper.ItemTouchHelperAdapter;
import com.bestofyou.fm.bestofyou.helper.ItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author
 */

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter

{
    private Cursor mCursor;
    //final private ItemChoiceManager mICM;
    final private Context mContext;
   // final private ForecastAdapterOnClickHandler mClickHandler;

    public static interface ForecastAdapterOnClickHandler {
        void onClick(Long date, ItemViewHolder vh);}
    private static final String[] STRINGS = new String[]{
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"
    };

    private final List<String> mItems = new ArrayList<>();

    /*public RecyclerListAdapter() {
        mItems.addAll(Arrays.asList(STRINGS));
    }*/

    /*public RecyclerListAdapter(Context context, ForecastAdapterOnClickHandler dh, View emptyView, int choiceMode) {
        mItems.addAll(Arrays.asList(STRINGS));
        mContext = context;
        mClickHandler = dh;
        //mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }
*/
    public RecyclerListAdapter(Context context) {
        mItems.addAll(Arrays.asList(STRINGS));
        mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ItemViewHolder(view);
    }

    /*
        * Returns this view's tag.
        *Returns
        *the Object stored in this view as a tag, or null if not set
        */
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        mCursor.moveToPosition(position);
       /* int dateColumnIndex = mCursor.getColumnIndex(SummaryContract.Rubric.WEIGHT);
        //!!!!!Important This going to send the date into mClickHandler then it will be deliver to constructor
        mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);*/
        // Read name from cursor
        String name = mCursor.getString(MainActivity.COL_RUBRIC_NAME);
        holder.name.setText(name);
        // Read weight from cursor
        Long weight = mCursor.getLong(MainActivity.COL_RUBRIC_WEIGHT);
        holder.weight.setText(weight.toString());
        //holder.textView.setText(mItems.get(position));
        holder.tickCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatedVectorDrawable drawable = holder.tick ? holder.tickToCross : holder.crossToTick;
                holder.tickCross.setImageDrawable(drawable);
                drawable.start();
                holder.tick = !holder.tick;
            }
        });
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
        //return mItems.size();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
       // mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }



  /*  public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }*/
    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if ( viewHolder instanceof ItemViewHolder ) {
            ItemViewHolder vfh = (ItemViewHolder)viewHolder;
            //// TODO: 2/20/2016 remove onClick
            //vfh.onClick(vfh.itemView);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        mCursor.moveToPosition(position);

        String name = mCursor.getString(MainActivity.COL_RUBRIC_NAME);

        // Read weight from cursor
        Long weight = mCursor.getLong(MainActivity.COL_RUBRIC_WEIGHT);

        mItems.remove(position);
        Toast.makeText(this.mContext, name+ "   "+weight , Toast.LENGTH_SHORT).show();
        notifyItemRemoved(position);
        int lastIndex = getItemCount();
        //notifyItemMoved(position, lastIndex-1);



    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public final TextView name;
        public final TextView weight;
        public CardView cardV;
        private ImageView tickCross;
        private AnimatedVectorDrawable tickToCross;
        private AnimatedVectorDrawable crossToTick;
        private boolean tick = true;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cardV  = (CardView) itemView.findViewById(R.id.card_view);
            name = (TextView) itemView.findViewById(R.id.name);
            weight = (TextView) itemView.findViewById(R.id.weight);
            tickCross = (ImageView) itemView.findViewById(R.id.tick_cross);
            tickToCross = (AnimatedVectorDrawable)itemView.getContext() .getDrawable(R.drawable.avd_tick_to_cross);
            crossToTick = (AnimatedVectorDrawable) itemView.getContext().getDrawable(R.drawable.avd_cross_to_tick);

        }
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.GRAY);
            //cardV.setCardBackgroundColor(Color.GREEN);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }


       /* @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            //get the index of this column
            int dateColumnIndex = mCursor.getColumnIndex(SummaryContract.Rubric.WEIGHT);
            //!!!!!Important This going to send the date into mClickHandler then it will be deliver to constructor
            mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);
            mICM.onClick(this);
        }*/

    }

}