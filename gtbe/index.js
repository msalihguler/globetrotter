/*
  253:SUCCESS ADDING
  254:ERROR WHILE ADDING
  255:USER EXISTS

*/

var express     =   require("express");
var app         =   express();
var bodyParser  =   require("body-parser");
var decode = require('urldecode')
var crypto = require("crypto");

var userprocesses     =   require("./models/users");
var routeprocesses     =   require("./models/routes");


app.use(bodyParser.json());
app.use(bodyParser.urlencoded({"extended" : false}));
app.post("/getroutes",function(req,res){
        var db = new routeprocesses();
        var response = {};
		var pagenumber = req.body.pagenumber;
		routeprocesses.find({})
                    .sort({"_id":-1})
                    .limit(10)
                    .exec(function(err, data){

            if(err) {
                response = {"error" : true,"message" : "Error fetching data"};
            } else {
                response = {"error" : false,"routes" : data};
            }
            res.status(253).send(JSON.stringify(response));
        });

});
app.post("/getdata",function(req,res){
  var type = req.body.type;
  var id = req.body.id;
  var db = routeprocesses();
    routeprocesses.findOne({"_id":id},function(err,user){
      if(err){
        res.status(512).send(err);
      }else{
        var likearray = JSON.parse(user.likes);
        likearray["likes"].push(id);
        user.likes=JSON.stringify(likearray);
        user.save(function(err){
                    if(err) {
                      res.status(52).send("wrong");
                    } else {
                      res.status(253).send(JSON.stringify(user));
                    }
      });
    }
  });

});
app.post("/createroute",function(req,res){
        var db = new routeprocesses();
        var response = {};
		var _title = req.body.title;
		var _latitude = req.body.latitude;
		var _longitude = req.body.longitude;
		var _locdetails = req.body.location_details;
		var _city = req.body.city;
		var _creator = req.body.creator;
		var _detail = req.body.detail;
		db.creator = decode(_creator);
		db.title = decode(_title);
		db.detail = decode(_detail);
		db.city = decode(_city);
		db.latitude = decode(_latitude);
		db.longitude =decode( _longitude);
		db.likes = "{\"likes\":[]}";
		db.comments = "{\"comments\":[]}";
		db.location_details = _locdetails;
		db.save(function(err,user){
				if(err) {
				res.status(254).send(JSON.stringify({"saved" : "false","message" : "Error adding data"}));
    			} else {
    				res.status(253).send(JSON.stringify({"username" : user.username,"user_id" : user._id}));
				}
		});

});


app.post("/signup",function(req,res){

        var db = new userprocesses();
        var response = {};
        var mail = req.body.email;

      userprocesses.findOne({"email":mail}, function(err, user) {
            if (err)
                return done(err);
            if (user) {
            	console.log("error");
            	response = {"saved" :"false","error":"User Exists!"};
                res.status(255).send(JSON.stringify(response));
            }else{
            	var pass = req.body.password;
					    db.email = req.body.email;
					    db.password =  crypto
									  .createHash('md5')
									  .update(pass)
									  .digest('hex');
									  db.username = req.body.username;
									  db.nameandsurname =req.body.nameandsurname;
					    db.save(function(err,user){
    						if(err) {
    							res.status(254).send(JSON.stringify({"saved" : "false","message" : "Error adding data"}));
    						} else {
    							res.status(253).send(JSON.stringify({"username" : user.username,"user_id" : user._id}));
    						}

					});
            }
        });

});
app.post("/login",function(req,res){
		var mail = req.body.email;
		var temp = req.body.password;

		 userprocesses.findOne({"email":mail}, function(err, user) {
            if (err)
					res.status(212).send(JSON.stringify({"login":"false","error":"user does not exist"}));

            if (user) {

				        var pass = crypto.createHash('md5').update(temp).digest('hex');

                if( pass === user.password){
								 console.log(user);
					             res.status(255).send(JSON.stringify({"username":user.username,"userid":user._id}));

				        }
             else{
					     res.status(258).send(JSON.stringify({"login":"false","error":"wrong password"}));
				     }
          }
		     	else {
				   	res.status(212).send(JSON.stringify({"login":"false","error":"user does not exist"}));
			    }
	});
});

app.listen(3000);
console.log("Listening to PORT 3000");
