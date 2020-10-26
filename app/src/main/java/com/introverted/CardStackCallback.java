package com.introverted;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class CardStackCallback extends DiffUtil.Callback {

    private List<cardModel> old, nuevo;

    public CardStackCallback(List<cardModel> old, List<cardModel> nuevo) {
        this.old = old;
        this.nuevo = nuevo;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return nuevo.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getImage() == nuevo.get(newItemPosition).getImage();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == nuevo.get(newItemPosition);
    }
}
