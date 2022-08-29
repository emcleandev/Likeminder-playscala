package models;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PostTags extends Model {
    @Id
    public Integer id;
    public String name;
    @ManyToMany
    public List<Post> taggedIn = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getTaggedIn() {
        return taggedIn;
    }

    public void setTaggedIn(List<Post> taggedIn) {
        this.taggedIn = taggedIn;
    }
}
