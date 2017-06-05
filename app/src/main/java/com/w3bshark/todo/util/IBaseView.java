package com.w3bshark.todo.util;

/**
 * Created by Tyler McCraw on 3/8/17.
 * <p/>
 * View Interface which exposes common
 * functions for all View instances
 */

public interface IBaseView<T> {
    void setPresenter(T presenter);
}