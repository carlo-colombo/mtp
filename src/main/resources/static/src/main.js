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
  .controller('RateGraphController', function($http, rate, $scope) {
    var self = this
    self.groupOptions = [{
      label: 'Day',
      value: 2
    }, {
      label: '12h',
      value: 3
    }, {
      label: '1h',
      value: 4
    }, {
      label: '1\' ',
      value: 5
    }, {
        label: '1" ',
        value: 6
      }]

    self.groupLevel = 3

    $scope.$watch('rate.groupLevel', function(val) {
      rate(val).then(function(res) {
        var data = _.sortBy(
          _.map(res.data, function(value, key) {
            return {
              time: _.last(key.split('/')),
              series: _.first(key.split('/')),
              value: value.average
            }
          }), 'time')

        var byTime = _.groupBy(data, 'time')
        var series = _.unique(_.pluck(data, 'series'))

        var byTimeAndSeries = _.mapValues(byTime, function(entry) {
          return _.groupBy(entry, 'series')
        })

        self.labels = _.keys(byTime)
        self.series = series

        self.data = _.map(series, function(s, j) {
          return _.map(self.labels, function(label, i) {
            if (byTimeAndSeries[label][s]) {
              return byTimeAndSeries[label][s][0].value
            }
            return null
          })
        })
      })
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
