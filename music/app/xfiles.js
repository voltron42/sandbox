(function(){
  var tempo = 190

  var top = [];
  var bottom = [];
  var bass = [];
  // bar 1
  top = top.concat("whole|");
  bottom = bottom.concat(
    "tripletQuarter|A3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3"
  );
  bass = bass.concat("whole|A2");

  // bar 2
  top = top.concat("half|","quarter|","quarter|A4");
  bottom = bottom.concat(
    "quarter|A4",
    "quarter|A4",
    "quarter|A4",
    "quarter|"
  );
  bass = bass.concat("whole|");

  // bar 3
  top = top.concat("quarter|E4","quarter|D4","quarter|E4","quarter|G4");
  bottom = bottom.concat(
    "tripletQuarter|A3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3"
  );
  bass = bass.concat("whole|");

  // bar 4
  top = top.concat("whole|E4");
  bottom = bottom.concat(
    "quarter|B4",
    "quarter|B4",
    "quarter|B4",
    "quarter|B4"
  );
  bass = bass.concat("whole|");

  // bar 5
  top = top.concat("half|","quarter|","quarter|A4");
  bottom = bottom.concat(
    "tripletQuarter|A3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3"
  );
  bass = bass.concat("half|A2","half|");

  // bar 6
  top = top.concat("quarter|E4","quarter|D4","quarter|E4","quarter|G4");
  bottom = bottom.concat("whole|A4");
  bass = bass.concat("whole|");

  // bar 7
  top = top.concat("whole|E4");
  bottom = bottom.concat(
    "tripletQuarter|A3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3"
  );
  bass = bass.concat("whole|");

  // bar 8
  top = top.concat("half|","quarter|","quarter|C5");
  bottom = bottom.concat(
    "quarter|B4",
    "quarter|B4",
    "quarter|B4",
    "quarter|B4"
  );
  bass = bass.concat("half|A2","half|");

  // bar 9
  top = top.concat("quarter|B5","quarter|A5","quarter|G4","quarter|A5");
  bottom = bottom.concat(
    "tripletQuarter|A3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3"
  );
  bass = bass.concat("whole|");

  // bar 10
  top = top.concat("whole|E4");
  bottom = bottom.concat(
    "quarter|A4",
    "quarter|A4",
    "quarter|A4",
    "quarter|A4"
  );
  bass = bass.concat("whole|");

  // bar 11
  top = top.concat("half|","quarter|","quarter|C4");
  bottom = bottom.concat(
    "tripletQuarter|A3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3",
    "tripletQuarter|F3",
    "tripletQuarter|C3",
    "tripletQuarter|E3"
  );
  bass = bass.concat("half|A2","half|");

  // bar 12
  top = top.concat("quarter|B5","quarter|A5","quarter|G4","quarter|A5");
  bottom = bottom.concat("whole|B4");
  bass = bass.concat("whole|");

  jukebox.load.x_files = {
    timeSignature: [4, 4],
    tempo: tempo,
    instruments: {
      rightHand: {
        name: 'triangle',
        pack: 'oscillators'
      },
      leftHand: {
        name: 'sine',
        pack: 'oscillators'
      },
      bass: {
        name: 'sine',
        pack: 'oscillators'
      }
    },
    notes: {
      // Shorthand notation
      rightHand: top,
      leftHand: bottom,
      bass: bass
    }
  }
})()
