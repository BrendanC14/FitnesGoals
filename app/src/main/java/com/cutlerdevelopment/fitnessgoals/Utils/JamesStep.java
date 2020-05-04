package com.cutlerdevelopment.fitnessgoals.Utils;


public class JamesStep {

    public String speechBubbleText;
    public String getSpeechBubbleText() { return speechBubbleText; }

    public boolean previousButtonAvailable;
    public boolean isPreviousButtonAvailable() { return previousButtonAvailable; }

    public boolean doneButtonAvailable;
    public boolean isDoneButtonAvailable() { return doneButtonAvailable; }

    public boolean nextButtonAvailable;
    public boolean isNextButtonAvailable() { return nextButtonAvailable; }

    public JamesStep(String speechBubbleText) {
        this.speechBubbleText = speechBubbleText;
        previousButtonAvailable = true;
        doneButtonAvailable = true;
        nextButtonAvailable = true;
    }

    public JamesStep(String speechBubbleText, boolean doneButtonAvailable) {
        this.speechBubbleText = speechBubbleText;
        previousButtonAvailable = true;
        this.doneButtonAvailable = doneButtonAvailable;
        nextButtonAvailable = true;
    }

    public JamesStep(String speechBubbleText,
                     boolean previousButtonAvailable,
                     boolean doneButtonAvailable,
                     boolean nextButtonAvailable) {
        this.speechBubbleText = speechBubbleText;
        this.previousButtonAvailable = previousButtonAvailable;
        this.doneButtonAvailable = doneButtonAvailable;
        this.nextButtonAvailable = nextButtonAvailable;
    }
}
