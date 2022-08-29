package controllers;

import models.Blogger;

import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.Context;


public class Secured extends Security.Authenticator {

    /**
     * Used by authentication annotation to determine if user is logged in.
     * @param ctx The context.
     * @return The email address of the logged in user, or null if not logged in.
     */
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("username");
    }

    /**
     * Instruct authenticator to automatically redirect to login page if unauthorized.
     * @param context The context.
     * @return The login page.
     */
    @Override
    public Result onUnauthorized(Context context) {
        return redirect(routes.ApplicationController.login());
    }

    /**
     * Return the email of the logged in user, or null if no logged in user.
     *
     * @param ctx the context containing the session
     * @return The email of the logged in user, or null if user is not logged in.
     */
    public static String getUser(Context ctx) {
        return ctx.session().get("username");
    }

    /**
     * True if there is a logged in user, false otherwise.
     * @param ctx The context.
     * @return True if user is logged in.
     */
    public static boolean isLoggedIn(Context ctx) {
        return (getUser(ctx) != null);
    }

    /**
     * Return the UserInfo of the logged in user, or null if no user is logged in.
     * @param ctx The context.
     * @return The UserInfo, or null.
     */
    public static Blogger getBlogger(Context ctx) {
        return (isLoggedIn(ctx) ? Blogger.find.query().where().eq("username", getUser(ctx)).findOne() : null);
    }
}
