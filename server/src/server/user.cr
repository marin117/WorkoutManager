require "json"

class User
  JSON.mapping({
    username: String,
    email:    String,
    picture:  String,
  })
end
