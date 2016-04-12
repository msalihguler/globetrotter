var mongoose    =   require("mongoose");
mongoose.connect('mongodb://localhost:27017/globetrotter');

var mongoSchema =   mongoose.Schema;
// create schema
var userSchema  = {
    "email" : String,
    "username" : String,
    "password" : String,
    "nameandsurname" : String,
    "profilepicroute" : String
};
// create model if not exists.
module.exports = mongoose.model('users',userSchema);
