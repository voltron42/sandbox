var Exec = (function(){
  var getTimes = function(timeinputs) {
    return {
      interval:time.toMillis(timeinputs.intervalvalue,timeinputs.intervalunit),
      offset:time.toMillis(timeinputs.offsetvalue,timeinputs.offsetunit),
      duration:time.toMillis(timeinputs.durationvalue,timeinputs.durationunit),
    }
  }
  var inputnames = [
    "intervalvalue",
    "intervalunit",
    "offsetvalue",
    "offsetunit",
    "durationvalue",
    "durationunit",
  ]
  var ctrlnames = [
    "start"
  ]
  var outputnames = [
    "repl",
    "current",
    "repltime",
    "queue"
    "table"
  ]
  var inputs = {}
  var outputs = {}
  var ctrls = {}
  var state = {}
  var step = 250
  var currtime
  var initcurrtime = function() {
    currtime = new Date()
    outputs.current.innerHTML = time.toString()
    setTimeout(function() {currenttime()}, step)
  }
  var repl = false;
  var repltime;
  var repltimeout;
  var replfn = function() {
    repltime += step
    repltimeout = setTimeout(function() {replfn()}, step)
  }
  var repltoggle = function() {
    if (repl) {
      repl = false;
      outputs.repl.value = "START"
      clearTimeout(repltimeout)
    } else {
      repl = true;
      outputs.repl.value = "STOP"
      repltimeout = setTimeout(function() {replfn()}, step)
    }
  }
  var replreset = function() {
    repltime = new Date()
    outputs.repltime.innerHTML = repltime.toString()
  }
  var jobFields = ["jobId","lower","upper"]
  var resultFields = ["start","end","lower","upper"]
  var results = []
  var updateOutput = function(elem, list, fieldset) {
    elem.innerHTML = list.map(function(job){
      return "<tr>" + fieldset.map(function(field) {
        return "<td>" + job[field] + "</td>"
      }).join("") + "</tr>"
    }).join("")
  }
  return {
    start:function() {
      inputnames.forEach(function(name) {
        elem = document.getElementById(name)
        elem.disabled = true
        inputs[name] = elem.value
      })
      outputnames.forEach(function(name) {
        outputs[name] = document.getElementById(name)
      })
      ctrlnames.forEach(function(name) {
        ctrls[name] = document.getElementById(name)
      })
      ctrls.start.disabled = true;
      outputs.repl.disabled = false;
      var times = getTimes(inputs);
      var ops = {
        getCurrTime:function() {
          return currtime
        },
        getReplTime:function() {
          return repltime
        },
        update:function(queue) {
          updateOutput(output.queue, queue, jobFields)
        },
        finish:function(job) {
          job.end = new Date();
          results.push(job)
          updateOutput(output.table, results, resultFields)
        }
      }
      var jqs = new JobQueueScheduler(times, ops)
      initcurrtime()
      replreset()
      repltoggle()
      jqs.invoke()
    },
    repl:{
      toggle:repltoggle,
      reset:replreset
    }
  }
})()
