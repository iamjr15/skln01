package com.autohub.skln.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-06-12.
 */
public class EmptyViewRecyclerView extends RecyclerView {
    private View mEmptyView;
    private boolean isAlreadyRegistered;
    private final RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkEmpty();
        }
    };

    public EmptyViewRecyclerView(@NonNull Context context) {
        super(context);
    }

    public EmptyViewRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyViewRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void checkEmpty() {
        Adapter adapter = getAdapter();
        if (mEmptyView != null && adapter != null) {
            mEmptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null && !isAlreadyRegistered) {
            isAlreadyRegistered = true;
            adapter.registerAdapterDataObserver(mObserver);
        }
        checkEmpty();
    }

    public void setEmptyView(@NonNull View view) {
        mEmptyView = view;
        Adapter adapter = getAdapter();
        if (adapter != null && !isAlreadyRegistered) {
            isAlreadyRegistered = true;
            adapter.registerAdapterDataObserver(mObserver);
        }
    }
}
