package com.henheang.saasolv.ex09_ulid;

import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Mirrors saas-olv: egovframework.com.cmm.service.IdGenService#generateUlid()
 *
 * A ULID = 128 bits = 48-bit millisecond timestamp + 80-bit randomness,
 * rendered as 26 Crockford-Base32 chars:
 *
 *   01J9Z3F8K2 9D4M7QABCDEFGH
 *   └ 10 chars ┘└── 16 chars ──┘
 *    timestamp      randomness
 *
 * Why ULID over UUID?
 *   - It is SORTABLE BY TIME — because the timestamp is the high bits, sorting
 *     ULIDs as plain strings sorts them by creation order. (UUID v4 is random,
 *     so it sorts into noise — bad for DB index locality.)
 *   - It is still globally unique (80 random bits per millisecond).
 *
 * Three Java concepts to study here:
 *   1. SecureRandom    — cryptographic randomness (not Math.random / Random)
 *   2. ReentrantLock   — make generation thread-safe AND monotonic
 *   3. Bit manipulation — packing timestamp+random into 5-bit Base32 symbols
 */
public final class UlidGenerator {

    /** Crockford Base32 alphabet: no I, L, O, U (avoids look-alike confusion). */
    private static final char[] ENCODING = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();

    private final SecureRandom random = new SecureRandom();
    private final ReentrantLock lock = new ReentrantLock();

    // shared state guarded by the lock — needed for monotonicity within a millisecond
    private long lastTimestamp = -1L;
    private final byte[] lastRandom = new byte[10]; // 10 bytes = 80 bits

    /**
     * Generate the next ULID. Thread-safe and monotonic: if two calls land in the
     * same millisecond, the random part is incremented (not regenerated) so the
     * result still sorts after the previous one.
     */
    public String next() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            if (now <= lastTimestamp) {
                incrementRandom();          // same (or clock went back) ms -> bump
            } else {
                lastTimestamp = now;
                random.nextBytes(lastRandom); // new ms -> fresh randomness
            }
            return encode(lastTimestamp, lastRandom);
        } finally {
            lock.unlock();
        }
    }

    /** Add 1 to the 80-bit random value (big-endian), carrying as needed. */
    private void incrementRandom() {
        for (int i = lastRandom.length - 1; i >= 0; i--) {
            int v = (lastRandom[i] & 0xFF) + 1;
            lastRandom[i] = (byte) (v & 0xFF);
            if (v <= 0xFF) {
                return;             // no carry -> done
            }
        }
        random.nextBytes(lastRandom); // overflowed all 80 bits (astronomically rare)
    }

    /** 48-bit timestamp -> first 10 chars (5 bits each = 50 bits, top 2 are 0). */
    private static String encode(long timestamp, byte[] random) {
        char[] out = new char[26];

        for (int i = 9; i >= 0; i--) {
            out[i] = ENCODING[(int) (timestamp & 0x1F)]; // take low 5 bits
            timestamp >>>= 5;                            // shift to next 5 bits
        }

        // 80 random bits -> 16 chars. Pack the 10 bytes into 5-bit groups.
        encodeRandom(random, out);
        return new String(out);
    }

    private static void encodeRandom(byte[] r, char[] out) {
        out[10] = ENCODING[(r[0] & 0xFF) >>> 3];
        out[11] = ENCODING[((r[0] & 0x07) << 2) | ((r[1] & 0xFF) >>> 6)];
        out[12] = ENCODING[(r[1] & 0x3E) >>> 1];
        out[13] = ENCODING[((r[1] & 0x01) << 4) | ((r[2] & 0xFF) >>> 4)];
        out[14] = ENCODING[((r[2] & 0x0F) << 1) | ((r[3] & 0xFF) >>> 7)];
        out[15] = ENCODING[(r[3] & 0x7C) >>> 2];
        out[16] = ENCODING[((r[3] & 0x03) << 3) | ((r[4] & 0xFF) >>> 5)];
        out[17] = ENCODING[r[4] & 0x1F];
        out[18] = ENCODING[(r[5] & 0xFF) >>> 3];
        out[19] = ENCODING[((r[5] & 0x07) << 2) | ((r[6] & 0xFF) >>> 6)];
        out[20] = ENCODING[(r[6] & 0x3E) >>> 1];
        out[21] = ENCODING[((r[6] & 0x01) << 4) | ((r[7] & 0xFF) >>> 4)];
        out[22] = ENCODING[((r[7] & 0x0F) << 1) | ((r[8] & 0xFF) >>> 7)];
        out[23] = ENCODING[(r[8] & 0x7C) >>> 2];
        out[24] = ENCODING[((r[8] & 0x03) << 3) | ((r[9] & 0xFF) >>> 5)];
        out[25] = ENCODING[r[9] & 0x1F];
    }

    /**
     * Decode the millisecond timestamp back out of a ULID's first 10 chars.
     * (Not in the saas-olv original — added so the demo can PROVE time-ordering.)
     */
    public static long decodeTimestamp(String ulid) {
        long ts = 0L;
        for (int i = 0; i < 10; i++) {
            ts = (ts << 5) | indexOf(ulid.charAt(i));
        }
        return ts;
    }

    private static int indexOf(char c) {
        for (int i = 0; i < ENCODING.length; i++) {
            if (ENCODING[i] == c) return i;
        }
        throw new IllegalArgumentException("Not a Crockford Base32 char: " + c);
    }
}
