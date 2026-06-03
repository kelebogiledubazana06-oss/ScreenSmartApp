# Screen Smart App - Submission Document

## GitHub Repository Link
Paste your GitHub repository link here: `https://github.com/kelebogileubazana06-oss/ScreenSmartApps`

## App Explanation
Screen Smart is a native Android Kotlin application that helps students manage and analyse their screen time over a seven-day period. The app records morning screen time, afternoon screen time and activity notes for each day. It then calculates the weekly total, average screen time, highest usage day, lowest usage day and number of healthy-balance days.

The app contains three screens: a Splash Screen, a Main Screen and a Detailed View Screen. The Splash Screen introduces the app and displays the student name, student number and logo. The Main Screen allows the user to input, validate, clear and load sample screen-time data. The Detailed View Screen displays a professional report for each day and includes weekly insights.

## Data Storage
The app uses parallel arrays:

- `days`
- `morningMinutes`
- `afternoonMinutes`
- `activityNotes`

These are stored in `ScreenTimeRepository.kt` so that all screens can access the same data during the app session.

## Error Handling
The app validates that:

- All fields are completed.
- Morning and afternoon values are whole numbers.
- Values are not negative.
- Session values are realistic and do not exceed 720 minutes.
- Activity notes are meaningful.

When an error occurs, the user receives feedback using a Toast message.

## Pseudocode
START

Display Splash Screen

IF user selects Enter App THEN
    Display Main Screen
ELSE IF user selects Exit THEN
    Close app
END IF

FOR each day in the week
    User enters morning minutes
    User enters afternoon minutes
    User enters activity notes
END FOR

WHEN Save and Analyse is clicked
    FOR each day
        Validate morning minutes
        Validate afternoon minutes
        Validate notes
        IF invalid THEN
            Show error message
            Stop saving
        ELSE
            Store values in parallel arrays
        END IF
    END FOR

    Calculate weekly total
    Calculate average
    Find highest screen-time day
    Find lowest screen-time day
    Count healthy days
    Display summary
END WHEN

WHEN Detailed View is clicked
    FOR each day
        Display morning minutes
        Display afternoon minutes
        Display total
        Display status
        Display notes
    END FOR
END WHEN

END

## Screenshots
Add screenshots of the following after running the app:

1. Splash Screen
2. Main Screen with data entered
3. Main Screen showing calculated summary
4. Detailed View Screen
5. Error handling message

## Notes for Submission
Before submitting, replace the placeholder student name and student number in the Kotlin files with your actual details. Push the complete project folder to GitHub and paste the repository link into this document.
