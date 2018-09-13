package com.mobioetech.keralathodoppam.keralathodoppam;

import java.io.Serializable;

public class Ariyippukal implements Serializable{
    private String heading;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;
    private String youtubeLink;

    public String getSupport_image() {
        return support_image;
    }

    public void setSupport_image(String support_image) {
        this.support_image = support_image;
    }

    private String support_image;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    private String details;

    public Ariyippukal() {
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }
}
