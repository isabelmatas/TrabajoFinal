package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import notion.api.v1.NotionClient;
import notion.api.v1.http.OkHttp5Client;
import notion.api.v1.logging.Slf4jLogger;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageParent;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.model.pages.PageProperty.RichText;
import notion.api.v1.model.pages.PageProperty.RichText.Text;
import notion.api.v1.request.databases.QueryDatabaseRequest;
import notion.api.v1.request.pages.CreatePageRequest;

public class NotionRepository implements IRepository
{
    private final NotionClient client;
    private final String databaseId;
    private final String titleColumnName = "identifier";

    public NotionRepository(String apiToken, String databaseId)
    {
        this.client = new NotionClient(apiToken);
        client.setHttpClient(new OkHttp5Client(60000, 60000, 60000));
        client.setLogger(new Slf4jLogger());
        this.databaseId = databaseId;
    }

    public Task addTask(Task tarea) throws RepositoryException
    {
        try
        {
            Map<String, PageProperty> properties = Map.of(
                "identifier", createTitleProperty(String.valueOf(tarea.getIdentifier())),
                "title", createRichTextProperty(tarea.getTitle()),
                "date", createDateProperty(tarea.getDate().toString()),
                "content", createRichTextProperty(tarea.getContent()),
                "priority", createNumberProperty(tarea.getPriority()),
                "estimatedDuration", createNumberProperty(tarea.getEstimatedDuration()),
                "completed", createCheckboxProperty(tarea.getCompleted())
            );
            PageParent parent = PageParent.database(databaseId);
            CreatePageRequest request = new CreatePageRequest(parent, properties);
            Page response = client.createPage(request);
            return tarea;
        }
        catch(Exception e)
        {
            throw new RepositoryException("Error al agregar la tarea a Notion");
        }
    }

    private PageProperty createTitleProperty(String title)
    {
        RichText idText = new RichText();
        idText.setText(new Text(title));
        PageProperty idProperty = new PageProperty();
        idProperty.setTitle(Collections.singletonList(idText));
        return idProperty;
    }

    public ArrayList<Task> getAllTask() throws RepositoryException
    {
        ArrayList<Task> tareas = new ArrayList<>();
        try
        {
            QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(databaseId);
            QueryResults queryResults = client.queryDatabase(queryRequest);
            for(Page page : queryResults.getResults())
            {
                Map<String, PageProperty> properties =page.getProperties();
                Task tarea = mapPageToTask(page.getId(), properties);
                if(tarea != null)
                {
                    tareas.add(tarea);
                }
                
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return tareas;
    }
}