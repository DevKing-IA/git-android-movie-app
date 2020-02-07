package com.evilkingmediabeta.radio.uamp.media;

import java.lang.System;

/**
 * Helper class to retrieve the the Metadata necessary for the ExoPlayer MediaSession connection
 * extension to call [MediaSessionCompat.setMetadata].
 */
@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/evilkingmediabeta/radio/uamp/media/UampQueueNavigator;", "Lcom/google/android/exoplayer2/ext/mediasession/TimelineQueueNavigator;", "mediaSession", "Landroid/support/v4/media/session/MediaSessionCompat;", "(Landroid/support/v4/media/session/MediaSessionCompat;)V", "window", "Lcom/google/android/exoplayer2/Timeline$Window;", "getMediaDescription", "Landroid/support/v4/media/MediaDescriptionCompat;", "player", "Lcom/google/android/exoplayer2/Player;", "windowIndex", "", "common_debug"})
final class UampQueueNavigator extends com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator {
    private final com.google.android.exoplayer2.Timeline.Window window = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.support.v4.media.MediaDescriptionCompat getMediaDescription(@org.jetbrains.annotations.NotNull()
    com.google.android.exoplayer2.Player player, int windowIndex) {
        return null;
    }
    
    public UampQueueNavigator(@org.jetbrains.annotations.NotNull()
    android.support.v4.media.session.MediaSessionCompat mediaSession) {
        super(null);
    }
}