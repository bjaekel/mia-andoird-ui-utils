package de.sevenfactory.mia.modules.paging;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public interface ItemFactory<T> {
    ItemFactory.ItemPresenter<T> createItem(ViewGroup parent, int viewType);
    
    abstract class ItemPresenter<T> extends RecyclerView.ViewHolder {
        public ItemPresenter(View itemView) {
            super(itemView);
        }
    
        public abstract void bind(T model);
    }
}
