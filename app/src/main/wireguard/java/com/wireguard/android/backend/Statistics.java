/*
 * Copyright © 2017-2025 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wireguard.android.backend;

import android.os.SystemClock;

import com.wireguard.crypto.Key;
import com.wireguard.util.NonNullForAll;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;

/**
 * Class representing transfer statistics for a {@link Tunnel} instance.
 */
@NonNullForAll
public class Statistics {
    public static final class PeerStats {
        public final long rxBytes;
        public final long txBytes;
        public final long latestHandshakeEpochMillis;

        public PeerStats(long rxBytes, long txBytes, long latestHandshakeEpochMillis) {
            this.rxBytes = rxBytes;
            this.txBytes = txBytes;
            this.latestHandshakeEpochMillis = latestHandshakeEpochMillis;
        }
    }

    private final Map<Key, PeerStats> stats = new HashMap<>();
    private long lastTouched = SystemClock.elapsedRealtime();

    Statistics() {}

    void add(final Key key, final long rxBytes, final long txBytes, final long latestHandshake) {
        stats.put(key, new PeerStats(rxBytes, txBytes, latestHandshake));
        lastTouched = SystemClock.elapsedRealtime();
    }

    public boolean isStale() {
        return SystemClock.elapsedRealtime() - lastTouched > 900;
    }

    @Nullable
    public PeerStats peer(final Key peer) {
        return stats.get(peer);
    }

    public Key[] peers() {
        return stats.keySet().toArray(new Key[0]);
    }
    public long totalRx() {
        long rx = 0;
        for (final PeerStats val : stats.values()) {
            rx += val.rxBytes;
        }
        return rx;
    }

    public long totalTx() {
        long tx = 0;
        for (final PeerStats val : stats.values()) {
            tx += val.txBytes;
        }
        return tx;
    }
}