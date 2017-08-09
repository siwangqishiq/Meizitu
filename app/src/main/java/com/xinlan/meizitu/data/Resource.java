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
}//end class
