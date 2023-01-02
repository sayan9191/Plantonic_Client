package com.example.plantonic.Adapter.listeners

import com.example.plantonic.firebaseClasses.CategoryItem

interface CategoryListener {
    fun onCategoryItemClicked(categoryItem: CategoryItem)
}