package fpt.edu.dawd_test.adapters;

import android.content.Intent;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public abstract class BaseAdapter<T, TViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<TViewHolder> {
    List<T> objects;
    public String action;

    public BaseAdapter(List<T> objects, String action) {
        this.objects = objects;
        this.action = action;
    }

    @Override
    public int getItemCount() {
        return objects == null ? 0 : objects.size();
    }

    protected void onClickItem(View view, int id, int position) {
        Intent intent = new Intent(action);
        intent.putExtra("id", id);
        intent.putExtra("position", position);
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
    }

    public void refreshAllItems(List<T> objects) {
        this.objects = objects;
        notifyItemRangeChanged(0, getItemCount());
    }

    public void refreshNewItem(T object) {
        this.objects.add(object);
        notifyItemInserted(this.objects.size());
    }

    public void refreshChangedItem(int position, T object) {
        this.objects.set(position, object);
        notifyItemChanged(position);
    }

    public void refreshRemovedItem(int position) {
        this.objects.remove(position);
        notifyItemRemoved(position);
    }
}
