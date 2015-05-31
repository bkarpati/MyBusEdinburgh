/*
 * Copyright (C) 2014 - 2015 Niall 'Rivernile' Scott
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors or contributors be held liable for
 * any damages arising from the use of this software.
 *
 * The aforementioned copyright holder(s) hereby grant you a
 * non-transferrable right to use this software for any purpose (including
 * commercial applications), and to modify it and redistribute it, subject to
 * the following conditions:
 *
 *  1. This notice may not be removed or altered from any file it appears in.
 *
 *  2. Any modifications made to this software, except those defined in
 *     clause 3 of this agreement, must be released under this license, and
 *     the source code of any modifications must be made available on a
 *     publically accessible (and locateable) website, or sent to the
 *     original author of this software.
 *
 *  3. Software modifications that do not alter the functionality of the
 *     software but are simply adaptations to a specific environment are
 *     exempt from clause 2.
 */

package uk.org.rivernile.android.bustracker.ui.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.test.InstrumentationTestCase;
import android.text.TextUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import uk.org.rivernile.android.bustracker.parser.twitter.Tweet;
import uk.org.rivernile.edinburghbustracker.android.R;

/**
 * Tests for {@link TweetAdapter}.
 * 
 * @author Niall Scott
 */
public class TweetAdapterTests extends InstrumentationTestCase {
    
    private TweetAdapter adapter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        final Context context = getInstrumentation().getTargetContext();
        context.setTheme(R.style.MyBusEdinburgh);
        adapter = new TweetAdapter(context);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        adapter = null;
    }

    /**
     * Test the default state of the adapter.
     */
    public void testDefault() {
        assertTrue(adapter.hasStableIds());
        assertTrue(adapter.isEmpty());
        assertEquals(0, adapter.getItemCount());
        assertEquals(0, adapter.getItemId(0));
        assertNull(adapter.getItem(0));
    }

    /**
     * Test the adapter coping with an empty {@link ArrayList} of {@link Tweet}s.
     */
    public void testNoItems() {
        final DataObserver observer = new DataObserver();
        adapter.registerAdapterDataObserver(observer);
        adapter.setTweets(new ArrayList<Tweet>(1));

        assertTrue(observer.onChangeCalled);
        assertTrue(adapter.isEmpty());
        assertNull(adapter.getItem(0));
    }

    /**
     * Test the adapter coping with a single {@link Tweet}.
     */
    public void testOneItem() {
        final DataObserver observer = new DataObserver();
        adapter.registerAdapterDataObserver(observer);
        assertFalse(observer.onChangeCalled);

        final Tweet tweet = new Tweet("Body", "User",
                new GregorianCalendar(2015, GregorianCalendar.JANUARY, 25, 13, 52, 11).getTime(),
                "a", "b");
        final ArrayList<Tweet> tweets = new ArrayList<>(1);
        tweets.add(tweet);
        adapter.setTweets(tweets);

        assertTrue(observer.onChangeCalled);
        assertFalse(adapter.isEmpty());
        assertEquals(1, adapter.getItemCount());
        assertEquals(tweet, adapter.getItem(0));
        assertNull(adapter.getItem(1));
        assertEquals(tweet.hashCode(), adapter.getItemId(0));
        assertEquals(0, adapter.getItemId(1));
        assertNull(adapter.getItem(-1));

        final TweetAdapter.ViewHolder vh = adapter.createViewHolder(null, 0);
        assertTrue(TextUtils.isEmpty(vh.text1.getText().toString()));
        assertTrue(TextUtils.isEmpty(vh.text2.getText().toString()));

        final DateFormat dateFormat = DateFormat.getDateTimeInstance();
        adapter.bindViewHolder(vh, 0);
        assertEquals("Body", vh.text1.getText().toString());
        assertEquals("User - " + dateFormat.format(tweet.getTime()), vh.text2.getText().toString());

        adapter.bindViewHolder(vh, 1);
        assertTrue(TextUtils.isEmpty(vh.text1.getText().toString()));
        assertTrue(TextUtils.isEmpty(vh.text2.getText().toString()));
    }

    /**
     * Test the adapter coping with multiple {@link Tweet}s.
     */
    public void testMultipleItems() {
        final DataObserver observer = new DataObserver();
        adapter.registerAdapterDataObserver(observer);
        assertFalse(observer.onChangeCalled);

        final Tweet tweet1 = new Tweet("Body", "User",
                new GregorianCalendar(2015, GregorianCalendar.JANUARY, 25, 13, 52, 11).getTime(),
                "a", "b");
        final Tweet tweet2 = new Tweet("Body 2", "User 2",
                new GregorianCalendar(2014, GregorianCalendar.DECEMBER, 2, 2, 13, 45).getTime(),
                "c", "d");
        final Tweet tweet3 = new Tweet("Body 3", "User 3",
                new GregorianCalendar(2014, GregorianCalendar.OCTOBER, 10, 21, 22, 23).getTime(),
                "e", "f");
        final ArrayList<Tweet> tweets = new ArrayList<>(3);
        tweets.add(tweet1);
        tweets.add(tweet2);
        tweets.add(tweet3);
        adapter.setTweets(tweets);

        assertTrue(observer.onChangeCalled);
        assertFalse(adapter.isEmpty());
        assertEquals(3, adapter.getItemCount());
        assertEquals(tweet1, adapter.getItem(0));
        assertEquals(tweet2, adapter.getItem(1));
        assertEquals(tweet3, adapter.getItem(2));
        assertNull(adapter.getItem(3));
        assertEquals(tweet1.hashCode(), adapter.getItemId(0));
        assertEquals(tweet2.hashCode(), adapter.getItemId(1));
        assertEquals(tweet3.hashCode(), adapter.getItemId(2));
        assertEquals(0, adapter.getItemId(3));

        final DateFormat dateFormat = DateFormat.getDateTimeInstance();

        final TweetAdapter.ViewHolder vh1 = adapter.createViewHolder(null, 0);
        assertTrue(TextUtils.isEmpty(vh1.text1.getText().toString()));
        assertTrue(TextUtils.isEmpty(vh1.text2.getText().toString()));

        adapter.bindViewHolder(vh1, 0);
        assertEquals("Body", vh1.text1.getText().toString());
        assertEquals("User - " + dateFormat.format(tweet1.getTime()),
                vh1.text2.getText().toString());

        final TweetAdapter.ViewHolder vh2 = adapter.createViewHolder(null, 0);
        assertTrue(TextUtils.isEmpty(vh2.text1.getText().toString()));
        assertTrue(TextUtils.isEmpty(vh2.text2.getText().toString()));

        adapter.bindViewHolder(vh2, 1);
        assertEquals("Body 2", vh2.text1.getText().toString());
        assertEquals("User 2 - " + dateFormat.format(tweet2.getTime()),
                vh2.text2.getText().toString());

        adapter.bindViewHolder(vh1, 2);
        assertEquals("Body 3", vh1.text1.getText().toString());
        assertEquals("User 3 - " + dateFormat.format(tweet3.getTime()),
                vh1.text2.getText().toString());
    }

    /**
     * This is used to record when callback methods are called to ensure that the adapter calls its
     * data change notifier.
     */
    private static class DataObserver extends RecyclerView.AdapterDataObserver {

        boolean onChangeCalled;
        boolean onItemRangeChangeCalled;
        boolean onItemRangeInsertedCalled;
        boolean onItemRangeRemovedCalled;
        boolean onItemRangeMovedCalled;

        @Override
        public void onChanged() {
            onChangeCalled = true;
        }

        @Override
        public void onItemRangeChanged(final int positionStart, final int itemCount) {
            onItemRangeChangeCalled = true;
        }

        @Override
        public void onItemRangeInserted(final int positionStart, final int itemCount) {
            onItemRangeInsertedCalled = true;
        }

        @Override
        public void onItemRangeRemoved(final int positionStart, final int itemCount) {
            onItemRangeRemovedCalled = true;
        }

        @Override
        public void onItemRangeMoved(final int fromPosition, final int toPosition,
                                     final int itemCount) {
            onItemRangeMovedCalled = true;
        }
    }
}