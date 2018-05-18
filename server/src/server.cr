require "./server/*"
require "kemal"
require "pg"
require "json"

db = DB.open "postgres:///WorkoutManagerDB"

get "/" do |e|
  a = ""
  query = db.query "select array_to_json(array_agg(row_to_json(t))) from (select routine_id, r.name, username, owner, location, date, r.appraisal from workout
join (select routine.*, username as owner from routine join person on person.id = user_id) as r on r.id = routine_id
join person on person.id = workout.user_id) t;" do |rs|
    rs.each do
      a = rs.read(JSON::Any)
    end
  end
  a.to_json
end

get "/routine/" do |e|
  id = e.params.query["id"]
  routine = db.query_one "select row_to_json(res) from (select id, user_id,x.name, array_to_json(array_agg(row_to_json(t))) as exercise, comment, appraisal
from (select routine_id, exercise_name, sets, reps from routine_exercise
where routine_id = $1) t join (select id,name, user_id,comment, appraisal from routine) x on x.id = t.routine_id
group by user_id,comment, appraisal, x.name, x.id) res", id, &.read(JSON::Any)
  routine.to_json
end

post "/routine/" do |e|
  STDOUT.puts e.params.json["routine"].to_json
  routine = Routine.from_json(e.params.json["routine"].to_json)
  STDOUT.puts e.params.json["date"]
end

Kemal.run
