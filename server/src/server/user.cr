require "json"

class User
  JSON.mapping({
    id:       String,
    username: String,
    email:    String,
    picture:  String,
    pushyid:  String,
    stars:    Int64,
    isstar:   Bool,
  })

  def initialize(id, username, email, picture, pushy_id)
    @id = id
    @username = username
    @email = email
    @picture = picture
    @pushyid = pushy_id
    @stars = 0i64
    @isstar = false
  end
end
