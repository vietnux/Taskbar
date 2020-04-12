package com.farmerbb.taskbar.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import com.farmerbb.taskbar.R;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.farmerbb.taskbar.util.SharedPreferenceConstant.SP_KEY_WINDOW_SIZE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricTestRunner.class)
public class SavedWindowSizesTest {
    private static final String CUSTOM_WINDOW_SIZE = "custom-window-size";
    private Context context;
    private SavedWindowSizes savedWindowSizes;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        savedWindowSizes = SavedWindowSizes.getInstance(context);
    }

    @Test
    public void testGetInstance() {
        assertNotNull(savedWindowSizes);
        for (int i = 1; i <= 20; i++) {
            assertEquals(savedWindowSizes, SavedWindowSizes.getInstance(context));
        }
    }

    @Test
    public void testGetWindowSize() {
        String defaultWindowSize = context.getString(R.string.tb_def_window_size);
        assertEquals(
                defaultWindowSize,
                savedWindowSizes.getWindowSize(context, context.getPackageName())
        );
        SharedPreferences prefs = U.getSharedPreferences(context);
        String newWindowSize = defaultWindowSize + "-new";
        prefs.edit().putString(SP_KEY_WINDOW_SIZE, newWindowSize).apply();
        assertEquals(
                newWindowSize,
                savedWindowSizes.getWindowSize(context, context.getPackageName())
        );
        prefs.edit().remove(SP_KEY_WINDOW_SIZE).apply();
        savedWindowSizes.setWindowSize(context, context.getPackageName(), CUSTOM_WINDOW_SIZE);
        assertEquals(
                CUSTOM_WINDOW_SIZE,
                savedWindowSizes.getWindowSize(context, context.getPackageName())
        );
        savedWindowSizes.clear(context);
    }

    @Test
    public void testClear() {
        savedWindowSizes.setWindowSize(context, context.getPackageName(), CUSTOM_WINDOW_SIZE);
        savedWindowSizes.clear(context);
        assertNotEquals(
                CUSTOM_WINDOW_SIZE,
                savedWindowSizes.getWindowSize(context, context.getPackageName())
        );
    }

    @Test
    public void testSerializable() {
        savedWindowSizes.setWindowSize(context, context.getPackageName(), CUSTOM_WINDOW_SIZE);
        SavedWindowSizes newSavedWindowSizes =
                SerializationUtils.deserialize(SerializationUtils.serialize(savedWindowSizes));
        assertEquals(
                CUSTOM_WINDOW_SIZE,
                newSavedWindowSizes.getWindowSize(context, context.getPackageName())
        );
        savedWindowSizes.clear(context);
    }
}
