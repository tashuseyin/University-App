package com.example.universityapp.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.universityapp.common.Constant.PREFERENCES_GLOBAL_TOKEN
import com.example.universityapp.common.Constant.PREFERENCES_LOGIN
import com.example.universityapp.common.Constant.PREFERENCES_NAME
import com.example.universityapp.common.Constant.PREFERENCES_PASSWORD
import com.example.universityapp.common.Constant.PREFERENCES_USERNAME
import com.example.universityapp.data.model.user_info.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferenceKeys {
        val token = stringPreferencesKey(PREFERENCES_GLOBAL_TOKEN)
        val loginStatus = booleanPreferencesKey(PREFERENCES_LOGIN)
        val usernameInfo = stringPreferencesKey(PREFERENCES_USERNAME)
        val passwordInfo = stringPreferencesKey(PREFERENCES_PASSWORD)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.token] = token
        }
    }

    val readToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            val token = it[PreferenceKeys.token] ?: ""
            token
        }

    suspend fun saveLoginStatus(status: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.loginStatus] = status
        }
    }

    val readLoginStatus: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            val status = it[PreferenceKeys.loginStatus] ?: false
            status
        }

    suspend fun saveUserInformation(username: String, password: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.usernameInfo] = username
            preferences[PreferenceKeys.passwordInfo] = password
        }
    }


    val readUserInfo: Flow<UserInfo> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            val usernameInfo = it[PreferenceKeys.usernameInfo] ?: ""
            val passwordInfo = it[PreferenceKeys.passwordInfo] ?: ""

            UserInfo(usernameInfo, passwordInfo)
        }
}