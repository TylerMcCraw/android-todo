# android-todo
ToDo app which showcases Offline-first MVP architecture with Dependency Injection

This project is based on Google's [Android Architecture samples](https://github.com/googlesamples/android-architecture), but here, we're taking it one step further and implementing the samples to show how one would build a _true_ offline-capable application.
We also aim to fully implement the network and persistence calls here, instead of implementing "dummy" network request simulations. That way, it's easy to see the pros/cons between our offline-capable architectures.

There are multiple branches in this project which show off real implementations of different architectures:

| Architecture                  | Branch Name   |
| ----------------------------- | ------------- |
| MVP Offline w/ Job Scheduling | [todo-mvp-job-scheduling](https://github.com/TylerMcCraw/android-todo/tree/todo-mvp-job-scheduling) |
| MVP Offline w/ Realtime DB    | [todo-mvp-realtime-db](https://github.com/TylerMcCraw/android-todo/tree/todo-mvp-realtime-db)      |
