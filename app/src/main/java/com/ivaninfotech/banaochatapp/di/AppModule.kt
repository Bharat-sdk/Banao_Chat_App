package com.ivaninfotech.banaochatapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.model.message.attachments.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.configuration.Config
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideChatClient(
        @ApplicationContext
        context: Context,
        offlinePluginFactory: StreamOfflinePluginFactory
    ): ChatClient {
        return ChatClient.Builder("a4uyyk9yj7bt", context)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .withPlugin(offlinePluginFactory)
            .build()
    }

    @Singleton
    @Provides
    fun getStreamOfflineFactory(
        @ApplicationContext
        context: Context
    ): StreamOfflinePluginFactory {
        return  StreamOfflinePluginFactory(
            config = Config(
                backgroundSyncEnabled = true,
                userPresence = true,
                persistenceEnabled = true,
                uploadAttachmentsNetworkType = UploadAttachmentsNetworkType.NOT_ROAMING,
            ),
            appContext =  context,
        )
    }


}