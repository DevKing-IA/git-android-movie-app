package com.evilkingmediabeta.radio.uamp.media;

import java.lang.System;

/**
 * This class is the entry point for browsing and playback commands from the APP's UI
 * and other apps that wish to play music via UAMP (for example, Android Auto or
 * the Google Assistant).
 *
 * Browsing begins with the method [MusicService.onGetRoot], and continues in
 * the callback [MusicService.onLoadChildren].
 *
 * For more information on implementing a MediaBrowserService,
 * visit [https://developer.android.com/guide/topics/media-apps/audio-app/building-a-mediabrowserservice.html](https://developer.android.com/guide/topics/media-apps/audio-app/building-a-mediabrowserservice.html).
 */
@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u00ac\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001:\u0001MB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u00105\u001a\u000206H\u0017J\b\u00107\u001a\u000206H\u0016J$\u00108\u001a\u0004\u0018\u0001092\u0006\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020=2\b\u0010>\u001a\u0004\u0018\u00010?H\u0016J$\u0010@\u001a\u0002062\u0006\u0010A\u001a\u00020;2\u0012\u0010B\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020E0D0CH\u0016J.\u0010F\u001a\u0002062\u0006\u0010G\u001a\u00020;2\b\u0010H\u001a\u0004\u0018\u00010?2\u0012\u0010B\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020E0D0CH\u0016J\u0010\u0010I\u001a\u0002062\u0006\u0010J\u001a\u00020KH\u0016J\b\u0010L\u001a\u000206H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\n\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u00020\u0013X\u0084.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0019X\u0084.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001a\u0010\u001e\u001a\u00020\u001fX\u0084.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u000e\u0010$\u001a\u00020%X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\'X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020/X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u00102\u001a\n 4*\u0004\u0018\u00010303X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006N"}, d2 = {"Lcom/evilkingmediabeta/radio/uamp/media/MusicService;", "Landroidx/media/MediaBrowserServiceCompat;", "()V", "becomingNoisyReceiver", "Lcom/evilkingmediabeta/radio/uamp/media/BecomingNoisyReceiver;", "browseTree", "Lcom/evilkingmediabeta/radio/uamp/media/library/BrowseTree;", "getBrowseTree", "()Lcom/evilkingmediabeta/radio/uamp/media/library/BrowseTree;", "browseTree$delegate", "Lkotlin/Lazy;", "exoPlayer", "Lcom/google/android/exoplayer2/ExoPlayer;", "getExoPlayer", "()Lcom/google/android/exoplayer2/ExoPlayer;", "exoPlayer$delegate", "isForegroundService", "", "mediaController", "Landroid/support/v4/media/session/MediaControllerCompat;", "getMediaController", "()Landroid/support/v4/media/session/MediaControllerCompat;", "setMediaController", "(Landroid/support/v4/media/session/MediaControllerCompat;)V", "mediaSession", "Landroid/support/v4/media/session/MediaSessionCompat;", "getMediaSession", "()Landroid/support/v4/media/session/MediaSessionCompat;", "setMediaSession", "(Landroid/support/v4/media/session/MediaSessionCompat;)V", "mediaSessionConnector", "Lcom/google/android/exoplayer2/ext/mediasession/MediaSessionConnector;", "getMediaSessionConnector", "()Lcom/google/android/exoplayer2/ext/mediasession/MediaSessionConnector;", "setMediaSessionConnector", "(Lcom/google/android/exoplayer2/ext/mediasession/MediaSessionConnector;)V", "mediaSource", "Lcom/evilkingmediabeta/radio/uamp/media/library/MusicSource;", "notificationBuilder", "Lcom/evilkingmediabeta/radio/uamp/media/NotificationBuilder;", "notificationManager", "Landroidx/core/app/NotificationManagerCompat;", "packageValidator", "Lcom/evilkingmediabeta/radio/uamp/media/PackageValidator;", "remoteJsonSource", "Landroid/net/Uri;", "serviceJob", "Lkotlinx/coroutines/Job;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "uAmpAudioAttributes", "Lcom/google/android/exoplayer2/audio/AudioAttributes;", "kotlin.jvm.PlatformType", "onCreate", "", "onDestroy", "onGetRoot", "Landroidx/media/MediaBrowserServiceCompat$BrowserRoot;", "clientPackageName", "", "clientUid", "", "rootHints", "Landroid/os/Bundle;", "onLoadChildren", "parentMediaId", "result", "Landroidx/media/MediaBrowserServiceCompat$Result;", "", "Landroid/support/v4/media/MediaBrowserCompat$MediaItem;", "onSearch", "query", "extras", "onTaskRemoved", "rootIntent", "Landroid/content/Intent;", "removeNowPlayingNotification", "MediaControllerCallback", "common_debug"})
public class MusicService extends androidx.media.MediaBrowserServiceCompat {
    private com.evilkingmediabeta.radio.uamp.media.BecomingNoisyReceiver becomingNoisyReceiver;
    private androidx.core.app.NotificationManagerCompat notificationManager;
    private com.evilkingmediabeta.radio.uamp.media.NotificationBuilder notificationBuilder;
    private com.evilkingmediabeta.radio.uamp.media.library.MusicSource mediaSource;
    private com.evilkingmediabeta.radio.uamp.media.PackageValidator packageValidator;
    private final kotlinx.coroutines.Job serviceJob = null;
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.NotNull()
    protected android.support.v4.media.session.MediaSessionCompat mediaSession;
    @org.jetbrains.annotations.NotNull()
    protected android.support.v4.media.session.MediaControllerCompat mediaController;
    @org.jetbrains.annotations.NotNull()
    protected com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector mediaSessionConnector;
    
    /**
     * This must be `by lazy` because the source won't initially be ready.
     * See [MusicService.onLoadChildren] to see where it's accessed (and first
     * constructed).
     */
    private final kotlin.Lazy browseTree$delegate = null;
    private boolean isForegroundService;
    private final android.net.Uri remoteJsonSource = null;
    private final com.google.android.exoplayer2.audio.AudioAttributes uAmpAudioAttributes = null;
    
    /**
     * Configure ExoPlayer to handle audio focus for us.
     * See [Player.AudioComponent.setAudioAttributes] for details.
     */
    private final kotlin.Lazy exoPlayer$delegate = null;
    
    @org.jetbrains.annotations.NotNull()
    protected final android.support.v4.media.session.MediaSessionCompat getMediaSession() {
        return null;
    }
    
    protected final void setMediaSession(@org.jetbrains.annotations.NotNull()
    android.support.v4.media.session.MediaSessionCompat p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final android.support.v4.media.session.MediaControllerCompat getMediaController() {
        return null;
    }
    
    protected final void setMediaController(@org.jetbrains.annotations.NotNull()
    android.support.v4.media.session.MediaControllerCompat p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector getMediaSessionConnector() {
        return null;
    }
    
    protected final void setMediaSessionConnector(@org.jetbrains.annotations.NotNull()
    com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector p0) {
    }
    
    /**
     * This must be `by lazy` because the source won't initially be ready.
     * See [MusicService.onLoadChildren] to see where it's accessed (and first
     * constructed).
     */
    private final com.evilkingmediabeta.radio.uamp.media.library.BrowseTree getBrowseTree() {
        return null;
    }
    
    /**
     * Configure ExoPlayer to handle audio focus for us.
     * See [Player.AudioComponent.setAudioAttributes] for details.
     */
    private final com.google.android.exoplayer2.ExoPlayer getExoPlayer() {
        return null;
    }
    
    @kotlinx.coroutines.ExperimentalCoroutinesApi()
    @java.lang.Override()
    public void onCreate() {
    }
    
    /**
     * This is the code that causes UAMP to stop playing when swiping it away from recents.
     * The choice to do this is app specific. Some apps stop playback, while others allow playback
     * to continue and allow uses to stop it with the notification.
     */
    @java.lang.Override()
    public void onTaskRemoved(@org.jetbrains.annotations.NotNull()
    android.content.Intent rootIntent) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    /**
     * Returns the "root" media ID that the client should request to get the list of
     * [MediaItem]s to browse/play.
     */
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public androidx.media.MediaBrowserServiceCompat.BrowserRoot onGetRoot(@org.jetbrains.annotations.NotNull()
    java.lang.String clientPackageName, int clientUid, @org.jetbrains.annotations.Nullable()
    android.os.Bundle rootHints) {
        return null;
    }
    
    /**
     * Returns (via the [result] parameter) a list of [MediaItem]s that are child
     * items of the provided [parentMediaId]. See [BrowseTree] for more details on
     * how this is build/more details about the relationships.
     */
    @java.lang.Override()
    public void onLoadChildren(@org.jetbrains.annotations.NotNull()
    java.lang.String parentMediaId, @org.jetbrains.annotations.NotNull()
    androidx.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result) {
    }
    
    /**
     * Returns a list of [MediaItem]s that match the given search query
     */
    @java.lang.Override()
    public void onSearch(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.Nullable()
    android.os.Bundle extras, @org.jetbrains.annotations.NotNull()
    androidx.media.MediaBrowserServiceCompat.Result<java.util.List<android.support.v4.media.MediaBrowserCompat.MediaItem>> result) {
    }
    
    /**
     * Removes the [NOW_PLAYING_NOTIFICATION] notification.
     *
     * Since `stopForeground(false)` was already called (see
     * [MediaControllerCallback.onPlaybackStateChanged], it's possible to cancel the notification
     * with `notificationManager.cancel(NOW_PLAYING_NOTIFICATION)` if minSdkVersion is >=
     * [Build.VERSION_CODES.LOLLIPOP].
     *
     * Prior to [Build.VERSION_CODES.LOLLIPOP], notifications associated with a foreground
     * service remained marked as "ongoing" even after calling [Service.stopForeground],
     * and cannot be cancelled normally.
     *
     * Fortunately, it's possible to simply call [Service.stopForeground] a second time, this
     * time with `true`. This won't change anything about the service's state, but will simply
     * remove the notification.
     */
    private final void removeNowPlayingNotification() {
    }
    
    public MusicService() {
        super();
    }
    
    /**
     * Class to receive callbacks about state changes to the [MediaSessionCompat]. In response
     * to those callbacks, this class:
     *
     * - Build/update the service's notification.
     * - Register/unregister a broadcast receiver for [AudioManager.ACTION_AUDIO_BECOMING_NOISY].
     * - Calls [Service.startForeground] and [Service.stopForeground].
     */
    @kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016J\u0012\u0010\u0007\u001a\u00020\u00042\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\u0019\u0010\n\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\f"}, d2 = {"Lcom/evilkingmediabeta/radio/uamp/media/MusicService$MediaControllerCallback;", "Landroid/support/v4/media/session/MediaControllerCompat$Callback;", "(Lcom/evilkingmediabeta/radio/uamp/media/MusicService;)V", "onMetadataChanged", "", "metadata", "Landroid/support/v4/media/MediaMetadataCompat;", "onPlaybackStateChanged", "state", "Landroid/support/v4/media/session/PlaybackStateCompat;", "updateNotification", "(Landroid/support/v4/media/session/PlaybackStateCompat;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "common_debug"})
    final class MediaControllerCallback extends android.support.v4.media.session.MediaControllerCompat.Callback {
        
        @java.lang.Override()
        public void onMetadataChanged(@org.jetbrains.annotations.Nullable()
        android.support.v4.media.MediaMetadataCompat metadata) {
        }
        
        @java.lang.Override()
        public void onPlaybackStateChanged(@org.jetbrains.annotations.Nullable()
        android.support.v4.media.session.PlaybackStateCompat state) {
        }
        
        public MediaControllerCallback() {
            super();
        }
    }
}