package com.cutlerdevelopment.fitnessgoals.Activities;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.cutlerdevelopment.fitnessgoals.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FullSeasonTesy {

    @Rule
    public ActivityTestRule<FirstMenu> mActivityTestRule = new ActivityTestRule<>(FirstMenu.class);

    @Test
    public void fullSeasonTesy() throws InterruptedException {
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
                allOf(withId(R.id.firstMenuSelectTeamMode), withText("Select Team"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuTeamMode),
                                        childAtPosition(
                                                withId(R.id.firstMenuPlayerModeMenu),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

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

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.firstMenuTeamPlayerNameConfirmButton), withText("Confirm"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuTeamPlayerNameLayout),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                4)),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

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

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.firstMenuSelectScaledMode), withText("Select Scaled"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuScaledMode),
                                        childAtPosition(
                                                withId(R.id.firstMenuTargetModeLayout),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

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
                .atPosition(9);
        materialTextView.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.firstMenuStepTargetConfirmButton), withText("Confirm"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuStepTargetLayout),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                8)),
                                0),
                        isDisplayed()));
        materialButton4.perform(click());

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

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.firstMenuDaysBetweenConfirmButton), withText("Confirm"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuDaysBetweenLayout),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                9)),
                                0),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.firstMenuReadyButton), withText("Ready!"),
                        childAtPosition(
                                allOf(withId(R.id.firstMenuSpeechBubble),
                                        childAtPosition(
                                                withId(R.id.firstMenuBackground),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton6.perform(click());
        Thread.sleep(10000);


        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.nextMatchCardPlayMatchButton), withText("Play Match"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.matchSetupAttackButton), withText("Attacking"),
                        childAtPosition(
                                allOf(withId(R.id.matchSetupButtonMenu),
                                        childAtPosition(
                                                withId(R.id.matchSetupLayout),
                                                4)),
                                2),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.matchSetupPlayMatchButton), withText("Play Match"),
                        childAtPosition(
                                allOf(withId(R.id.matchSetupLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                5),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.matchResultDoneButton), withText("Done"),
                        childAtPosition(
                                allOf(withId(R.id.matchResultLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                7),
                        isDisplayed()));
        materialButton10.perform(click());

        for (int i = 0; i < 45; i++) {
            materialButton7.perform(click());
            materialButton8.perform(click());
            materialButton9.perform(click());
            materialButton10.perform(click());

        }


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
