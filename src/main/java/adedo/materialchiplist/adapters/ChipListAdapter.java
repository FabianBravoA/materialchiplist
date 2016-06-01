package adedo.materialchiplist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import adedo.materialchiplist.R;
import adedo.materialchiplist.model.ChipData;
import adedo.materialchiplist.views.Chip;



public class ChipListAdapter extends RecyclerView.Adapter<Chip> implements Chip.ChipListener {

    private ArrayList<ChipData> mChipList   = new ArrayList<>();
    private int mBackgroundColor;
    private int mTextColor;
    private int mSelectedColor;
    private int mIconColor;
    private Chip.ChipListener mChipListener;

    public ChipListAdapter(){
        super();

        setHasStableIds(true);
    }

    @Override
    public Chip onCreateViewHolder(ViewGroup parent, int viewType) {
        Chip chip   = new Chip( LayoutInflater.from(parent.getContext()).inflate(R.layout.chip_layout, parent, false));
        chip.setAttributes(mBackgroundColor, mTextColor, mSelectedColor, mIconColor);
        chip.setActionListener(this);
        return chip;
    }

    @Override
    public void onBindViewHolder(Chip holder, int position) {
        holder.reset();
        ChipData data   = mChipList.get(position);

        if( data.getIconURL() != null && !data.getIconURL().isEmpty() )
            holder.setIcon(data.getIconURL());
        else
            holder.setIcon(data.getIconDrawable() );

        holder.setInfo(data.getInfo());
        holder.setmUniqueId(data.getUniqueId());
        holder.setSelected(data.isSelected());
    }

    @Override
    public long getItemId(int position) {
        return mChipList.get(position).getUniqueId().hashCode();
    }

    @Override
    public int getItemCount() {
        return mChipList.size();
    }

    public ArrayList<ChipData> getChipList() {
        return mChipList;
    }

    public void setChipList(ArrayList<ChipData> chipList) {
        this.mChipList = chipList;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.mSelectedColor = selectedColor;
    }

    public void setIconColor(int iconColor) {
        this.mIconColor = iconColor;
    }

    public void setChipListener( Chip.ChipListener chipListener) {
        this.mChipListener = chipListener;
    }

    @Override
    public void OnChipCloseAction(String uniqueId) {
        if(mChipListener != null){
            mChipListener.OnChipCloseAction(uniqueId);
        }
    }

    @Override
    public void OnChipSelected(String uniqueId, boolean selected) {
        if(mChipListener != null){
            mChipListener.OnChipSelected(uniqueId, selected);
        }
    }
}
