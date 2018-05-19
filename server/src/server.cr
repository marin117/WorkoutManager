require "./server/*"
require "kemal"
require "pg"
require "json"

db = DB.open "postgres:///WorkoutManagerDB"

get "/" do |e|
  a = ""
  query = db.query "select array_to_json(array_agg(row_to_json(t))) from (select routine_id, r.name, username, owner, location, date, r.appraisal from workout
join (select routine.*, username as owner from routine join person on person.id = user_id) as r on r.id = routine_id
join person on person.id = workout.user_id order by date desc) t;" do |rs|
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
# {"appraisal":0,"comment":"",
# "exercise":[{"exercise_name":"Dumbell bench press","reps":0,"sets":0},
# {"exercise_name":"Dumbell bench press","reps":0,"sets":0},
# {"exercise_name":"Dumbell bench press","reps":0,"sets":0}],"id":1,"name":"","user_id":2}

post "/routine/" do |e|
  routine = Routine.from_json(e.params.json["routine"].to_json)
  STDOUT.puts routine.exercise
  time = Time.parse(e.params.json["date"].as(String), "%Y-%m-%d %H:%M:%S%z")
  location = e.params.json["location"].as(String)
  query = "insert into routine_exercise(routine_id, exercise_name, sets, reps) values ($1, $2, $3, $4)"
  id = db.query_one "insert into routine(name, user_id, appraisal, comment) values ($1, $2, 0, $3) returning id",
    routine.name, routine.user_id, routine.comment, &.read(Int32)
  routine.exercise.each do |exercise|
    db.exec(query, id, exercise.exercise_name, exercise.sets, exercise.reps)
  end
  db.exec("insert into workout(user_id, routine_id, date, location) values ($1,$2,$3,$4)", routine.user_id, id, time, location)
  time = Time.parse(e.params.json["date"].as(String), "%Y-%m-%d %H:%M:%S%z")
  STDOUT.puts time
end

Kemal.run
