package com.cutlerdevelopment.fitnessgoals.Activities;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.cutlerdevelopment.fitnessgoals.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SetupTest {

    @Rule
    public ActivityTestRule<FirstMenu> mActivityTestRule = new ActivityTestRule<>(FirstMenu.class);

    @Test
    public void setupTest() {
        ViewInteraction constraintLayout = onView(
                allOf(withId(R.id.firstMenuBackground),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout.perform(click());

        ViewInteraction constraintLayout2 = onView(
                allOf(withId(R.id.firstMenuBackground),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout2.perform(click());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.firstMenuAppList),
                        childAtPosition(
                                withId(R.id.firstMenuAppOptions),
                                0)))
                .atPosition(0);
        linearLayout.perform(click());

        ViewInteraction constraintLayout3 = onView(
                allOf(withId(R.id.firstMenuBackground),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout3.perform(click());

        ViewInteraction constraintLayout4 = onView(
                allOf(withId(R.id.firstMenuBackground),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout4.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.firstMenuWhatIsTeamMode), withText("What is Team Mode?"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuTeamMode),
                                        childAtPosition(
                                                withId(R.id.firstMenuPlayerModeMenu),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.firstMenuWhatIsPlayerMode), withText("What is Player Mode?"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuPlayerMode),
                                        childAtPosition(
                                                withId(R.id.firstMenuPlayerModeMenu),
                                                1)),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.firstMenuSelectTeamMode), withText("Select Team"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuTeamMode),
                                        childAtPosition(
                                                withId(R.id.firstMenuPlayerModeMenu),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction constraintLayout5 = onView(
                allOf(withId(R.id.firstMenuBackground),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout5.perform(click());

        ViewInteraction textInputEditText = onView(
                                withId(R.id.firstMenuTeamPlayerNameEditText));
        textInputEditText.perform(replaceText("Poole FC"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.firstMenuTeamPlayerNameConfirmButton), withText("Confirm"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuTeamPlayerNameLayout),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                4)),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());

        DataInteraction constraintLayout6 = onData(anything())
                .inAdapterView(allOf(withId(R.id.firstMenuColourGrid),
                        childAtPosition(
                                withId(R.id.firstMenuTeamColourLayout),
                                0)))
                .atPosition(11);
        constraintLayout6.perform(click());

        ViewInteraction constraintLayout7 = onView(
                allOf(withId(R.id.firstMenuBackground),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout7.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.firstMenuWhatIsTargetMode), withText("What is Targeted Mode?"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuTargetMode),
                                        childAtPosition(
                                                withId(R.id.firstMenuTargetModeLayout),
                                                0)),
                                0),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.firstMenuWhatIsScaledMode), withText("What is Scaled Mode?"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuScaledMode),
                                        childAtPosition(
                                                withId(R.id.firstMenuTargetModeLayout),
                                                1)),
                                0),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.firstMenuSelectScaledMode), withText("Select Scaled"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuScaledMode),
                                        childAtPosition(
                                                withId(R.id.firstMenuTargetModeLayout),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction constraintLayout8 = onView(
                allOf(withId(R.id.firstMenuBackground),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout8.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.firstMenuStepTargetSpinner),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuStepTargetLayout),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                8)),
                                1),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(7);
        materialTextView.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.firstMenuStepTargetConfirmButton), withText("Confirm"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuStepTargetLayout),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                8)),
                                0),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction constraintLayout9 = onView(
                allOf(withId(R.id.firstMenuBackground),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout9.perform(click());

        ViewInteraction constraintLayout10 = onView(
                allOf(withId(R.id.firstMenuBackground),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(R.id.action_bar_root),
                                                1)),
                                0),
                        isDisplayed()));
        constraintLayout10.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.firstMenuDaysBetweenSpinner),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuDaysBetweenLayout),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                9)),
                                1),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        DataInteraction materialTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        materialTextView2.perform(click());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.firstMenuDaysBetweenConfirmButton), withText("Confirm"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuDaysBetweenLayout),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                9)),
                                0),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.firstMenuReadyButton), withText("Ready!"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuSpeechBubble),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton10.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
