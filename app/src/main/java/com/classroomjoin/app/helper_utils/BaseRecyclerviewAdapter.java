package com.classroomjoin.app.helper_utils;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public abstract class BaseRecyclerviewAdapter<T> extends RecyclerView.Adapter{
    private static final String TAG = "BASEADAPTER";
    private List<T> mObjects;

    public BaseRecyclerviewAdapter(final List<T> objects) {
        if(objects!=null) mObjects = objects;
        else mObjects=new ArrayList<T>();
    }
    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(final T object) {
        mObjects.add(object);
        notifyItemInserted(getItemCount() - 1);
    }
    /**
     *
     * clear would make the existing list clear and the objects will be added
     *
     * During scrolling the clear value should be made false
     * @param clear
     * @param mObjects
     */

    public void addItems(boolean clear,List<T> mObjects){
        if(clear)clear();
        for(T object:mObjects){
            add(object);
        }
    }

    public void additemtolist(int position,List<T> objects){
        mObjects.addAll(position,objects);
        notifyItemRangeChanged(position,objects.size());
    }

    public void additemat(int position,T object){
        mObjects.add(position,object);
        notifyItemInserted(position);
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        int size = getItemCount();
        for(int i=0;i<size;i++)
        {
            mObjects.remove(0);
            notifyItemRemoved(0);
        }
    }

    public void clearList(){
        mObjects.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mObjects == null ? 0 : mObjects.size();
    }

    public T getItem(final int position) {
        return mObjects.get(position);
    }

    public long getItemId(final int position) {
        return position;
    }
    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(final T item) {
        return mObjects.indexOf(item);
    }
    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(final T object, int index) {
        mObjects.add(index, object);
        notifyItemInserted(index);
    }
    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        final int position = getPosition(object);
        mObjects.remove(object);
        notifyItemRemoved(position);
    }
    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        Collections.sort(mObjects, comparator);
        notifyItemRangeChanged(0, getItemCount());
    }

    public void remove(int position){
        mObjects.remove(position);
        notifyItemRemoved(position);
    }


    public void removeall(){
        while (!mObjects.isEmpty()) {
            mObjects.remove(0);
            notifyItemRemoved(0);
        }
    }

    public List<T> getItems(){
        return mObjects;
    }


}

