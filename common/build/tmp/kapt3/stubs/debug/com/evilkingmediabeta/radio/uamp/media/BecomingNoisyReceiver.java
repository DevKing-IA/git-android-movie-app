package com.evilkingmediabeta.radio.uamp.media;

import java.lang.System;

/**
 * Helper class for listening for when headphones are unplugged (or the audio
 * will otherwise cause playback to become "noisy").
 */
@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0006\u0010\u0011\u001a\u00020\u000eJ\u0006\u0010\u0012\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/evilkingmediabeta/radio/uamp/media/BecomingNoisyReceiver;", "Landroid/content/BroadcastReceiver;", "context", "Landroid/content/Context;", "sessionToken", "Landroid/support/v4/media/session/MediaSessionCompat$Token;", "(Landroid/content/Context;Landroid/support/v4/media/session/MediaSessionCompat$Token;)V", "controller", "Landroid/support/v4/media/session/MediaControllerCompat;", "noisyIntentFilter", "Landroid/content/IntentFilter;", "registered", "", "onReceive", "", "intent", "Landroid/content/Intent;", "register", "unregister", "common_debug"})
final class BecomingNoisyReceiver extends android.content.BroadcastReceiver {
    private final android.content.IntentFilter noisyIntentFilter = null;
    private final android.support.v4.media.session.MediaControllerCompat controller = null;
    private boolean registered;
    private final android.content.Context context = null;
    
    public final void register() {
    }
    
    public final void unregister() {
    }
    
    @java.lang.Override()
    public void onReceive(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
    }
    
    public BecomingNoisyReceiver(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.support.v4.media.session.MediaSessionCompat.Token sessionToken) {
        super();
    }
}