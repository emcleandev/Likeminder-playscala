package models;

import io.ebean.Model;
import org.joda.time.DateTime;
import scala.collection.script.Remove;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Comments extends Model {

    @Id
    public Integer Id;
    @ManyToOne(cascade = CascadeType.REFRESH)
    public Post post;
    @ManyToOne(cascade = CascadeType.REFRESH)
    public Blogger blogger;
    public String comment;
    @Column(columnDefinition = "datetime")
    public Timestamp createdAt;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public DateTime getDate() {
        org.joda.time.DateTime date = new org.joda.time.DateTime(this.getCreatedAt());

        return date;
    }
}
