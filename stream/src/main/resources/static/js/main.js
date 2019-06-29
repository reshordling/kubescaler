function loadMessages () {

    this.source = null;

    this.start = function () {

        var messageTable = document.getElementById("messages");

        // Relative URL to stream data from microservice
        this.source = new EventSource("/stream");

        this.source.addEventListener("message", function (event) {

            // These events are JSON, so parsing and DOM fiddling are needed
            var message = JSON.parse(event.data);

            var row = messageTable.getElementsByTagName("tbody")[0].insertRow(0);
            var cell0 = row.insertCell(0);
            var cell1 = row.insertCell(1);
            var cell2 = row.insertCell(2);

            cell0.className = "author-style";
            cell0.innerHTML = message.host;

            cell1.className = "date";
            cell1.innerHTML = message.timestamp;

            cell2.className = "text";
            cell2.innerHTML = message.message.slice(0, 34);

        });

        this.source.onerror = function () {
            this.close();
        };

    };

    this.stop = function() {
        this.source.close();
    }

}

message = new loadMessages();

/*
 * Register callbacks for starting and stopping the SSE controller.
 */
window.onload = function() {
    message.start();
};
window.onbeforeunload = function() {
    message.stop();
}