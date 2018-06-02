require "./server/*"
require "kemal"
require "pg"
require "json"

db = DB.open "postgres:///WorkoutManagerDB"

get "/" do |e|
  a = ""
  query = db.query "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select routine_id, r.name, username, owner, location, date, r.appraisal from workout
join (select routine.*, username as owner from routine join person on person.id = user_id) as r on r.id = routine_id
join person on person.id = workout.user_id order by date desc) t;" do |rs|
    rs.each do
      a = rs.read(JSON::Any)
    end
  end
  a.to_json
end

get "/:userId/workout" do |e|
  user_id = e.params.url["userId"]
  a = ""
  STDOUT.puts user_id
  query = db.query "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select routine_id, r.name, username, owner, location, date, r.appraisal from workout
join (select routine.*, username as owner from routine join person on person.id = user_id) as r on r.id = routine_id
join person on person.id = workout.user_id where person.id = $1 order by date desc) t", user_id do |rs|
    rs.each do
      a = rs.read(JSON::Any)
    end
  end
  a.to_json
end

get "/routine/" do |e|
  id = e.params.query["id"]
  user_id = e.params.query["user_id"]
  routine = db.query_one "select row_to_json(res) from (select id, x.user_id, x.name, array_to_json(array_agg(row_to_json(t))) as exercise, comment, appraisal, case when
c.user_id = b.user_id then true else false end as isMy
from (select routine_id, exercise_name, sets, reps from routine_exercise
where routine_id = $2) t join (select id,name, user_id,comment, appraisal from routine) x on x.id = t.routine_id left join
(select * from workout where user_id =$1) b on b.routine_id  = x.id left join
(select * from workout where routine_id = $2 and user_id =$1)
c on c.routine_id = x.id
group by x.user_id,comment, appraisal, x.name, x.id, b.user_id, c.user_id) res;;", user_id, id, &.read(JSON::Any)
  routine.to_json
end

put "/routine/" do |e|
  routine = Routine.from_json(e.params.json["routine"].to_json)
  time = Time.parse(e.params.json["date"].as(String), "%Y-%m-%d %H:%M:%S%z")
  location = e.params.json["location"].as(String)

  db.exec "insert into workout (user_id, routine_id, date, location) values ($1, $2, $3, $4)", routine.user_id, routine.id, time, location

  "response"
end

post "/routine/" do |e|
  routine = Routine.from_json(e.params.json["routine"].to_json)
  time = Time.parse(e.params.json["date"].as(String), "%Y-%m-%d %H:%M:%S%z")
  location = e.params.json["location"].as(String)
  query = "insert into routine_exercise(routine_id, exercise_name, sets, reps) values ($1, $2, $3, $4)"
  id = db.query_one "insert into routine(name, user_id, appraisal, comment) values ($1, $2, 0, $3) returning id",
    routine.name, routine.user_id, routine.comment, &.read(Int64)
  routine.exercise.each do |exercise|
    db.exec(query, id, exercise.exercise_name, exercise.sets, exercise.reps)
  end
  query = "insert into routine_type(routine_id, type_name) values ($1, $2)"
  routine.types.each do |type_name|
    db.exec(query, id, type_name)
  end
  db.exec("insert into workout(user_id, routine_id, date, location) values ($1,$2,$3,$4)", routine.user_id, id, time, location)
  time = Time.parse(e.params.json["date"].as(String), "%Y-%m-%d %H:%M:%S%z")
  STDOUT.puts time
end

post "/token/" do |e|
  url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + e.params.json["tokenId"].to_s
  response = HTTP::Client.get url
  STDOUT.puts response.body
  google_resp = JSON.parse(response.body)
  sub = google_resp["sub"].to_s
  name = google_resp["name"].to_s
  email = google_resp["email"].to_s
  begin
    db.exec("insert into person(id, username, email) values ($1, $2, $3)", sub, name, email)
  rescue e : Exception
    STDOUT.puts e
  end
  sub
end

Kemal.run
