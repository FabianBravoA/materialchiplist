package adedo.materialchiplist.views;

import android.app.Application;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import adedo.materialchiplist.R;
import adedo.materialchiplist.adapters.ChipListAdapter;

public class Chip extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mClose;
    private final ImageView mIcon;
    private TextView mInfo;
    private RelativeLayout mView;
    private String mUniqueId;
    private int mSelectedColor;
    private int mBackgroundColor;
    private boolean mSelectionState;
    private ChipListener mActionListener;

    public Chip(View itemView) {
        super(itemView);
        mView       = (RelativeLayout) itemView.findViewById( R.id.chip );
        mInfo       = (TextView) itemView.findViewById(R.id.chip_info);
        mClose      = (ImageView) itemView.findViewById(R.id.chip_close);
        mIcon       = (ImageView) itemView.findViewById(R.id.chip_icon);
        mUniqueId   = "";

        mView.setOnClickListener(this);
        mClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mSelectionState){
            if( v == mClose ){
                if(mActionListener != null){
                    mActionListener.OnChipCloseAction(mUniqueId);
                }
            }else {
                setSelected(false);
            }
        }else{
            setSelected(true);
        }
    }

    public void reset() {
        mSelectionState     = false;
        mInfo.setText("");
        mIcon.setImageDrawable(mView.getResources().getDrawable(R.color.transparent));
        mUniqueId           = "";
    }

    public String getmUniqueId() {
        return mUniqueId;
    }

    public void setmUniqueId(String mUniqueId) {
        this.mUniqueId = mUniqueId;
    }

    public void setInfo(String info){
        mInfo.setText(info);
    }

    public void setIcon(Drawable drawable){
        mIcon.setImageDrawable(drawable);
    }

    public void setIcon(String url){
        Picasso.with(mView.getContext()).load(url).into(mIcon);
    }

    public void setAttributes(int backgroundColor, int textColor, int selectedColor, int iconColor) {
        mBackgroundColor    = backgroundColor;
        Drawable backgroundDrawable     = DrawableCompat.wrap(mView.getContext().getResources().getDrawable(R.drawable.chip_background));
        DrawableCompat.setTint( backgroundDrawable, mBackgroundColor);
        mView.setBackground(backgroundDrawable);
        mInfo.setTextColor(textColor);
        mSelectedColor      = selectedColor;
        mIcon.setColorFilter(iconColor);
    }

    public boolean isSelected() {
        return mSelectionState;
    }

    public void setSelected(boolean selectionState) {
        this.mSelectionState = selectionState;

        if( mSelectionState ) {
            Drawable backgroundDrawable = DrawableCompat.wrap(mView.getContext().getResources().getDrawable(R.drawable.chip_background));
            DrawableCompat.setTint(backgroundDrawable, mSelectedColor);
            mView.setBackground(backgroundDrawable);

            mIcon.animate().alpha(0.0f).setDuration(200).start();
            mClose.animate().alpha(1f).setDuration(200).setStartDelay(150).start();

            if(mActionListener != null){
                mActionListener.OnChipSelected( mUniqueId, true );
            }
        }else{
            Drawable backgroundDrawable = DrawableCompat.wrap(mView.getContext().getResources().getDrawable(R.drawable.chip_background));
            DrawableCompat.setTint(backgroundDrawable, mBackgroundColor);
            mView.setBackground(backgroundDrawable);

            mIcon.animate().alpha(1f).setDuration(200).setStartDelay(150).start();
            mClose.animate().alpha(0f).setDuration(200).start();

            if(mActionListener != null){
                mActionListener.OnChipSelected( mUniqueId, false );
            }
        }
    }

    public void setActionListener(ChipListener actionListener) {
        this.mActionListener = actionListener;
    }


    public interface ChipListener{
        public void OnChipCloseAction( String uniqueId );
        public void OnChipSelected( String uniqueId, boolean selected );
    }
}
