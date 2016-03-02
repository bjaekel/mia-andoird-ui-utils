package de.sevenfactory.mia.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import de.sevenfactory.mia.modules.R;

public class IrregularGridLayout extends GridLayout {
    private static final float CHILD_SIZE_RATIO_LARGE    = 0.897f;
    private static final float CHILD_SIZE_RATIO_SMALL    = 1.27f;
    private static final float CHILD_ROW_HEIGHT_GRID     = CHILD_SIZE_RATIO_LARGE * 3.0f;
    private static final int   COLUMN_COUNT_GRID         = 3;// Only relevant for grid mode
    private static final int   FIRST_ELEMENT_EXTRA_CELLS = 3;
    private static final int   FIRST_ELEMENT_SPAN        = 2;

    public IrregularGridLayout(Context context) {
        super(context);
    }

    public IrregularGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IrregularGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int width = getMeasuredWidth();

        int     height;
        boolean gridMode = isGridMode(width);
        View[]  children = getChildren();

        if (children.length == 0) {
            height = 0;
        } else {
            // Height is calculated depending on the current device width (list or grid)
            if (gridMode) {
                if (children.length == 1) {
                    // Special case for single image: span 3 rows and 3 columns
                    height = (int) (width / CHILD_ROW_HEIGHT_GRID) * getColumnCount();
                } else {
                    // All other cases
                    int gridCellCount   = children.length + FIRST_ELEMENT_EXTRA_CELLS;
                    int additionalCells = gridCellCount % getColumnCount();
                    if (additionalCells > 0) {
                        gridCellCount += getColumnCount() - additionalCells;
                    }
                    height = (int) (width / CHILD_ROW_HEIGHT_GRID) * (gridCellCount / getColumnCount());
                }
            } else {
                height = (int) (width / CHILD_SIZE_RATIO_LARGE) + (children.length - 1) * (int) (width / CHILD_SIZE_RATIO_SMALL);
            }

            setMeasuredDimension(width, height);
        }
    }

    public void adjustGrid(int width, int oldWidth, int paddingsSide, boolean removeOldViews) {
        if (width == oldWidth) {
            return;
        }
        boolean gridMode = isGridMode(width);
        View[]  children = getChildren();

        if (removeOldViews) {
            removeAllViews();
        }

        setColumnCount(gridMode ? COLUMN_COUNT_GRID : 1);

        for (int i = 0; i < children.length; i++) {
            LayoutParams params = (LayoutParams) children[i].getLayoutParams();
            params.setGravity(Gravity.FILL_VERTICAL | Gravity.FILL_HORIZONTAL);
            params.height = 0;
            View                      largeItem   = ((ViewGroup) children[i]).getChildAt(0);
            LinearLayout.LayoutParams largeParams = (LinearLayout.LayoutParams) largeItem.getLayoutParams();
            largeParams.weight = 1;
            if (gridMode) {
                params.width = 0;
                if (i == 0) {
                    // The top part of the view should be 3 times the size of the lower text part
                    largeParams.weight = 3;
                    // The first grid item spans two cells in both directions
                    params.columnSpec = GridLayout.spec(0, 2, 2);
                    params.rowSpec = GridLayout.spec(0, 2, 2);
                } else if (i >= (2 * getColumnCount()) - FIRST_ELEMENT_EXTRA_CELLS) {
                    // The items in the bottom rows of the layout span one cell in either direction
                    params.columnSpec = GridLayout.spec(i % getColumnCount(), 1, 1);
                    params.rowSpec = GridLayout.spec((i + FIRST_ELEMENT_EXTRA_CELLS) / getColumnCount(), 1, 1);
                } else {
                    // The items in the right columns of the layout next to the large image span one cell in either direction
                    boolean firstRow = i <= getColumnCount() - FIRST_ELEMENT_SPAN;
                    params.columnSpec = GridLayout.spec(firstRow ? i + FIRST_ELEMENT_SPAN - 1 : (i + FIRST_ELEMENT_SPAN + 1) % getColumnCount(), 1, 1);
                    params.rowSpec = GridLayout.spec(firstRow ? 0 : 1, 1, 1);
                }
            } else {
                // The top part of the view should be 2 times the size of the lower text
                // part if it is the first item, same size otherwise
                largeParams.weight = i == 0 ? 2 : 1;

                // There is only one column on narrow devices/orientations
                params.columnSpec = GridLayout.spec(0, 1, 1);
                // The height of the first item is 1.5 times the size of the remaining items
                params.rowSpec = GridLayout.spec(i, 1, (i == 0) ? 3 : 2);
                params.width = width - paddingsSide;
            }
            largeItem.setLayoutParams(largeParams);
            children[i].setLayoutParams(params);

            if (removeOldViews) {
                addView(children[i]);
            }
        }
        // A layout pass has to be requested to make sure the grid is displayed correctly after rotation
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @NonNull
    private View[] getChildren() {
        View[] children = new View[getChildCount()];

        for (int i = 0; i < children.length; i++) {
            children[i] = getChildAt(i);
        }
        return children;
    }

    private boolean isGridMode(int width) {
        return width >= getResources().getDimensionPixelSize(R.dimen.horizontal_grid_min_width);
    }
}
