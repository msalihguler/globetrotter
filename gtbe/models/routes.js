var mongoose    =   require("mongoose");
//mongoose.connect('mongodb://localhost:27017/globetrotter');

var mongoSchema =   mongoose.Schema;
// create schema
var routeSchema  = {
    "creator" : String,
    "title" : String,
    "detail" : String,
    "city" : String,
    "latitude" : String,
	"longitude" : String,
	"likes": String,
	"comments":String,
	"location_details": String
};
// create model if not exists.
module.exports = mongoose.model('route',routeSchema);
