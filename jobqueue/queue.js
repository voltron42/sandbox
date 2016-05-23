var Q = (function(){
  return function() {
    var queue = []
    this.enqueue = function(job) {
      queue.push(job)
    }
    this.dequeue = function() {
      return queue.shift()
    }
    this.isEmpty = function() {
      return queue.length == 0
    }
    this.first = function() {
      return queue[0]
    }
    this.last = function() {
      return queue[queue.length - 1]
    }
    this.toTable = function() {
      return queue.map(function(job) {
        return job
      })
    }
  }
})()
