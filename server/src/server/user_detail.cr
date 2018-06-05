require "json"
require "./user"
require "./workout"

class UserDetails
  JSON.mapping({
    user:     User,
    workouts: Array(Workout),
  })

  def initialize(user, workouts)
    @user = user
    @workouts = workouts
  end
end
