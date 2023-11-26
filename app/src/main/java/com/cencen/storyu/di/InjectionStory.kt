package com.cencen.storyu.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.cencen.storyu.data.connection.api.ApiConfig
import com.cencen.storyu.data.repositories.StoryRepositories
import com.cencen.storyu.data.settingpreference.PreferencesSetting

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storyu")

object InjectionStory {
    fun setRepositories(con: Context): StoryRepositories {
        val conf = PreferencesSetting.getInstance(con.dataStore)
        val apis = ApiConfig.getApiMember()
        return StoryRepositories.getInstance(conf, apis)
    }
}