package models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class blogCategory extends Model {
    @Id
    public int id;
    public String name;
    @ManyToMany
    public List<Blog> blogs = new ArrayList<Blog>();

    public static Finder<Integer, blogCategory> find = new Finder<>(blogCategory.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }
}
