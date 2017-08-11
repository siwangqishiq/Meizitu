package com.xinlan.meizitu.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by panyi on 2017/8/9.
 */

public class Resource {
    private static volatile Resource mInstance;

    public static Resource getInstance() {
        if (mInstance == null) {
            synchronized (Resource.class) {
                if (mInstance == null) {
                    mInstance = new Resource();
                }
            }
        }
        return mInstance;
    }

    private ReadWriteLock mLock;

    private List<Node> rootList;
    private String nextPage;

    private LastRecord lstRecord;

    private Resource() {
        mLock = new ReentrantReadWriteLock();

        rootList = new ArrayList<Node>(20);
    }

    public void addRootList(Node node) {
        if (node == null)
            return;
        Lock lock = mLock.writeLock();
        lock.lock();
        rootList.add(node);
        lock.unlock();
    }

    public void setNextPage(String next){
        Lock lock = mLock.writeLock();
        lock.lock();
        nextPage = next;
        lock.unlock();
    }

    public List<Node> getRootList(){
        return rootList;
    }

    public Node findImageNode(final int pos){
        if(pos < 0)
            return null;

        if(rootList.size() > pos){
            return rootList.get(pos);
        }
        return null;
    }

    public void saveLstRecord(){
        this.lstRecord = new LastRecord();
        lstRecord.positionStart = getRootList().size();
        lstRecord.itemCount = getRootList().size();
    }

    public void setLstRecord(){
        if(lstRecord!=null){
            lstRecord.itemCount = getRootList().size() - lstRecord.itemCount;
        }
    }

    public LastRecord getLstRecord(){
        LastRecord record = lstRecord;
        lstRecord = null;
        return record;
    }

    public String getNextPage(){
        return nextPage;
    }

    public static class LastRecord{
        public int positionStart;
        public int itemCount;

        @Override
        public String toString() {
            return "LastRecord{" +
                    "positionStart=" + positionStart +
                    ", itemCount=" + itemCount +
                    '}';
        }
    }
}//end class
