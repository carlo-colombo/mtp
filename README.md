# mtp
###a simple dashboard 

- Built with java8 on top of SpringBoot and ready to be deployed on openshift
- Query interface and internals inspired by CouchDB
- Data persistance delegated to https://github.com/OpenHFT/Chronicle-Map
- Dashboard frontend build with AngularJS and https://github.com/nnnick/Chart.js (throught https://github.com/jtblin/angular-chart.js)

###Views
- countries `/api/view/countries`
- amountSold `/api/view/amountSold`
- amountBought `/api/view/amountBought`
- rate `/api/view/rate`

views could be reduced adding parameter `group=true` and  `groupLevel=<groupLevel>`

###Dashboard
- `/index.html`

###Post data
- `/api/post`

###Run & Test
- Run: `gradle bootRun`
- Test: `gradle test`  

###How to write a view
Create a bean of type `class mtp.services.View` and set `name`, 
map function (`Function<Entry, Result> map`) and optional a reduce function (`Collector<Result, ?, ?> reduce`).

View will be accesible at path `/api/view/<name>`

###Performance
Simple benchmarking with `ab` (with a concurrency level of 10) reports that endpoint `/api/post` could handle about 30 req/s on one openshift small gear and about 1 req/s when querying with datastore filled with about 20k record. 30 req/s seems the limit of the openshift small gear because benchmarking versus endpoint with no complexity return the same value.
