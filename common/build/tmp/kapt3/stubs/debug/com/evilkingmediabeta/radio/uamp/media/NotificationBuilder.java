package com.evilkingmediabeta.radio.uamp.media;

import java.lang.System;

/**
 * Helper class to encapsulate code for building notifications.
 */
@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\b\u0010\u0014\u001a\u00020\u0015H\u0003J\b\u0010\u0016\u001a\u00020\u0017H\u0003J\u001b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001cJ\b\u0010\u001d\u001a\u00020\u0017H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n \u000e*\u0004\u0018\u00010\r0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001e"}, d2 = {"Lcom/evilkingmediabeta/radio/uamp/media/NotificationBuilder;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "pauseAction", "Landroidx/core/app/NotificationCompat$Action;", "platformNotificationManager", "Landroid/app/NotificationManager;", "playAction", "skipToNextAction", "skipToPreviousAction", "stopPendingIntent", "Landroid/app/PendingIntent;", "kotlin.jvm.PlatformType", "buildNotification", "Landroid/app/Notification;", "sessionToken", "Landroid/support/v4/media/session/MediaSessionCompat$Token;", "(Landroid/support/v4/media/session/MediaSessionCompat$Token;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createNowPlayingChannel", "", "nowPlayingChannelExists", "", "resolveUriAsBitmap", "Landroid/graphics/Bitmap;", "uri", "Landroid/net/Uri;", "(Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "shouldCreateNowPlayingChannel", "common_debug"})
public final class NotificationBuilder {
    private final android.app.NotificationManager platformNotificationManager = null;
    private final androidx.core.app.NotificationCompat.Action skipToPreviousAction = null;
    private final androidx.core.app.NotificationCompat.Action playAction = null;
    private final androidx.core.app.NotificationCompat.Action pauseAction = null;
    private final androidx.core.app.NotificationCompat.Action skipToNextAction = null;
    private final android.app.PendingIntent stopPendingIntent = null;
    private final android.content.Context context = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object buildNotification(@org.jetbrains.annotations.NotNull()
    android.support.v4.media.session.MediaSessionCompat.Token sessionToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super android.app.Notification> p1) {
        return null;
    }
    
    private final boolean shouldCreateNowPlayingChannel() {
        return false;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final boolean nowPlayingChannelExists() {
        return false;
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    private final void createNowPlayingChannel() {
    }
    
    public NotificationBuilder(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
}