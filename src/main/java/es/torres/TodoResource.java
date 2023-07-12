package es.torres;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/todos")
public class TodoResource {

    @ConfigProperty(name = "scheme")
    String scheme;

    @Context
    UriInfo uriInfo;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Todo>  getAll() {
        return Todo.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Todo create(Todo newTodo) throws MalformedURLException {
        newTodo.url = uriInfo.getAbsolutePathBuilder().scheme(scheme).build().toURL();
        newTodo.persist();
        return newTodo;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteAll() {
        Todo.deleteAll();
        return Response.ok().build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response deleteOne(@PathParam("id") Long id) {
        Todo todo = Todo.findById(id);
        todo.delete();
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Todo getOne(@PathParam("id") Long id) {
        Optional<Todo> optional = Todo.findByIdOptional(id);
        Todo todo = optional.orElseThrow(() -> new NotFoundException("Todo does not exist!"));
        return todo;
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Todo edit(@PathParam("id") Long id, Todo updates) {
        Optional<Todo> optional = Todo.findByIdOptional(id);
        Todo byId = optional.orElseThrow(() -> new NotFoundException("Todo does not exist!"));
        merge(byId, updates);
        return byId;
    }

    private void merge(Todo current, Todo todoItem) {
        current.title = (String) (getLatest(current.title, todoItem.title));
        current.completed = ((Boolean) getLatest(current.completed, todoItem.completed));
        current.order = ((Integer) getLatest(current.order, todoItem.order));
    }

    private Object getLatest(Object old, Object latest) {
        return latest == null ? old : latest;
    }
}