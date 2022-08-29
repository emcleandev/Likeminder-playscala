package controllers;

import com.mysql.jdbc.Blob;
import io.ebean.ExampleExpression;
import io.ebean.Model;
import models.*;
import org.joda.time.DateTime;
import play.data.*;
import play.data.validation.Constraints;
import play.mvc.Result;
import play.mvc.Security;


import views.html.editblog;
import views.html.makePost;
import play.mvc.*;
import play.mvc.Http.*;

import play.data.Form;


import javax.inject.Inject;
import javax.persistence.Entity;
import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Constraint;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.sql.DriverManager;
import java.sql.Timestamp;

import static play.mvc.Controller.*;
import static play.mvc.Results.*;
import static play.mvc.Results.redirect;

import play.mvc.*;
import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;

import play.data.Form;


import models.Image;
import views.html.*;
public class BlogController {
    @Inject
    FormFactory formFactory;

    @Security.Authenticated(Secured.class)
    public Result editmyBlog() {
        System.out.println("runnninggggg");
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            return notFound("Blog User not Found ");
        }
        Blog blog = blogger.blog;
        Form<Blog> blogForm = formFactory.form(Blog.class).fill(blog);
        return ok(editblog.render(blogForm, blog.getId()));
    }

    @Security.Authenticated(Secured.class)
    public Result updatemyBlog(Integer blogId) {
        Form<Blog> form = formFactory.form(Blog.class).bindFromRequest();
        Blog newblog = form.get();
        Blogger blogger = Secured.getBlogger(ctx());
        if(form.hasErrors()){
            flash("danger","Please Correct the form Entry Below");
            return badRequest(editblog.render(form, blogId));
        }

        if (blogger == null) {
            return notFound("Blog User not Found ");
        }
        Blog blog = Blog.find.byId(blogId);
        if (blog == null) {
            return notFound("Blog not Found ");
        }
        blog.setName(newblog.getName());
        blog.setAboutme(newblog.getAboutme());
        System.out.println(blog.getAboutme());                                            // LATER ALTER THIS TO CARRY OVER BLOG CATERGORIES
        blog.update();
        // DO I NEED TO SAVE IT
        return redirect(routes.ApplicationController.viewmyBlog());
    }
    @Security.Authenticated(Secured.class)
    public Result likePost (Integer postId) {
        System.out.println("Like post");
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            return notFound("Blogger User not Found ");
        }
        Blog blog = blogger.blog;
        Post post = Post.find.byId(postId);
        post.addLike(blogger);
        blogger.update();
        flash("success", "liked");
        System.out.println(request().toString());
        System.out.println(request().path());
        return redirect(request().getHeader("referer"));
    }
    @Security.Authenticated(Secured.class)
    public Result reportPost (Integer postId) {
        System.out.println("report post");
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            return notFound("Blogger User not Found ");
        }
        Blog blog = blogger.blog;
        Post post = Post.find.byId(postId);
        post.report(blogger);
        blogger.update();
        flash("warning", "Reported");
        return redirect(request().getHeader("referer"));
    }
    @Security.Authenticated(Secured.class)
    public Result deletePost (Integer postId) {
        System.out.println("report post");
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            return notFound("Blogger User not Found ");
        }
        Blog blog = blogger.blog;
        Post post = Post.find.byId(postId);
        post.delete();
        return redirect(request().getHeader("referer"));
    }

    @Security.Authenticated(Secured.class)
    public Result follow (Integer blogId) {
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            return notFound("Blogger User not Found ");
        }
        Blog blog = Blog.find.byId(blogId);
    //    blogger.following.add(blog);                                  ////////////////////////////
        blog.followers.add(blogger);
        blogger.update();
        blog.update();
        return redirect(request().getHeader("referer"));
    }

    @Security.Authenticated(Secured.class)
    public Result makePost () {

        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            return notFound("Blogger User not Found ");
        }
        Blog blog = blogger.blog;

        Form<PostEntry> postForm = formFactory.form(PostEntry.class);
        return ok(makePost.render(postForm));
    }

    @Security.Authenticated(Secured.class)
    public Result comment (Integer postid) {
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            return notFound("Blogger User not Found ");
        }
        Post post = Post.find.byId(postid);

        final DynamicForm forms = formFactory.form().bindFromRequest();
        Comments comment = new Comments();
        comment.setComment(forms.get("comment"));
        comment.setBlogger(blogger);
        comment.setPost(Post.find.byId(postid));
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        comment.save();
        Post.find.byId(postid).comments.add(comment);
        Post.find.byId(postid).update();
        System.out.println(comment.comment);
        return redirect(request().getHeader("referer"));
    }



    @Security.Authenticated(Secured.class)
    public Result uploadPost () {
        Blogger blogger = Secured.getBlogger(ctx());
        Form<UploadImageForm> form = formFactory.form(UploadImageForm.class).bindFromRequest();
        if(form.hasErrors()){
            System.out.println("HEYYYYYYY");
            flash("danger","Please Correct the form Entry Below");
            return  badRequest(index.render());
            //return badRequest(makePost.render(form));
        }
        UploadImageForm tempPost = form.get();


        if (blogger == null) {
            return notFound("Blog not Found ");
        }
        Blog blog = blogger.blog;
        Post post = new Post(blog);
        try {
        String name = tempPost.image.getFilename();
        File image = tempPost.image.getFile();

       /* try {
            System.out.println("ddd");
            byte[] bytesArray = new byte[(int) tempPost.image.length()];
            System.out.println("ddd");
            bytesArray = Files.readAllBytes(tempPost.image.toPath());
            System.out.println("ddd");
        //    fis.read(bytesArray); //read file into bytes[]

            System.out.println("ddd");

            post.setTitle(tempPost.title);
            System.out.println("ddd");
            post.setCaption(tempPost.caption);
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// CHANGE PASSSWORD
           // Blob blob = DriverManager.getConnection("jdbc:mysql://localhost/likeminded_db1", "root", "Mclean10");
            post.image.setBytes(1, bytesArray);
            System.out.println("ddd");
           // post.setImage(blob);
        } catch (Exception e) {
            flash("danger", "File too large for our current Resources");
            return redirect(routes.BlogController.makePost());
        }
*/

          //  File c = new File("public/images/","heyyyy.jpg");
          //  System.out.println(c.getAbsolutePath());
          //  c.createNewFile();
            System.out.println(tempPost.title);
            image.renameTo(new File("public/images/", name));
          //  image.createNewFile();
            System.out.println("ccccc");

            post.setTitle(tempPost.title);
            System.out.println("ddd");
            post.setCaption(tempPost.caption);
            post.setSports(tempPost.isSports());
            System.out.println(post.isSports());

             post.setImage(name);
        } catch (Exception e) {
            flash("danger", "File too large for our current Resources");
            return redirect(routes.BlogController.makePost());
        }
        post.setBlog(blog);


        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        System.out.println(new DateTime(post.getCreatedAt()).year().getAsString()+ "    " + post.getCreatedAt().getTime());
        System.out.println("ddddddddddddd" + post.getCreatedAt().toLocalDateTime() + " " + post.getCreatedAt().toString());
        // Update and save

        post.save();
        System.out.println(post.toString());
        System.out.println(post.blog.toString());
        System.out.println("ddddddddddddd");
        System.out.println("ddddddddddddd");
        blog.update();
        System.out.println("ddddddddddddd");

        flash("success", "Post uploaded");
        return redirect(routes.ApplicationController.viewmyBlog());
    }


   /* public Result getImage (Integer postId) {
        Post post = Post.find.byId(postId);
        int length= 0;
        try{
           length = (int)post.image.length();
            post.image.free();
            return ok(post.getImage().getBytes(1, length)).as("image/jpeg");
        }catch (Exception e) {
            System.out.println("image not found");
            return notFound("Post not Found ");
        }
*/



    // This is the form that will be filled to make a Post. Update here when updatting "make a post" form.
    public static class UploadImageForm {
        public String title;
        public FilePart<File> image;
        public String caption;
        public boolean sports;
        public String validate() {
            MultipartFormData<File> data = request().body().asMultipartFormData();
            image = data.getFile("image");

            if (image == null) {
                return "File is missing.";
            }

            return null;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public boolean isSports() {
            return sports;
        }

        public void setSports(boolean sports) {
            this.sports = sports;
        }
    }


}
