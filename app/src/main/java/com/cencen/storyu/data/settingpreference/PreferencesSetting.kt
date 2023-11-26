package com.cencen.storyu.data.settingpreference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cencen.storyu.data.models.Member
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesSetting private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun signin() {
        dataStore.edit { conf ->
            conf[STATE] = true
        }
    }

    fun getMemberData(): Flow<Member> {
        return dataStore.data.map { conf ->
            Member(
                conf[NAME] ?: "",
                conf[TOKEN] ?: "",
                conf[STATE] ?: false
            )
        }
    }

    suspend fun saveMemberData(mem: Member) {
        dataStore.edit { conf ->
            conf[NAME] = mem.name
            conf[STATE] = mem.isSignin
            conf[TOKEN] = mem.token
        }
    }

    suspend fun signout() {
        dataStore.edit { conf ->
            conf.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferencesSetting? = null

        fun getInstance(dataStore: DataStore<Preferences>): PreferencesSetting {
            return INSTANCE ?: synchronized(this) {
                val instant = PreferencesSetting(dataStore)
                INSTANCE = instant
                instant
            }
        }

        private val TOKEN = stringPreferencesKey("token")
        private val NAME = stringPreferencesKey("name")
        private val STATE = booleanPreferencesKey("state")
    }
}