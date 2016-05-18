var Exec = (function(){
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
    "table"
  ]
  var inputs = {}
  var outputs = {}
  var ctrls = {}
  var state = {}
  var currenttime = function() {
    var time = new Date()
    outputs.current.innerHTML = time.toString()
    setTimeout(function() {currenttime()}, 500)
  }
  var repl = true;
  var repltoggle = function() {
    if (repl) {}
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
      currenttime()

    },
    repl:{
      toggle:function() {

      }
    }
  }
})()
