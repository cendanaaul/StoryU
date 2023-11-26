package com.cencen.storyu.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cencen.storyu.data.connection.api.ApiServices
import com.cencen.storyu.data.models.RosterStory
import com.cencen.storyu.data.settingpreference.PreferencesSetting
import kotlinx.coroutines.flow.first

class StoryPageSources(
    private val apis: ApiServices,
    private val conf: PreferencesSetting
) : PagingSource<Int, RosterStory>() {
    override fun getRefreshKey(state: PagingState<Int, RosterStory>): Int? {
        return state.anchorPosition?.let {
            val pageAnchor = state.closestPageToPosition(it)
            pageAnchor?.prevKey?.plus(1) ?: pageAnchor?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RosterStory> {
        return try {
            val paging = params.key ?: INITIAL_PAGE_IDX
            val tokens = "Bearer ${conf.getMemberData().first().token}"
            val dataRes = apis.getMemberStory(tokens, paging, params.loadSize).listStory

            LoadResult.Page(
                data = dataRes,
                prevKey = if (paging == INITIAL_PAGE_IDX) null else paging - 1,
                nextKey = if (dataRes.isNullOrEmpty()) null else paging + 1
            )
        } catch (exc: Exception) {
            return LoadResult.Error(exc)
        }
    }

    companion object {
        const val INITIAL_PAGE_IDX = 1
    }

}