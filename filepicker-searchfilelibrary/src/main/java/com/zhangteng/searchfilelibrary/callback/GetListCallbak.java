package com.zhangteng.searchfilelibrary.callback;

import java.util.List;

/**
 * Created by swing on 2018/8/27.
 */
public interface GetListCallbak<T> {
    int SUCCESS = 100;
    int FAILED = 101;

    void onSuccess(List<T> list);

    void onFailed(String msg);
}
