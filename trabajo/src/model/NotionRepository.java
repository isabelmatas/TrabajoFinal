package model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import notion.api.v1.request.pages.UpdatePageRequest;

public class NotionRepository implements IRepository
{
    private final NotionClient client;
    private final String databaseId;
    private final String titleColumnName = "identifier";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private IExporter exporter;

    public NotionRepository(String apiToken, String databaseId)
    {
        this.client = new NotionClient(apiToken);
        client.setHttpClient(new OkHttp5Client(60000, 60000, 60000));
        client.setLogger(new Slf4jLogger());
        this.databaseId = databaseId;
    }

    @Override
    public Task addTask(Task tarea) throws RepositoryException
    {
        try
        {
            String id = findPageIdByIdentifier(tarea.getIdentifier());
            if(id != null)
            {
                throw new RepositoryException("Ya existe una tarea con ese id");
            }
            Map<String, PageProperty> properties = Map.of(
                "identifier", createTitleProperty(String.valueOf(tarea.getIdentifier())),
                "taskTitle", createRichTextProperty(tarea.getTitle()),
                "date", createDateProperty(dateFormat.format(tarea.getDate())),
                "content", createRichTextProperty(tarea.getContent()),
                "priority", createNumberProperty(tarea.getPriority()),
                "estimatedDuration", createNumberProperty(tarea.getEstimatedDuration()),
                "completed", createCheckboxProperty(tarea.getCompleted())
            );
            PageParent parent = PageParent.database(databaseId);
            CreatePageRequest request = new CreatePageRequest(parent, properties);
            client.createPage(request);
            return tarea;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RepositoryException("Error al agregar la tarea a Notion: " + e.getMessage());
        }
    }

    private PageProperty createTitleProperty(String identifier)
    {
        RichText idText = new RichText();
        idText.setText(new Text(identifier));
        PageProperty idProperty = new PageProperty();
        idProperty.setTitle(Collections.singletonList(idText));
        return idProperty;
    }

    private PageProperty createRichTextProperty(String text)
    {
        RichText richText = new RichText();
        richText.setText(new Text(text));
        PageProperty property = new PageProperty();
        property.setRichText(Collections.singletonList(richText));
        return property;
    }

    private PageProperty createNumberProperty(int number)
    {
        PageProperty property = new PageProperty();
        property.setNumber(number);
        return property;
    }

    private PageProperty createDateProperty(String date)
    {
        PageProperty property = new PageProperty();
        PageProperty.Date dateProperty = new PageProperty.Date();
        dateProperty.setStart(date);
        property.setDate(dateProperty);
        return property;
    }

    private PageProperty createCheckboxProperty(boolean checked)
    {
        PageProperty property = new PageProperty();
        property.setCheckbox(checked);
        return property;
    }

    @Override
    public ArrayList<Task> getAllTask() throws RepositoryException
    {
        ArrayList<Task> tareas = new ArrayList<>();
        try
        {
            QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(databaseId);
            QueryResults queryResults = client.queryDatabase(queryRequest);
            for(Page page : queryResults.getResults())
            {
                Map<String, PageProperty> properties = page.getProperties();
                Task tarea = mapPageToTask(properties);
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

    @Override
    public void modifyTask(Task tarea) throws RepositoryException
    {
        try
        {
            String pageId = findPageIdByIdentifier(tarea.getIdentifier());
            if(pageId == null)
            {
                throw new RepositoryException("La tarea no se ha encontrado");
            }
            Map<String, PageProperty> updatedProperties = Map.of(
                "taskTitle", createRichTextProperty(tarea.getTitle()),
                "date", createDateProperty(dateFormat.format(tarea.getDate())),
                "content", createRichTextProperty(tarea.getContent()),
                "priority", createNumberProperty(tarea.getPriority()),
                "estimatedDuration", createNumberProperty(tarea.getEstimatedDuration()),
                "completed", createCheckboxProperty(tarea.getCompleted())
            );
            UpdatePageRequest updateRequest = new UpdatePageRequest(pageId, updatedProperties);
            client.updatePage(updateRequest);
        }
        catch(Exception e)
        {
            throw new RepositoryException("Error al modificar la tarea: " + e.getMessage());
        }
    }

    @Override
    public void removeTask(Task tarea) throws RepositoryException
    {
        try
        {
            String pageId = findPageIdByIdentifier(tarea.getIdentifier());
            if(pageId == null)
            {
                throw new RepositoryException("La tarea no se ha encontrado");
            }
            UpdatePageRequest updateRequest = new UpdatePageRequest(pageId, Collections.emptyMap(), true);
            client.updatePage(updateRequest);
        }
        catch(Exception e)
        {
            throw new RepositoryException("Error al eliminar la tarea");
        }
    }

    private Task mapPageToTask(Map<String, PageProperty> properties)
    {
        try
        {
            int identifier = Integer.parseInt(properties.get("identifier").getTitle().get(0).getText().getContent());
            String title = properties.get("taskTitle").getRichText().get(0).getText().getContent();
            String dateString = properties.get("date").getDate().getStart();
            Date date = dateFormat.parse(dateString);
            String content = properties.get("content").getRichText().get(0).getText().getContent();
            int priority = properties.get("priority").getNumber().intValue();
            int estimatedDuration = properties.get("estimatedDuration").getNumber().intValue();
            boolean completed = properties.get("completed").getCheckbox();
            return new Task(identifier, title, date, content, priority, estimatedDuration, completed);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    private String findPageIdByIdentifier(int identifier) throws RepositoryException
    {
        try
        {
            QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(databaseId);
            QueryResults queryResults = client.queryDatabase(queryRequest);

            for (Page page : queryResults.getResults())
            {
                Map<String, PageProperty> properties = page.getProperties();
                if (properties.containsKey(titleColumnName) && properties.get(titleColumnName).getTitle().get(0).getText().getContent().equals(String.valueOf(identifier)))
                {
                    return page.getId();
                }
            }
        }
        catch(Exception e)
        {
            throw new RepositoryException("Error al buscar por identificador");
        }
        return null;
    }

    @Override
    public void setExporter(IExporter exporter)
    {
        this.exporter = exporter;
    }

    @Override
    public void exportarTareas(String archivo) throws RepositoryException, ExporterException
    {
        if (exporter == null)
        {
            throw new RepositoryException("No se ha configurado un exportador");
        }
        exporter.exportarTareas(getAllTask(), archivo); 
    }

    @Override
    public void importarTareas(String archivo) throws RepositoryException, ExporterException
    {
        if (exporter == null)
        {
            throw new RepositoryException("No se ha configurado un exportador");
        }

        ArrayList<Task> tareas = exporter.importarTareas(archivo);
        for (Task t : tareas)
        {
            try
            {
                addTask(t);
            }
            catch(RepositoryException e)
            {

            }
        }
    }

    @Override
    public void guardarEstado() throws RepositoryException
    {
    
    }
}