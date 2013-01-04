system = require 'system'
page = require('webpage').create()

if system.args.length != 2
  console.log 'Usage: snap.coffee URL'
  phantom.exit 1
else
  address = system.args[1]
  output = system.args[2]
  page.viewportSize =
    width: 800
    height: 600
  page.paperSize =
    width: 800,
    height: 600,
    border: '0px'

  page.open address, (status) ->
    if status isnt 'success'
      console.log "Unable to load the address! #{status}"
      phantom.exit()
    else
      window.setTimeout ->
        console.log page.renderBase64('PNG')
        phantom.exit()
      , 200
