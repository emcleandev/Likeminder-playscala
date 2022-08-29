package controllers;
import io.ebean.Finder;
import models.Blog;
import models.Blogger;


import models.LoginEntry;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.collection.JavaConverters;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import play.data.*;
import views.html.bloggers.index;
import views.html.bloggers.create;
import views.html.bloggers.edit;
import views.html.bloggers.show;
import views.html.login;
import views.html.registerblogger;


import static play.mvc.Controller.*;
import static play.mvc.Http.Context.Implicit.request;
import static play.mvc.Results.*;


public class BloggersController {

    @Inject
    FormFactory formFactory;

    // for al Bloggers
    @Security.Authenticated(Secured.class)
    public Result index(){
        List<Blogger> bloggers = Blogger.find.all() ;
        System.out.println(bloggers.size());
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            flash("danger", "Not logged in");
            Form<LoginEntry> loginForm = formFactory.form(LoginEntry.class);
            return badRequest(login.render(loginForm));
        }
        return ok(index.render(bloggers));
    }

    // to create a Blogger

    public Result create(){
        Form<Blogger> bloggerForm = formFactory.form(Blogger.class);
        return ok(create.render(bloggerForm));
    }


    // to save blogger
    public Result save(){
        Form<Blogger> bloggerForm = formFactory.form(Blogger.class).bindFromRequest();
        if(bloggerForm.hasErrors()){
            flash("danger","Please Correct the form Entry Below");
            return badRequest(create.render(bloggerForm));
        }

        System.out.println(bloggerForm.rawData());
        Blogger blogger = bloggerForm.get();

        //Authentication
        if (routes.BloggersController.create().path().contains(request().path())){  // if a normal user regersirting, i want to redirect them to login
                if (!Blogger.find.query().where().eq("username",blogger.username).findList().isEmpty()) {
                    flash("danger", "Username already taken");
                    return badRequest(create.render(bloggerForm));
                }

                blogger.save();
                Blog blog = new Blog(blogger);
                blog.save();

                blogger.setBlog(Blog.find.byId(blog.id));
                blogger.update();

                flash("success","Blogger Registration Successful");
                if((Secured.getBlogger(ctx()) != null)&&(Secured.getBlogger(ctx()).getPrivillages()==3)){
                    return redirect(routes.AdminController.index());
                }
                return redirect(routes.ApplicationController.login());
        }

        blogger.save();
        Blog blog = new Blog(blogger);
        blog.save();

        blogger.setBlog(Blog.find.byId(blog.id));
        blogger.update();




        flash("success","Blogger Registration Successful");
        return redirect(routes.BloggersController.index());

    }

    // to edit Blogger
    public Result edit(Integer id){
        Blogger blogger = Blogger.find.byId(id);
        if (blogger == null) {
            return notFound("Blogger User not Found ");
        }
        Form<Blogger> bloggerForm = formFactory.form(Blogger.class).fill(blogger);
        return ok(edit.render(bloggerForm, blogger.id));

    }

    public Result update(Integer id) {
        Blogger editblogger = formFactory.form(Blogger.class).bindFromRequest().get();
        Blogger blogger = Blogger.find.byId(id);;

        System.out.println(blogger.getUsername());
        System.out.println(editblogger.getUsername());
        if (blogger == null) {
            return notFound("Blogger not found");
        }
        if ((!blogger.getUsername().equals(editblogger.getUsername())) && (!Blogger.find.query().where().eq("username", editblogger.username).findList().isEmpty())) {
            flash("danger", "Username taken");
            return redirect(Controller.request().getHeader("referer"));
        }


            blogger.setFirstName(editblogger.getFirstName());
            blogger.setLastName(editblogger.getLastName());
            blogger.setEmail(editblogger.getEmail());
            blogger.setUsername(editblogger.getUsername());
            blogger.setPassword(editblogger.getPassword());
            blogger.update();
            blogger = Secured.getBlogger(ctx());
            if ((blogger == null)||(blogger.getPrivillages()==3)){
                return redirect(routes.BloggersController.index());
            }
            return redirect(routes.ApplicationController.viewmyAccount());

    }

    public Result destroy(Integer id){
        Blogger blogger = Blogger.find.byId(id);
        if (blogger == null) {
            return notFound("Blogger not found");
        }
    //    blogger.blog.owner=null;
    //    blogger.blog.update();
    //    blogger.blog = null;
      //  blogger.update();

       // Blog.find.byId(blogger.following.get(0).id).followers.remove(blogger);
       // Blog.find.byId(blogger.following.get(0).id).update();
        if(blogger.getPrivillages() == 3){
            redirect(routes.ApplicationController.index());
        }
        Blogger loggedInBlogger = Secured.getBlogger(ctx());
        if (loggedInBlogger.id == blogger.id) {
            for(int i= 0 ; i < blogger.getCommentsMade().size(); i++){
                blogger.getCommentsMade().remove(i);
            }
            System.out.println("HERERERERERERRRRRRRRRRRRRRRRRRR");
            blogger.update();
            blogger.delete();
            redirect(routes.ApplicationController.logout());

        }
        System.out.println("HERERERERERERRRRRRRRRRRRRRRRRRYYYYYYYYYYYY");
        blogger.delete();
        return redirect(routes.BloggersController.index());
    }

    public Result show(Integer id){
        Blogger blogger = Blogger.find.byId(id);
        if (blogger == null) {
            return notFound("Blogger User not Found ");
        }
        return ok(show.render(blogger));
    }

    public Blogger copyBloggersAttributes(Blogger blogger,Blogger tocopyblogger, boolean copyId){
        if (copyId){
            blogger.id = tocopyblogger.id;
        }
        blogger.title = tocopyblogger.title;
        blogger.firstName = tocopyblogger.firstName;
        blogger.lastName = tocopyblogger.lastName;
        blogger.email = tocopyblogger.email;
        blogger.username = tocopyblogger.username;
        blogger.password =tocopyblogger.password;

        return  blogger;
    }


}
