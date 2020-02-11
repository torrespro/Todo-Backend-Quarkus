package es.torres;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.ws.rs.core.UriBuilder;

@Entity
public class Todo extends PanacheEntity {

    public String title;
    public Boolean completed;
    @Column(name = "\"order\"")
    public Integer order;
    public URL url;

    public URL getUrl() throws URISyntaxException, MalformedURLException {
        if (this.id != null) {
            return UriBuilder.fromUri(url.toURI()).scheme("https").path(this.id.toString()).build().toURL();
        }
        return this.url;
    }

    public Boolean getCompleted(){
        return this.completed == null ? false : this.completed;
    }

}
