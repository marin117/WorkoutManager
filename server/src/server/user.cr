require "json"

class User
  JSON.mapping({
    id:       String,
    username: String,
    email:    String,
    picture:  String,
  })

  def initialize(id, username, email, picture)
    @id = id
    @username = username
    @email = email
    @picture = picture
  end
end
