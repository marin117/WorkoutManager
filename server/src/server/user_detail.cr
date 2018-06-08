require "json"
require "./user"
require "./workout"

class UserDetails
  JSON.mapping({
    user:     User,
    workouts: Array(Workout),
    type:     Array(String),
    exercise: Array(String),
  })

  def initialize(user, workouts, type, exercise)
    @user = user
    @workouts = workouts
    @type = type
    @exercise = exercise
  end
end
