package com.evilkingmediabeta.radio.uamp.media.library;

import java.lang.System;

/**
 * Represents a tree of media that's used by [MusicService.onLoadChildren].
 *
 * [BrowseTree] maps a media id (see: [MediaMetadataCompat.METADATA_KEY_MEDIA_ID]) to one (or
 * more) [MediaMetadataCompat] objects, which are children of that media id.
 *
 * For example, given the following conceptual tree:
 * root
 * +-- Albums
 * |    +-- Album_A
 * |    |    +-- Song_1
 * |    |    +-- Song_2
 * ...
 * +-- Artists
 * ...
 *
 * Requesting `browseTree["root"]` would return a list that included "Albums", "Artists", and
 * any other direct children. Taking the media ID of "Albums" ("Albums" in this example),
 * `browseTree["Albums"]` would return a single item list "Album_A", and, finally,
 * `browseTree["Album_A"]` would return "Song_1" and "Song_2". Since those are leaf nodes,
 * requesting `browseTree["Song_1"]` would return null (there aren't any children of it).
 */
@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u0011\u001a\u00020\u000bH\u0002J\u0019\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n2\u0006\u0010\u0013\u001a\u00020\tH\u0086\u0002R \u0010\u0007\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\rX\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0014"}, d2 = {"Lcom/evilkingmediabeta/radio/uamp/media/library/BrowseTree;", "", "context", "Landroid/content/Context;", "musicSource", "Lcom/evilkingmediabeta/radio/uamp/media/library/MusicSource;", "(Landroid/content/Context;Lcom/evilkingmediabeta/radio/uamp/media/library/MusicSource;)V", "mediaIdToChildren", "", "", "", "Landroid/support/v4/media/MediaMetadataCompat;", "searchableByUnknownCaller", "", "getSearchableByUnknownCaller", "()Z", "buildAlbumRoot", "mediaItem", "get", "mediaId", "common_debug"})
public final class BrowseTree {
    private final java.util.Map<java.lang.String, java.util.List<android.support.v4.media.MediaMetadataCompat>> mediaIdToChildren = null;
    
    /**
     * Whether to allow clients which are unknown (non-whitelisted) to use search on this
     * [BrowseTree].
     */
    private final boolean searchableByUnknownCaller = true;
    
    public final boolean getSearchableByUnknownCaller() {
        return false;
    }
    
    /**
     * Provide access to the list of children with the `get` operator.
     * i.e.: `browseTree\[UAMP_BROWSABLE_ROOT\]`
     */
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<android.support.v4.media.MediaMetadataCompat> get(@org.jetbrains.annotations.NotNull()
    java.lang.String mediaId) {
        return null;
    }
    
    /**
     * Builds a node, under the root, that represents an album, given
     * a [MediaMetadataCompat] object that's one of the songs on that album,
     * marking the item as [MediaItem.FLAG_BROWSABLE], since it will have child
     * node(s) AKA at least 1 song.
     */
    private final java.util.List<android.support.v4.media.MediaMetadataCompat> buildAlbumRoot(android.support.v4.media.MediaMetadataCompat mediaItem) {
        return null;
    }
    
    public BrowseTree(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.evilkingmediabeta.radio.uamp.media.library.MusicSource musicSource) {
        super();
    }
}