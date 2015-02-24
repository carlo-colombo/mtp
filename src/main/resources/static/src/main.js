angular.module('MTP', [ 'chart.js' ]).controller('CountryController',
		function(countries) {
			var self = this
			countries().then(function(res){
				self.countries = Object.keys(res.data)
				self.values = self.countries.map(function(k){return res.data[k]})
				self.data = self.countries.map(function(k){
					return {key: k, value: res.data[k]}
				})
			})
			
		})
.factory('allEntries', function($http) {
	return function() {
		return $http.get('/api/list')
	}
}).factory('countries', function($http) {
	return function() {
		return $http.get('/api/view/countries', {
			params: {
				groupLevel : 1,
				group : true
			}
		})
	}
})