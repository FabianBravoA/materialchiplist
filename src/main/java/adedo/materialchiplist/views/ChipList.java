package adedo.materialchiplist.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import adedo.materialchiplist.R;
import adedo.materialchiplist.adapters.ChipListAdapter;
import adedo.materialchiplist.model.ChipData;

public class ChipList extends ScrollView implements Chip.ChipListener {
    private ArrayList<ChipData> mChipList       = new ArrayList<>();
    private RecyclerView mChipListView;
    private ChipListAdapter mChipListAdapter;

    private int mChipBackgroundColor;
    private int mChipTextColor;
    private int mChipSelectedColor;
    private int mChipIconColor;
    private int mChipPromptColor;
    private ChipActionListener mChipActionListener;
    private String mNoChipsPrompt;
    private TextView mNoChipsPromptView;
    private CharSequence mChipPromptText    = "";


    public ChipList(Context context) {
        super(context);

        loadChipList();
    }

    public ChipList(Context context, AttributeSet attrs) {
        super(context, attrs);

        loadChipListAttr(context, attrs);
        loadChipList();
    }

    public ChipList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadChipListAttr(context, attrs);
        loadChipList();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChipList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        loadChipListAttr(context, attrs);
        loadChipList();
    }

    public void loadChipListAttr(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ChipList, 0, 0);

        //Check for custom chip style attributes
        try{
            mChipBackgroundColor    = a.getColor(R.styleable.ChipList_chip_background_color, ContextCompat.getColor(context, R.color.background_default));
            mChipTextColor          = a.getColor(R.styleable.ChipList_chip_text_color, ContextCompat.getColor(context, R.color.text_default));
            mChipSelectedColor      = a.getColor(R.styleable.ChipList_chip_selected_background_color, ContextCompat.getColor(context, R.color.selected_default));
            mChipIconColor          = a.getColor(R.styleable.ChipList_chip_icon_tint, ContextCompat.getColor(context, R.color.icon_default));
            mChipPromptColor        = a.getColor(R.styleable.ChipList_chip_prompt_color, ContextCompat.getColor(context, R.color.prompt_text_default));
            mChipPromptText         = a.getText(R.styleable.ChipList_no_chips_prompt_text);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            a.recycle();
        }
    }

    public void loadChipList(){
        mNoChipsPromptView  = new TextView(getContext());
        mNoChipsPromptView.setTextColor(mChipPromptColor);
        mNoChipsPromptView.setText(mChipPromptText);
        mNoChipsPromptView.setGravity(Gravity.CENTER);
        mNoChipsPromptView.setTextSize(15f);
        addView(mNoChipsPromptView);

        mChipListView       = new RecyclerView(getContext());

        mChipListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mChipListView.setItemAnimator(new DefaultItemAnimator());

        mChipListAdapter    = new ChipListAdapter();
        mChipListAdapter.setBackgroundColor(mChipBackgroundColor);
        mChipListAdapter.setTextColor(mChipTextColor);
        mChipListAdapter.setSelectedColor(mChipSelectedColor);
        mChipListAdapter.setIconColor(mChipIconColor);
        mChipListAdapter.setChipListener(this);

        mChipListAdapter.setChipList(mChipList);
        mChipListView.setAdapter( mChipListAdapter );

        showPrompt();
    }

    public void showPrompt(){
        removeAllViews();
        addView(mNoChipsPromptView);
    }

    public void hidePrompt(){
        removeAllViews();
        addView(mChipListView);
    }

    public void addChip( ChipData newChip ) {
        mChipList.add(newChip);
        mChipListAdapter.notifyDataSetChanged();

        hidePrompt();
    }

    public void removeChip(ChipData chipToRemove) {
        mChipList.remove(chipToRemove);
        mChipListAdapter.notifyDataSetChanged();

        if( mChipList.size() == 0 ){
            showPrompt();
        }
    }

    @Override
    public void OnChipCloseAction(String uniqueId) {
        for( int i = 0; i < mChipList.size(); i++ ){
            if( uniqueId != null && uniqueId.equals( mChipList.get(i).getUniqueId() ) ){
                ChipData dataRemoved    = mChipList.remove(i);
                if( mChipActionListener != null ){
                    mChipActionListener.onChipDeleted(dataRemoved);
                }
                mChipListAdapter.notifyDataSetChanged();

                if( mChipList.size() == 0 ){
                    showPrompt();
                }else{
                    hidePrompt();
                }
                break;
            }
        }
    }

    @Override
    public void OnChipSelected(String uniqueId, boolean selected ) {
        for( int i = 0; i < mChipList.size(); ++i ){
            if( mChipList.get(i).getUniqueId().equals( uniqueId ) ) {
                if (mChipActionListener != null) {
                    mChipActionListener.onChipSelected(mChipList.get(i), selected);
                    mChipList.get(i).setSelected(selected);
                }
            }
        }
    }

    public void setChipActionListener( ChipActionListener actionListener ){
        mChipActionListener     = actionListener;
    }

    public ArrayList<ChipData> getChipList() {
        return mChipList;
    }

    public String getNoChipsPrompt() {
        return mNoChipsPrompt;
    }

    public void setNoChipsPrompt(String noChipsPrompt) {
        this.mNoChipsPrompt = noChipsPrompt;
    }

    public TextView getChipsPromptView(){
        return mNoChipsPromptView;
    }

    public interface ChipActionListener{
        void onChipDeleted( ChipData chipDeleted );
        void onChipSelected( ChipData chipSelected, boolean selected );
    }
}
