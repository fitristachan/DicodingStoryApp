package com.dicoding.dicodingstoryapp

import com.dicoding.dicodingstoryapp.retrofit.response.ListStoryItem

object DataDummy {

    fun generateDummyStoriesResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(
                "photoUrl $i",
                "createdAt $i",
                "name $i",
                "description $i",
                "id $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(stories)
        }
        return items
    }
}