package com.dto;

public class CourseDetails {
    private String courseName;
    private String coordinatorName;
    private long internsInProgress;
    private long internsCompleted;
    private long internsDroppedOut;
    private long internsFailed;

    public CourseDetails(String courseName, String coordinatorName,
                         long internsInProgress, long internsCompleted, long internsDroppedOut, long internsFailed) {
        this.courseName = courseName;
        this.coordinatorName = coordinatorName;
        this.internsInProgress = internsInProgress;
        this.internsCompleted = internsCompleted;
        this.internsDroppedOut = internsDroppedOut;
        this.internsFailed = internsFailed;
    }

    // Getters e Setters
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCoordinatorName() { return coordinatorName; }
    public void setCoordinatorName(String coordinatorName) { this.coordinatorName = coordinatorName; }

    public long getInternsInProgress() { return internsInProgress; }
    public void setInternsInProgress(long internsInProgress) { this.internsInProgress = internsInProgress; }

    public long getInternsCompleted() { return internsCompleted; }
    public void setInternsCompleted(long internsCompleted) { this.internsCompleted = internsCompleted; }

    public long getInternsDroppedOut() { return internsDroppedOut; }
    public void setInternsDroppedOut(long internsDroppedOut) { this.internsDroppedOut = internsDroppedOut; }

    public long getInternsFailed() { return internsFailed; }
    public void setInternsFailed(long internsFailed) { this.internsFailed = internsFailed; }
}
