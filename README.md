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


###Run & Test
- Run: `gradle bootRun`
- Test: `gradle test`  

###How to write a view
Create a bean of type `class mtp.services.View` and set `name`, 
map function (`Function<Entry, Result> map`) and optional a reduce function (`Collector<Result, ?, ?> reduce`).

View will be accesible at path `/api/view/<name>`
