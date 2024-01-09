package com.tienbx.diary.data.repository

import com.tienbx.diary.model.Diary
import com.tienbx.diary.util.Constants
import com.tienbx.diary.util.RequestState
import com.tienbx.diary.util.toInstance
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId

object MongoDB : MongoRepository {

    private val app = App.create(Constants.APP_ID)
    private var user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        user = app.currentUser
        user?.let {
            val config = SyncConfiguration.Builder(it, setOf(Diary::class))
                .log(LogLevel.DEBUG)
                .initialSubscriptions { realm ->
                    add(realm.query<Diary>(), "sync subscription")
                }.build()
            realm = Realm.open(config)
        }
    }

    override fun getAllDiaries(): Flow<Diaries> {
        user = app.currentUser
        return if (user != null) {
            try {
                realm.query<Diary>().sort(property = "date", sortOrder = Sort.DESCENDING).asFlow().map { result ->
                    RequestState.Success(data = result.list.groupBy {
                        it.date.toInstance()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    })
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }
}

private class UserNotAuthenticatedException : Exception("User is not Logged in.")
