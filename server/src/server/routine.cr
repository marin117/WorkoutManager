require "json"
require "./exercise"

class Routine
  JSON.mapping({
    appraisal: Int32,
    comment:   String,
    exercise:  Array(Exercise),
    name:      String,
    id:        Int32,
    user_id:   String,
    types:     Array(String),
  })
end
