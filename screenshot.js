// SlimerJS

var page = require("webpage").create();
page
    .open("http://vizualizacija.herokuapp.com")
    .then(function(status) {
        page.viewportSize = { width: 320, height: 1200 };
        return page.evaluate(function () {
            document.querySelector('#izraz').value = "A AND NOT B";
            document.querySelector('input[type=submit]').click();
        });
    })
    .then(function(status) {
        slimer.wait(1000);
        page.render("screenshot.png");
        page.close();
        slimer.exit();
    });
