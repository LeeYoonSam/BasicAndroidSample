package com.ys.basicandroid.presentaion.base.adapter

interface IViewTypeGetter<VT : Enum<VT>> {
    fun getViewType(): VT

    fun getViewTypeOrdinal(): Int {
        return getViewType().ordinal
    }

    fun getViewTypeName(): String {
        return getViewType().name
    }
}
