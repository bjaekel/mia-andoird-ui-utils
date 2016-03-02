package de.sevenfactory.mia.modules.paging;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Presenter;

public class PagingPresenter<T> extends RecyclerView.Adapter<ItemFactory.ItemPresenter<T>> implements Presenter, Observable.Observer<List<T>> {

    private PagingInteractor<T> mInteractor;
    private Observable<List<T>> mObservable;
    private ItemFactory<T>      mItemFactory;
    private Filter<T>           mFilter;

    private List<T> mItems;

    @UiThread
    public PagingPresenter(PagingInteractor<T> interactor, Observable<List<T>> observable, ItemFactory<T> itemFactory, Filter<T> filter) {
        mInteractor = interactor;
        mObservable = observable;
        mItemFactory = itemFactory;
        mFilter = filter;

        mItems = new ArrayList<>();
    }

    /* RecyclerView.Adapter */

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    
    @Override
    public ItemFactory.ItemPresenter<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return mItemFactory.createItem(parent, viewType);
    }
    
    @Override
    public void onBindViewHolder(ItemFactory.ItemPresenter<T> holder, int position) {
        T model = mItems.get(position);
        holder.bind(model);
    }
    
    /* Presenter */

    @Override
    public void register() {
        mObservable.addObserver(this);
    }
    
    @Override
    public void unregister() {
        mObservable.removeObserver(this);
    }

    @Override
    public void update() {
        mInteractor.fetchFirstPage();
    }

    public void fetchNextPage() {
        mInteractor.fetchNextPage();
    }

    /* Observable */

    @Override
    public void onLoading() {

    }
    
    @Override
    public void onCompleted(List<T> items) {
        mItems.clear();

        if (mFilter != null) {
            mFilter.filter(items);
        }
        mItems.addAll(items);

        notifyDataSetChanged();
    }
    
    @Override
    public void onFail(Failure failure) {
    }

    @Override
    public void onCancelled() {

    }
}
