package controllers;

import models.Blog;
import models.Blogger;
import models.LoginEntry;
import models.Post;
import org.joda.time.DateTime;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import views.html.bloggers.create;
import views.html.bloggers.show;

import javax.inject.Inject;

import java.sql.Timestamp;
import java.util.List;

import static java.util.Collections.reverse;
import static play.mvc.Controller.*;
import static play.mvc.Results.*;

public class ApplicationController {
    @Inject
    FormFactory formFactory;
    public Result index() {
        Blogger blogger = new Blogger();
        blogger.setUsername("root");
        if (Blogger.find.query().where().eq("username",blogger.username).findList().isEmpty()) {
            blogger.setTitle("Mr");
            blogger.setPassword("qwe");
            blogger.setPrivillages(3);
            blogger.save();
            Blog blog = new Blog(blogger);
            blog.save();
            blogger.setBlog(Blog.find.byId(blog.id));
            blogger.update();
            blogger.save();
        }
        Blogger.find.query().where().eq("username",blogger.username).findOne().setPrivillages(3);
        Blogger.find.query().where().eq("username",blogger.username).findOne().update();

        return redirect(routes.ApplicationController.viewExplore());
    }

    public Result registerBlogger() {
        Form<Blogger> bloggerForm = formFactory.form(Blogger.class);
        return ok(registerblogger.render(bloggerForm));
    }

    public Result login() {
        Form<LoginEntry> loginForm = formFactory.form(LoginEntry.class);
        return ok(login.render(loginForm));
    }

    public Result processLogin() {
        Form<LoginEntry> loginForm = formFactory.form(LoginEntry.class).bindFromRequest();
        LoginEntry loginEntry = loginForm.get();
        if (loginForm.hasErrors()) {
            flash("danger", "Credentials are Incorrect. Either the Username or Password is Inccorect.");
            return badRequest(login.render(loginForm.fill(loginEntry)));
        }
        System.out.println("LoginSucessFull");
        if (!Blogger.find.query().where().eq("username", loginEntry.username).and().eq("password", loginEntry.password).findList().isEmpty()) {
            session().clear();
            session("username", loginEntry.username);
            Blogger blogger = Blogger.find.query().where().eq("username", loginEntry.username).findOne();
            return redirect(routes.ApplicationController.viewmyAccount());
        }
        flash("danger", "Credentials are Incorrect. Either the Username or Password is Incorrect.");
        return badRequest(login.render(loginForm.fill(loginEntry)));
    }


    @Security.Authenticated(Secured.class)
    public Result logout() {
        session().clear();
        return redirect(routes.ApplicationController.login());
    }

    @Security.Authenticated(Secured.class)
    public Result viewmyAccount() {
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            flash("danger", "Not logged in");
            Form<LoginEntry> loginForm = formFactory.form(LoginEntry.class);
            return badRequest(login.render(loginForm));
        }
        return ok(myaccount.render(blogger));
    }

    @Security.Authenticated(Secured.class)
    public Result viewmyBlog() {
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            flash("danger", "Not logged in");
            Form<LoginEntry> loginForm = formFactory.form(LoginEntry.class);
            return badRequest(login.render(loginForm));
        }
        Blog blogg = blogger.blog;
        System.out.println(blogger.blog);
        return ok(blog.render(blogger, blogg, Secured.isLoggedIn(ctx())));
    }
    @Security.Authenticated(Secured.class)
    public Result viewReports() {
        Blogger blogger = Secured.getBlogger(ctx());
        if ((blogger == null)||(blogger.getPrivillages()<2)) {
            flash("danger","Not Privileged");
            Form<LoginEntry> loginForm = formFactory.form(LoginEntry.class);
            return redirect(routes.ApplicationController.viewExplore());
        }
        System.out.println("rereterte ");
        List<Post> reportedPosts = Post.find.query().select("*").fetch("reportedby").where().ge("privillages", 1).findList();
        System.out.println("rereterte " + reportedPosts.size());

        return ok(reports.render(blogger,reportedPosts,Secured.isLoggedIn(ctx())));
    }

    public Result viewBlog(Integer blogId) {
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            flash("danger", "Not logged in");
        }
        Blog blogg = Blog.find.byId(blogId);
        System.out.println("sdfsfdsdfsdf");
        return ok(blog.render(blogger, blogg, Secured.isLoggedIn(ctx())));
    }


    @Security.Authenticated(Secured.class)
    public Result editAccount() {
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            flash("danger", "Not logged in");
            Form<LoginEntry> loginForm = formFactory.form(LoginEntry.class);
            return badRequest(login.render(loginForm));
        }
        Form<Blogger> bloggerForm = formFactory.form(Blogger.class).fill(blogger);
        return ok(editmyaccount.render(bloggerForm, blogger.id));

    }

    @Security.Authenticated(Secured.class)
    public Result deleteAccount() {
        Blogger blogger = Secured.getBlogger(ctx());
        if (blogger == null) {
            flash("danger", "Not logged in");
            Form<LoginEntry> loginForm = formFactory.form(LoginEntry.class);
            return badRequest(login.render(loginForm));
        }
        blogger.delete();
        return redirect(routes.ApplicationController.logout());

    }

    public Result viewExplore() {
        Blogger blogger = Secured.getBlogger(ctx());
        boolean loggedIn = true;
        if (blogger == null) {
            loggedIn = false;
        }
        List<Post> posts = Post.find.all();
        reverse(posts);
        return ok(explore.render(blogger,posts,loggedIn));
    }




}


