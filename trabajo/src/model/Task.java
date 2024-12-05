package model;
import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int identifier;
    private String title;
    private Date date;
    private String content;
    private int priority;
    private int estimatedDuration;
    private boolean completed;
    
    public Task(int identifier, String title, Date date, String content, int priority, int estimatedDuration, boolean completed)
    {
        this.identifier = identifier;
        this.title = title;
        this.date = date;
        this.content = content;
        this.priority = priority;
        this.estimatedDuration = estimatedDuration;
        this.completed = completed;
    }

    public void setIdentifier(int identifier) 
    {
        this.identifier = identifier;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public void setPriority(int priority) 
    {
        this.priority = priority;
    }

    public void setEstimatedDuration(int estimatedDuration) 
    {
        this.estimatedDuration = estimatedDuration;
    }

    public void setCompleted(boolean completed) 
    {
        this.completed = completed;
    }

    public int getIdentifier() 
    {
        return identifier;
    }

    public String getTitle() 
    {
        return title;
    }

    public Date getDate() 
    {
        return date;
    }

    public String getContent() 
    {
        return content;
    }
    public int getPriority() 
    {
        return priority;
    }

    public int getEstimatedDuration()
    {
        return estimatedDuration;
    }
    
    public boolean getCompleted()
    {
        return completed;
    }

    @Override
    public String toString()
    {
        return "Tarea [identifer: " + identifier + ", title: " + title + ", date: " + date + ", content: " + content + ", priority: " + priority
        + ", estimatedDuration: " + estimatedDuration + ", completed: " + completed + "]";
    }
}