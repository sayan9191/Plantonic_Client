package co.`in`.plantonic.Adapter.listeners

import co.`in`.plantonic.firebaseClasses.CategoryItem

interface CategoryListener {
    fun onCategoryItemClicked(categoryItem: CategoryItem)
}