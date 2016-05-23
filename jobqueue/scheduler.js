var Scheduler = (function(){
  return {
    schedule:function(fn,interval) {
      var job = {};
      var jobfn = function() {
        fn()
        job.timer = setTimeout(jobfn, interval)
      }
      return job
    }
    cancel:function(job) {
      clearTimeout(job.timer)
    }
  }
})()
