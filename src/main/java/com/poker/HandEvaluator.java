package com.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Evaluates best 5-card poker hands from 7 cards (2 hole + 5 community).
 * Uses hand rank + tie-breaker values for comparison.
 */
public class HandEvaluator {

    /** Returns index of winning player, or -1 if none. Ties go to first active player. */
    public static int showdownWinner(ArrayList<Player> playerList, standardCard[] communityCards, int[] playersTotalBets) {
        if (communityCards == null || communityCards.length < 5) return -1;
        long bestScore = -1;
        int winnerIdx = -1;
        for (int i = 0; i < playerList.size(); i++) {
            if (!playerList.get(i).getIsInGame()) continue;
            standardCard[] hole = playerList.get(i).getHoleCards();
            if (hole == null || hole.length < 2 || hole[0] == null || hole[1] == null) continue;
            long score = scoreBestFive(hole[0], hole[1], communityCards);
            if (score > bestScore) {
                bestScore = score;
                winnerIdx = i;
            }
        }
        return winnerIdx;
    }

    /** Best 5-card hand score (higher = better). Encodes hand rank and tie-breakers. */
    public static long scoreBestFive(standardCard c1, standardCard c2, standardCard[] community) {
        standardCard[] all = new standardCard[7];
        all[0] = c1;
        all[1] = c2;
        for (int i = 0; i < 5; i++) all[2 + i] = community[i];
        long best = -1;
        for (int i = 0; i < 7; i++)
            for (int j = i + 1; j < 7; j++)
                for (int k = j + 1; k < 7; k++)
                    for (int l = k + 1; l < 7; l++)
                        for (int m = l + 1; m < 7; m++) {
                            standardCard[] five = { all[i], all[j], all[k], all[l], all[m] };
                            long s = scoreFive(five);
                            if (s > best) best = s;
                        }
        return best;
    }

    /** Score a 5-card hand: high 8 bits = hand rank (9=straight flush down to 0=high card), rest = tie-breakers. */
    private static long scoreFive(standardCard[] five) {
        if (five.length != 5) return 0;
        int[] values = new int[5];
        int[] suits = new int[5];
        for (int i = 0; i < 5; i++) {
            values[i] = five[i].value;
            String su = five[i].suit;
            if ("Spades".equals(su)) suits[i] = 0;
            else if ("Hearts".equals(su)) suits[i] = 1;
            else if ("Diamonds".equals(su)) suits[i] = 2;
            else suits[i] = 3;
        }
        Arrays.sort(values);
        boolean flush = suits[0] == suits[1] && suits[1] == suits[2] && suits[2] == suits[3] && suits[3] == suits[4];
        boolean straight = isStraight(values);
        if (flush && straight) {
            int high = straightHigh(values);
            return (9L << 52) + (high << 48);
        }
        int[] counts = valueCounts(values);
        if (counts[0] == 4) {
            long quad = counts[1];
            long kicker = counts[2];
            return (8L << 52) + (quad << 48) + (kicker << 44);
        }
        if (counts[0] == 3 && counts[3] == 2) {
            long trip = counts[1], pair = counts[2];
            return (7L << 52) + (trip << 48) + (pair << 44);
        }
        if (flush) {
            long f = 0;
            for (int i = 4; i >= 0; i--) f = f * 16 + values[i];
            return (6L << 52) + f;
        }
        if (straight) {
            int high = straightHigh(values);
            return (5L << 52) + (high << 48);
        }
        if (counts[0] == 3) {
            long trip = counts[1], k1 = counts[2], k2 = counts[3];
            return (4L << 52) + (trip << 48) + (k1 << 44) + (k2 << 40);
        }
        if (counts[0] == 2 && counts[2] == 2) {
            long p1 = counts[1], p2 = counts[2], kick = counts[4];
            return (3L << 52) + (p1 << 48) + (p2 << 44) + (kick << 40);
        }
        if (counts[0] == 2) {
            long pair = counts[1], k1 = counts[2], k2 = counts[3], k3 = counts[4];
            return (2L << 52) + (pair << 48) + (k1 << 44) + (k2 << 40) + (k3 << 36);
        }
        long high = 0;
        for (int i = 4; i >= 0; i--) high = high * 16 + values[i];
        return (1L << 52) + high;
    }

    private static boolean isStraight(int[] v) {
        int[] a = v.clone();
        Arrays.sort(a);
        boolean normal = a[1] == a[0] + 1 && a[2] == a[1] + 1 && a[3] == a[2] + 1 && a[4] == a[3] + 1;
        boolean wheel = a[0] == 2 && a[1] == 3 && a[2] == 4 && a[3] == 5 && a[4] == 14;
        return normal || wheel;
    }

    private static int straightHigh(int[] v) {
        int[] a = v.clone();
        Arrays.sort(a);
        if (a[0] == 2 && a[4] == 14) return 5;
        return a[4];
    }

    /** counts: [0]=max count, [1]=value of that count, [2],[3],[4]=other values for tie-break (desc). */
    private static int[] valueCounts(int[] values) {
        int[] copy = values.clone();
        Arrays.sort(copy);
        List<int[]> groups = new ArrayList<>();
        int i = 0;
        while (i < 5) {
            int j = i;
            while (j < 5 && copy[j] == copy[i]) j++;
            groups.add(new int[] { copy[i], j - i });
            i = j;
        }
        groups.sort((a, b) -> {
            int c = Integer.compare(b[1], a[1]);
            if (c != 0) return c;
            return Integer.compare(b[0], a[0]);
        });
        int[] out = new int[5];
        out[0] = groups.get(0)[1];
        out[1] = groups.get(0)[0];
        List<Integer> rest = new ArrayList<>();
        for (int g = 1; g < groups.size(); g++)
            for (int k = 0; k < groups.get(g)[1]; k++)
                rest.add(groups.get(g)[0]);
        rest.sort(Comparator.reverseOrder());
        for (int r = 0; r < 3 && r < rest.size(); r++) out[2 + r] = rest.get(r);
        return out;
    }
}
