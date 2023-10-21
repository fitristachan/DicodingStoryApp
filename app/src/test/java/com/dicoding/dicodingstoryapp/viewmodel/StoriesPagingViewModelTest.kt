package com.dicoding.dicodingstoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.dicodingstoryapp.DataDummy
import com.dicoding.dicodingstoryapp.MainDispatcherRule
import com.dicoding.dicodingstoryapp.getOrAwaitValue
import com.dicoding.dicodingstoryapp.repository.StoriesPagingRepository
import com.dicoding.dicodingstoryapp.retrofit.response.ListStoryItem
import com.dicoding.dicodingstoryapp.stories.adapter.StoriesPagingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesPagingViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storiesPagingRepository: StoriesPagingRepository

    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {
        val dummyStories = DataDummy.generateDummyStoriesResponse()

        val data: PagingData<ListStoryItem> = StoriesPagingSource.snapshot(dummyStories)
        val expectedStories = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStories.value = data

        `when`(storiesPagingRepository.getStories()).thenReturn(expectedStories)
        val storiesPagingViewModel = StoriesPagingViewModel(storiesPagingRepository)
        val actualStories: PagingData<ListStoryItem> =
            storiesPagingViewModel.getStories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0], differ.snapshot()[0])
    }
}

class StoriesPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}