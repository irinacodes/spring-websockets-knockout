
function ApplicationModel(stompClient) {
  var self = this;

  self.username = ko.observable();
  self.timetable = ko.observable(new TimetableModel());
  self.delay = ko.observable(new DelayModel(stompClient));
  self.notifications = ko.observableArray();

  self.parseBody = function(message) {
    return JSON.parse(message.body);
  }

  self.connect = function() {
      stompClient.connect({}, function(frame) {

      console.log('Connected ' + frame);
      self.username(frame.headers['user-name']);

      stompClient.subscribe("/app/items", function(message) {
        self.timetable().loadTimetable(self.parseBody(message));
      });
      stompClient.subscribe("/topic/random.delay.*", function(message) {
        self.timetable().processDelay(self.parseBody(message));
      });
      stompClient.subscribe("/user/queue/delay-updates", function(message) {
        self.pushNotification("Delay update: " + self.parseBody(message).delayInMinutes + " minutes for train "
        + self.parseBody(message).trainCode);
        self.timetable().updateItem(self.parseBody(message));
      });
      stompClient.subscribe("/user/queue/errors", function(message) {
        self.pushNotification("Error " + message.body);
      });
    }, function(error) {
      console.log("STOMP protocol error " + error);
    });
  }

  self.pushNotification = function(text) {
    self.notifications.push({notification: text});
    if (self.notifications().length > 5) {
      self.notifications.shift();
    }
  }

  self.logout = function() {
    stompClient.disconnect();
    window.location.href = "logout.html";
  }
}

function TimetableModel() {
  var self = this;

  self.rows = ko.observableArray([]);

  var rowLookup = {};

  self.loadTimetable = function(items) {
    for (var i = 0; i < items.length; i++) {
      var row = new TimetableItem(items[i]);
      self.rows.push(row);
      rowLookup[row.trainId] = row;
    }
  };

  self.processDelay = function(delay) {
    if (rowLookup.hasOwnProperty(delay.trainId)) {
      rowLookup[delay.trainId].updateDelay(delay.randomDelayInMinutes);
    }
  };

  self.updateItem = function(item) {
    rowLookup[item.trainId].delayInMinutes(item.delayInMinutes);
  };
};

function TimetableItem(data) {
  var self = this;

  self.formatMinutes = function(minutes) {
    return (minutes >= 0 && minutes < 10) ? '0'+minutes : minutes;
  };

  self.arrow = ko.observable('<i class="icon-arrow-up"></i>');
  self.change = ko.observable(0);

  self.trainId = data.trainId;
  self.trainCode = data.trainCode;
  self.carrier = data.carrier;
  self.track = data.track;
  self.departureStation = data.departureStation;
  self.destinationStation = data.destinationStation;
  self.plannedArrivalHour = data.plannedArrivalHour;
  self.plannedArrivalMinute = data.plannedArrivalMinute;
  self.displayArrivalMinute = self.formatMinutes(data.plannedArrivalMinute);

  self.formattedPlannedArrivalTime = ko.computed(function() {
    return self.plannedArrivalHour + ":" + self.displayArrivalMinute; }, self);

  self.delayInMinutes = ko.observable(data.delayInMinutes);

  self.randomDelayInMinutes = ko.observable(data.randomDelayInMinutes);

  self.formattedExpectedArrivalTime = ko.computed(function() {
    var delayInMins = self.plannedArrivalMinute + self.delayInMinutes() + self.randomDelayInMinutes();
    var negative = false;
    if(delayInMins < 0) {
      delayInMins = 60 + delayInMins;
      negative = true;
    }
    var remainingMins = delayInMins % 60;
    var updatedHours = Math.trunc((delayInMins / 60));
    if(negative) {
      updatedHours = -1;
    }

    return ((self.plannedArrivalHour + updatedHours) % 24) + ":" + self.formatMinutes(remainingMins); }, self);

  self.updateDelay = function(newDelay) {
    self.arrow((newDelay <= 0) ? '<i class="icon-arrow-down"></i>' : '<i class="icon-arrow-up"></i>');
    self.randomDelayInMinutes(newDelay);
  };

};

function DelayModel(stompClient) {
  var self = this;

  self.delayInMinutes = ko.observable(0);
  self.currentRow = ko.observable({});
  self.error = ko.observable('');
  self.suppressValidation = ko.observable(false);

  self.showDelayModal = function(row) {
    self.delayInMinutes(0);
    self.currentRow(row);
    self.error('');
    self.suppressValidation(false);
    $('#delay-dialog').modal();
  };

  $('#delay-dialog').on('shown', function () {
    var input = $('#timetable-dialog input');
    input.focus();
    input.select();
  });
  
  var validateDelay = function() {
      if (isNaN(self.delayInMinutes()) || (self.delayInMinutes() < 1)) {
        self.error('Invalid number');
        return false;
      }
      return true;
  }

  self.addDelay = function() {
    if (!self.suppressValidation() && !validateDelay()) {
      return;
    }
    var delay = {
        "trainId" : self.currentRow().trainId,
        "delayInMinutes" : self.delayInMinutes()
      };
    console.log(delay);
    stompClient.send("/app/delay", {}, JSON.stringify(delay));
    $('#delay-dialog').modal('hide');
  };
}
