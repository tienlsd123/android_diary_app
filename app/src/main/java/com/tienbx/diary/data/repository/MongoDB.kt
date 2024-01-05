package com.tienbx.diary.data.repository

import com.tienbx.diary.model.Diary
import com.tienbx.diary.util.Constants
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDB : MongoRepository {

    private val app = App.create(Constants.APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                .log(LogLevel.DEBUG)
                .initialSubscriptions { realm ->
                    add(realm.query<Diary>(), "sync subscription")
                }.build()
            realm = Realm.open(config)
        }
    }
}
