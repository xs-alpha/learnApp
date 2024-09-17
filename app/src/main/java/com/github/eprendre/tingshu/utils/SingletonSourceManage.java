package com.github.eprendre.tingshu.utils;

import com.blankj.utilcode.util.CollectionUtils;
import com.github.eprendre.tingshu.sources.TingShu;

import java.util.LinkedList;
import java.util.List;

public class SingletonSourceManage {
    // 听书源单例
    private static List<TingShu> sourceList;
    private final static Object sourceListLock = new Object();

    public static List<TingShu> getInstance(){
        if (CollectionUtils.isEmpty(sourceList)){
            synchronized (sourceListLock){
                if (CollectionUtils.isEmpty(sourceList)){
                    sourceList = new LinkedList<>();
                }
            }
        }
        return sourceList;
    }

}
