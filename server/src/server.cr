require "./server/*"
require "kemal"
require "pg"
require "json"

db = DB.open "postgres:///WorkoutManagerDB"

before_all do |e|
  e.response.status_code = 200
  e.response.content_type = "application/json"
end
get "/" do |e|
  filter = e.params.query.fetch("filter", nil)
  response = ""
  if (filter == nil)
    STDOUT.puts filter
    response = db.query_one "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select workout.routine_id, r.name, username,owner, location, to_char(date, 'HH24:mm TZ DD.Month') as date, cnt.used,
case when likes.count is null then 0 else likes.count end as appraisal,
case when r.user_id = workout.user_id then (select true) else (select false) end as isOwner from workout
join (select routine.*, username as owner from routine join person on person.id = user_id) as r on r.id = routine_id
join person on person.id = workout.user_id join (select routine_id, count(routine_id) - 1 as used from workout group by routine_id)
as cnt on cnt.routine_id = r.id
left join (select routine_id, count(routine_id) from likes group by routine_id) likes on r.id = likes.routine_id
order by workout.date desc) t;
", &.read(JSON::Any)
  else
    filter = "%#{filter}%"
    STDOUT.puts filter
    response = db.query_one "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select workout.routine_id, r.name, username,owner, location, to_char(date, 'HH24:mm TZ DD.Month') as date, r.appraisal, cnt.used,
case when likes.count is null then 0 else likes.count end as appraisal,
case when r.user_id = workout.user_id then (select true) else (select false) end as isOwner from workout
join (select routine.*, username as owner from routine join person on person.id = user_id) as r
on r.id = workout.routine_id join (select distinct(id) from routine join routine_type on routine.id = routine_id
join routine_exercise on id = routine_exercise.routine_id
where (lower(routine.name) like lower($1) or lower(type_name) like lower($1) or lower(exercise_name) like lower($1))
) as rt on r.id = rt.id join (select routine_id, count(routine_id) - 1 as used from workout group by routine_id)
as cnt on cnt.routine_id = r.id
left join (select routine_id, count(routine_id) from likes group by routine_id) likes on r.id = likes.routine_id
join person on person.id = workout.user_id order by workout.date desc) t;", filter, &.read(JSON::Any)
  end

  STDOUT.puts response.to_json
  response.to_json
end

get "/:userId/workout" do |e|
  user_id = e.params.url["userId"]
  a = ""
  STDOUT.puts user_id
  query = db.query "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select routine_id, r.name, username,owner, location, to_char(date, 'HH24:mm TZ DD.Month') as date, r.appraisal,
case when r.user_id = workout.user_id then (select true) else (select false) end as isOwner from workout
join (select routine.*, username as owner from routine join person on person.id = user_id) as r on r.id = routine_id
join person on person.id = workout.user_id where person.id = $1 order by workout.date desc) as t", user_id do |rs|
    rs.each do
      a = rs.read(JSON::Any)
    end
  end
  a.to_json
end

get "/user/" do |e|
  user_id = e.params.query["userId"]
  id = e.params.query["id"]
  STDOUT.puts user_id
  fav_exercises = Array(String).new
  fav_types = Array(String).new
  response = ""
  user_query = db.query_one "select row_to_json(t) from (select person.*, case when count is null then 0 else count end as stars, case when c.star = person.id then true else false end as isstar
from person
left join (select star, count(star) from user_stars group by star) x on person.id = star left join
(select * from user_stars where id = $2) l on l.star = person.id left join
(select * from user_stars where id = $2 and star = $1) c
 on c.star = person.id
group by person.id, count, c.star
having person.id = $1) t;", user_id, id, &.read(JSON::Any)
  user = User.from_json(user_query.to_json)
  workout_query = db.query_one "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select workout.routine_id, r.name, username,owner, location, to_char(date, 'HH24:mm TZ DD.Month') as date, cnt.used,
case when likes.count is null then 0 else likes.count end as appraisal,
case when r.user_id = workout.user_id then (select true) else (select false) end as isOwner from workout
join (select routine.*, username as owner from routine join person on person.id = user_id) as r on r.id = routine_id
join person on person.id = workout.user_id join (select routine_id, count(routine_id) - 1 as used from workout group by routine_id)
as cnt on cnt.routine_id = r.id
left join (select routine_id, count(routine_id) from likes group by routine_id) likes on r.id = likes.routine_id
where person.id = $1 order by workout.date desc) t", user_id, &.read(JSON::Any)
  fav_exercises = db.query_one "select case when array_agg(exercise_name) is null then '{}' else array_agg(exercise_name) end  from (select exercise_name, count(exercise_name) from (select w.routine_id, exercise_name from workout as w join (select routine_id, exercise_name from routine_exercise) as x
on w.routine_id = x.routine_id group by w.routine_id, exercise_name, w.user_id
having w.user_id = $1) res group by exercise_name order by count desc limit 3) t", user_id, &.read(Array(String))

  fav_types = db.query_one "select case when array_agg(type_name) is null then '{}' else array_agg(type_name) end  from (select type_name, count(type_name) from (select w.routine_id, type_name from workout as w join (select routine_id, type_name from routine_type) as x
on w.routine_id = x.routine_id
group by w.routine_id, type_name, w.user_id
having w.user_id = $1) res group by type_name order by count desc limit 3) t;", user_id, &.read(Array(String))
  begin
    workout = Array(Workout).from_json(workout_query.to_json)
  rescue ex
    workout = Array(Workout).new.push(Workout.new)
  end
  response = UserDetails.new(user, workout, fav_types, fav_exercises)
  STDOUT.puts response.to_json
  STDOUT.puts e.response.status_code
  response.to_json
end

get "/persons/" do |e|
  filter = e.params.query.fetch("filter", nil)
  users = ""
  if filter == nil
    users = db.query_one "select array_to_json(array_agg(row_to_json(t))) from (select person.*, case when count is null then 0 else count end as stars from person
left join (select star, count(star) from user_stars group by star) x on person.id = star) t;
;", &.read(JSON::Any)
  else
    filter = "%#{filter}%"
    STDOUT.puts filter
    users = db.query_one "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
    from (select id,username,email,picture, case when count is null then 0 else count end as stars from person
left join (select star, count(star) from user_stars group by star) x on person.id = star where lower(username)
like lower($1)) t ;", filter, &.read(JSON::Any)
  end
  STDOUT.puts users.to_json

  users.to_json
end

get "/routine/" do |e|
  id = e.params.query["id"]
  user_id = e.params.query["user_id"]
  routine = db.query_one "select row_to_json(res) from (select id, x.user_id, x.name, array_to_json(array_agg(row_to_json(t))) as exercise, comment, case when
c.user_id = b.user_id then true else false end as isMy, cnt.used,
case when likes.count is null then 0 else likes.count end as appraisal,
case when lk.routine_id is null then false else true end as isliked
from (select routine_id, exercise_name, sets, reps from routine_exercise
where routine_id = $2) t join (select id,name, user_id,comment, appraisal from routine) x on x.id = t.routine_id left join
(select * from workout where user_id =$1) b on b.routine_id  = x.id left join
(select * from workout where routine_id = $2 and user_id = $1)
c on c.routine_id = x.id join (select routine_id, count(routine_id) - 1 as used from workout group by routine_id)
as cnt on cnt.routine_id = x.id
left join (select routine_id, count(routine_id) from likes group by routine_id) likes on x.id = likes.routine_id
left join (select * from likes where user_id = $1 and routine_id = $2 ) as lk
on lk.routine_id = x.id
group by x.user_id,comment, appraisal, x.name, x.id, b.user_id, c.user_id, cnt.used, likes.count, lk.routine_id) res;", user_id, id, &.read(JSON::Any)
  routine.to_json
end

put "/routine/" do |e|
  routine = Routine.from_json(e.params.json["routine"].to_json)
  user_id = e.params.query["id"]
  time = Time.parse(e.params.json["date"].as(String), "%Y-%m-%d %H:%M:%S%z")
  location = e.params.json["location"].as(String)
  username, pushyid = db.query_one "insert into workout (user_id, routine_id, date, location) values ($1, $2, $3, $4)
  returning (select username from person where id = $1), (select pushyid from person where id = $5)",
    user_id, routine.id, time, location, routine.user_id, as: {String, String}

  data = {
    "to":   pushyid,
    "data": {
      "type":         "reuse",
      "user":         username,
      "message":      "reused",
      "routine_name": routine.name,
      "routine_id":   routine.id,
    },
  }.to_json

  response = HTTP::Client.post("https://api.pushy.me/push?api_key=f41e98360f8af1e55d4e44be7dc55d1941451f9be38da7bd7f62904faaad69d8",
    headers: HTTP::Headers{"Content-type" => "application/json"}, body: data)

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

patch "/routine/" do |e|
  routine = Routine.from_json(e.params.json["routine"].to_json)
  user_id = e.params.query["id"]
  username, pushyid = db.query_one "insert into likes (user_id, routine_id) values ($1, $2) returning
(select username from person where id =$1), (select pushyid from person where id =$3)", user_id, routine.id, routine.user_id, as: {String, String}
  data = {
    "to":   pushyid,
    "data": {
      "type":         "like",
      "user":         username,
      "message":      "liked",
      "routine_name": routine.name,
      "routine_id":   routine.id,
    },
  }.to_json
  STDOUT.puts data
  response = HTTP::Client.post("https://api.pushy.me/push?api_key=f41e98360f8af1e55d4e44be7dc55d1941451f9be38da7bd7f62904faaad69d8",
    headers: HTTP::Headers{"Content-type" => "application/json"}, body: data)
end

delete "/routine/" do |e|
  user_id = e.params.query["userId"].to_s
  routine_id = e.params.query["routineId"].to_i32
  db.exec "delete from likes where user_id = $1  and routine_id = $2", user_id, routine_id
end

post "/token/" do |e|
  STDOUT.puts e.params.json["pushyToken"].to_s
  url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + e.params.json["googleToken"].to_s
  response = HTTP::Client.get url
  STDOUT.puts response.body
  google_resp = JSON.parse(response.body)
  sub = google_resp["sub"].to_s
  name = google_resp["name"].to_s
  email = google_resp["email"].to_s
  picture = google_resp["picture"].to_s
  user = User.new(sub, name, email, picture, e.params.json["pushyToken"].to_s)
  begin
    db.exec("INSERT INTO person(id, username, email, picture, pushyid) VALUES ($1, $2, $3, $4, $5)", sub, name, email, picture, e.params.json["pushyToken"].to_s)
  rescue ex : Exception
    STDOUT.puts ex.message
    db.exec("UPDATE person set pushyid = $2 WHERE id = $1", sub, e.params.json["pushyToken"].to_s)
  end
  user.to_json
end

put "/user/" do |e|
  user_id = e.params.query["id"].to_s
  user_star_id = e.params.json["id"].to_s
  pushy_id = e.params.json["pushyid"].to_s
  username = e.params.json["username"].to_s

  username = db.query_one "insert into user_stars values($1, $2) returning
(select username from person where id = $1)", user_id, user_star_id, &.read(String)

  data = {
    "to":   pushy_id,
    "data": {
      "type":    "follow",
      "user":    username,
      "message": "followed",
    },
  }.to_json

  response = HTTP::Client.post("https://api.pushy.me/push?api_key=f41e98360f8af1e55d4e44be7dc55d1941451f9be38da7bd7f62904faaad69d8",
    headers: HTTP::Headers{"Content-type" => "application/json"}, body: data)
end

delete "/user/" do |e|
  user_id = e.params.query["id"].to_s
  user_star_id = e.params.query["star"].to_s
  STDOUT.puts user_id
  STDOUT.puts user_star_id

  db.exec "delete from user_stars where id = $1 and star = $2", user_id, user_star_id
end

get "/likes/" do |e|
  user_id = e.params.query["id"].to_s
  response = db.query_one "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select workout.routine_id, r.name, username,owner, location, to_char(date, 'HH24:mm TZ DD.Month') as date, cnt.used,
case when likes.count is null then 0 else likes.count end as appraisal,
case when r.user_id = workout.user_id then (select true) else (select false) end as isOwner from workout
join (select routine.*, username as owner from routine join person on person.id = user_id) as r on r.id = routine_id
join person on person.id = workout.user_id join (select routine_id, count(routine_id) - 1 as used from workout group by routine_id)
as cnt on cnt.routine_id = r.id
left join (select routine_id, count(routine_id) from likes group by routine_id) likes on r.id = likes.routine_id
join (select * from likes where user_id = $1) as islike on islike.routine_id = workout.routine_id
where person.id = $1 order by workout.date desc) t;", user_id, &.read(JSON::Any)

  response.to_json
end

get "/stars/" do |e|
  user_id = e.params.query["id"].to_s
  response = db.query_one "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select person.*, case when count is null then 0 else count end as stars from person
join (select star, count(star) from user_stars where id = $1 group by star) x on person.id = star) t", user_id, &.read(JSON::Any)
  response.to_json
end

get "/:userId/stars/" do |e|
  user_id = e.params.url["userId"]
  stars = db.query_one "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select person.*, case when count is null then 0 else count end as stars from person join (select * from user_stars where star = $1) x on x.id = person.id
left join (select star, count(star) from user_stars group by star) c on person.id = c.star) t;", user_id, &.read(JSON::Any)
  stars.to_json
end

get "/likes/:routineId/" do |e|
  routine_id = e.params.url["routineId"].to_i
  likes = db.query_one "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select person.*, case when count is null then 0 else count end as stars from person join (select * from likes where routine_id = $1) x on x.user_id = person.id
left join (select star, count(star) from user_stars group by star) c on person.id = c.star) t;", routine_id, &.read(JSON::Any)
  likes.to_json
end

get "/reuse/:routineId/" do |e|
  routine_id = e.params.url["routineId"].to_i
  reuse = db.query_one "select case when array_to_json(array_agg(row_to_json(t))) is null then row_to_json(row(0)) else array_to_json(array_agg(row_to_json(t))) end
from (select person.*, case when count is null then 0 else count end as stars from person join (select distinct(user_id) from workout where routine_id = $1) x on x.user_id = person.id
left join (select star, count(star) from user_stars group by star) c on person.id = c.star) t", routine_id, &.read(JSON::Any)
  reuse.to_json
end
Kemal.run
