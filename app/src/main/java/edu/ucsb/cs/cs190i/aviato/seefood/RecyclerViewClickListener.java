package edu.ucsb.cs.cs190i.aviato.seefood;

 import android.view.View;

/*
 * Created by sal on 5/14/17.
 */

public interface RecyclerViewClickListener
{
    public void recyclerViewListClicked(View v, int position, int callingView);
}