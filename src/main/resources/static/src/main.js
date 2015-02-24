angular
  .module('MTP', ['chart.js'])
  .controller('CountryController', function(countries) {
    var self = this
    countries().then(function(res) {
      self.data = _.pairs(res.data)
      self.countries = _.map(self.data, '0')
      self.values = _.map(self.data, '1')
    })

  })
  .controller('SoldBoughtController', function($q, amountBought, amountSold) {
    var self = this
    $q.all({
      sold: amountSold(),
      bought: amountBought()
    }).then(
      function(res) {
        self.labels = _.unique(_.keys(res.bought.data).concat(
          _.keys(res.sold.data)))
        var sold = self.labels.map(function(k) {
          return res.sold.data[k] || 0
        }),
          bought = self.labels.map(function(k) {
            return res.bought.data[k] || 0
          })

          self.data = [sold, bought]
          self.series = ["Sold", "Bought"]
          self.table = _.zip(self.labels, sold, bought)
      })
  })
  .controller('RateController', function($http, rate) {
    var self = this
    rate().then(function(res) {
      self.data = res.data
    })
  })
  .controller('RateGraphController', function($http, rate) {
    var self = this
    rate(4).then(function(res) {
    	var data = _.sortBy( 
    		_.map(res.data, function(value, key){
    	  return {
    		  time: _.last(key.split('/')),
    		  series: _.first(key.split('/')) ,
    		  value: value.average
    	  }
    	}),'time')
    })
  })
  .factory('countries', makeView('countries'))
  .factory('amountSold', makeView('amountSold'))
  .factory('amountBought', makeView('amountBought'))
  .factory('rate', makeView('rate'))

function makeView(view) {
  return function($http) {
    return function(level) {
      return $http.get('/api/view/' + view, {
        params: {
          groupLevel: level || 1,
          group: true
        }
      })
    }
  }
}
