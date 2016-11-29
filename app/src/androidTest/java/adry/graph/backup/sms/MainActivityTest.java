package adry.graph.backup.sms;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Audrey on 16/11/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    /** Launches {@link MainActivity} for every test */
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testFileCreatedAfterSaveButtonClicked() {
        MainActivity activity = mActivityRule.getActivity();
        assertFalse (activity.isWriteSmsFileThreadAlive());
        onView(ViewMatchers.withId(R.id.activity_main_loader_pb)).check(ViewAssertions.matches(not(isDisplayed())));
        onView(ViewMatchers.withId(R.id.activity_main_save_b)).perform(click());
        onView(ViewMatchers.withId(R.id.activity_main_loader_pb)).check(ViewAssertions.matches(isDisplayed()));
    }
}