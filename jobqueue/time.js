var Time = (function(){
  var units = {
    MILLIS:function(value) {
      return value
    },
    SECOND:function(value) {
      return 1000 * value
    },
    MINUTE:function(value) {
      return 60 * 1000 * value
    },
    HOUR:function(value) {
      return 60 * 60 * 1000 * value
    }
  }
  return {
    getUnits:function() {
      return Object.keys(units)
    },
    toMillis:function(time, unit) {
      return units[unit](time)
    }
  }
})()
