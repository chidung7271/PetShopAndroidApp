package app.code.petshopandroidapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_pref")

class TokenManager(private val context: Context) {

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = token
        }
    }

    fun getTokenFlow() = context.dataStore.data.map { prefs ->
        prefs[ACCESS_TOKEN] ?: ""
    }

    suspend fun getTokenOnce(): String {
        return context.dataStore.data.first()[ACCESS_TOKEN] ?: ""
    }

    suspend fun clearToken() {
        context.dataStore.edit { it.remove(ACCESS_TOKEN) }
    }
}