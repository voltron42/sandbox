(function(){
  jukebox.load.g_major = {
    timeSignature: [4, 4],
    tempo: 120,
    instruments: {
        rightHand: {
            name: 'square',
            pack: 'oscillators'
        },
        leftHand: {
            name: 'sawtooth',
            pack: 'oscillators'
        }
    },
    notes: {
        // Shorthand notation
        rightHand: [
            'quarter|G4',
            'quarter|A4',
            'quarter|B4',
            'quarter|C5',
            'quarter|D5',
            'quarter|E5',
            'quarter|F#5',
            'quarter|G5'
        ],
        // More verbose notation
        leftHand: [
            {
                type: 'note',
                pitch: 'G3',
                rhythm: 'quarter'
            },
            {
                type: 'note',
                pitch: 'A3',
                rhythm: 'quarter'
            },
            {
                type: 'note',
                pitch: 'B3',
                rhythm: 'quarter'
            },
            {
                type: 'note',
                pitch: 'C4',
                rhythm: 'quarter'
            },
            {
                type: 'note',
                pitch: 'D4',
                rhythm: 'quarter'
            },
            {
                type: 'note',
                pitch: 'E4',
                rhythm: 'quarter'
            },
            {
                type: 'note',
                pitch: 'F#4',
                rhythm: 'quarter'
            },
            {
                type: 'note',
                pitch: 'G4',
                rhythm: 'quarter'
            }
        ]
    }
  }
})()
