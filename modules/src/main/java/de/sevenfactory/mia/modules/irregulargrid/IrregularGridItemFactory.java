package de.sevenfactory.mia.modules.irregulargrid;

import android.content.Context;
import android.view.View;

/**
 * Created by bernd on 04.02.2016.
 */
public interface IrregularGridItemFactory<T> {

    View createIrregularGridItem(Context context, T inIrregularGridItem, OnItemClickedListener onItemClickedListener);

    interface OnItemClickedListener {
        void onItemClicked(String id);
    }
}
