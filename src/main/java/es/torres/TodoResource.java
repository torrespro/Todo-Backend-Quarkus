package es.torres;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/todos")
public class TodoResource {

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
        newTodo.url = uriInfo.getAbsolutePathBuilder().scheme("https").build().toURL();
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