package com.example.myschoolwearclient.Adapter.listeners

import com.example.myschoolwearclient.firebaseClasses.CategoryItem

interface CategoryListener {
    fun onCategoryItemClicked(categoryItem: CategoryItem)
}