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
end
