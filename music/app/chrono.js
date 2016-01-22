(function() {
  jukebox.compose.chrono = function(conductor) {
    var tempo = 126
    conductor.setTempo(tempo);
    conductor.setTimeSignature(4, 4);

    var right = conductor.createInstrument('square', 'oscillators'),
        left = conductor.createInstrument('square', 'oscillators');

    right.note('tripletEighth', 'A4')
    .note('tripletEighth', 'G4')
    .note('tripletEighth', 'A4')
    .note('quarter', 'E4')
    .note('quarter', 'A4')
    .note('tripletQuarter', 'B4')
    .note('tripletEighth', 'C5');

    left.note('quarter', 'C4, A3')
    .note('quarter', 'C4, A3')
    .note('quarter', 'C4, A3')
    .note('quarter', 'C4, A3');

    right.note('tripletEighth', 'B4')
    .note('tripletEighth', 'A4')
    .note('tripletEighth', 'B4')
    .note('half', 'G4')
    .note('quarter', 'D4');

    left.note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3');

    right.note('tripletEighth', 'A4')
    .note('tripletEighth', 'G4')
    .note('tripletEighth', 'A4')
    .note('quarter', 'E4')
    .note('quarter', 'A4')
    .note('tripletQuarter', 'B4')
    .note('tripletEighth', 'C5');

    left.note('quarter', 'A3, F3')
    .note('quarter', 'A3, F3')
    .note('quarter', 'A3, F3')
    .note('quarter', 'A3, F3');

    right.note('tripletEighth', 'B4')
    .note('tripletEighth', 'C5')
    .note('tripletEighth', 'D5')
    .note('dottedHalf', 'B4');

    left.note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3');

    right.note('tripletEighth', 'A4')
    .note('tripletEighth', 'G4')
    .note('tripletEighth', 'A4')
    .note('quarter', 'E4')
    .note('quarter', 'A4')
    .note('tripletQuarter', 'B4')
    .note('tripletEighth', 'C5');

    left.note('quarter', 'C4, A3')
    .note('quarter', 'C4, A3')
    .note('quarter', 'C4, A3')
    .note('quarter', 'C4, A3');

    right.note('tripletEighth', 'B4')
    .note('tripletEighth', 'A4')
    .note('tripletEighth', 'B4')
    .note('half', 'G4')
    .note('quarter', 'D4');

    left.note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3');

    right.note('tripletEighth', 'A4')
    .note('tripletEighth', 'G4')
    .note('tripletEighth', 'A4')
    .note('quarter', 'B4')
    .note('quarter', 'C5')
    .note('quarter', 'B4');

    left.note('quarter', 'A3, F3')
    .note('quarter', 'A3, F3')
    .note('quarter', 'B3, G3')
    .note('quarter', 'B3, G3');

    right.note('tripletEighth', 'A4')
    .note('tripletEighth', 'G4')
    .note('tripletEighth', 'A4')
    .note('tripletEighth', 'A4')
    .note('tripletEighth', 'G4')
    .note('tripletEighth', 'A4')
    .note('quarter', 'A4')
    .rest('quarter');

    left.note('quarter', 'C4, A3')
    .note('quarter', 'C4, A3')
    .note('quarter', 'C4, A3')
    .rest('quarter');

    right.note('tripletQuarter', 'E5')
    .note('tripletEighth', 'E5')
    .note('tripletEighth', 'E5')
    .note('tripletEighth', 'C5')
    .note('tripletEighth', 'D5')
    .note('quarter', 'E5')
    .note('quarter', 'A5');

    left.note('quarter', 'E4, C4, A3')
    .note('quarter', 'E4, C4, A3')
    .note('quarter', 'E4, C4, A3')
    .note('quarter', 'E4, C4, A3');

    right.note('half', 'G5')
    .note('dottedQuarter', 'E5')
    .note('eighth', 'C5');

    left.note('quarter', 'C4, G3, E3')
    .note('quarter', 'C4, G3, E3')
    .note('quarter', 'C4, G3, E3')
    .note('quarter', 'C4, G3, E3');

    right.note('dottedHalf', 'A4')
    .rest('quarter');

    left.note('quarter', 'C4, A3, F3')
    .note('quarter', 'C4, A3, F3')
    .note('quarter', 'C4, A3, F3')
    .note('quarter', 'C4, A3, F3');

    right.note('tripletEighth', 'A4')
    .note('tripletEighth', 'B4')
    .note('tripletEighth', 'C5')
    .note('tripletEighth', 'B4')
    .note('tripletEighth', 'C5')
    .note('tripletEighth', 'D5')
    .note('tripletEighth', 'C5')
    .note('tripletEighth', 'D5')
    .note('tripletEighth', 'E5')
    .note('tripletEighth', 'D5')
    .note('tripletEighth', 'E5')
    .note('tripletEighth', 'G5');

    left.note('quarter', 'D4, B3, G3')
    .note('quarter', 'D4, B3, G3')
    .rest('tripletEighth')
    .note('tripletEighth', 'D4, B3, G3')
    .rest('tripletEighth')
    .note('quarter', 'D4, B3, G3');

    right.note('tripletQuarter', 'E5')
    .note('tripletEighth', 'E5')
    .note('tripletEighth', 'E5')
    .note('tripletEighth', 'C5')
    .note('tripletEighth', 'D5')
    .note('quarter', 'E5')
    .note('quarter', 'A5');

    left.note('quarter', 'E4, C4, A3')
    .note('quarter', 'E4, C4, A3')
    .note('quarter', 'E4, C4, A3')
    .note('quarter', 'E4, C4, A3');

    right.note('quarter', 'B5', true)
    .note('tripletEighth', 'B5')
    .note('tripletEighth', 'C6')
    .note('tripletEighth', 'B5')
    .note('quarter', 'A5')
    .note('quarter', 'G5');

    left.note('quarter', 'D4, B3, G3')
    .note('quarter', 'D4, B3, G3')
    .note('quarter', 'D4, B3, G3')
    .note('quarter', 'D4, B3, G3');

    right.note('whole', 'A5');

    left.note('quarter', 'C4, A3, F3')
    .note('quarter', 'C4, A3, F3')
    .note('quarter', 'C4, A3, F3')
    .note('quarter', 'C4, A3, F3');

    right.note('half', 'A5')
    .note('half', 'B5');

    left.note('quarter', 'B3, A3, E3')
    .note('quarter', 'B3, A3, E3')
    .rest('tripletEighth')
    .note('tripletEighth', 'B3, G#3, E3')
    .rest('tripletEighth')
    .note('quarter', 'B3, G#3, E3');

    right.note('tripletQuarter', 'C6')
      .note('tripletEighth', 'C6')
      .note('tripletEighth', 'C6')
      .note('tripletEighth', 'B5')
      .note('tripletEighth', 'A5')
      .note('quarter', 'G5')
      .note('quarter', 'E5');

    left.note('quarter', 'F3')
      .note('quarter', 'C4, A3, F3')
      .note('quarter', 'G3')
      .note('quarter', 'D4, B3, G3');

    right.note('tripletQuarter', 'A5')
      .note('tripletEighth', 'A5')
      .note('tripletEighth', 'A5')
      .note('tripletEighth', 'G5')
      .note('tripletEighth', 'F5')
      .note('half', 'E5');

    left.note('quarter', 'F3')
      .note('quarter', 'C4, A3, F3')
      .note('quarter', 'A3')
      .note('quarter', 'E4, B3, A3');

    right.note('tripletQuarter', 'C6')
      .note('tripletEighth', 'C6')
      .note('tripletEighth', 'C6')
      .note('tripletEighth', 'B5')
      .note('tripletEighth', 'A5')
      .note('quarter', 'G5')
      .note('quarter', 'E5');


    left.note('quarter', 'F3')
      .note('quarter', 'C4, A3, F3')
      .note('quarter', 'G3')
      .note('quarter', 'D4, B3, G3');

    right.note('half', 'A5')
      .note('half', 'B5');

    left.note('quarter', 'A3, F#3, D3')
      .note('tripletEighth', 'A3, F#3, D3')
      .note('tripletEighth', 'A3, F#3, D3')
      .note('tripletEighth', 'A3, F#3, D3')
      .note('quarter', 'B3, G#3, E3')
      .note('tripletEighth', 'B3, G#3, E3')
      .note('tripletEighth', 'B3, G#3, E3')
      .note('tripletEighth', 'B3, G#3, E3');

    right.note('tripletQuarter', 'C6')
      .note('tripletEighth', 'C6')
      .note('tripletEighth', 'C6')
      .note('tripletEighth', 'B5')
      .note('tripletEighth', 'A5')
      .note('quarter', 'G5')
      .note('quarter', 'E5');

    left.note('quarter', 'F3')
      .note('quarter', 'C4, A3, F3')
      .note('quarter', 'G3')
      .note('quarter', 'D4, B3, G3');

    right.note('tripletQuarter', 'A5')
      .note('tripletEighth', 'A5')
      .note('tripletEighth', 'A5')
      .note('tripletEighth', 'G5')
      .note('tripletEighth', 'F5')
      .note('half', 'E5');

    left.note('quarter', 'F3')
      .note('quarter', 'C4, A3, F3')
      .note('quarter', 'A3')
      .note('quarter', 'E4, B3, A3');

    right.note('tripletQuarter', 'C6')
      .note('tripletEighth', 'C6')
      .note('tripletEighth', 'C6')
      .note('tripletEighth', 'B5')
      .note('tripletEighth', 'C6')
      .note('quarter', 'D6')
      .note('quarter', 'B5');

    left.note('quarter', 'F3')
      .note('quarter', 'C4, A3, F3')
      .note('quarter', 'G3')
      .note('quarter', 'D4, B3, G3');

    right.note('half', 'B5')
      .note('quarter', 'A5')
      .rest('quarter');

    left.note('quarter', 'E4, C4, A3')
      .note('tripletEighth', 'E4, C4, A3')
      .note('tripletEighth', 'E4, C4, A3')
      .note('tripletEighth', 'E4, C4, A3')
      .note('quarter', 'E4, C4, A3')
      .rest('quarter');

    return tempo
  }
})()
