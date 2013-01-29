system = require 'system'
page = require('webpage').create()

if system.args.length != 2
  console.log 'Usage: snaps.coffee URL'
  phantom.exit 1
else
  address = system.args[1]
  output = system.args[2]
  page.viewportSize =
    width: '800px'
    height: '600px'
  page.paperSize =
    width: 800,
    height: 600,
    border: '0px'

  page.open address, (status) ->
    if status isnt 'success'
      console.log "Unable to load the address! #{status}"
      phantom.exit()
    else
      i = 0
      interval = window.setInterval ->
        if ++i > 10
          clearInterval(interval)
          phantom.exit()
        console.log page.renderBase64('PNG')
      , 1000
