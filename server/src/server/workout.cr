require "json"

class Workout
  JSON.mapping({
    routine_id: Int64,
    name:       String,
    owner:      String,
    username:   String,
    location:   String,
    date:       String,
    appraisal:  Int64,
    isowner:    Bool,
    used:       Int64,
  })

  def initialize
    @routine_id = -1i64
    @name = ""
    @owner = ""
    @username = ""
    @location = ""
    @date = ""
    @appraisal = -1i64
    @isowner = false
    @used = -1i64
  end
end
