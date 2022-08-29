package models;


import com.mysql.jdbc.Blob;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.migration.util.IOUtils;
import org.joda.time.DateTime;
import play.data.validation.Constraints;

import javax.persistence.*;

import play.data.format.*;
import play.routing.Router;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.UnexpectedException;

import java.sql.Timestamp;
import java.util.*;



@Entity
public class Post extends Model {
    @Id
    public Integer id;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(nullable = true)
   // @JoinColumn(name = "post_blog_index", referencedColumnName = "id")
    public Blog blog;
    @Constraints.Required
    public String title;

    @Constraints.Required
    public String image;
    public String caption;
    @Column(columnDefinition = "datetime")
    protected Timestamp createdAt;
    @OneToMany(cascade = CascadeType.REMOVE)
    public List<Comments> comments;
    @ManyToMany(mappedBy = "likedPosts", cascade = CascadeType.REMOVE)
    @JoinColumn(nullable=true)
    public List<Blogger> likedby;
    @ManyToMany(mappedBy = "reportedPosts", cascade = CascadeType.REMOVE)
    @JoinColumn(nullable=true)
    public List<Blogger> reportedby;
    @ManyToMany
    @JoinTable(name ="post_tags")
    public List<PostTags> tags = new ArrayList<>();

    public boolean sports;

    public static Finder<Integer, Post> find = new Finder<>(Post.class);
    public  Post(Blog blog){
        this.blog = blog;
    }


    public Integer getId() {
        return id;
    }

    public List<Blogger> getReportedby() {
        return reportedby;
    }

    public void setReportedby(List<Blogger> reportedby) {
        this.reportedby = reportedby;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSports() {
        return sports;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<Blogger> getLikedby() {
        return likedby;
    }

    public void setLikedby(List<Blogger> likedby) {
        this.likedby = likedby;
    }

    public List<PostTags> getTags() {
        return tags;
    }

    public void setTags(List<PostTags> tags) {
        this.tags = tags;
    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public DateTime getDate() {
        org.joda.time.DateTime date = new org.joda.time.DateTime(this.getCreatedAt());

        return date;
    }
    public void addLike(Blogger blogger) {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
        if (!this.likedby.contains(blogger)) {
            this.likedby.add(blogger);
        }
        this.update();
    }
    public void report(Blogger blogger) {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
        if (!this.reportedby.contains(blogger)) {
            this.reportedby.add(blogger);
        }
        this.update();
    }

    public Integer getLikes() {
        System.out.println("xxxxxx" + this.likedby.size());
        return this.likedby.size();
    }

}
