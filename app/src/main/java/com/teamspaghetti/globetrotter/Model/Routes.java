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
    String _id="";
    public Routes(String id,String _creator,String _title,String _details,String _likes,String _comments){
        setID(id);
        setCreator(_creator);
        setTitle(_title);
        setDetail(_details);
        setLikes(_likes);
        setComments(_comments);    }
    public String getID(){
        return _id;
    }
    public void setID(String id){
        this._id=id;
    }
    public String getCreator(){
        return creator;
    }
    public void setCreator(String creator){
        this.creator=creator;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getDetail(){
        return detail;
    }
    public void setDetail(String _detail){
        this.detail=_detail;
    }
    public String getLikes(){
        return likes;
    }
    public void setLikes(String _likes){
        this.likes=_likes;
    }
    public String getComments(){
        return comments;
    }
    public void setComments(String _comment){
        this.comments=_comment;
    }
}
