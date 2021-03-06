package uk.ac.warwick.my.app.services;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Date;

import uk.ac.warwick.my.app.bridge.MyWarwickPreferences;
import uk.ac.warwick.my.app.data.Event;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class EventFetcherTest {
    @Mock
    private SharedPreferences prefs;

    @Mock
    private SharedPreferences features;

    @Mock
    private Context ctx;

    private EventFetcher fetcher;

    @Before
    public void before() {
         fetcher = new EventFetcher(ctx, new MyWarwickPreferences(ctx, prefs, features));
    }

    @Test
    public void buildObjectZulu() throws Exception {
        JSONObject json = new JSONObject()
                .put("id", "1")
                .put("type", "meeting")
                // Normally has millis; just testing that they're optional.
                .put("start", "2017-10-13T12:00:00Z")
                .put("end", "2017-10-13T13:00:00.123Z");
        Event event = fetcher.buildEvent(json);
        assertEquals(Date.from(Instant.parse("2017-10-13T12:00:00.000Z")), event.getStart());
        assertEquals(Date.from(Instant.parse("2017-10-13T13:00:00.123Z")), event.getEnd());
        assertEquals("", event.getTitle());
        assertEquals("1", event.getServerId());
        assertEquals("meeting", event.getType());
    }

    @Test
    public void buildObjectGMT() throws Exception {
        JSONObject json = new JSONObject()
                .put("id", "1")
                .put("type", "meeting")
                // Normally has millis; just testing that they're optional.
                .put("start", "2017-11-13T12:00:00+00:00")
                .put("end", "2017-11-13T13:00:00.123+0000");
        Event event = fetcher.buildEvent(json);
        assertEquals(Date.from(Instant.parse("2017-11-13T12:00:00.000Z")), event.getStart());
        assertEquals(Date.from(Instant.parse("2017-11-13T13:00:00.123Z")), event.getEnd());
        assertEquals("", event.getTitle());
        assertEquals("1", event.getServerId());
        assertEquals("meeting", event.getType());
    }

    @Test
    public void buildObjectBST() throws Exception {
        JSONObject json = new JSONObject()
                .put("id", "1")
                .put("type", "meeting")
                // Normally has millis; just testing that they're optional.
                .put("start", "2017-10-13T13:00:00+01:00")
                .put("end", "2017-10-13T14:00:00.123+0100");
        Event event = fetcher.buildEvent(json);
        assertEquals(Date.from(Instant.parse("2017-10-13T12:00:00.000Z")), event.getStart());
        assertEquals(Date.from(Instant.parse("2017-10-13T13:00:00.123Z")), event.getEnd());
        assertEquals("", event.getTitle());
        assertEquals("1", event.getServerId());
        assertEquals("meeting", event.getType());
    }

    @Test
    public void buildObjectCET() throws Exception {
        JSONObject json = new JSONObject()
                .put("id", "1")
                .put("type", "meeting")
                // Normally has millis; just testing that they're optional.
                .put("start", "2017-12-13T13:00:00+01:00")
                .put("end", "2017-12-13T14:00:00.123+0100");
        Event event = fetcher.buildEvent(json);
        assertEquals(Date.from(Instant.parse("2017-12-13T12:00:00.000Z")), event.getStart());
        assertEquals(Date.from(Instant.parse("2017-12-13T13:00:00.123Z")), event.getEnd());
        assertEquals("", event.getTitle());
        assertEquals("1", event.getServerId());
        assertEquals("meeting", event.getType());
    }
}