package edu.ucsb.cs.cs190i.aviato.seefood.recycleViewHelpers;

import android.support.v7.widget.helper.ItemTouchHelper;

// SOURCES: https://github.com/iPaulPro/Android-ItemTouchHelper-Demo/tree/master/app/src/main/java/co/paulburke/android/itemtouchhelperdemo/helper


/**
 * Interface to notify an item ViewHolder of relevant callbacks from {@link
 * android.support.v7.widget.helper.ItemTouchHelper.Callback}.
 *
 * @author Paul Burke (ipaulpro)
 */
public interface ItemTouchHelperViewHolder {

    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();


    /**
     * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
     * state should be cleared.
     */
    void onItemClear();
}
