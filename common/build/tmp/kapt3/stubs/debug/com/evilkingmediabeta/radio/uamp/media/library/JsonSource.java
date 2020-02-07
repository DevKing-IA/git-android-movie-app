package com.evilkingmediabeta.radio.uamp.media.library;

import java.lang.System;

/**
 * Source of [MediaMetadataCompat] objects created from a basic JSON stream.
 *
 * The definition of the JSON is specified in the docs of [JsonMusic] in this file,
 * which is the object representation of it.
 */
@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0005H\u0002J\u000f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0010H\u0096\u0002J\u0011\u0010\u0011\u001a\u00020\u0012H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J!\u0010\u0014\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\b2\u0006\u0010\u000e\u001a\u00020\u0005H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0016"}, d2 = {"Lcom/evilkingmediabeta/radio/uamp/media/library/JsonSource;", "Lcom/evilkingmediabeta/radio/uamp/media/library/AbstractMusicSource;", "context", "Landroid/content/Context;", "source", "Landroid/net/Uri;", "(Landroid/content/Context;Landroid/net/Uri;)V", "catalog", "", "Landroid/support/v4/media/MediaMetadataCompat;", "glide", "Lcom/bumptech/glide/RequestManager;", "downloadJson", "Lcom/evilkingmediabeta/radio/uamp/media/library/JsonCatalog;", "catalogUri", "iterator", "", "load", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCatalog", "(Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "common_debug"})
public final class JsonSource extends com.evilkingmediabeta.radio.uamp.media.library.AbstractMusicSource {
    private java.util.List<android.support.v4.media.MediaMetadataCompat> catalog;
    private final com.bumptech.glide.RequestManager glide = null;
    private final android.net.Uri source = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.Iterator<android.support.v4.media.MediaMetadataCompat> iterator() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public java.lang.Object load(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> p0) {
        return null;
    }
    
    /**
     * Attempts to download a catalog from a given Uri.
     *
     * @param catalogUri URI to attempt to download the catalog form.
     * @return The catalog downloaded, or an empty catalog if an error occurred.
     */
    private final com.evilkingmediabeta.radio.uamp.media.library.JsonCatalog downloadJson(android.net.Uri catalogUri) throws java.io.IOException {
        return null;
    }
    
    public JsonSource(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.net.Uri source) {
        super();
    }
}