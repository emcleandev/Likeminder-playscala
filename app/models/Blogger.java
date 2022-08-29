package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Constraint;

@Entity
public class Blogger extends Model{
    @Id
    public Integer id;
    @Constraints.MaxLength(4)
    @Constraints.Required
    public String title;
    public String firstName;
    public String lastName;
    public String email;
    @Column(unique = true)
    @Constraints.Required
    public String username;
    public String password;
    @Column(columnDefinition = "integer default 1")
    public Integer privillages = 1;
    @OneToOne(mappedBy = "owner",cascade = CascadeType.ALL)
    @JoinColumn(nullable=true)
    public Blog blog;
    @ManyToMany(mappedBy = "followers", cascade = CascadeType.REMOVE)
    @JoinColumn(nullable=true)
    public List<Blog> following;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(nullable=true)
    public List<Post> likedPosts;

    @OneToMany(mappedBy = "blogger", cascade = CascadeType.ALL)
    @JoinColumn(nullable=true)
    public List<Comments> commentsMade;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(nullable=true)
    @JoinTable(name ="blogger_reported_post")
    public List<Post> reportedPosts;

    public static Finder<Integer, Blogger> find = new Finder<>(Blogger.class);

    public Blog getBlog() {
        return blog;
    }

    public Blogger(){}
    public List<Comments> getCommentsMade() {
        return commentsMade;
    }

    public void setCommentsMade(List<Comments> commentsMade) {
        this.commentsMade = commentsMade;
    }

    public List<Post> getReportedPosts() {
        return reportedPosts;
    }

    public void setReportedPosts(List<Post> reportedPosts) {
        this.reportedPosts = reportedPosts;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public List<Blog> getFollowing() {
        return following;
    }

    public void setFollowing(List<Blog> following) {
        this.following = following;
    }

    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(List<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public Integer getId() {
        return id;
    }

    public int getPrivillages() {
        return privillages;
    }

    public void setPrivillages(int privillages) {
        this.privillages = privillages;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}





