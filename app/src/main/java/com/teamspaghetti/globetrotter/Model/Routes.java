package com.teamspaghetti.globetrotter.Model;

/**
 * Created by msalihguler on 04.04.2016.
 */
public class Routes {
    String creator ="";
    String title ="";
    String latitude ="";
    String longitude ="";
    String locdetails ="";
    String city ="";
    String detail ="";
    String likes ="";
    String comments ="";
    public Routes(String _creator,String _title,String _details,String _likes,String _comments){
        this.creator=_creator;
        this.title=_title;
        this.detail=_details;
        this.likes=_likes;
        this.comments=_comments;
    }


}
