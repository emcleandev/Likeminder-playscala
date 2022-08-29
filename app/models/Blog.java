package models;

import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Blog extends Model {
    @Id
    public Integer id;
    @OneToOne(cascade = CascadeType.REMOVE)
    public Blogger owner;
    public String name;
    public String aboutme;
   // public String nothing;              // i toggle this from comment to code to restart database
    //@ManyToMany(mappedBy = "")

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(nullable=true)
    @JoinTable(name ="Blog_followers_bloggers")
    public List<Blogger> followers;
//neToMany(mappedBy = "blog", cascade = CascadeType.ALL)

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    @JoinColumn(nullable=true)
    @JoinTable(name ="bloggers_posts")
    public List<Post> posts;



    public static Finder<Integer, Blog> find = new Finder<>(Blog.class);

    public Blog (Blogger owner){
        this.owner = owner;
        name = "Blog's Name";
        aboutme = "Write about you";
        followers = new ArrayList<>();
        posts = new ArrayList<>();

    }
    public Integer getId() {
        return id;
    }

    public Blogger getOwner() {
        return owner;
    }

    public void setOwner(Blogger owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAboutme() {
        return aboutme;
    }


    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }




    public void setId(Integer id) {
        this.id = id;
    }

    public List<Blogger> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Blogger> followers) {
        this.followers = followers;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
