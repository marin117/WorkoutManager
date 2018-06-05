require "json"

class Workout
  JSON.mapping({
    routine_id: Int64,
    name:       String,
    owner:      String,
    username:   String,
    location:   String,
    date:       Time,
    appraisal:  Int32,
    isowner:    Bool,
  })
end
