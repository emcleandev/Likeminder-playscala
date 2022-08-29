package models;

import io.ebean.Model;
import play.api.mvc.MultipartFormData;
import play.data.validation.Constraints;
import play.shaded.ahc.org.asynchttpclient.request.body.multipart.FilePart;

import javax.persistence.Entity;



public class PostEntry extends Model {
    @Constraints.Required
    public String title;
    @Constraints.Required
    public FilePart image;
    public boolean sports;
    @Constraints.Required
    public String caption;

    public PostEntry(){}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FilePart getImage() {
        return image;
    }

    public void setImage(FilePart image) {
        this.image = image;
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
