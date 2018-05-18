require "json"

class Exercise
  JSON.mapping({
    exercise_name: String,
    sets:          Int32,
    reps:          Int32,
  })
end
