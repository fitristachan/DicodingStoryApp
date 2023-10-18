package com.dicoding.dicodingstoryapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "logindata")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val token = stringPreferencesKey("token")
    private val session = booleanPreferencesKey("session")
    private val name = stringPreferencesKey("name")

    suspend fun getToken(): String {
        return dataStore.data.map { preferences ->
            preferences[token] ?: ""
        }.first()
    }

    fun getSession(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[session] ?: false
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[name] ?: ""
        }
    }

    suspend fun saveUser(token: String, session: Boolean, name: String) {
        dataStore.edit { preferences ->
            preferences[this.token] = token
            preferences[this.session] = session
            preferences[this.name] = name
        }
    }

    suspend fun deleteAll() {
        dataStore.edit { preferences ->
            preferences[this.token] = ""
            preferences[this.session] = false
            preferences[this.name] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}