package controllers;

import models.Blogger;
import models.Post;
import play.data.FormFactory;
import play.mvc.Result;
import views.html.bloggers.index;

import javax.inject.Inject;
import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

public class AdminController {
    @Inject
    FormFactory formFactory;

    public Result index() {
        List<Blogger> bloggers = Blogger.find.all();
        System.out.println(bloggers.size());
        return ok(index.render(bloggers));
    }

    public Result makeAdmin(Integer bloggerID) {
        Blogger blogger = Blogger.find.byId(bloggerID);
        blogger.setPrivillages(3);
        blogger.update();
        return redirect(request().getHeader("referer"));
    }

    public Result makeMod(Integer bloggerID) {
        Blogger blogger = Blogger.find.byId(bloggerID);
        blogger.setPrivillages(2);
        blogger.update();
        return redirect(request().getHeader("referer"));
    }

    public Result demoteMod(Integer bloggerID) {
        Blogger blogger = Blogger.find.byId(bloggerID);
        blogger.setPrivillages(1);
        blogger.update();
        return redirect(request().getHeader("referer"));
    }

    public Result deletePost(Integer postID) {
        Post post = Post.find.byId(postID);
        post.delete();
        return redirect(request().getHeader("referer"));
    }

    public Result deleteBlogger(Integer bloggerID) {
        Blogger blogger = Blogger.find.byId(bloggerID);
        blogger.delete();
        return redirect(request().getHeader("referer"));

    }


}
